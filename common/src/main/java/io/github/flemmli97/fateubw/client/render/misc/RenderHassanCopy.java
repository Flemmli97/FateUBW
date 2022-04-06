package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelHassanClone;
import io.github.flemmli97.fateubw.common.entity.minions.HassanClone;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderHassanCopy extends MobRenderer<HassanClone, ModelHassanClone<HassanClone>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/hassan.png");

    public RenderHassanCopy(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelHassanClone<>(ctx.bakeLayer(ModelHassanClone.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(HassanClone servant) {
        return textures;
    }
}
