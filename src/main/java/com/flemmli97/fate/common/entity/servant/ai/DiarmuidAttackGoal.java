package com.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.fate.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

public class DiarmuidAttackGoal extends BaseServantAttackGoal<EntityDiarmuid>{

    public DiarmuidAttackGoal(EntityDiarmuid entity) {
        super(entity, 1.2f);
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        return this.distanceToTargetSq < this.attackRange;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, EntityServant.AttackType.NP)) {
            if (anim.canAttack() && this.distanceToTargetSq < this.attackRange) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP(this.target);
                this.attacker.forcedNP = false;
            }
        } else {
            super.handleAttack(anim);
        }
    }
}
