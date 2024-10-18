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

public class PegasusFlyingAttackGoal extends AnimatedAttackGoal<Pegasus> {

    private double[] chargeMotion;

    public PegasusFlyingAttackGoal(Pegasus entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        return this.attacker.canFly() && super.canUse();
    }

    @Override
    public AnimatedAction randomAttack() {
        this.chargeMotion = null;
        return Pegasus.CHARGING;
    }

    @Override
    public void handlePreAttack() {
        this.movementDone = true;
        this.attacker.lookAt(this.target, 90, 10);
        this.attacker.chargingHandler.lockYaw(this.attacker.getYRot());
        if (this.next.is(Pegasus.CHARGING))
            this.chargeMotion = this.getChargeTo(this.next, this.target.position());
    }

    @Override
    public void handleAttack(AnimatedAction animatedAction) {
        this.attacker.getNavigation().stop();
        if (this.attacker.getAnimationHandler().isCurrent(Pegasus.CHARGING)) {
            if (animatedAction.getTick() >= animatedAction.getAttackTime()) {
                this.attacker.setDeltaMovement(this.chargeMotion[0], this.attacker.getDeltaMovement().y, this.chargeMotion[2]);
                List<LivingEntity> list = this.attacker.level.getEntitiesOfClass(LivingEntity.class, this.attacker.attackAABB(this.attacker.getAnimationHandler().getAnimation()).inflate(0.5), EntitySelector.NO_SPECTATORS.and(e -> !this.attacker.hasPassenger(e)));
                for (LivingEntity e : list) {
                    if (e != this.attacker) {
                        e.hurt(CustomDamageSource.pegasusCharge(this.attacker, this.attacker.getControllingPassenger()), Config.Common.gordiusDmg);
                    }
                }
                this.attacker.playSound(SoundEvents.COW_STEP, 0.4F, 0.4F);
            } else {
                this.attacker.lookAt(this.target, 90, 10);
                this.attacker.chargingHandler.lockYaw(this.attacker.getYRot());
            }
        }
    }

    @Override
    public void handleIdle() {
        this.moveToWithDelay(1.1);
    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return this.attacker.getRandom().nextInt(15) + 15;
    }

    public double[] getChargeTo(AnimatedAction anim, Vec3 pos) {
        int length = anim.getLength() - anim.getAttackTime();
        Vec3 vec = pos.subtract(this.attacker.position()).normalize().scale(12);
        return new double[]{vec.x / length, this.attacker.getY(), vec.z / length};
    }
}
