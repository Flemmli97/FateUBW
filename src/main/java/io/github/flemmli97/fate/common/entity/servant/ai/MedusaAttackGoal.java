package io.github.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.fate.common.entity.servant.EntityMedusa;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;

public class MedusaAttackGoal extends BaseServantAttackGoal<EntityMedusa> {

    public MedusaAttackGoal(EntityMedusa entity) {
        super(entity, 1);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, EntityServant.AttackType.NP)) {
            this.attacker.faceEntity(this.target, 0, 0);
            if (anim.canAttack()) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP();
                this.attacker.forcedNP = false;
            }
        } else {
            super.handleAttack(anim);
        }
    }
}
