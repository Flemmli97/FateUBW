package io.github.flemmli97.fate.common.entity.ai;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import io.github.flemmli97.fate.common.entity.minions.EntityPegasus;

public class PegasusAttackGoal extends AnimatedAttackGoal<EntityPegasus> {

    public PegasusAttackGoal(EntityPegasus entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        return null;
    }

    @Override
    public void handlePreAttack() {

    }

    @Override
    public void handleAttack(AnimatedAction animatedAction) {

    }

    @Override
    public void handleIddle() {

    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return 0;
    }
}
