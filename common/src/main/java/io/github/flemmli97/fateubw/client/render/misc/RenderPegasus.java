package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelPegasus;
import io.github.flemmli97.fateubw.common.entity.minions.Pegasus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderPegasus extends MobRenderer<Pegasus, ModelPegasus> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/pegasus.png");

    public RenderPegasus(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelPegasus(ctx.bakeLayer(ModelPegasus.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Pegasus pegasus) {
        return this.tex;
    }
}
