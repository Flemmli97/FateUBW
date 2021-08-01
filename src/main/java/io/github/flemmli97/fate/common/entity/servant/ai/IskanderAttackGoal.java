package io.github.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.fate.common.entity.servant.EntityIskander;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;

public class IskanderAttackGoal extends BaseServantAttackGoal<EntityIskander> {

    public IskanderAttackGoal(EntityIskander entity) {
        super(entity, 1);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.canUse(anim, EntityServant.AttackType.NP)) {
            this.attacker.faceEntity(this.target, 0, 0);
            if (anim.canAttack()) {
                if (!this.attacker.forcedNP)
                    this.attacker.useMana(this.attacker.props().hogouMana());
                this.attacker.attackWithNP();
                this.attacker.forcedNP = false;
            }
        } else {
            super.handleAttack(anim);
        }
    }

    /*@Override
    public void updateTask() {
        EntityLivingBase target = this.attackingEntity.getAttackTarget();
        AnimatedAction anim = this.attackingEntity.getAnimation();
        if(anim==null && !this.attackingEntity.isRiding() && ((this.attackingEntity.canUseNP() && this.attackingEntity.getOwner() == null && this.attackingEntity.getMana()>=this.attackingEntity.props().hogouMana()) || this.attackingEntity.forcedNP))
        {
            anim = this.attackingEntity.getRandomAttack(AttackType.NP);
            this.attackingEntity.setAnimation(anim);
        }
        if(anim!=null && this.attackingEntity.canUse(anim, AttackType.NP))
        {
            if(anim.canAttack())
            {
                if(!this.attackingEntity.forcedNP)
                    this.attackingEntity.useMana(this.attackingEntity.props().hogouMana());
                this.attackingEntity.attackWithNP();
                this.attackingEntity.forcedNP = false;
            }
        }
        else if(this.attackingEntity.getRidingEntity() instanceof EntityGordiusWheel)
        {
            EntityGordiusWheel mount = (EntityGordiusWheel) this.attackingEntity.getRidingEntity();
            this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
            if(!mount.isCharging()  && !mount.isPreparingCharge() && this.attackingEntity.getRNG().nextFloat()<0.2)
            {
                mount.startCharging();
                this.attackingEntity.world.playSound(null, this.attackingEntity.getPosition(), SoundEvents.ENTITY_COW_AMBIENT, SoundCategory.NEUTRAL, 1, (this.attackingEntity.getRNG().nextFloat() - this.attackingEntity.getRNG().nextFloat()) * 0.4F);
            }
            if(mount.isPreparingCharge())
            {
                this.prepare++;
                if(this.prepare>=20)
                {
                    Vec3d moveVec = mount.getPositionVector().add(target.getPositionVector().subtract(mount.getPositionVector()).normalize().scale(16));
                    this.attackingEntity.getLookHelper().setLookPosition(moveVec.x, moveVec.y, moveVec.z, 30.0F, 30.0F);
                    this.attackingEntity.getNavigator().tryMoveToXYZ(moveVec.x, moveVec.y, moveVec.z, this.speedTowardsTarget);
                    mount.setCharging(true);
                    this.prepare=0;
                }
            }
        }
        else if(!(this.attackingEntity.getRidingEntity() instanceof EntityGordiusWheel))
            super.updateTask();
    }*/
}
