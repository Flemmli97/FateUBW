package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;
import com.flemmli97.fatemod.common.entity.servant.IRanged;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityAIAnimatedAttack extends EntityAIBase{

	protected World worldObj;
    protected EntityServant attackingEntity;

    protected int followTicker;

    protected double speedTowardsTarget;

    /** The PathEntity of our entity. */
    protected Path entityPathEntity;
	protected boolean evade, isRanged;
    
    protected int moveDelay;
    private int attackCooldown;
    protected double posX,posY,posZ,rangeModifier;
	
    /**doAttackTime is the time during animationDuration, when the entity should deal damage. Should be smaller than animationduration;
     * duration is in tick (20 tick = 1 sec); minecraft mob default attack speed is 20 tick;
     * default rangeModifier should be 1;
     * for ranged=true --> rangeModifier = range*/
	public EntityAIAnimatedAttack(EntityServant selectedEntity, boolean isRanged, double speedToTarget,double rangeModifier) 
	{
		this.attackingEntity = selectedEntity;
		this.speedTowardsTarget = speedToTarget;  

        this.worldObj = selectedEntity.world;
        this.setMutexBits(3);
        this.rangeModifier = rangeModifier*rangeModifier; 
        this.isRanged = isRanged;
	}

	@Override
	public boolean shouldExecute()
    {
        EntityLivingBase var1 = this.attackingEntity.getAttackTarget();

        if (var1 == null)
        {
            return false;
        }
        else if(!var1.isEntityAlive())
        {
            return false;
        }
        else if(var1.isEntityInvulnerable(DamageSource.causeMobDamage(attackingEntity)))
        {
        	return false;
        }
        else
        {
            this.entityPathEntity = this.attackingEntity.getNavigator().getPathToEntityLiving(var1);
            return this.entityPathEntity != null;
        }
    }
	
	@Override
	public boolean shouldContinueExecuting()
    {
        EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        return var1 == null ? false : (!var1.isEntityAlive() ? false : true);
    }
	
	@Override
	public void startExecuting()
    {
        this.attackingEntity.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.moveDelay = 0;
    }
	
	@Override
	public void resetTask()
    {
        this.attackingEntity.getNavigator().clearPath();
    }
	
	@Override
	public void updateTask()
    {
		if(this.attackingEntity instanceof IRanged && isRanged)
		{
			this.rangedAttack();
		}
		else
		{
			this.meleeAttack(); 
		}
    }
	
	public void meleeAttack()
	{
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        
        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        double attackRange = rangeModifier*(double)(this.attackingEntity.width * 2.2F * this.attackingEntity.width * 2.2F + target.width+1);
        --this.moveDelay;
        State state = this.attackingEntity.entityState();

        //Movement
        if (!State.isAttack(state) && (this.attackingEntity.getEntitySenses().canSee(target)) && this.moveDelay <= 0 && (this.posX == 0.0D && this.posY == 0.0D && this.posZ == 0.0D || this.attackingEntity.getRNG().nextFloat() < 1.0F))
        {
            this.posX = target.posX;
            this.posY = target.getEntityBoundingBox().minY;
            this.posZ = target.posZ;
            this.moveDelay = this.attackingEntity.getRNG().nextInt(7);

            if (distanceToTarget > 1024.0D)//32
            {
                this.moveDelay += 10;
            }
            else if (distanceToTarget > 256.0D)//16
            {
                this.moveDelay += 5;
            }

            if (!this.attackingEntity.getNavigator().tryMoveToEntityLiving(target, this.speedTowardsTarget))
            {
                this.moveDelay += 10;
            }
        }

        //Attack
        if(state==State.IDDLE)
        	--this.attackCooldown;
        if (distanceToTarget <= attackRange*1.5)
        {
        	if(state==State.IDDLE)
        	{
        		if(this.attackCooldown<=0)
        		{
	        		state = State.randomAttackState(this.attackingEntity.getRNG());
	        		this.attackingEntity.setState(state);
	        		this.attackCooldown=this.attackingEntity.attackCooldown();
        		}
        	}
        	if(State.isAttack(state))
        		this.attackingEntity.getNavigator().clearPath();
        	if(this.attackingEntity.canAttack())
        	{
        		this.attackingEntity.attackEntityAsMob(target);
        	}
        }
       /* if (distanceToTarget <= attackRange)
        {
        		this.attackingEntity.getNavigator().clearPath();
	        	if(this.attackCoolDown == 0)
	        	{
	        		this.attackCoolDown = this.animationDuration;
	        		attackingEntity.attackTimerValue = animationDuration;
	        		attackingEntity.world.setEntityState(attackingEntity, (byte)4);
	        	}
	        	else
	        	{
	        		attackCoolDown --;
	            	if(this.attackCoolDown == this.doAttackTime)
	            	{
	            		this.attackingEntity.attackEntityAsMob(target);
	            	}       		
	        	}
        }
        else
		{
			this.attackCoolDown = 0;
		} */
	}
	
	public void rangedAttack()
	{
		IRanged iRanged = (IRanged) this.attackingEntity;
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
		boolean canSee = this.attackingEntity.getEntitySenses().canSee(target);
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        --followTicker;
        State state = this.attackingEntity.entityState();

        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        //Movement
        if(!State.isAttack(state))
        {
	        if(canSee)
	        {
	        	followTicker = 60;
	        	if(distanceToTarget<64.0D)
	        	{
	        		evade = true;
	        		float strafeSideRand = this.attackingEntity.getRNG().nextFloat();
	                this.attackingEntity.getNavigator().clearPath();
	                this.attackingEntity.getMoveHelper().strafe(-0.5F, strafeSideRand < 0.5 ? 0.5F : -0.5F);
	                this.attackingEntity.faceEntity(target, 30.0F, 30.0F);
	        	}
	        	else
	        	{
	        		evade = false;
	        	}
	        }
	        else
	        {
	        		evade = false;
	        }
	        
	        if(!canSee && followTicker>0 || canSee && !evade && distanceToTarget > 81.0D)
	        {
	    		this.attackingEntity.getNavigator().tryMoveToEntityLiving(target, this.speedTowardsTarget);
	        }
        }
        
        //Attack
        if(state==State.IDDLE)
    	{
    		this.attackCooldown--;
    	}
        if (distanceToTarget <= rangeModifier && canSee)
        {
        	if(state==State.IDDLE)
        	{
        		if(this.attackCooldown<=0)
        		{
        		//this.attackingEntity.getNavigator().clearPath();
	        		state = State.randomAttackState(this.attackingEntity.getRNG());
	        		this.attackingEntity.setState(state);
	        		this.attackCooldown=this.attackingEntity.attackCooldown();
        		}
        	}
        	if(State.isAttack(state))
        		this.attackingEntity.getNavigator().clearPath();
        	if(this.attackingEntity.canAttack())
        	{
         		iRanged.attackWithRangedAttack(target);
        	}
        }
        
        /*if (distanceToTarget <= rangeModifier && canSee)
        {
         	if(this.attackCoolDown == 0)
         	{
         		this.attackCoolDown = this.animationDuration;
         		attackingEntity.attackTimerValue = animationDuration;
         		attackingEntity.world.setEntityState(attackingEntity, (byte)4);
         	}
         	else
             {
         		attackCoolDown --;
         		
             	if(this.attackCoolDown == this.doAttackTime)
             	{
             		iRanged.attackWithRangedAttack(target);
             	}       		
             }
         }
         else
 		{
 			this.attackCoolDown = 0;
 		}*/
	}
}
