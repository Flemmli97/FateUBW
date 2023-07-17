package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.common.entity.minions.Gordius;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.EntityIskander;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

public class IskanderAttackGoal extends BaseServantAttackGoal<EntityIskander> {

    private boolean isRidingGordius;

    public IskanderAttackGoal(EntityIskander entity) {
        super(entity, 1);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, BaseServant.AttackType.NP)) {
            this.attacker.lookAt(this.target, 0, 0);
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

    @Override
    public AnimatedAction randomAttack() {
        if (this.isRidingGordius)
            return null;
        return super.randomAttack();
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.isRidingGordius = this.attacker.getVehicle() instanceof Gordius;
    }

    @Override
    public void handleIdle() {
        if (this.isRidingGordius) {
            this.attacker.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        } else
            super.handleIdle();
    }
}
