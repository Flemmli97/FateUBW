package io.github.flemmli97.fateubw.common.entity.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.mixinhelper.PathNavigationEx;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.registry.TenshilibMemoryModules;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class MoveBehindBehaviour<E extends PathfinderMob & AnimatedEntity & AOEAttackEntity> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = MemoryTest.builder(2)
            .hasMemory(TenshilibMemoryModules.ANIMATION_TO_PLAY.get())
            .hasMemory(MemoryModuleType.ATTACK_TARGET);

    private Vec3 targetPos;
    private List<BlockPos> pathPos;
    private int closeDuration;
    private boolean clockwise;

    protected BiFunction<E, LivingEntity, Float> speedMod = (owner, target) -> 1f;

    public static boolean behind(Entity source, Entity target) {
        Vec3 vec3 = target.getViewVector(1.0f);
        Vec3 vec31 = source.position().vectorTo(target.position()).normalize();
        vec31 = new Vec3(vec31.x, 0.0, vec31.z);
        return vec31.dot(vec3) > 0.0;
    }

    public MoveBehindBehaviour<E> speedMod(float speedModifier) {
        return this.speedMod((owner, target) -> speedModifier);
    }

    public MoveBehindBehaviour<E> speedMod(BiFunction<E, LivingEntity, Float> speedModifier) {
        this.speedMod = speedModifier;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        return !behind(entity, target);
    }

    @Override
    protected void start(E entity) {
        this.clockwise = entity.getRandom().nextBoolean();
    }

    @Override
    protected void stop(E entity) {
        this.targetPos = null;
        this.pathPos = null;
        this.closeDuration = 0;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if (target == null)
            return false;
        AnimationPlayHolder<?> animation = BrainUtils.getMemory(entity, TenshilibMemoryModules.ANIMATION_TO_PLAY.get());
        if (animation == null)
            return false;
        OrientedBoundingBox aabb = entity.prepareAttackBox(animation.animation(), target, -0.15, true);
        boolean behind = behind(entity, target);
        return !aabb.intersects(target.getBoundingBox()) || !entity.getSensing().hasLineOfSight(target)
                || (++this.closeDuration <= 30 && (this.closeDuration <= 3 || !behind));
    }

    @Override
    protected void tick(E entity) {
        OrientedBoundingBox aabb = null;
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if (this.targetPos != null && entity.tickCount % 3 == 0) {
            Vec3 dir = target.position().subtract(this.targetPos).normalize();
            float yRot = MathsHelper.YRotFrom(dir);
            AnimationPlayHolder<?> animation = BrainUtils.getMemory(entity, TenshilibMemoryModules.ANIMATION_TO_PLAY.get());
            aabb = entity.prepareAttackBox(animation.animation(), target, -0.15, false);
            double off = aabb.getOffset().y() - entity.getY();
            aabb = aabb.setPos(this.targetPos.add(0, off, 0)).rotate(yRot, aabb.getXRot());
        }
        if (this.targetPos == null || (aabb != null && !aabb.intersects(target.getBoundingBox()))) {
            // Reset position if invalid
            this.targetPos = target.position().add(Vec3.directionFromRotation(0, target.getYRot())
                    .scale(-(target.getBbWidth() * 0.5 + 0.5)));
            this.pathPos = null;
        }
        this.createAndSetPath(entity, target, this.speedMod.apply(entity, target));
    }

    protected void createAndSetPath(E entity, LivingEntity target, double speed) {
        if (this.pathPos == null || this.pathPos.isEmpty()) {
            // Calculate the arcs position
            Vec3 dir = this.targetPos.subtract(entity.position()).scale(0.5);
            dir = new Vec3(dir.x(), 0, dir.z());
            Vec3 side = dir.yRot((this.clockwise ? 90 : -90) * Mth.DEG_TO_RAD);
            List<BlockPos> pos = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                double t = i * Mth.PI / 4;
                Vec3 vec3 = side.scale(Math.sin(t)).subtract(dir.scale(Math.cos(t))).add(dir).add(entity.position());
                BlockPos newPos = new BlockPos((int) Math.round(vec3.x()), (int) Math.round(vec3.y()), (int) Math.round(vec3.z()));
                if (i != 0 && i != 4) {
                    // Try evade if too close
                    if (target.getBoundingBox().contains(Vec3.atCenterOf(newPos))) {
                        Vec3 away = vec3.subtract(target.position());
                        vec3 = vec3.add(away.scale(target.getBbWidth() * 0.5 + 0.3));
                        newPos = new BlockPos((int) Math.round(vec3.x()), (int) Math.round(vec3.y()), (int) Math.round(vec3.z()));
                    }
                }
                if (!pos.contains(newPos)) {
                    pos.add(newPos);
                }
            }
            this.pathPos = pos;
            entity.getNavigation().stop();
        }
        if (!this.pathPos.isEmpty()) {
            if (entity.getNavigation().isDone()) {
                Path path = ((PathNavigationEx) entity.getNavigation())
                        .fateubw$createPathFor(this.pathPos, 0);
                if (path != null) {
                    entity.getNavigation().moveTo(path, speed);
                }
            }
        }
    }
}
