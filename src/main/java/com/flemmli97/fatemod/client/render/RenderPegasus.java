package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityPegasus;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderPegasus extends RenderLiving<EntityPegasus>{

	public static ResourceLocation tex = new ResourceLocation(LibReference.MODID, "");
	public RenderPegasus(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelHorse(), 1);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPegasus entity) {
		return tex;
	}

}
