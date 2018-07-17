package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIEmiya extends EntityAIAnimatedAttack{

	public EntityAIEmiya(EntityServant selectedEntity, boolean isRanged, double speedToTarget, int animationDuration,
			int doAttackTime, int specialAnimationDuration, int doSpecialAttack, double rangeModifier) {
		super(selectedEntity, isRanged, speedToTarget, animationDuration, doAttackTime, specialAnimationDuration,
				doSpecialAttack, rangeModifier);
	}

	@Override
	public void updateTask() {	
		EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
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
        		attackCoolDown --;
        		if(this.attackCoolDown>doSpecialAttack+20)
        		this.attackingEntity.getMoveHelper().strafe(-2, 0);
            	if(this.attackCoolDown == this.doSpecialAttack)
            	{
            		if(!attackingEntity.forcedNP)
            			attackingEntity.useMana(attackingEntity.getSpecialMana());
            		((EntityEmiya)attackingEntity).attackWithNP();
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
