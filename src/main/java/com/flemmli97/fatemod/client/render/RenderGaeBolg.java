package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;

public class RenderGaeBolg extends RenderProjectileBase<EntityGaeBolg>{
	
    public RenderGaeBolg(RenderManager renderManagerIn)
    {
        super(renderManagerIn, true);
    }

	@Override
	public ItemStack renderItemStackFromEntity(EntityGaeBolg entity) {
		return new ItemStack(ModItems.gaebolg);
	}

	@Override
	public int getItemType() {
		return 0;
	}

}