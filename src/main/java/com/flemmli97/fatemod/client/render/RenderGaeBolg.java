package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.client.render.RenderProjectileItem;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;

public class RenderGaeBolg extends RenderProjectileItem<EntityGaeBolg>{
	
    public RenderGaeBolg(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

	@Override
	public ItemStack getRenderItemStack(EntityGaeBolg entity) {
		return new ItemStack(ModItems.gaebolg);
	}

	@Override
	public RenderType getRenderType(EntityGaeBolg entity) {
		return RenderType.WEAPON;
	}
}