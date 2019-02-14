package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityGem;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.client.render.RenderProjectileItem;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;

public class RenderGem extends RenderProjectileItem<EntityGem> {
	
    public RenderGem(RenderManager render)
    {
    	super(render);
    }

    private static final ItemStack gem = new ItemStack(ModItems.crystalCluster);
    
	@Override
	public ItemStack getRenderItemStack(EntityGem entity) {
		return gem;
	}

	@Override
	public RenderType getRenderType(EntityGem entity) {
		return RenderType.NORMAL;
	}
}
