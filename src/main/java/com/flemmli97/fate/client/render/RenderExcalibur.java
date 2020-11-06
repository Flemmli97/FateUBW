package com.flemmli97.fate.client.render;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.entity.EntityExcalibur;
import com.flemmli97.tenshilib.client.render.RenderBeam;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public class RenderExcalibur extends RenderBeam<EntityExcalibur> {

    private static final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/excalibur.png");

    public RenderExcalibur(EntityRendererManager manager) {
        super(manager, EntityExcalibur.radius);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityExcalibur entity) {
        return tex;
    }

    @Override
    public Pair<ResourceLocation, Float> startTexture(EntityExcalibur entity) {
        return null;
    }

    @Override
    public Pair<ResourceLocation, Float> endTexture(EntityExcalibur entity) {
        return null;
    }

    @Override
    public double segmentLength() {
        return 0;
    }

    @Override
    public int animationFrames(BeamPart part) {
        //if(part==BeamPart.MIDDLE)
        //  return 1;
        return 1;
    }
}
