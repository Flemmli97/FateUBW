package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.EntityArthur;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.InteractionHand;

public class ArthurAttackGoal extends BaseServantAttackGoal<EntityArthur> {

    private double[] targetPos;
    private boolean switchFlag;

    public ArthurAttackGoal(EntityArthur entity) {
        super(entity, 1);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, BaseServant.AttackType.NP)) {
            this.attacker.lookAt(this.target, 0, 0);
            if (anim.getTick() == 3)
                this.targetPos = new double[]{this.target.getX(), this.target.getY() + this.target.getEyeHeight(), this.target.getZ()};
            if (anim.canAttack()) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP(this.targetPos);
                this.attacker.forcedNP = false;
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.canUse(this.next, BaseServant.AttackType.NP)) {
            if (!this.switchFlag) {
                this.attacker.switchableWeapon.switchItems(false);
                this.switchFlag = true;
                return;
            } else
                this.attacker.startUsingItem(InteractionHand.MAIN_HAND);
        }
        super.handlePreAttack();
        this.switchFlag = false;
    }
}
