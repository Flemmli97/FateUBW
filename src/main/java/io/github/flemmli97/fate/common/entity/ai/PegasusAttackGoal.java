package io.github.flemmli97.fate.common.entity.ai;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.minions.EntityPegasus;
import io.github.flemmli97.fate.common.utils.CustomDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class PegasusAttackGoal extends AnimatedAttackGoal<EntityPegasus> {

    private double[] chargeMotion;

    public PegasusAttackGoal(EntityPegasus entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        this.chargeMotion = null;
        return this.attacker.getChargingAnim();
    }

    @Override
    public void handlePreAttack() {
        this.movementDone = true;
        this.attacker.faceEntity(this.target, 90, 10);
        this.attacker.chargingHandler.lockYaw(this.attacker.rotationYaw);
        this.chargeMotion = this.getChargeTo(this.next, this.target.getPositionVec());
    }

    @Override
    public void handleAttack(AnimatedAction animatedAction) {
        this.attacker.getNavigator().clearPath();
        if (animatedAction.getTick() >= animatedAction.getAttackTime()) {
            this.attacker.setMotion(this.chargeMotion[0], this.attacker.getMotion().y, this.chargeMotion[2]);
            List<LivingEntity> list = this.attacker.world.getEntitiesWithinAABB(LivingEntity.class, this.attacker.getBoundingBox().grow(0.5), EntityPredicates.NOT_SPECTATING.and(e -> !this.attacker.isPassenger(e)));
            for (LivingEntity e : list) {
                if (e != this.attacker) {
                    e.attackEntityFrom(CustomDamageSource.pegasusCharge(this.attacker, (LivingEntity) this.attacker.getPassengers().get(0)), Config.Common.gordiusDmg);
                }
            }
            this.attacker.playSound(SoundEvents.ENTITY_COW_STEP, 0.4F, 0.4F);
        } else {
            this.attacker.faceEntity(this.target, 90, 10);
            this.attacker.chargingHandler.lockYaw(this.attacker.rotationYaw);
        }
    }

    @Override
    public void handleIddle() {
    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return this.attacker.getRNG().nextInt(13) + 7;
    }

    public double[] getChargeTo(AnimatedAction anim, Vector3d pos) {
        int length = anim.getLength() - anim.getAttackTime();
        Vector3d vec = pos.subtract(this.attacker.getPositionVec()).normalize().scale(12);
        return new double[]{vec.x / length, this.attacker.getPosY(), vec.z / length};
    }
}
