package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.client.model.ModelStarfishDemon;
import com.flemmli97.fatemod.common.entity.EntityLesserMonster;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderStarfish extends RenderLiving<EntityLesserMonster>{

	public static ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/starfish.png");
	
	public RenderStarfish(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelStarfishDemon(), 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLesserMonster entity) {
		return tex;
	}

}
