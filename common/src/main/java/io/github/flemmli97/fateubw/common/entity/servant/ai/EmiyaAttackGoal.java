package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.EntityEmiya;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

public class EmiyaAttackGoal extends BaseServantAttackGoal<EntityEmiya> {

    private boolean doRanged;
    private final float shootRangeSq;
    private boolean iddleFlag, clockwise;

    public EmiyaAttackGoal(EntityEmiya entity, float shootRange) {
        super(entity, 1);
        this.shootRangeSq = shootRange * shootRange;
    }

    @Override
    public AnimatedAction randomAttack() {
        if ((this.attacker.canUseNP() && this.attacker.getOwner() == null && this.attacker.getMana() >= this.attacker.props().hogouMana()) || this.attacker.forcedNP)
            return this.attacker.getRandomAttack(BaseServant.AttackType.NP);
        if (this.doRanged)
            return this.attacker.getRandomAttack(BaseServant.AttackType.RANGED);
        return this.attacker.getRandomAttack(BaseServant.AttackType.MELEE);
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, BaseServant.AttackType.RANGED))
            return this.distanceToTargetSq < this.shootRangeSq;
        return super.canChooseAttack(anim);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, BaseServant.AttackType.NP)) {
            this.attacker.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            if (anim.canAttack()) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP(this.target);
                this.attacker.forcedNP = false;
            }
        } else if (this.doRanged) {
            this.attacker.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            if (anim.canAttack() && this.distanceToTargetSq <= this.shootRangeSq)
                this.attacker.attackWithRangedAttack(this.target);
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public void handlePreAttack() {
        this.iddleFlag = false;
        if (this.attacker.canUse(this.next, BaseServant.AttackType.NP))
            this.attacker.switchableWeapon.switchItems(false);
        super.handlePreAttack();
    }

    @Override
    public void handleIddle() {
        if (this.doRanged) {
            this.circleAroundTargetFacing(8, this.clockwise, 0.8f);
        } else
            super.handleIddle();
    }

    @Override
    public void setupValues() {
        this.doRanged = this.attacker.getMainHandItem().getItem() == ModItems.archbow.get();
        super.setupValues();
    }
}