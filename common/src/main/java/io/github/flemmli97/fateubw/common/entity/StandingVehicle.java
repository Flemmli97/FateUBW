package io.github.flemmli97.fateubw.common.entity;

import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.world.entity.Entity;

public interface StandingVehicle {

    static boolean shouldSit(Entity entity) {
        if (entity.getVehicle() instanceof StandingVehicle stand && stand.shouldStand())
            return false;
        return entity.getVehicle() != null && Platform.INSTANCE.shouldSit(entity);
    }

    default boolean shouldStand() {
        return true;
    }
}
