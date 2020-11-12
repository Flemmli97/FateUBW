package com.flemmli97.fate.client.render;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.entity.EntityExcalibur;
import com.flemmli97.tenshilib.client.render.RenderBeam;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderExcalibur extends RenderBeam<EntityExcalibur> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/excalibur.png");

    public RenderExcalibur(EntityRendererManager manager) {
        super(manager, EntityExcalibur.radius);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityExcalibur entity) {
        return tex;
    }

    @Override
    public ResourcePair startTexture(EntityExcalibur entity) {
        return null;
    }

    @Override
    public ResourcePair endTexture(EntityExcalibur entity) {
        return null;
    }

    //getTextSeeThrough
    @Override
    protected RenderType getRenderLayer(EntityExcalibur entity, ResourceLocation loc) {
        return super.getRenderLayer(entity, loc);
    }
}