package io.github.flemmli97.fateubw.neoforge.compat;

import io.github.flemmli97.fateubw.client.render.layer.PetrificationGeoLayer;
import net.neoforged.bus.api.SubscribeEvent;
import software.bernie.geckolib.event.GeoRenderEvent;

public class GeoEvents {

    @SubscribeEvent
    public static void layerModels(GeoRenderEvent.Entity.CompileRenderLayers event) {
        event.addLayer(new PetrificationGeoLayer<>(event.getRenderer()));
    }
}
