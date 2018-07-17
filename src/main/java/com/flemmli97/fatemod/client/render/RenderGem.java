package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityGem;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;

public class RenderGem extends RenderProjectileBase<EntityGem> {
	
    public RenderGem(RenderManager render)
    {
    	super(render, true);
    }

	@Override
	public ItemStack renderItemStackFromEntity(EntityGem entity) {
		return new ItemStack(ModItems.crystal, 1, entity.getStackMeta());
	}

	@Override
	public int getItemType() {
		return 1;
	}
}
