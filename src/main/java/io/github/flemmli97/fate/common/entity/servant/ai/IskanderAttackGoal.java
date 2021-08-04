package io.github.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.fate.common.entity.minions.EntityGordius;
import io.github.flemmli97.fate.common.entity.servant.EntityIskander;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;

public class IskanderAttackGoal extends BaseServantAttackGoal<EntityIskander> {

    private boolean isRidingGordius;

    public IskanderAttackGoal(EntityIskander entity) {
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

    @Override
    public AnimatedAction randomAttack() {
        if (this.isRidingGordius)
            return null;
        return super.randomAttack();
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.isRidingGordius = this.attacker.getRidingEntity() instanceof EntityGordius;
    }

    @Override
    public void handleIddle() {
        if (this.isRidingGordius) {
            EntityGordius gordius = (EntityGordius) this.attacker.getRidingEntity();
            if (gordius.getAttackTarget() != this.attacker.getAttackTarget())
                gordius.setAttackTarget(this.attacker.getAttackTarget());
            this.attacker.getLookController().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
        } else
            super.handleIddle();
    }
}
