package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.servant.ModelMedea;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntityMedea;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderMedea extends ServantRenderer<EntityMedea, ModelMedea> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/medea.png");

    public RenderMedea(EntityRendererManager manager) {
        super(manager, new ModelMedea());
    }

    @Override
    public ResourceLocation servantTexture(EntityMedea servant) {
        return textures;
    }
}
