package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityMedusa;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.AttackType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIMedusa extends EntityAIAnimatedAttack<EntityMedusa>{

	public EntityAIMedusa(EntityMedusa selectedEntity) {
		super(selectedEntity, false, 1, 1);
	}

	@Override
	public void updateTask() {	
		EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
		AnimatedAction anim = this.attackingEntity.getAnimation();
		if(anim==null && ((this.attackingEntity.canUseNP() && this.attackingEntity.getOwner() == null && this.attackingEntity.getMana()>=this.attackingEntity.props().hogouMana()) || this.attackingEntity.forcedNP))
		{
        	anim = this.attackingEntity.getRandomAttack(AttackType.NP);
        	this.attackingEntity.setAnimation(anim);		
		}
		if(anim!=null && this.attackingEntity.canUse(anim, AttackType.NP))
        {
        	if(anim.canAttack())
        	{
        		if(!this.attackingEntity.forcedNP)
        			this.attackingEntity.useMana(this.attackingEntity.props().hogouMana());
        		this.attackingEntity.attackWithNP();
        		this.attackingEntity.forcedNP = false;
        	}       		
        }
		else
		{
			super.updateTask();
		}		
	}	
}
