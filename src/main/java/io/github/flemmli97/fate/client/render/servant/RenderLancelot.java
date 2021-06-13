package io.github.flemmli97.fate.client.render.servant;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelServant;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityLancelot;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderLancelot extends ServantRenderer<EntityLancelot, ModelServant<EntityLancelot>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/lancelot.png");

    public RenderLancelot(EntityRendererManager manager) {
        super(manager, new ModelServant<>("lancelot"));
    }

    @Override
    public ResourceLocation servantTexture(EntityLancelot servant) {
        return textures;
    }
}
