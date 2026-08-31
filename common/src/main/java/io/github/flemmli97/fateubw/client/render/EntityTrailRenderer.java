package io.github.flemmli97.fateubw.client.render;

import io.github.flemmli97.fateubw.client.particles.TrailRenderer;
import io.github.flemmli97.fateubw.common.entity.utils.EntityTrailHandler;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.HashSet;
import java.util.Set;

public class EntityTrailRenderer {

    private static final Set<EntityTrailHandler> HANDLERS = new HashSet<>();

    public static void addHandler(EntityTrailHandler handler) {
        HANDLERS.add(handler);
    }

    public static void tick() {
        HANDLERS.removeIf(EntityTrailHandler::tick);
    }

    public static void render(MultiBufferSource buffer, float partialTick) {
        HANDLERS.forEach(handler -> {
            if (handler.valid() && handler.getInfo() != null) {
                TrailRenderer.render(handler.getEntity(), handler.getInfo(), handler.getPositions(), buffer.getBuffer(FateRenders.TRAIL_TRANSLUCENT), partialTick);
            }
        });
    }
}
