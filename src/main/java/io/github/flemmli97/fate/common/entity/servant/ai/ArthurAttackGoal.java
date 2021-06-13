package io.github.flemmli97.fate.common.entity.servant.ai;

import io.github.flemmli97.fate.common.entity.servant.EntityArthur;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

public class ArthurAttackGoal extends BaseServantAttackGoal<EntityArthur> {

    private double[] targetPos;

    public ArthurAttackGoal(EntityArthur entity) {
        super(entity, 1);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, EntityServant.AttackType.NP)) {
            this.attacker.faceEntity(this.target, 0, 0);
            if (anim.getTick() == 3)
                this.targetPos = new double[]{this.target.getPosX(), this.target.getPosY() + this.target.getEyeHeight(), this.target.getPosZ()};
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
}
