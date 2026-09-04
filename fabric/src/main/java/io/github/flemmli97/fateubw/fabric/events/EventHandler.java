package io.github.flemmli97.fateubw.fabric.events;

import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.common.event.EventCalls;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EventHandler {

    public static void entityTick(Entity entity) {
        EventCalls.tick(entity);
        if (entity instanceof LivingEntity living) {
            EventCalls.tickLiving(living);
            if (living.level().isClientSide)
                ClientCalls.tick(living);
        }
    }
}
