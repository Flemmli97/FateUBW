package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

public class EntityAIHeracles extends EntityAIAnimatedAttack{
	
	public EntityAIHeracles(EntityServant selectedEntity)
	{
		super(selectedEntity,false, 1, 15, 5, 15, 5, 1.5);
	}

}
