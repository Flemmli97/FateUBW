package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.EntityPegasus;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIPegasusFly extends EntityAIBase{

	EntityPegasus pegasus;
	public EntityAIPegasusFly(EntityPegasus ent)
	{
		this.pegasus = ent;
	}
	
	@Override
	public boolean shouldExecute() {
		EntityLiving rider = (EntityLiving) pegasus.getRidingEntity();
		if(rider!=null)
		{
			EntityLivingBase target = rider.getAttackTarget();
			if(target!=null)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		pegasus.getNavigator().clearPath();
	}

	@Override
	public void updateTask() {
		super.updateTask();
	}

	
}
