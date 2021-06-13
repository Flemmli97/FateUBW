package io.github.flemmli97.fate.client.render;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.entity.EntityEnumaElish;
import io.github.flemmli97.fate.common.entity.EntityExcalibur;
import com.flemmli97.tenshilib.client.render.RenderBeam;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderEA extends RenderBeam<EntityEnumaElish> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/ea.png");

    public RenderEA(EntityRendererManager manager) {
        super(manager, EntityExcalibur.radius);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEnumaElish entity) {
        return this.tex;
    }

    @Override
    public ResourcePair startTexture(EntityEnumaElish entity) {
        return null;
    }

    @Override
    public ResourcePair endTexture(EntityEnumaElish entity) {
        return null;
    }
}
