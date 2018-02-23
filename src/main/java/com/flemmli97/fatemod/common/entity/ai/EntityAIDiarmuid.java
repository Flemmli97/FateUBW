package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIDiarmuid extends EntityAIAnimatedAttack{

	public EntityAIDiarmuid(EntityServant selectedEntity) {
		super(selectedEntity,false, 1, 15, 13, 25, 5, 1.5);
	}

	@Override
	public void updateTask() {	
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        double attackRange = (double)(this.attackingEntity.width * 2.0F * this.attackingEntity.width * 2.0F + target.width*3);

        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
		if(attackingEntity.canUseNP && attackingEntity.getOwner() == null && attackingEntity.getMana() > 20)
		{
			if (distanceToTarget <= attackRange)
	        {
				if(this.attackCoolDown == 0)
				{			
					this.attackCoolDown = this.specialAnimationDuration;
					attackingEntity.specialAnimation = specialAnimationDuration;
					attackingEntity.worldObj.setEntityState(attackingEntity, (byte)5);
				}
				else
				{
					attackCoolDown --;
					if(this.attackCoolDown == this.doSpecialAttack)
					{
						((EntityDiarmuid)attackingEntity).attackWithNP(target);
					}   
				}
	        }
			else
			{
				this.attackCoolDown = 0;
			}
		}
		else
		{
			super.updateTask();
		}		
	}
	
}
