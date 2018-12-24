package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.items.ItemSpawn;
import com.flemmli97.fatemod.common.utils.SpawnEntityCustomList;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;

public class MultiItemColor implements IItemColor{
	
	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex)
    {
		EntityList.EntityEggInfo eggInfo = SpawnEntityCustomList.entityEggs.get(ItemSpawn.getEntityIdFromItem(stack));
        return eggInfo == null ? -1 : (tintIndex == 0 ? eggInfo.primaryColor : eggInfo.secondaryColor);
    }

}
