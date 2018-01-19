package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowMaster extends EntityAIBase
{
    private EntityServant servant;
    private Entity theOwner;
    World theWorld;
    private double d1;
    private PathNavigate petPathfinder;
    private int int1;
    float maxDist;
    float minDist;
    private float avoidWater;

    public EntityAIFollowMaster(EntityServant servant, double teleport, float minDistance, float maxDistance)
    {
        this.servant = servant;
        this.theWorld = servant.worldObj;
        this.d1 = teleport;
        this.petPathfinder = servant.getNavigator();
        this.minDist = minDistance;
        this.maxDist = maxDistance;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        Entity var1 = this.servant.getOwner();

        if (var1 == null)
        {
            return false;
        }
        else if (this.servant.getDistanceSqToEntity(var1) < (double)(this.minDist * this.minDist))
        {
            return false;
        }
        else if(this.servant.getAttackTarget() != null)
        {
        		return false;
        }
        else
        {
            this.theOwner = var1;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.petPathfinder.noPath() && this.servant.getDistanceSqToEntity(this.theOwner) 
        		> (double)(this.maxDist * this.maxDist);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.int1 = 0;
        this.avoidWater = this.servant.getPathPriority(PathNodeType.WATER);
        this.servant.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        this.servant.setPathPriority(PathNodeType.WATER, avoidWater);
    }
    
    private boolean isEmptyBlock(BlockPos pos)
    {
        IBlockState iblockstate = this.theWorld.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR ? true : !iblockstate.isFullCube();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.servant.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.servant.getVerticalFaceSpeed());
        
            if (--this.int1 <= 0)
            {
                this.int1 = 10;

                if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.d1))
                {
                    if (!this.servant.getLeashed())
                    {
                        if (this.servant.getDistanceSqToEntity(this.theOwner) >= 576.0D)
                        {
                            int i = MathHelper.floor_double(this.theOwner.posX) - 2;
                            int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
                            int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l)
                            {
                                for (int i1 = 0; i1 <= 4; ++i1)
                                {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isOpaqueCube() && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1)))
                                    {
                                        this.servant.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.servant.rotationYaw, this.servant.rotationPitch);
                                        this.petPathfinder.clearPathEntity();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        
    }
}