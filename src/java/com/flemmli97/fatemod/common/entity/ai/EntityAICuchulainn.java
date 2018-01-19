package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAICuchulainn extends EntityAIAnimatedAttack{
	
	public EntityAICuchulainn(EntityServant selectedEntity) 
	{
		super(selectedEntity,false, 1, 15, 13, 15, 5, 1.3);
	}

	@Override
	public void updateTask() {				
		EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
		if((attackingEntity.canUseNP && attackingEntity.getOwner() == null && attackingEntity.getMana() > attackingEntity.getSpecialMana()) || attackingEntity.forcedNP)
		{
			if(this.attackCoolDown == 0)
        	{			

        		this.attackCoolDown = this.specialAnimationDuration;
        		attackingEntity.specialAnimation = specialAnimationDuration;
        		attackingEntity.motionX=0;
        		attackingEntity.motionZ=0;
        		Vec3d back = attackingEntity.getLookVec().scale(-2);
        		attackingEntity.setVelocity(back.xCoord, 1.2, back.zCoord);
        		attackingEntity.worldObj.setEntityState(attackingEntity, (byte)5);
        	}
        	else
            {
        		attackCoolDown --;
        		attackingEntity.motionX=0;
        		attackingEntity.motionZ=0;
            	if(this.attackCoolDown == this.doSpecialAttack && !attackingEntity.onGround)
            	{
            		if(!attackingEntity.forcedNP)
            			attackingEntity.useMana(attackingEntity.getSpecialMana());	
            		((EntityCuchulainn)attackingEntity).attackWithNP();
            		attackingEntity.forcedNP = false;

            	}       		
            }
		}
		else if(attackingEntity.onGround)
		{
			super.updateTask();
		}
	}
}
