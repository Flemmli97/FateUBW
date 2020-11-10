package com.flemmli97.fate.client.render.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.servant.ModelDiarmuid;
import com.flemmli97.fate.client.model.servant.ModelLancelot;
import com.flemmli97.fate.client.render.ServantRenderer;
import com.flemmli97.fate.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fate.common.entity.servant.EntityLancelot;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderLancelot extends ServantRenderer<EntityLancelot, ModelLancelot> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/lancelot.png");

    public RenderLancelot(EntityRendererManager manager) {
        super(manager, new ModelLancelot());
    }

    @Override
    public ResourceLocation servantTexture(EntityLancelot servant) {
        return textures;
    }
}
