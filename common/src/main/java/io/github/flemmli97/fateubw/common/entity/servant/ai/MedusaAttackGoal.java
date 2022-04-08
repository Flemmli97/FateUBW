package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedusa;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

public class MedusaAttackGoal extends BaseServantAttackGoal<EntityMedusa> {

    private int closeDaggerCooldown;

    public MedusaAttackGoal(EntityMedusa entity) {
        super(entity, 1);
    }

    @Override
    public AnimatedAction randomAttack() {
        if ((this.attacker.canUseNP() && this.attacker.getOwner() == null && this.attacker.getMana() >= this.attacker.props().hogouMana()) || this.attacker.forcedNP)
            return this.attacker.getRandomAttack(BaseServant.AttackType.NP);
        if (this.distanceToTargetSq > this.attackRange + 16 && this.attacker.getRandom().nextFloat() < 0.4f)
            return this.attacker.getRandomAttack(BaseServant.AttackType.RANGED);
        AnimatedAction anim = null;
        if (--this.closeDaggerCooldown <= 0 && this.attacker.getRandom().nextFloat() < 0.1f) {
            anim = this.attacker.getRandomAttack(BaseServant.AttackType.RANGED);
            this.closeDaggerCooldown = this.attacker.getRandom().nextInt(15) + 32;
        }
        return anim == null ? this.attacker.getRandomAttack(BaseServant.AttackType.MELEE) : anim;
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        if (anim == null)
            return false;
        if (anim.getID().equals(EntityMedusa.daggerAttack.getID()))
            return this.attacker.canThrow();
        return this.distanceToTargetSq < this.attackRange || this.attacker.canUse(anim, BaseServant.AttackType.NP);
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
        } else if (anim.getID().equals(EntityMedusa.daggerAttack.getID())) {
            this.attacker.lookAt(this.target, 0, 0);
            if (anim.canAttack()) {
                this.attacker.throwDaggerAt(this.target);
            }
        } else if (anim.getID().equals(EntityMedusa.daggerRetract.getID())) {
            this.moveToWithDelay(1);
        } else {
            super.handleAttack(anim);
        }
    }
}
