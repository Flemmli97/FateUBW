package io.github.flemmli97.fateubw.client.render.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelServant;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.common.entity.servant.EntityGilles;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderGilles extends ServantRenderer<EntityGilles, ModelServant<EntityGilles>> {

    public static final ResourceLocation TEXTURES = new ResourceLocation(Fate.MODID, "textures/entity/servant/gilles.png");

    public RenderGilles(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelServant<>(ctx.bakeLayer(ModelServant.LAYER_LOCATION), "gilles"));
    }

    @Override
    public ResourceLocation servantTexture(EntityGilles servant) {
        return TEXTURES;
    }
}
