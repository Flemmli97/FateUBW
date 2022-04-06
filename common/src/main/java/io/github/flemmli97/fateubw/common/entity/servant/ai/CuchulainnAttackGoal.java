package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.EntityCuchulainn;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.phys.Vec3;

public class CuchulainnAttackGoal extends BaseServantAttackGoal<EntityCuchulainn> {

    public CuchulainnAttackGoal(EntityCuchulainn entity) {
        super(entity, 1.2f);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, BaseServant.AttackType.NP)) {
            if (anim.canAttack() && !this.attacker.isOnGround()) {
                this.attacker.setDeltaMovement(Vec3.ZERO);
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP(this.target);
                this.attacker.forcedNP = false;
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.canUse(this.next, BaseServant.AttackType.NP)) {
            Vec3 back = this.attacker.getLookAngle().scale(-1.1);
            this.attacker.lerpMotion(back.x, 1, back.z);
        }
        super.handlePreAttack();
    }
}
