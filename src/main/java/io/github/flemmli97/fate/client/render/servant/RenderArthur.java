package io.github.flemmli97.fate.client.render.servant;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelServant;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityArthur;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderArthur extends ServantRenderer<EntityArthur, ModelServant<EntityArthur>> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/arthur.png");

    public RenderArthur(EntityRendererManager manager) {
        super(manager, new ModelServant<>("arthur"));
    }

    @Override
    public ResourceLocation servantTexture(EntityArthur servant) {
        return textures;
    }
}
