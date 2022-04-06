package io.github.flemmli97.fateubw.common.entity.ai;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.pathfinder.Path;

import java.util.function.Function;

public class AnimatedMeleeGoal<T extends PathfinderMob & IAnimated> extends AnimatedAttackGoal<T> {

    protected int attackMoveDelay;

    protected double attackRange;
    private final Function<T, AnimatedAction> randomAttack;

    private boolean smallerRange;

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
        this.attacker.getNavigation().stop();
        if (this.distanceToTargetSq <= this.attackRange * 3 && anim.canAttack()) {
            this.attacker.doHurtTarget(this.target);
        }
    }

    @Override
    public void handlePreAttack() {
        this.moveToWithDelay(1);
        this.attacker.getLookControl().setLookAt(this.target, 30, 30);
        if (this.attackMoveDelay <= 0)
            this.attackMoveDelay = this.attacker.getRandom().nextInt(50) + 100;
        if (this.distanceToTargetSq <= this.attackRange * 2) {
            this.movementDone = true;
            this.attacker.getLookControl().setLookAt(this.target, 360, 90);
        } else if (this.attackMoveDelay-- == 1) {
            this.attackMoveDelay = 0;
            this.next = null;
        }
    }

    @Override
    public void handleIddle() {
        this.moveToWithDelay(1);
        this.attacker.getLookControl().setLookAt(this.target, 30, 30);
    }

    @Override
    protected void moveToWithDelay(double speed) {
        if (this.smallerRange) {
            if (this.pathFindDelay <= 0) {
                Path path = this.attacker.getNavigation().createPath(this.target, 0);
                if (path == null || this.attacker.getNavigation().moveTo(path, speed)) {
                    this.pathFindDelay += 15;
                }

                this.pathFindDelay += this.attacker.getRandom().nextInt(10) + 5;
            }
        } else
            super.moveToWithDelay(speed);
    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return 20;
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.attackRange = this.attacker.getBbWidth() * 2.2F * this.attacker.getBbWidth() * 2.2F + this.target.getBbWidth();
        this.smallerRange = this.attackRange <= 1.2;
    }
}
