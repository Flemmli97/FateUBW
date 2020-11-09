package com.flemmli97.fate.common.entity.ai;

import com.flemmli97.fate.common.entity.EntityLesserMonster;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;

public class StarfishAttackGoal extends AnimatedAttackGoal<EntityLesserMonster> {

    protected double attackRange;

    public StarfishAttackGoal(EntityLesserMonster entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        return EntityLesserMonster.attack;
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        return this.distanceToTargetSq < this.attackRange;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        this.attacker.getNavigator().clearPath();
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
        return 20;
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.attackRange = (this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + target.getWidth());
    }
}
