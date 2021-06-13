package io.github.flemmli97.fate.common.entity.servant.ai;

import io.github.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.util.math.vector.Vector3d;

public class CuchulainnAttackGoal extends BaseServantAttackGoal<EntityCuchulainn> {

    public CuchulainnAttackGoal(EntityCuchulainn entity) {
        super(entity, 1.2f);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, EntityServant.AttackType.NP)) {
            if (anim.canAttack() && !this.attacker.isOnGround()) {
                this.attacker.setMotion(Vector3d.ZERO);
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
        if (this.attacker.canUse(this.next, EntityServant.AttackType.NP)) {
            Vector3d back = this.attacker.getLookVec().scale(-1.1);
            this.attacker.setVelocity(back.x, 1, back.z);
        }
        super.handlePreAttack();
    }
}
