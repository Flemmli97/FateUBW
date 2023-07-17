package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.EntityGilles;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

public class GillesAttackGoal extends BaseServantAttackGoal<EntityGilles> {

    private final float shootRangeSq;
    private boolean idleFlag, clockwise;

    public GillesAttackGoal(EntityGilles entity, float shootRange) {
        super(entity, 1);
        this.shootRangeSq = shootRange * shootRange;
    }

    @Override
    public AnimatedAction randomAttack() {
        if ((this.attacker.canUseNP() && this.attacker.getOwner() == null && this.attacker.getMana() >= this.attacker.props().hogouMana()) || this.attacker.forcedNP)
            return this.attacker.getRandomAttack(BaseServant.AttackType.NP);
        return this.attacker.getRandomAttack(BaseServant.AttackType.MELEE);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, BaseServant.AttackType.NP)) {
            this.attacker.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            if (anim.canAttack()) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP();
                this.attacker.forcedNP = false;
            }
        } else {
            this.attacker.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            if (anim.canAttack() && this.distanceToTargetSq <= this.shootRangeSq)
                this.attacker.attackWithRangedAttack(this.target);
        }
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, BaseServant.AttackType.RANGED))
            return this.distanceToTargetSq < this.shootRangeSq;
        return this.attacker.canUse(anim, BaseServant.AttackType.NP);
    }

    @Override
    public void handlePreAttack() {
        this.idleFlag = false;
        super.handlePreAttack();
    }

    @Override
    public void handleIdle() {
        if (this.distanceToTargetSq < 9)
            this.randomPosAwayFrom(this.target, 8);
        else {
            if (!this.idleFlag) {
                this.clockwise = this.attacker.getRandom().nextBoolean();
                this.idleFlag = true;
            }
            this.circleAroundTargetFacing(8, this.clockwise, 0.8f);
        }
    }
}
