package io.github.flemmli97.fate.common.entity.ai;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.entity.CreatureEntity;

import java.util.function.Function;

public class AnimatedMeleeGoal<T extends CreatureEntity & IAnimated<T>> extends AnimatedAttackGoal<T> {

    protected int attackMoveDelay;

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
        this.moveToWithDelay(1);
        this.attacker.getLookController().setLookPositionWithEntity(this.target, 30, 30);
        if (this.attackMoveDelay <= 0)
            this.attackMoveDelay = this.attacker.getRNG().nextInt(50) + 100;
        if (this.distanceToTargetSq <= this.attackRange * 2) {
            this.movementDone = true;
            this.attacker.getLookController().setLookPositionWithEntity(this.target, 360, 90);
        } else if (this.attackMoveDelay-- == 1) {
            this.attackMoveDelay = 0;
            this.next = null;
        }
    }

    @Override
    public void handleIddle() {
        this.moveToWithDelay(1);
        this.attacker.getLookController().setLookPositionWithEntity(this.target, 30, 30);
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
