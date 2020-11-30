package com.flemmli97.fate.client.render;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.entity.EntityArcherArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderArcherArrow extends ArrowRenderer<EntityArcherArrow> {

    public final ResourceLocation LOCATION = new ResourceLocation(Fate.MODID, "textures/entity/arrows.png");

    public RenderArcherArrow(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityArcherArrow arrow) {
        return this.LOCATION;
    }
}
