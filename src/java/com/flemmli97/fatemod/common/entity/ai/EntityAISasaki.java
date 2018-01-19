package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.CustomDamageSource;

import net.minecraft.entity.EntityLivingBase;

public class EntityAISasaki extends EntityAIAnimatedAttack{

	public EntityAISasaki(EntityServant selectedEntity) {
		super(selectedEntity, false, 1, 30, 15, 50, 10, 2);
	}

	@Override
	public void updateTask() {
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
		if((attackingEntity.canUseNP && attackingEntity.getOwner() == null && attackingEntity.getMana()>=attackingEntity.getSpecialMana()) || attackingEntity.forcedNP)
		{
			if(this.attackCoolDown == 0)
	        	{			
	        		this.attackCoolDown = this.specialAnimationDuration;
	        		attackingEntity.specialAnimation = specialAnimationDuration;
	        		attackingEntity.worldObj.setEntityState(attackingEntity, (byte)5);
	        	}
			else
			{
				attackCoolDown--;
		        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
				this.attackingEntity.getNavigator().tryMoveToEntityLiving(target, 1);
				if(attackCoolDown==30 && distanceToTarget<(2.5*2.5))
				{
					if(!attackingEntity.forcedNP)
						target.attackEntityFrom(CustomDamageSource.hiKen(attackingEntity), 10);
				}
				else if(attackCoolDown==20 && distanceToTarget<(2.5*2.5))
				{
					if(!attackingEntity.forcedNP)
						target.attackEntityFrom(CustomDamageSource.hiKen(attackingEntity), 15);
				}
				else if(attackCoolDown==10 && distanceToTarget<(4*4))
				{
					if(!attackingEntity.forcedNP)
            			attackingEntity.useMana(attackingEntity.getSpecialMana());
            			target.attackEntityFrom(CustomDamageSource.hiKen(attackingEntity), 25);
            			attackingEntity.forcedNP = false;
				}
			}
		}
		else
		{
			super.updateTask();
		}
	}
	
}
