package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntitySasaki;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.AttackType;
import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;

public class EntityAISasaki extends EntityAIAnimatedAttack{

	private int attackCount;
	private int[] damageAdd = new int [] {0,5,15};
	public EntityAISasaki(EntitySasaki selectedEntity) {
		super(selectedEntity, false, 1, 1.5);
	}

	@Override
	public void updateTask() {
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        AnimatedAction anim = this.attackingEntity.getAnimation();
		if(anim==null && ((attackingEntity.canUseNP() && attackingEntity.getOwner() == null && attackingEntity.getMana()>=attackingEntity.props().hogouMana()) || attackingEntity.forcedNP))
		{
        	anim = this.attackingEntity.getRandomAttack(AttackType.NP);
        	this.attackingEntity.setAnimation(anim);		
		}
		if(anim!=null && this.attackingEntity.canUse(anim, AttackType.NP))
        {
			double distanceToTarget = this.attackingEntity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
			this.attackingEntity.getNavigator().tryMoveToEntityLiving(target, 1);
        	if(((EntitySasaki)this.attackingEntity).canAttackNP() && distanceToTarget<(2.5*2.5))
        	{
        		target.attackEntityFrom(CustomDamageSource.hiKen(attackingEntity), 10+this.damageAdd[this.attackCount]);
				this.attackCount++;
				if(this.attackCount>2)
				{
        			if(!attackingEntity.forcedNP)
        				this.attackingEntity.useMana(attackingEntity.props().hogouMana());
					this.attackCount=0;
        			this.attackingEntity.forcedNP = false;
        			this.attackCooldown=10;
				}
        	}       		
        }
		else
		{
			super.updateTask();
		}
	}
	
}
