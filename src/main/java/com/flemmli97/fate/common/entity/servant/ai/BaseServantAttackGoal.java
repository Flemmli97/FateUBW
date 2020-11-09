package com.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;

public class BaseServantAttackGoal<T extends EntityServant> extends AnimatedAttackGoal<T> {

    public final float rangeModifier;
    protected double attackRange;

    public BaseServantAttackGoal(T entity, float rangeModifier) {
        super(entity);
        this.rangeModifier = rangeModifier;
    }

    @Override
    public AnimatedAction randomAttack() {
        if ((this.attacker.canUseNP() && this.attacker.getOwner() == null && this.attacker.getMana() >= this.attacker.props().hogouMana()) || this.attacker.forcedNP)
            return this.attacker.getRandomAttack(EntityServant.AttackType.NP);
        return this.attacker.getRandomAttack(EntityServant.AttackType.MELEE);
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        return this.distanceToTargetSq < this.attackRange || this.attacker.canUse(anim, EntityServant.AttackType.NP);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        this.attacker.getNavigator().clearPath();
        this.attacker.getLookController().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
        if (this.distanceToTargetSq <= attackRange * 3 && anim.canAttack()) {
            this.attacker.attackEntityAsMob(target);
        }
    }

    @Override
    public void handlePreAttack() {
        this.movementDone = true;
    }

    @Override
    public void handleIddle() {
        this.moveToWithDelay(1);
    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return this.attacker.attackCooldown(animatedAction);
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.attackRange = this.rangeModifier * (this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + target.getWidth());
    }
}
