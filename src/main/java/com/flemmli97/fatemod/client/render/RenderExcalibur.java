package com.flemmli97.fatemod.client.render;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderBeam;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderExcalibur extends RenderBeam<EntityExcalibur>{
	
    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/excalibur.png");
    private static final Pair<ResourceLocation,Integer> start = Pair.of(new ResourceLocation(LibReference.MODID, "textures/entity/excalibur_start.png"), 3);
    private static final Pair<ResourceLocation,Integer> end = Pair.of(new ResourceLocation(LibReference.MODID, "textures/entity/excalibur_end.png"), 1);

    public RenderExcalibur(RenderManager renderManagerIn)
    {
        super(renderManagerIn, EntityExcalibur.radius);
        this.setAlpha(200);
    }  

	@Override
	protected ResourceLocation getEntityTexture(EntityExcalibur entity) {
		return tex;
	}

	@Override
	public Pair<ResourceLocation, Integer> startTexture(EntityExcalibur entity) {
		return start;
	}

	@Override
	public Pair<ResourceLocation, Integer> endTexture(EntityExcalibur entity) {
		return end;
	}
}
