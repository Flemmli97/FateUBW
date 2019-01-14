package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityGem;
import com.flemmli97.fatemod.common.items.ItemGemShard;
import com.flemmli97.tenshilib.client.render.RenderProjectileItem;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;

public class RenderGem extends RenderProjectileItem<EntityGem> {
	
    public RenderGem(RenderManager render)
    {
    	super(render);
    }

	@Override
	public ItemStack getRenderItemStack(EntityGem entity) {
		return new ItemStack(ItemGemShard.getItemFromType(entity.getType()));
	}

	@Override
	public RenderType getRenderType(EntityGem entity) {
		return RenderType.NORMAL;
	}
}
