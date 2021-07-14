package io.github.flemmli97.fate.common.entity.servant.ai;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.fate.common.entity.servant.EntityDiarmuid;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;

public class DiarmuidAttackGoal extends BaseServantAttackGoal<EntityDiarmuid> {

    private boolean retryNP;

    public DiarmuidAttackGoal(EntityDiarmuid entity) {
        super(entity, 1.2f);
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        return this.distanceToTargetSq < this.attackRange;
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.retryNP || (this.attacker.canUseNP() && this.attacker.getOwner() == null && this.attacker.getMana() >= this.attacker.props().hogouMana()) || this.attacker.forcedNP)
            return this.attacker.getRandomAttack(EntityServant.AttackType.NP);
        return this.attacker.getRandomAttack(EntityServant.AttackType.MELEE);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, EntityServant.AttackType.NP)) {
            if (anim.canAttack()) {
                if (this.distanceToTargetSq < this.attackRange) {
                    if (!this.attacker.forcedNP)
                        this.attacker.useMana(this.attacker.props().hogouMana());
                    this.attacker.attackWithNP(this.target);
                    this.attacker.forcedNP = false;
                    this.retryNP = false;
                } else
                    this.retryNP = true;
            }
        } else {
            super.handleAttack(anim);
        }
    }
}
