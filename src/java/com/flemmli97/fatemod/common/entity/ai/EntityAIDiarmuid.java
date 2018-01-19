package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIDiarmuid extends EntityAIAnimatedAttack{

	public EntityAIDiarmuid(EntityServant selectedEntity) {
		super(selectedEntity,false, 1, 15, 13, 15, 5, 1.5);
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
						((EntityDiarmuid)attackingEntity).attackWithNP();
					}  
					if(this.attackCoolDown == this.doSpecialAttack-2)
					{
						((EntityDiarmuid)attackingEntity).attackWithNP();
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
