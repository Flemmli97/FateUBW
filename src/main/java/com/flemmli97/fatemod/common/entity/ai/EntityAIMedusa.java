package com.flemmli97.fatemod.common.entity.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

public class EntityAIMedusa extends EntityAIAnimatedAttack{

	public EntityAIMedusa(EntityServant selectedEntity, boolean isRanged, double speedToTarget, int animationDuration,
			int doAttackTime, int specialAnimationDuration, int doSpecialAttack, double rangeModifier) {
		super(selectedEntity, isRanged, speedToTarget, animationDuration, doAttackTime, specialAnimationDuration,
				doSpecialAttack, rangeModifier);
	}

}
