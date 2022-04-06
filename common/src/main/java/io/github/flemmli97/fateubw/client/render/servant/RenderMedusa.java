package io.github.flemmli97.fateubw.client.render.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelServant;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedusa;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderMedusa extends ServantRenderer<EntityMedusa, ModelServant<EntityMedusa>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/medusa.png");

    public RenderMedusa(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelServant<>(ctx.bakeLayer(ModelServant.LAYER_LOCATION), "medusa"));
    }

    @Override
    public ResourceLocation servantTexture(EntityMedusa servant) {
        return textures;
    }
}
