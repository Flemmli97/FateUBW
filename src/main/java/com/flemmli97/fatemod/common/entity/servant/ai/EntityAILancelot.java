package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityLancelot;

public class EntityAILancelot extends EntityAIAnimatedAttack<EntityLancelot>{

	public EntityAILancelot(EntityLancelot selectedEntity)
	{
		super(selectedEntity,false, 1, 1);
	}

	@Override
	public void updateTask() {
		super.updateTask();
	}
}
