package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAICuchulainn extends EntityAIAnimatedAttack{
	
	public EntityAICuchulainn(EntityServant selectedEntity) 
	{
		super(selectedEntity, false, 1, 1.3);
	}

	@Override
	public void updateTask() {				
		EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
        if((attackingEntity.canUseNP() && attackingEntity.getOwner() == null && attackingEntity.getMana()>=attackingEntity.props().hogouMana()) || attackingEntity.forcedNP)
		{
        	State state = this.attackingEntity.entityState();
	        if(state==State.IDDLE)
	        {			
	        	this.attackingEntity.setState(State.NP);
        		attackingEntity.motionX=0;
        		attackingEntity.motionZ=0;
        		Vec3d back = attackingEntity.getLookVec().scale(-2);
        		attackingEntity.setVelocity(back.x, 1.2, back.z);
        	}
	        else if(state==State.NP)
            {
	        	if(this.attackingEntity.canAttack() && !attackingEntity.onGround)
            	{
	        		attackingEntity.motionX=0;
	        		attackingEntity.motionZ=0;
	        		attackingEntity.motionY=0;
            		if(!attackingEntity.forcedNP)
            			attackingEntity.useMana(attackingEntity.props().hogouMana());
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
