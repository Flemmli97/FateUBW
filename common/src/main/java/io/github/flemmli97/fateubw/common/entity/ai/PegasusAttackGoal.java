package io.github.flemmli97.fateubw.common.entity.ai;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.minions.Pegasus;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PegasusAttackGoal extends AnimatedAttackGoal<Pegasus> {

    private double[] chargeMotion;

    public PegasusAttackGoal(Pegasus entity) {
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
        this.attacker.lookAt(this.target, 90, 10);
        this.attacker.chargingHandler.lockYaw(this.attacker.getYRot());
        this.chargeMotion = this.getChargeTo(this.next, this.target.position());
    }

    @Override
    public void handleAttack(AnimatedAction animatedAction) {
        this.attacker.getNavigation().stop();
        if (animatedAction.getTick() >= animatedAction.getAttackTime()) {
            this.attacker.setDeltaMovement(this.chargeMotion[0], this.attacker.getDeltaMovement().y, this.chargeMotion[2]);
            List<LivingEntity> list = this.attacker.level.getEntitiesOfClass(LivingEntity.class, this.attacker.getBoundingBox().inflate(0.5), EntitySelector.NO_SPECTATORS.and(e -> !this.attacker.hasPassenger(e)));
            for (LivingEntity e : list) {
                if (e != this.attacker) {
                    e.hurt(CustomDamageSource.pegasusCharge(this.attacker, (LivingEntity) this.attacker.getPassengers().get(0)), Config.Common.gordiusDmg);
                }
            }
            this.attacker.playSound(SoundEvents.COW_STEP, 0.4F, 0.4F);
        } else {
            this.attacker.lookAt(this.target, 90, 10);
            this.attacker.chargingHandler.lockYaw(this.attacker.getYRot());
        }
    }

    @Override
    public void handleIddle() {
    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return this.attacker.getRandom().nextInt(13) + 7;
    }

    public double[] getChargeTo(AnimatedAction anim, Vec3 pos) {
        int length = anim.getLength() - anim.getAttackTime();
        Vec3 vec = pos.subtract(this.attacker.position()).normalize().scale(12);
        return new double[]{vec.x / length, this.attacker.getY(), vec.z / length};
    }
}
