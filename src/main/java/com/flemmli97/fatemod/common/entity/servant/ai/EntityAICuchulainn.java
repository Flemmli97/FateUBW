package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.AttackType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAICuchulainn extends EntityAIAnimatedAttack{
	
	public EntityAICuchulainn(EntityCuchulainn selectedEntity) 
	{
		super(selectedEntity, false, 1, 1.3);
	}

	@Override
	public void updateTask() {				
		EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
        AnimatedAction anim = this.attackingEntity.getAnimation();
		if(anim==null && this.attackingEntity.onGround && ((this.attackingEntity.canUseNP() && this.attackingEntity.getOwner() == null && this.attackingEntity.getMana()>=this.attackingEntity.props().hogouMana()) || this.attackingEntity.forcedNP))
		{
        	anim = this.attackingEntity.getRandomAttack(AttackType.NP);
        	this.attackingEntity.setAnimation(anim);
        	this.attackingEntity.motionX=0;
        	this.attackingEntity.motionZ=0;
    		Vec3d back = this.attackingEntity.getLookVec().scale(-1.2);
    		this.attackingEntity.setVelocity(back.x, 1.1, back.z);
		}
		if(anim!=null && this.attackingEntity.canUse(anim, AttackType.NP))
        {
        	if(anim.canAttack() && !this.attackingEntity.onGround)
        	{
        		this.attackingEntity.motionX=0;
        		this.attackingEntity.motionZ=0;
        		this.attackingEntity.motionY=0;
        		if(!this.attackingEntity.forcedNP)
        			this.attackingEntity.useMana(this.attackingEntity.props().hogouMana());
    			((EntityCuchulainn)this.attackingEntity).attackWithNP();
    			this.attackingEntity.forcedNP = false;
        	}       		
        }
		else if(this.attackingEntity.onGround)
		{
			super.updateTask();
		}
	}
}
