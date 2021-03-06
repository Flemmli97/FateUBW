package io.github.flemmli97.fate.client.render.servant;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelHeracles;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityHeracles;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderHeracles extends ServantRenderer<EntityHeracles, ModelHeracles> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/heracles.png");

    public RenderHeracles(EntityRendererManager manager) {
        super(manager, new ModelHeracles());
    }

    @Override
    public ResourceLocation servantTexture(EntityHeracles servant) {
        return textures;
    }
}
