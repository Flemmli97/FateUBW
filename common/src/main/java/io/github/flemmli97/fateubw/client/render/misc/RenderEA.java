package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
import io.github.flemmli97.tenshilib.client.render.RenderBeam;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderEA extends RenderBeam<EnumaElish> {

    public static final ResourceLocation TEX = new ResourceLocation(Fate.MODID, "textures/entity/ea.png");

    public RenderEA(EntityRendererProvider.Context ctx) {
        super(ctx, EnumaElish.RADIUS - 0.2f, 4);
    }

    @Override
    public ResourceLocation getTextureLocation(EnumaElish entity) {
        return TEX;
    }

    @Override
    public ResourcePair startTexture(EnumaElish entity) {
        return null;
    }

    @Override
    public ResourcePair endTexture(EnumaElish entity) {
        return null;
    }
}
