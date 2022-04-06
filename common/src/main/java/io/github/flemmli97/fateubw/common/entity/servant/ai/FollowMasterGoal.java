package io.github.flemmli97.fateubw.common.entity.servant.ai;

import net.minecraft.core.BlockPos;
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

    public final T goalOwner;
    private Entity follow;
    private double minTPDist;
    private int followDelay;
    public final float maxDist;
    public final float minDist;
    private float oldWaterCost;
    private final Predicate<T> additionalPred;

    public FollowMasterGoal(T goalOwner, double teleport, float minDistance, float maxDistance) {
        this(goalOwner, teleport, minDistance, maxDistance, s -> false);
    }

    public FollowMasterGoal(T goalOwner, double teleport, float minDistance, float maxDistance, Predicate<T> more) {
        this.goalOwner = goalOwner;
        this.minTPDist = teleport * teleport;
        this.minDist = minDistance;
        this.maxDist = maxDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.additionalPred = more;
    }

    @Override
    public boolean canUse() {
        Entity livingentity = this.goalOwner.getOwner();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.additionalPred.test(this.goalOwner)) {
            return false;
        } else if (this.goalOwner.distanceToSqr(livingentity) < (this.minDist * this.minDist)) {
            return false;
        } else {
            this.follow = livingentity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.goalOwner.getNavigation().isDone()) {
            return false;
        } else if (this.additionalPred.test(this.goalOwner)) {
            return false;
        } else {
            return !(this.goalOwner.distanceToSqr(this.follow) <= (this.maxDist * this.maxDist));
        }
    }

    @Override
    public void start() {
        this.followDelay = 0;
        this.oldWaterCost = this.goalOwner.getPathfindingMalus(BlockPathTypes.WATER);
        this.goalOwner.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.follow = null;
        this.goalOwner.getNavigation().stop();
        this.goalOwner.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.goalOwner.getLookControl().setLookAt(this.follow, 10.0F, this.goalOwner.getMaxHeadXRot());
        if (--this.followDelay <= 0) {
            this.followDelay = 10;
            if (!this.goalOwner.isLeashed()) {
                if (this.goalOwner.distanceToSqr(this.follow) >= this.minTPDist) {
                    this.tryTeleport();
                } else {
                    this.goalOwner.getNavigation().moveTo(this.follow, 1);
                }

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
            this.goalOwner.moveTo(x + 0.5D, y, z + 0.5D, this.goalOwner.getYRot(), this.goalOwner.getXRot());
            this.goalOwner.getNavigation().stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        BlockPathTypes pathnodetype = WalkNodeEvaluator.getBlockPathTypeStatic(this.goalOwner.level, pos.mutable());
        if (pathnodetype != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockPos blockpos = pos.subtract(this.goalOwner.blockPosition());
            return this.goalOwner.level.noCollision(this.goalOwner, this.goalOwner.getBoundingBox().move(blockpos));
        }
    }

    private int getRandomInt(int min, int max) {
        return this.goalOwner.getRandom().nextInt(max - min + 1) + min;
    }
}
