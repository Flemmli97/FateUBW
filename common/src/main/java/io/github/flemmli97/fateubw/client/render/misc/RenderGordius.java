package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelGordiusWheel;
import io.github.flemmli97.fateubw.common.entity.minions.Gordius;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderGordius extends MobRenderer<Gordius, ModelGordiusWheel> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/gordius_wheel.png");

    public RenderGordius(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelGordiusWheel(ctx.bakeLayer(ModelGordiusWheel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Gordius gordiusWheel) {
        return this.tex;
    }
}
