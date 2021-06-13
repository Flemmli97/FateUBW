package io.github.flemmli97.fate.client.render.servant;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelServant;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityHassan;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderHassan extends ServantRenderer<EntityHassan, ModelServant<EntityHassan>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/hassan.png");

    public RenderHassan(EntityRendererManager manager) {
        super(manager, new ModelServant<>("hassan"));
    }

    @Override
    public ResourceLocation servantTexture(EntityHassan servant) {
        return textures;
    }
}
