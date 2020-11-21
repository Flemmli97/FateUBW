package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntityIskander;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderIskander extends ServantRenderer<EntityIskander, ModelServant<EntityIskander>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/iskander.png");

    public RenderIskander(EntityRendererManager manager) {
        super(manager, new ModelServant<>("iskander"));
    }

    @Override
    public ResourceLocation servantTexture(EntityIskander servant) {
        return textures;
    }
}
