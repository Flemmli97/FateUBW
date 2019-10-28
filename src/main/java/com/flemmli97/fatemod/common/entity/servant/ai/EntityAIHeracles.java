package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityHeracles;

public class EntityAIHeracles extends EntityAIAnimatedAttack<EntityHeracles>{
	
	public EntityAIHeracles(EntityHeracles selectedEntity)
	{
		super(selectedEntity,false, 1, 1.2);
	}

}
