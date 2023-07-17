package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedea;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

public class MedeaAttackGoal extends BaseServantAttackGoal<EntityMedea> {

    private boolean doRanged;
    private double[] targetPos;
    private final float shootRangeSq;
    private boolean idleFlag, clockwise;

    public MedeaAttackGoal(EntityMedea entity, float shootRange) {
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
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, BaseServant.AttackType.NP)) {
            this.attacker.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            if (anim.getTick() == 7)
                this.targetPos = new double[]{this.target.getX(), this.target.getY(), this.target.getZ()};
            if (anim.canAttack()) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP();
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
    public boolean canChooseAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, BaseServant.AttackType.RANGED))
            return this.distanceToTargetSq < this.shootRangeSq;
        return this.distanceToTargetSq < this.attackRange || this.attacker.canUse(anim, BaseServant.AttackType.NP);
    }

    @Override
    public void handlePreAttack() {
        this.idleFlag = false;
        if (this.attacker.canUse(this.next, BaseServant.AttackType.NP)) {
            Platform.INSTANCE.getItemStackData(this.attacker.getMainHandItem()).ifPresent(data -> data.setInUse(this.attacker, true, true));
        }
        super.handlePreAttack();
    }

    @Override
    public void handleIdle() {
        if (this.doRanged) {
            if (this.distanceToTargetSq < 9)
                this.randomPosAwayFrom(this.target, 8);
            else {
                if (!this.idleFlag) {
                    this.clockwise = this.attacker.getRandom().nextBoolean();
                    this.idleFlag = true;
                }
                this.circleAroundTargetFacing(8, this.clockwise, 0.8f);
            }
        } else
            super.handleIdle();
    }

    @Override
    public void setupValues() {
        this.doRanged = true;
        super.setupValues();
    }
}
