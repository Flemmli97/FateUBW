package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityMagicBeam;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderMagicBeam extends RenderExcalibur<EntityMagicBeam>{

    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/beam.png");

	public RenderMagicBeam(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.radiusPoint=0.3F;
	}

	@Override
	public ItemStack renderItemStackFromEntity(EntityMagicBeam entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getItemType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMagicBeam entity) {
		return tex;
	}

}
