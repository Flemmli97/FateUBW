package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityGilgamesh;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIGilgamesh extends EntityAIAnimatedAttack{

	public EntityAIGilgamesh(EntityServant selectedEntity, boolean isRanged, double speedToTarget, int animationDuration, int doAttackTime, int specialAnimationDuration, int doSpecialAttack, double rangeModifier) {
		super(selectedEntity,isRanged, speedToTarget, animationDuration, doAttackTime, specialAnimationDuration, doSpecialAttack, rangeModifier);
	}
	
	@Override
	public void updateTask() {	
		EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        double distanceToTarget = this.attackingEntity.getDistanceSq(var1.posX, var1.getEntityBoundingBox().minY, var1.posZ);
        double attackRange = (double)(this.attackingEntity.width * 2.0F * this.attackingEntity.width * 2.0F + var1.width*3);

        this.attackingEntity.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
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
						((EntityGilgamesh)attackingEntity).attackWithNP();
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
