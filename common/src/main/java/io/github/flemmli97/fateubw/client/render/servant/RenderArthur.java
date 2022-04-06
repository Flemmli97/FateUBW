package io.github.flemmli97.fateubw.client.render.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelServant;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.common.entity.servant.EntityArthur;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderArthur extends ServantRenderer<EntityArthur, ModelServant<EntityArthur>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/arthur.png");

    public RenderArthur(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelServant<>(ctx.bakeLayer(ModelServant.LAYER_LOCATION), "arthur"));
    }

    @Override
    public ResourceLocation servantTexture(EntityArthur servant) {
        return textures;
    }
}
