package io.github.flemmli97.fateubw.client.render.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelServant;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.common.entity.servant.EntitySasaki;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderSasaki extends ServantRenderer<EntitySasaki, ModelServant<EntitySasaki>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/sasaki.png");

    public RenderSasaki(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelServant<>(ctx.bakeLayer(ModelServant.LAYER_LOCATION), "sasaki"));
    }

    @Override
    public ResourceLocation servantTexture(EntitySasaki servant) {
        return textures;
    }
}
