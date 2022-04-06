package io.github.flemmli97.fateubw.client.render.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelServant;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.common.entity.servant.EntityCuchulainn;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderCuchulainn extends ServantRenderer<EntityCuchulainn, ModelServant<EntityCuchulainn>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/cuchulainn.png");

    public RenderCuchulainn(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelServant<>(ctx.bakeLayer(ModelServant.LAYER_LOCATION), "cuchulainn"));
    }

    @Override
    public ResourceLocation servantTexture(EntityCuchulainn servant) {
        return textures;
    }
}
