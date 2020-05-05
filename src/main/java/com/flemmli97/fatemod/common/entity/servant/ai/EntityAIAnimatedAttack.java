package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.AttackType;
import com.flemmli97.fatemod.common.entity.servant.IRanged;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;

public class EntityAIAnimatedAttack<T extends EntityServant> extends EntityAIBase{

    protected T attackingEntity;
    protected int followTicker;
    protected double speedTowardsTarget;

    /** The PathEntity of our entity. */
    protected Path entityPathEntity;
	protected boolean evade, isRanged;
    
    protected int moveDelay;
    protected int attackCooldown;
    protected double posX,posY,posZ,rangeModifier;
	
    /**doAttackTime is the time during animationDuration, when the entity should deal damage. Should be smaller than animationduration;
     * duration is in tick (20 tick = 1 sec); minecraft mob default attack speed is 20 tick;
     * default rangeModifier should be 1;
     * for ranged=true --> rangeModifier = range*/
	public EntityAIAnimatedAttack(T selectedEntity, boolean isRanged, double speedToTarget,double rangeModifier) 
	{
		this.attackingEntity = selectedEntity;
		this.speedTowardsTarget = speedToTarget;  
        this.setMutexBits(3);
        this.rangeModifier = isRanged?rangeModifier*rangeModifier:rangeModifier; 
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
        else if(var1.isEntityInvulnerable(DamageSource.causeMobDamage(this.attackingEntity)))
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
		if(this.attackingEntity instanceof IRanged && this.isRanged)
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
        
        EntityLivingBase distEnt = this.attackingEntity.attacksFromMount() && this.attackingEntity.getRidingEntity() instanceof EntityLivingBase?(EntityLivingBase) this.attackingEntity.getRidingEntity():this.attackingEntity;
        double distanceToTarget = distEnt.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        double attackRange = this.rangeModifier*(double)(distEnt.width * 2.0F * distEnt.width * 2.0F + target.width);

        --this.moveDelay;
        AnimatedAction anim = this.attackingEntity.getAnimation();
       
        //Attack
        if(anim==null)
        	--this.attackCooldown;
        if (distanceToTarget <= attackRange)
        {
        	if(anim==null)
        	{
        		if(this.attackCooldown<=0)
        		{
	        		anim = this.attackingEntity.getRandomAttack(AttackType.MELEE);
	        		this.attackingEntity.setAnimation(anim);
	        		this.attackCooldown=this.attackingEntity.attackCooldown();
        		}
        	}
        }
        if(anim!=null && this.attackingEntity.canUse(anim, AttackType.MELEE))
        {
    		this.attackingEntity.getNavigator().clearPath();
	        if (distanceToTarget <= attackRange*3 && anim.canAttack())
	        {
	        	this.attackingEntity.attackEntityAsMob(target);
	        }
        }
        
        //Movement
        if (anim==null && (this.attackingEntity.getEntitySenses().canSee(target)) && this.moveDelay <= 0 && (this.posX == 0.0D && this.posY == 0.0D && this.posZ == 0.0D || this.attackingEntity.getRNG().nextFloat() < 1.0F))
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
	}
	
	public void rangedAttack()
	{
		IRanged iRanged = (IRanged) this.attackingEntity;
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
		boolean canSee = this.attackingEntity.getEntitySenses().canSee(target);
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        --this.followTicker;

        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        
        AnimatedAction anim = this.attackingEntity.getAnimation();
        
        //Attack
        if(anim==null)
    	{
    		this.attackCooldown--;
    	}
        
        if (distanceToTarget <= this.rangeModifier && canSee)
        {
        	if(anim==null)
        	{
        		if(this.attackCooldown<=0)
        		{
	        		anim = this.attackingEntity.getRandomAttack(AttackType.RANGED);
	        		this.attackingEntity.setAnimation(anim);
	        		this.attackCooldown=this.attackingEntity.attackCooldown();
	        		this.attackingEntity.motionX=0;
	        		this.attackingEntity.motionZ=0;
	        		this.attackingEntity.getNavigator().clearPath();
	        		this.attackingEntity.setMoveForward(0);
	        		this.attackingEntity.setMoveStrafing(0);
        		}
        	}
        	if(anim!=null && this.attackingEntity.canUse(anim, AttackType.RANGED))
        	{
        		this.attackingEntity.getNavigator().clearPath();
	        	if(anim.canAttack())
	        	{
	         		iRanged.attackWithRangedAttack(target);
	        	}
        	}
        }
    	
    	//Movement
        if(anim==null)
        {
	        if(canSee)
	        {
	        	this.followTicker = 60;
	        	if(distanceToTarget<64.0D)
	        	{
	        		this.evade = true;
	        		float strafeSideRand = this.attackingEntity.getRNG().nextFloat();
	                this.attackingEntity.getNavigator().clearPath();
	                this.attackingEntity.getMoveHelper().strafe(-0.5F, strafeSideRand < 0.5 ? 0.5F : -0.5F);
	                this.attackingEntity.faceEntity(target, 30.0F, 30.0F);
	        	}
	        	else
	        	{
	        		this.evade = false;
	        	}
	        }
	        else
	        {
        		this.evade = false;
	        }
	        if(!canSee && this.followTicker >0 || canSee && !this.evade && distanceToTarget > 81.0D)
	        {
	    		this.attackingEntity.getNavigator().tryMoveToEntityLiving(target, this.speedTowardsTarget);
	        }
        } 
	}
}
