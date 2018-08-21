package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIArthur extends EntityAIAnimatedAttack{
	
	public EntityAIArthur(EntityArthur selectedEntity)
	{
		super(selectedEntity,false, 1, 1);
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
            		if(!this.attackingEntity.forcedNP)
            			attackingEntity.useMana(this.attackingEntity.props().hogouMana());
            		((EntityArthur)this.attackingEntity).attackWithNP();
            		this.attackingEntity.forcedNP = false;
            	}       		
            }
		}
		else
		{
			super.updateTask();
		}		
	}	
}
