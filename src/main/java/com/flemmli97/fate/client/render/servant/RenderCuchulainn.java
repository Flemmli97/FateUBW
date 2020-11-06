package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.servant.ModelCuchulainn;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderCuchulainn extends ServantRenderer<EntityCuchulainn, ModelCuchulainn> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/cuchulainn.png");

    public RenderCuchulainn(EntityRendererManager manager) {
        super(manager, new ModelCuchulainn());
    }

    @Override
    public ResourceLocation servantTexture(EntityCuchulainn servant) {
        return textures;
    }
}
