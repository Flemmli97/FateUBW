package io.github.flemmli97.fateubw.common.entity.ai;

import io.github.flemmli97.fateubw.common.utils.TeleportUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;
import java.util.function.Predicate;

/**
 * Copy of FollowOwnerGoal cause thats only for Tameable
 */
public class FollowMasterGoal<T extends PathfinderMob & OwnableEntity> extends Goal {

    public final T mob;

    private Entity follow;
    private final double minTPDist;
    private int followDelay;
    public final float maxDist;
    public final float minDist;
    private float oldWaterCost;
    private final Predicate<T> additionalPred;
    private int teleportCooldown;

    public FollowMasterGoal(T mob, double teleport, float minDistance, float maxDistance) {
        this(mob, teleport, minDistance, maxDistance, s -> false);
    }

    public FollowMasterGoal(T mob, double teleport, float minDistance, float maxDistance, Predicate<T> more) {
        this.mob = mob;
        this.minTPDist = teleport * teleport;
        this.minDist = minDistance;
        this.maxDist = maxDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.additionalPred = more;
    }

    @Override
    public boolean canUse() {
        Entity livingentity = this.mob.getOwner();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.additionalPred.test(this.mob)) {
            return false;
        } else if (this.mob.distanceToSqr(livingentity) < (this.minDist * this.minDist)) {
            return false;
        } else {
            this.follow = livingentity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.getNavigation().isDone()) {
            return false;
        } else if (this.additionalPred.test(this.mob)) {
            return false;
        } else {
            return !(this.mob.distanceToSqr(this.follow) <= (this.maxDist * this.maxDist));
        }
    }

    @Override
    public void start() {
        this.followDelay = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(BlockPathTypes.WATER);
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.follow = null;
        this.mob.getNavigation().stop();
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.follow, 10.0F, this.mob.getMaxHeadXRot());
        if (--this.followDelay <= 0) {
            this.followDelay = 10;
            if (!this.mob.isLeashed()) {
                if (this.mob.distanceToSqr(this.follow) >= this.minTPDist) {
                    this.tryTeleport();
                } else {
                    this.mob.getNavigation().moveTo(this.follow, 1);
                }

            }
        }
        if (--this.teleportCooldown <= 0 && this.follow.level.dimension() != this.mob.level.dimension()) {
            if (!TeleportUtils.safeDimensionTeleport(this.mob, (ServerLevel) this.follow.getLevel(), this.follow.blockPosition())) {
                this.teleportCooldown = 10;
            }
        }
    }


    private void tryTeleport() {
        BlockPos blockpos = this.follow.blockPosition();

        for (int i = 0; i < 10; ++i) {
            int j = this.getRandomInt(-3, 3);
            int k = this.getRandomInt(-1, 1);
            int l = this.getRandomInt(-3, 3);
            boolean flag = this.tryTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }

    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs(x - this.follow.getX()) < 2.0D && Math.abs(z - this.follow.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.mob.moveTo(x + 0.5D, y, z + 0.5D, this.mob.getYRot(), this.mob.getXRot());
            this.mob.getNavigation().stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        BlockPathTypes pathnodetype = WalkNodeEvaluator.getBlockPathTypeStatic(this.mob.level, pos.mutable());
        if (pathnodetype != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockPos blockpos = pos.subtract(this.mob.blockPosition());
            return this.mob.level.noCollision(this.mob, this.mob.getBoundingBox().move(blockpos));
        }
    }

    private int getRandomInt(int min, int max) {
        return this.mob.getRandom().nextInt(max - min + 1) + min;
    }
}
