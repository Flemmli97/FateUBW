package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.EntityLesserMonster;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;

public class EntityAIStarfishAttack extends EntityAIBase{

    protected EntityLesserMonster attackingEntity;
    protected int followTicker;
    protected double speedTowardsTarget;

    /** The PathEntity of our entity. */
    protected Path entityPathEntity;
	protected boolean evade, isRanged;
    
    protected int moveDelay;
    private int attackCooldown;
    protected double posX,posY,posZ;

	public EntityAIStarfishAttack(EntityLesserMonster selectedEntity, double speedToTarget) 
	{
		this.attackingEntity = selectedEntity;
		this.speedTowardsTarget = speedToTarget;  
        this.setMutexBits(3);
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
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        
        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        double attackRange = (double)(this.attackingEntity.width * 2.0F * this.attackingEntity.width * 2.0F + target.width);

        --this.moveDelay;
        AnimatedAction anim = this.attackingEntity.getAnimation();
        //Attack
        boolean isAttack = anim!=null && anim.getID().equals("attack");
        if(!isAttack)
        	--this.attackCooldown;
        if (distanceToTarget <= attackRange)
        {
        	if(!isAttack)
        	{
        		if(this.attackCooldown<=0)
        		{
        			anim = EntityLesserMonster.attack;
        			this.attackingEntity.setAnimation(anim);
        		}
        	}
        }
        if(anim!=null&&anim.getID().equals("attack"))
        {
    		this.attackingEntity.getNavigator().clearPath();
	        if (distanceToTarget <= attackRange*3 && this.attackingEntity.getAnimation().canAttack())
	        {
	        	this.attackingEntity.attackEntityAsMob(target);
	        }
        }
        //Movement
        if ((anim==null||!anim.getID().equals("attack")) && (this.attackingEntity.getEntitySenses().canSee(target)) && this.moveDelay <= 0 && (this.posX == 0.0D && this.posY == 0.0D && this.posZ == 0.0D || this.attackingEntity.getRNG().nextFloat() < 1.0F))
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
}
