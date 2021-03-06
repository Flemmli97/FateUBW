package io.github.flemmli97.fate.client.render.servant;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelServant;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderCuchulainn extends ServantRenderer<EntityCuchulainn, ModelServant<EntityCuchulainn>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/cuchulainn.png");

    public RenderCuchulainn(EntityRendererManager manager) {
        super(manager, new ModelServant<>("cuchulainn"));
    }

    @Override
    public ResourceLocation servantTexture(EntityCuchulainn servant) {
        return textures;
    }
}
