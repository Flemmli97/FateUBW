package com.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.fate.common.entity.servant.EntityServant;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

/**
 * Copy of FollowOwnerGoal cause thats only for Tameable
 */
public class FollowMasterGoal extends Goal {

    public final EntityServant servant;
    private LivingEntity follow;
    private double minTPDist;
    private int followDelay;
    public final float maxDist;
    public final float minDist;
    private float oldWaterCost;

    public FollowMasterGoal(EntityServant servant, double teleport, float minDistance, float maxDistance) {
        this.servant = servant;
        this.minTPDist = teleport * teleport;
        this.minDist = minDistance;
        this.maxDist = maxDistance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity livingentity = this.servant.getOwner();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.servant.isStaying()) {
            return false;
        } else if (this.servant.getDistanceSq(livingentity) < (this.minDist * this.minDist)) {
            return false;
        } else {
            this.follow = livingentity;
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.servant.getNavigator().noPath()) {
            return false;
        } else if (this.servant.isStaying()) {
            return false;
        } else {
            return !(this.servant.getDistanceSq(this.follow) <= (this.maxDist * this.maxDist));
        }
    }

    @Override
    public void startExecuting() {
        this.followDelay = 0;
        this.oldWaterCost = this.servant.getPathPriority(PathNodeType.WATER);
        this.servant.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        this.follow = null;
        this.servant.getNavigator().clearPath();
        this.servant.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.servant.getLookController().setLookPositionWithEntity(this.follow, 10.0F, this.servant.getVerticalFaceSpeed());
        if (--this.followDelay <= 0) {
            this.followDelay = 10;
            if (!this.servant.getLeashed() && !this.servant.isPassenger()) {
                if (this.servant.getDistanceSq(this.follow) >= minTPDist) {
                    this.tryTeleport();
                } else {
                    this.servant.getNavigator().tryMoveToEntityLiving(this.follow, 1);
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
            this.servant.setLocationAndAngles(x + 0.5D, y, z + 0.5D, this.servant.rotationYaw, this.servant.rotationPitch);
            this.servant.getNavigator().clearPath();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathNodeType pathnodetype = WalkNodeProcessor.getLandNodeType(this.servant.world, pos.mutableCopy());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockPos blockpos = pos.subtract(this.servant.getBlockPos());
            return this.servant.world.isSpaceEmpty(this.servant, this.servant.getBoundingBox().offset(blockpos));
        }
    }

    private int getRandomInt(int min, int max) {
        return this.servant.getRNG().nextInt(max - min + 1) + min;
    }
}
