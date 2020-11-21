package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntityDiarmuid;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderDiarmuid extends ServantRenderer<EntityDiarmuid, ModelServant<EntityDiarmuid>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/diarmuid.png");

    public RenderDiarmuid(EntityRendererManager manager) {
        super(manager, new ModelServant<>("diarmuid"));
    }

    @Override
    public ResourceLocation servantTexture(EntityDiarmuid servant) {
        return textures;
    }
}
