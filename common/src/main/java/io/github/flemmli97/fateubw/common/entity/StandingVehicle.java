package io.github.flemmli97.fateubw.common.entity;

import net.minecraft.world.entity.Entity;

public interface StandingVehicle {

    static boolean stand(Entity entity) {
        return entity == null || entity instanceof StandingVehicle stand && stand.shouldStand();
    }

    default boolean shouldStand() {
        return true;
    }
}
