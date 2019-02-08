package com.flemmli97.fatemod.client.render;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityMagicBeam;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderBeam;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMagicBeam extends RenderBeam<EntityMagicBeam>{

    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/beam.png");

	public RenderMagicBeam(RenderManager renderManagerIn) {
		super(renderManagerIn, 0.5f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityMagicBeam entity) {
		return tex;
	}

	@Override
	public Pair<ResourceLocation, Float> startTexture(EntityMagicBeam entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<ResourceLocation, Float> endTexture(EntityMagicBeam entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
