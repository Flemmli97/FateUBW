package com.flemmli97.fate.client.render;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.ModelHassanCopy;
import com.flemmli97.fate.common.entity.EntityHassanCopy;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderHassanCopy extends MobRenderer<EntityHassanCopy, ModelHassanCopy> {

    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID, "textures/entity/servant/hassan.png");

    public RenderHassanCopy(EntityRendererManager manager) {
        super(manager, new ModelHassanCopy(), 0.5F);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityHassanCopy servant) {
        return textures;
    }
}
