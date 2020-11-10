package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.servant.ModelEmiya;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntityEmiya;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderEmiya extends ServantRenderer<EntityEmiya, ModelEmiya> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/emiya.png");

    public RenderEmiya(EntityRendererManager manager) {
        super(manager, new ModelEmiya());
    }

    @Override
    public ResourceLocation servantTexture(EntityEmiya servant) {
        return textures;
    }
}
