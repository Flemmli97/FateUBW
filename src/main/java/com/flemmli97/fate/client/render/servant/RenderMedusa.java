package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntityMedusa;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderMedusa extends ServantRenderer<EntityMedusa, ModelServant<EntityMedusa>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/medusa.png");

    public RenderMedusa(EntityRendererManager manager) {
        super(manager, new ModelServant<>("medusa"));
    }

    @Override
    public ResourceLocation servantTexture(EntityMedusa servant) {
        return textures;
    }
}
