package io.github.flemmli97.fate.client.render.servant;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelServant;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityEmiya;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderEmiya extends ServantRenderer<EntityEmiya, ModelServant<EntityEmiya>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/emiya.png");

    public RenderEmiya(EntityRendererManager manager) {
        super(manager, new ModelServant<>("emiya"));
    }

    @Override
    public ResourceLocation servantTexture(EntityEmiya servant) {
        return textures;
    }
}
