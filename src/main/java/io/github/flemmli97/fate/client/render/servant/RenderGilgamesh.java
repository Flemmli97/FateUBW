package io.github.flemmli97.fate.client.render.servant;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelServant;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityGilgamesh;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderGilgamesh extends ServantRenderer<EntityGilgamesh, ModelServant<EntityGilgamesh>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/gilgamesh.png");

    public RenderGilgamesh(EntityRendererManager manager) {
        super(manager, new ModelServant<>("gilgamesh"));
    }

    @Override
    public ResourceLocation servantTexture(EntityGilgamesh servant) {
        return textures;
    }
}
