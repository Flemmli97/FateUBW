package io.github.flemmli97.fateubw.client.render.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelHeracles;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.common.entity.servant.EntityHeracles;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderHeracles extends ServantRenderer<EntityHeracles, ModelHeracles<EntityHeracles>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/heracles.png");

    public RenderHeracles(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelHeracles<>(ctx.bakeLayer(ModelHeracles.LAYER_LOCATION)));
    }

    @Override
    public ResourceLocation servantTexture(EntityHeracles servant) {
        return textures;
    }
}
