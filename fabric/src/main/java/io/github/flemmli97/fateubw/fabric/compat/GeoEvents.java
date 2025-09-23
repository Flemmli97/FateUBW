package io.github.flemmli97.fateubw.fabric.compat;

import io.github.flemmli97.fateubw.client.render.layer.PetrificationGeoLayer;
import software.bernie.geckolib.event.GeoRenderEvent;

public class GeoEvents {

    public static void init() {
        GeoRenderEvent.Entity.CompileRenderLayers.EVENT.register(event -> {
            event.addLayer(new PetrificationGeoLayer<>(event.getRenderer()));
        });
    }
}
