package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityGilles;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIGilles extends EntityAIAnimatedAttack{

	public EntityAIGilles(EntityServant selectedEntity) {
		super(selectedEntity,true, 5, 1);
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
	        }
        	else if(state==State.NP)
            {
            	if(this.attackingEntity.canAttack())
            	{
            		if(!attackingEntity.forcedNP)
            			attackingEntity.useMana(attackingEntity.props().hogouMana());
            		((EntityGilles)attackingEntity).attackWithNP();
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
