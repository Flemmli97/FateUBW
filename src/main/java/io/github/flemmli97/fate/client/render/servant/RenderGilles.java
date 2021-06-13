package io.github.flemmli97.fate.client.render.servant;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelServant;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityGilles;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderGilles extends ServantRenderer<EntityGilles, ModelServant<EntityGilles>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/gilles.png");

    public RenderGilles(EntityRendererManager manager) {
        super(manager, new ModelServant<>("gilles"));
    }

    @Override
    public ResourceLocation servantTexture(EntityGilles servant) {
        return textures;
    }
}
