package com.flemmli97.fate.common.entity.ai;

import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.entity.CreatureEntity;

import java.util.function.Function;

public class AnimatedMeleeGoal<T extends CreatureEntity & IAnimated> extends AnimatedAttackGoal<T> {

    protected double attackRange;
    private final Function<T, AnimatedAction> randomAttack;

    public AnimatedMeleeGoal(T entity, Function<T, AnimatedAction> randomAttack) {
        super(entity);
        this.randomAttack = randomAttack;
    }

    @Override
    public AnimatedAction randomAttack() {
        return this.randomAttack.apply(this.attacker);
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        return this.distanceToTargetSq < this.attackRange;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        this.attacker.getNavigator().clearPath();
        if (this.distanceToTargetSq <= this.attackRange * 3 && anim.canAttack()) {
            this.attacker.attackEntityAsMob(this.target);
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
        this.attackRange = (this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + this.target.getWidth());
    }
}
