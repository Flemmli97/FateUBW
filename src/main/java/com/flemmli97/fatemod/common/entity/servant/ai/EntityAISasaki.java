package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;
import com.flemmli97.fatemod.common.handler.CustomDamageSource;

import net.minecraft.entity.EntityLivingBase;

public class EntityAISasaki extends EntityAIAnimatedAttack{

	private int attackCount;
	private int[] damageAdd = new int [] {0,5,15};
	public EntityAISasaki(EntityServant selectedEntity) {
		super(selectedEntity, false, 1, 2);
	}

	@Override
	public void updateTask() {
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if((attackingEntity.canUseNP() && attackingEntity.getOwner() == null && attackingEntity.getMana()>=attackingEntity.props().hogouMana()) || attackingEntity.forcedNP)
		{
        	State state = this.attackingEntity.entityState();
	        if(state==State.IDDLE)
	        {
	        	state = State.NP;
	        	this.attackingEntity.setState(state);
	        }
        	if(state==State.NP)
            {
		        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
				this.attackingEntity.getNavigator().tryMoveToEntityLiving(target, 1);
        		if(this.attackingEntity.canAttack() && distanceToTarget<(2.5*2.5))
            	{
					target.attackEntityFrom(CustomDamageSource.hiKen(attackingEntity), 10*this.damageAdd[this.attackCount]);
					this.attackCount++;
					if(this.attackCount>2)
					{
            			if(!attackingEntity.forcedNP)
            				attackingEntity.useMana(attackingEntity.props().hogouMana());
						this.attackCount=0;
            			attackingEntity.forcedNP = false;
					}
            	}
			}
		}
		else
		{
			super.updateTask();
		}
	}
	
}
