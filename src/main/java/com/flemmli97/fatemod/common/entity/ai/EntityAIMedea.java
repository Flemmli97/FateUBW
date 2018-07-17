package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

public class EntityAIMedea extends EntityAIAnimatedAttack{

	public EntityAIMedea(EntityServant selectedEntity) {
		super(selectedEntity, true, 1, 15, 5, 15,
				5, 16);
	}

}
