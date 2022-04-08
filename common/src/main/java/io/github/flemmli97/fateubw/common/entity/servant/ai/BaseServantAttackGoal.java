package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.world.level.pathfinder.Path;

public class BaseServantAttackGoal<T extends BaseServant> extends AnimatedAttackGoal<T> {

    public final float rangeModifier;
    protected double attackRange;

    public BaseServantAttackGoal(T entity, float rangeModifier) {
        super(entity);
        this.rangeModifier = rangeModifier;
    }

    @Override
    public AnimatedAction randomAttack() {
        if ((this.attacker.canUseNP() && this.attacker.getOwner() == null && this.attacker.getMana() >= this.attacker.props().hogouMana()) || this.attacker.forcedNP)
            return this.attacker.getRandomAttack(BaseServant.AttackType.NP);
        return this.attacker.getRandomAttack(BaseServant.AttackType.MELEE);
    }

    @Override
    public boolean canChooseAttack(AnimatedAction anim) {
        if (anim == null)
            return false;
        return this.distanceToTargetSq < this.attackRange || this.attacker.canUse(anim, BaseServant.AttackType.NP);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (!this.attacker.canUse(anim, BaseServant.AttackType.MELEE))
            return;
        this.attacker.getNavigation().stop();
        this.attacker.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (this.distanceToTargetSq <= this.attackRange * 3 && anim.canAttack()) {
            this.attacker.doHurtTarget(this.target);
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
    protected void moveToWithDelay(double speed) {
        if (this.pathFindDelay <= 0) {
            Path path = this.attacker.getNavigation().createPath(this.target, 0);
            if (path != null && this.attacker.getNavigation().moveTo(path, speed)) {
                this.pathFindDelay += 15;
            }
            this.pathFindDelay += this.attacker.getRandom().nextInt(10) + 5;
        }
    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return this.attacker.attackCooldown(animatedAction);
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.attackRange = this.rangeModifier * (this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + this.target.getBbWidth());
    }
}
