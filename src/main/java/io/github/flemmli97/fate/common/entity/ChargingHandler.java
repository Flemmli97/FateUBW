package io.github.flemmli97.fate.common.entity;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.network.datasync.DataParameter;

import java.util.function.Function;

public class ChargingHandler<T extends LivingEntity & IAnimated> {

    private final T entity;
    private final DataParameter<Float> lockingData;
    private final Function<AnimatedAction, Boolean> isChargeAttack;

    public ChargingHandler(T entity, DataParameter<Float> lockingData, Function<AnimatedAction, Boolean> isChargeAttack) {
        this.entity = entity;
        this.lockingData = lockingData;
        this.isChargeAttack = isChargeAttack;
    }

    public void tick() {
        if (this.entity.getAnimationHandler().getAnimation().map(this.isChargeAttack).orElse(false)) {
            this.entity.rotationPitch = 0;
            this.entity.rotationYaw = this.entity.getDataManager().get(this.lockingData);
            if (!this.entity.getPassengers().isEmpty() && this.entity.getPassengers().get(0) instanceof MobEntity) {
                MobEntity pass = (MobEntity) this.entity.getPassengers().get(0);
                pass.rotationYaw = this.entity.getDataManager().get(this.lockingData);
            }
        }
    }

    public void lockYaw(float yaw) {
        this.entity.getDataManager().set(this.lockingData, yaw);
    }
}
