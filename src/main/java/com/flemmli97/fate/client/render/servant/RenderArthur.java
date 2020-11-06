package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.servant.ModelArthur;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntityArthur;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderArthur extends ServantRenderer<EntityArthur, ModelArthur> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/arthur.png");

    public RenderArthur(EntityRendererManager manager) {
        super(manager, new ModelArthur());
    }

    @Override
    public ResourceLocation servantTexture(EntityArthur servant) {
        return textures;
    }
}
