package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.AttackType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIDiarmuid extends EntityAIAnimatedAttack{

	public EntityAIDiarmuid(EntityDiarmuid selectedEntity) {
		super(selectedEntity, false, 1, 1.5);
	}

	@Override
	public void updateTask() {	
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        double attackRange = this.attackingEntity.width * 2.0F * this.attackingEntity.width * 2.0F + target.width*3;
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);

        AnimatedAction anim = this.attackingEntity.getAnimation();
		if(anim==null && distanceToTarget <= attackRange && ((this.attackingEntity.canUseNP() && this.attackingEntity.getOwner() == null && this.attackingEntity.getMana()>=this.attackingEntity.props().hogouMana()) || this.attackingEntity.forcedNP))
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
        		((EntityDiarmuid)this.attackingEntity).attackWithNP(target);
        		this.attackingEntity.forcedNP = false;
        	}       		
        }
		else
		{
			super.updateTask();
		}		
	}
	
}
