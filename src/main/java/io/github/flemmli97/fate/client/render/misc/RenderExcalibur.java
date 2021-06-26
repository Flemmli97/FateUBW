package io.github.flemmli97.fate.client.render.misc;

import com.flemmli97.tenshilib.client.render.RenderBeam;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.entity.EntityExcalibur;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderExcalibur extends RenderBeam<EntityExcalibur> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/excalibur.png");

    public RenderExcalibur(EntityRendererManager manager) {
        super(manager, EntityExcalibur.radius);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityExcalibur entity) {
        return this.tex;
    }

    @Override
    public ResourcePair startTexture(EntityExcalibur entity) {
        return null;
    }

    @Override
    public ResourcePair endTexture(EntityExcalibur entity) {
        return null;
    }
}
