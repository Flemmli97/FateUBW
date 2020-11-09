package com.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.fate.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fate.common.entity.servant.EntityEmiya;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

public class EmiyaAttackGoal  extends BaseServantAttackGoal<EntityEmiya> {

    private boolean doRanged;

    public EmiyaAttackGoal(EntityEmiya entity) {
        super(entity, 1);
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
            if (anim.canAttack()) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP(this.target);
                this.attacker.forcedNP = false;
                this.attacker.switchToNPWeapon(true);
            }
        } else if (this.doRanged) {

        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.canUse(this.next, EntityServant.AttackType.NP))
            this.attacker.switchToNPWeapon(false);
        super.handlePreAttack();
    }

    @Override
    public void handleIddle() {
        if (this.doRanged) {

        } else
            super.handleIddle();
    }

    @Override
    public void setupValues() {
        this.doRanged = this.attacker.getHeldItemMainhand().getItem() == ModItems.archbow;
        super.setupValues();
    }
}