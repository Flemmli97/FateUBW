package io.github.flemmli97.fateubw.common.entity.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.common.utils.TeleportUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class TeleportBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = MemoryTest.builder(1)
            .hasMemories(MemoryModuleType.ATTACK_TARGET);

    private double minDistSqr = 9, maxDistSqr = 100;
    private int teleportMin = 5, teleportMax = 10;

    public TeleportBehaviour<E> min(double min) {
        this.minDistSqr = min * min;
        return this;
    }

    public TeleportBehaviour<E> max(double max) {
        this.maxDistSqr = max * max;
        return this;
    }

    public TeleportBehaviour<E> teleportMin(int teleportMin) {
        this.teleportMin = teleportMin;
        return this;
    }

    public TeleportBehaviour<E> teleportMax(int teleportMax) {
        this.teleportMax = Math.max(this.teleportMin + 1, teleportMax);
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        for (int i = 0; i < 32; ++i) {
            Vec3 posAway;
            if (!entity.isWithinRestriction() && entity.getRandom().nextFloat() < 0.5) {
                posAway = DefaultRandomPos.getPosTowards(entity, entity.getRandom().nextInt(this.teleportMax - this.teleportMin) + this.teleportMin,
                        8, Vec3.atCenterOf(entity.getRestrictCenter()), 90 * Mth.DEG_TO_RAD);
            } else if (target.distanceToSqr(entity) > this.maxDistSqr) {
                posAway = DefaultRandomPos.getPosTowards(entity, entity.getRandom().nextInt(this.teleportMax - this.teleportMin) + this.teleportMin, 8, target.position(), 90 * Mth.DEG_TO_RAD);
            } else {
                posAway = DefaultRandomPos.getPosAway(entity, entity.getRandom().nextInt(this.teleportMax - this.teleportMin) + this.teleportMin, 8, target.position());
            }
            if (posAway != null && target.distanceToSqr(posAway) >= this.minDistSqr) {
                if (entity.isWithinRestriction(BlockPos.containing(posAway))) {
                    TeleportUtils.teleportTo(entity, posAway.x(), posAway.y(), posAway.z(),
                            SoundEvents.ENDERMAN_TELEPORT, ParticleTypes.WITCH);
                }
            }
        }
    }
}
