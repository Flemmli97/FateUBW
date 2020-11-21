package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.servant.ModelMedusa;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntityMedusa;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderMedusa extends ServantRenderer<EntityMedusa, ModelMedusa> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/medusa.png");

    public RenderMedusa(EntityRendererManager manager) {
        super(manager, new ModelMedusa());
    }

    @Override
    public ResourceLocation servantTexture(EntityMedusa servant) {
        return textures;
    }
}
