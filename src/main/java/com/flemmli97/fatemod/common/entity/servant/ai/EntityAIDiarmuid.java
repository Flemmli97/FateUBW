package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIDiarmuid extends EntityAIAnimatedAttack{

	public EntityAIDiarmuid(EntityServant selectedEntity) {
		super(selectedEntity, false, 1, 1.5);
	}

	@Override
	public void updateTask() {	
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        double attackRange = this.attackingEntity.width * 2.0F * this.attackingEntity.width * 2.0F + target.width*3;

        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if(distanceToTarget <= attackRange && ((attackingEntity.canUseNP() && attackingEntity.getOwner() == null && attackingEntity.getMana()>=attackingEntity.props().hogouMana()) || attackingEntity.forcedNP))
		{
        	State state = this.attackingEntity.entityState();
	        if(state==State.IDDLE)
	        {
	        	state = State.NP;
	        	this.attackingEntity.setState(state);
	        }
	        if(state==State.NP)
            {
	        	if(this.attackingEntity.canAttack())
            	{
	        		if(!attackingEntity.forcedNP)
            			attackingEntity.useMana(attackingEntity.props().hogouMana());
					((EntityDiarmuid)attackingEntity).attackWithNP(target);
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
