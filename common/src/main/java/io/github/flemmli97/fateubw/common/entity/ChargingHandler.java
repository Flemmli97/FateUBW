package io.github.flemmli97.fateubw.common.entity;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.function.Function;

public class ChargingHandler<T extends LivingEntity & IAnimated> {

    private final T entity;
    private final EntityDataAccessor<Float> lockingData;
    private final Function<AnimatedAction, Boolean> isChargeAttack;

    public ChargingHandler(T entity, EntityDataAccessor<Float> lockingData, Function<AnimatedAction, Boolean> isChargeAttack) {
        this.entity = entity;
        this.lockingData = lockingData;
        this.isChargeAttack = isChargeAttack;
    }

    public void tick() {
        if (this.isChargeAttack.apply(this.entity.getAnimationHandler().getAnimation())) {
            this.entity.setXRot(0);
            this.entity.setYRot(this.entity.getEntityData().get(this.lockingData));
            if (!this.entity.getPassengers().isEmpty() && this.entity.getPassengers().get(0) instanceof Mob) {
                Mob pass = (Mob) this.entity.getPassengers().get(0);
                pass.setYRot(this.entity.getEntityData().get(this.lockingData));
            }
        }
    }

    public void lockYaw(float yaw) {
        this.entity.getEntityData().set(this.lockingData, yaw);
    }
}
