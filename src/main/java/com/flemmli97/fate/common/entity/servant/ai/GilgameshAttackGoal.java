package com.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.fate.common.entity.servant.EntityGilgamesh;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

public class GilgameshAttackGoal extends BaseServantAttackGoal<EntityGilgamesh> {

    private boolean doRanged;
    private double[] targetPos;
    private final float shootRangeSq;
    private boolean iddleFlag, clockwise;

    public GilgameshAttackGoal(EntityGilgamesh entity, float shootRange) {
        super(entity, 1);
        this.shootRangeSq = shootRange * shootRange;
    }

    @Override
    public AnimatedAction randomAttack() {
        if ((this.attacker.canUseNP() && this.attacker.getOwner() == null && this.attacker.getMana() >= this.attacker.props().hogouMana()) || this.attacker.forcedNP)
            return this.attacker.getRandomAttack(EntityServant.AttackType.NP);
        if (this.doRanged)
            return this.attacker.getRandomAttack(EntityServant.AttackType.RANGED);
        return this.attacker.getRandomAttack(EntityServant.AttackType.MELEE);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, EntityServant.AttackType.NP)) {
            this.attacker.getLookController().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
            if (anim.getTick() == 7)
                this.targetPos = new double[]{this.target.getX(), this.target.getY(), this.target.getZ()};
            if (anim.canAttack()) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP(this.targetPos);
                this.attacker.forcedNP = false;
            }
        } else if (this.doRanged) {
            this.attacker.getLookController().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
            if (anim.canAttack() && this.distanceToTargetSq <= this.shootRangeSq)
                this.attacker.attackWithRangedAttack(this.target);
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, EntityServant.AttackType.RANGED))
            return this.distanceToTargetSq < this.shootRangeSq;
        return this.distanceToTargetSq < this.attackRange || this.attacker.canUse(anim, EntityServant.AttackType.NP);
    }

    @Override
    public void handlePreAttack() {
        this.iddleFlag = false;
        if (this.attacker.canUse(this.next, EntityServant.AttackType.NP))
            this.attacker.switchToNPWeapon(false);
        super.handlePreAttack();
    }

    @Override
    public void handleIddle() {
        if (this.doRanged) {
            if (this.distanceToTargetSq < 9)
                this.randomPosAwayFrom(this.target, 8);
            else {
                if (!this.iddleFlag) {
                    this.clockwise = this.attacker.getRNG().nextBoolean();
                    this.iddleFlag = true;
                }
                this.circleAroundTargetFacing(8, this.clockwise, 0.8f);
            }
        } else
            super.handleIddle();
    }

    @Override
    public void setupValues() {
        this.doRanged = this.attacker.getHeldItemMainhand().isEmpty(); //.getItem() != ModItems.archbow;
        super.setupValues();
    }
}
