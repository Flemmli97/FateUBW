package io.github.flemmli97.fateubw.common.entity;

import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.world.entity.Entity;

public interface StandingVehicle {

    static boolean shouldSit(Entity entity) {
        return entity.getVehicle() != null && (entity.getVehicle() instanceof StandingVehicle stand && !stand.shouldStand()
                || Platform.INSTANCE.shouldSit(entity));
    }

    default boolean shouldStand() {
        return true;
    }
}
