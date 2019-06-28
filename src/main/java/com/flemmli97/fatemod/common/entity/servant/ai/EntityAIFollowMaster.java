package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowMaster extends EntityAIBase
{
    public final EntityServant servant;
    private EntityLivingBase follow;
    World theWorld;
    private double minTPDist;
    private PathNavigate pathfinder;
    private int followDelay;
    public final float maxDist;
    public final float minDist;
    private float avoidWater;

    public EntityAIFollowMaster(EntityServant servant, double teleport, float minDistance, float maxDistance)
    {
        this.servant = servant;
        this.theWorld = servant.world;
        this.minTPDist = teleport;
        this.pathfinder = servant.getNavigator();
        this.minDist = minDistance;
        this.maxDist = maxDistance;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
    	EntityLivingBase owner = this.servant.getOwner();
        if (owner == null)
        {
            return false;
        }
        else if (this.servant.getDistanceSq(owner) < (double)(this.minDist * this.minDist))
        {
            return false;
        }
        else if(this.servant.getAttackTarget() != null)
        {
    		return false;
        }
        else
        {
        	this.setFollowTarget(owner);
            return true;
        }
    }
    
    public void setFollowTarget(EntityLivingBase entity)
    {
    	this.follow=entity;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
	public boolean shouldContinueExecuting()
    {
        return !this.pathfinder.noPath() && this.servant.getDistanceSq(this.follow) > (this.maxDist * this.maxDist);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
        this.followDelay = 0;
        this.avoidWater = this.servant.getPathPriority(PathNodeType.WATER);
        this.servant.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    @Override
	public void resetTask()
    {
        this.follow = null;
        this.pathfinder.clearPath();
        this.servant.setPathPriority(PathNodeType.WATER, avoidWater);
    }

    /**
     * Updates the task
     */
    @Override
	public void updateTask()
    {
        this.servant.getLookHelper().setLookPositionWithEntity(this.follow, 10.0F, (float)this.servant.getVerticalFaceSpeed());
        
            if (--this.followDelay <= 0)
            {
                this.followDelay = 10;

                if (!this.pathfinder.tryMoveToEntityLiving(this.follow, 1))
                {
                    if (this.servant.getDistanceSq(this.follow) >= this.minTPDist*this.minTPDist && !this.servant.isRiding())
                    {
                    	//Try teleport
                        int i = MathHelper.floor(this.follow.posX) - 2;
                        int j = MathHelper.floor(this.follow.posZ) - 2;
                        int k = MathHelper.floor(this.follow.getEntityBoundingBox().minY);

                        for (int l = 0; l <= 4; ++l)
                        {
                            for (int i1 = 0; i1 <= 4; ++i1)
                            {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1))
                                {
                                    this.servant.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.servant.rotationYaw, this.servant.rotationPitch);
                                    this.pathfinder.clearPath();
                                    return;
                                }
                            }
                        }
                    }
                }
            } 
    }
    
    protected boolean isTeleportFriendlyBlock(int x, int p_192381_2_, int y, int p_192381_4_, int p_192381_5_)
    {
        BlockPos blockpos = new BlockPos(x + p_192381_4_, y - 1, p_192381_2_ + p_192381_5_);
        IBlockState iblockstate = this.servant.world.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(this.servant.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && this.servant.world.isAirBlock(blockpos.up()) && this.servant.world.isAirBlock(blockpos.up(2));
    }
}