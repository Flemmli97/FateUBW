//package io.github.flemmli97.fateubw.common.entity.ai;
//
//import io.github.flemmli97.fateubw.common.utils.MathsHelper;
//import io.github.flemmli97.fateubw.mixinhelper.PathNavigationEx;
//import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
//import io.github.flemmli97.tenshilib.api.entity.AoeAttackEntity;
//import io.github.flemmli97.tenshilib.api.entity.IAnimated;
//import io.github.flemmli97.tenshilib.common.entity.ai.animated.ActionRun;
//import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
//import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.PathfinderMob;
//import net.minecraft.world.level.pathfinder.Path;
//import net.minecraft.world.phys.Vec3;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MoveBehindAttackRunner<T extends PathfinderMob & IAnimated & AoeAttackEntity> implements ActionRun<T> {
//
//    private final double speed;
//    private final boolean needsLoS;
//
//    private Vec3 targetPos;
//    private List<BlockPos> pathPos;
//    private int closeDuration;
//    private int circleDirection = -1;
//
//    public MoveBehindAttackRunner(double speed) {
//        this(speed, true);
//    }
//
//    public MoveBehindAttackRunner(double speed, boolean needsLoS) {
//        this.speed = speed;
//        this.needsLoS = needsLoS;
//    }
//
//    public static boolean behind(Entity source, Entity target) {
//        Vec3 vec3 = target.getViewVector(1.0f);
//        Vec3 vec31 = source.position().vectorTo(target.position()).normalize();
//        vec31 = new Vec3(vec31.x, 0.0, vec31.z);
//        return vec31.dot(vec3) > 0.0;
//    }
//
//    @Override
//    public boolean run(AnimatedAttackGoal<T> goal, LivingEntity target, AnimatedAction anim) {
//        if (anim == null)
//            return false;
//        if (this.circleDirection == -1)
//            this.circleDirection = goal.attacker.getRandom().nextInt(2);
//        OrientedBoundingBox aabb = null;
//        if (this.targetPos != null && goal.attacker.tickCount % 3 == 0) {
//            Vec3 dir = target.position().subtract(this.targetPos).normalize();
//            float yRot = MathsHelper.YRotFrom(dir);
//            aabb = goal.attacker.prepareAttackBox(anim, target, -0.15, false);
//            double off = aabb.getOffset().y() - goal.attacker.getY();
//            aabb = aabb.setPos(this.targetPos.add(0, off, 0)).rotate(yRot, aabb.getXRot());
//        }
//        if (this.targetPos == null || (aabb != null && !aabb.intersects(target.getBoundingBox()))) {
//            // Reset position if invalid
//            this.targetPos = target.position().add(Vec3.directionFromRotation(0, target.getYRot())
//                    .scale(-(target.getBbWidth() * 0.5 + 0.5)));
//            this.pathPos = null;
//        }
//        aabb = goal.attacker.prepareAttackBox(anim, target, -0.15, true);
//        boolean behind = behind(goal.attacker, target);
//        if (aabb.intersects(target.getBoundingBox()) && (!this.needsLoS || goal.canSee) && (++this.closeDuration > 30 || (this.closeDuration > 3 && behind))) {
//            goal.attacker.getLookControl().setLookAt(target, 360, 90);
//            return true;
//        }
//        this.moveToPosition(goal.attacker, target, this.speed);
//        return false;
//    }
//
//    /**
//     * Move in an arc to the position
//     */
//    public void moveToPosition(T entity, LivingEntity target, double speed) {
//        if (this.pathPos == null || this.pathPos.isEmpty()) {
//            // Calculate the arcs position
//            Vec3 dir = this.targetPos.subtract(entity.position()).scale(0.5);
//            dir = new Vec3(dir.x(), 0, dir.z());
//            Vec3 side = dir.yRot(this.circleDirection == 1 ? 90 : -90);
//            List<BlockPos> pos = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                double t = i * Mth.PI / 4;
//                Vec3 vec3 = side.scale(Math.sin(t)).subtract(dir.scale(Math.cos(t))).add(dir).add(entity.position());
//                BlockPos newPos = new BlockPos(Math.round(vec3.x()), Math.round(vec3.y()), Math.round(vec3.z()));
//                if (i != 0 && i != 4) {
//                    // Try evade if too close
//                    if (target.getBoundingBox().contains(Vec3.atCenterOf(newPos))) {
//                        Vec3 away = vec3.subtract(target.position());
//                        vec3 = vec3.add(away.scale(target.getBbWidth() * 0.5 + 0.3));
//                        newPos = new BlockPos(Math.round(vec3.x()), Math.round(vec3.y()), Math.round(vec3.z()));
//                    }
//                }
//                if (!pos.contains(newPos)) {
//                    pos.add(newPos);
//                }
//            }
//            this.pathPos = pos;
//            entity.getNavigation().stop();
//        }
//        if (!this.pathPos.isEmpty()) {
//            if (entity.getNavigation().isDone()) {
//                Path path = ((PathNavigationEx) entity.getNavigation())
//                        .fateubw$createPathFor(this.pathPos, 0);
//                if (path != null) {
//                    entity.getNavigation().moveTo(path, speed);
//                }
//            }
//        }
//    }
//}
