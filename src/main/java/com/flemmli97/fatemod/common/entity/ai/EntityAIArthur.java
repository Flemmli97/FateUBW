package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIArthur extends EntityAIAnimatedAttack{
	
	public EntityAIArthur(EntityArthur selectedEntity)
	{
		super(selectedEntity,false, 1, 10, 5, 50, 5, 1);
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
	            	if(this.attackCoolDown == this.doSpecialAttack)
	            	{
	            		if(!attackingEntity.forcedNP)
	            		{
	            			attackingEntity.useMana(attackingEntity.getSpecialMana());
	            			((EntityArthur)attackingEntity).attackWithNP();
	            		}
	            		else
	            		{
	            			((EntityArthur)attackingEntity).attackWithNP();
	            		}
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
