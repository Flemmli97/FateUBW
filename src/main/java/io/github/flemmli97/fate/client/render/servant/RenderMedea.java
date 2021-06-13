package io.github.flemmli97.fate.client.render.servant;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelServant;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityMedea;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderMedea extends ServantRenderer<EntityMedea, ModelServant<EntityMedea>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/medea.png");

    public RenderMedea(EntityRendererManager manager) {
        super(manager, new ModelServant<>("medea"));
    }

    @Override
    public ResourceLocation servantTexture(EntityMedea servant) {
        return textures;
    }
}
