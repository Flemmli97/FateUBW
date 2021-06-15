package io.github.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.fate.common.entity.servant.EntityEmiya;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import io.github.flemmli97.fate.common.registry.ModItems;

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
            return this.attacker.getRandomAttack(EntityServant.AttackType.NP);
        if (this.doRanged)
            return this.attacker.getRandomAttack(EntityServant.AttackType.RANGED);
        return this.attacker.getRandomAttack(EntityServant.AttackType.MELEE);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, EntityServant.AttackType.NP)) {
            this.attacker.getLookController().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
            if (anim.canAttack()) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP(this.target);
                this.attacker.forcedNP = false;
                this.attacker.switchToNPWeapon(true);
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
    public void handlePreAttack() {
        this.iddleFlag = false;
        if (this.attacker.canUse(this.next, EntityServant.AttackType.NP))
            this.attacker.switchToNPWeapon(false);
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
        this.doRanged = this.attacker.getHeldItemMainhand().getItem() == ModItems.archbow.get();
        super.setupValues();
    }
}