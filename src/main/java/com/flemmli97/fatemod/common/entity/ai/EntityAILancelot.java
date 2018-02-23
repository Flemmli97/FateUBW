package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

public class EntityAILancelot extends EntityAIAnimatedAttack{

	public EntityAILancelot(EntityServant selectedEntity)
	{
		super(selectedEntity,false, 1, 15, 6, 15, 5, 1);
	}

	@Override
	public void updateTask() {
		// TODO Auto-generated method stub
		super.updateTask();
	}
	
	
}
