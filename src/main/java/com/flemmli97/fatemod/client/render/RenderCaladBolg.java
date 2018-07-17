package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.client.model.ModelCaladBolg;
import com.flemmli97.fatemod.common.entity.EntityCaladBolg;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderCaladBolg extends RenderProjectileBase<EntityCaladBolg>{

    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/caladBolg.png");

	public RenderCaladBolg(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelCaladBolg());
	}

	@Override
	public ItemStack renderItemStackFromEntity(EntityCaladBolg entity) {
		return null;
	}

	@Override
	public int getItemType() {
		return 0;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCaladBolg entity) {
		return tex;
	}

}
