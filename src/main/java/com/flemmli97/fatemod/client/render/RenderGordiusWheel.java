package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.client.model.ModelGordiusWheel;
import com.flemmli97.fatemod.common.entity.EntityGordiusWheel;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGordiusWheel extends RenderLiving<EntityGordiusWheel>{

	public static ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/gordiusWheel.png");

	public RenderGordiusWheel(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelGordiusWheel(), 1);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGordiusWheel entity) {
		return tex;
	}

}
