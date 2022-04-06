package io.github.flemmli97.fateubw.client.render.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelMedea;
import io.github.flemmli97.fateubw.client.model.ModelServant;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedea;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderMedea extends ServantRenderer<EntityMedea, ModelServant<EntityMedea>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/medea.png");

    public RenderMedea(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelMedea<>(ctx.bakeLayer(ModelMedea.LAYER_LOCATION)));
    }

    @Override
    public ResourceLocation servantTexture(EntityMedea servant) {
        return textures;
    }
}
