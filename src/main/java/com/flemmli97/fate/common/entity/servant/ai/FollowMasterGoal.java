package com.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.tenshilib.api.entity.IOwnable;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;
import java.util.function.Predicate;

/**
 * Copy of FollowOwnerGoal cause thats only for Tameable
 */
public class FollowMasterGoal<T extends CreatureEntity & IOwnable> extends Goal {

    public final T goalOwner;
    private LivingEntity follow;
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
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        this.additionalPred = more;
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity livingentity = this.goalOwner.getOwner();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.additionalPred.test(this.goalOwner)) {
            return false;
        } else if (this.goalOwner.getDistanceSq(livingentity) < (this.minDist * this.minDist)) {
            return false;
        } else {
            this.follow = livingentity;
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.goalOwner.getNavigator().noPath()) {
            return false;
        } else if (this.additionalPred.test(this.goalOwner)) {
            return false;
        } else {
            return !(this.goalOwner.getDistanceSq(this.follow) <= (this.maxDist * this.maxDist));
        }
    }

    @Override
    public void startExecuting() {
        this.followDelay = 0;
        this.oldWaterCost = this.goalOwner.getPathPriority(PathNodeType.WATER);
        this.goalOwner.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        this.follow = null;
        this.goalOwner.getNavigator().clearPath();
        this.goalOwner.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.goalOwner.getLookController().setLookPositionWithEntity(this.follow, 10.0F, this.goalOwner.getVerticalFaceSpeed());
        if (--this.followDelay <= 0) {
            this.followDelay = 10;
            if (!this.goalOwner.getLeashed() && !this.goalOwner.isPassenger()) {
                if (this.goalOwner.getDistanceSq(this.follow) >= minTPDist) {
                    this.tryTeleport();
                } else {
                    this.goalOwner.getNavigator().tryMoveToEntityLiving(this.follow, 1);
                }

            }
        }
    }


    private void tryTeleport() {
        BlockPos blockpos = this.follow.getBlockPos();

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
            this.goalOwner.setLocationAndAngles(x + 0.5D, y, z + 0.5D, this.goalOwner.rotationYaw, this.goalOwner.rotationPitch);
            this.goalOwner.getNavigator().clearPath();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathNodeType pathnodetype = WalkNodeProcessor.getLandNodeType(this.goalOwner.world, pos.mutableCopy());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockPos blockpos = pos.subtract(this.goalOwner.getBlockPos());
            return this.goalOwner.world.isSpaceEmpty(this.goalOwner, this.goalOwner.getBoundingBox().offset(blockpos));
        }
    }

    private int getRandomInt(int min, int max) {
        return this.goalOwner.getRNG().nextInt(max - min + 1) + min;
    }
}
