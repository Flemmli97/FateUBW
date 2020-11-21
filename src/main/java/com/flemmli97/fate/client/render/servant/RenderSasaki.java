package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntitySasaki;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderSasaki extends ServantRenderer<EntitySasaki, ModelServant<EntitySasaki>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/sasaki.png");

    public RenderSasaki(EntityRendererManager manager) {
        super(manager, new ModelServant<>("sasaki"));
    }

    @Override
    public ResourceLocation servantTexture(EntitySasaki servant) {
        return textures;
    }
}
