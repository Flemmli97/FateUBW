package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
import io.github.flemmli97.tenshilib.client.render.BeamRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderEA extends BeamRenderer<EnumaElish> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/ea.png");

    private final float widthMod;

    public RenderEA(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 0.9f, 4);
        this.widthMod = Mth.sqrt(0.9f * 0.9f / 2) * 2;
    }

    @Override
    public float widthFunc(EnumaElish entity) {
        return super.widthFunc(entity) / this.widthMod;
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
