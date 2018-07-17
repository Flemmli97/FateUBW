package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.servant.SpawnEntityCustomList;
import com.flemmli97.fatemod.common.entity.servant.SpawnEntityCustomList.EntityEggInfo;
import com.flemmli97.fatemod.common.item.ItemSpawn;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;

public class MultiItemColor implements IItemColor{
	
	ItemColors itemcolors = FMLClientHandler.instance().getClient().getItemColors();

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
    {
		EntityEggInfo eggInfo = (SpawnEntityCustomList.EntityEggInfo)SpawnEntityCustomList.entityEggs.get(ItemSpawn.getEntityIdFromItem(stack));
        return eggInfo == null ? -1 : (tintIndex == 0 ? eggInfo.primaryColor : eggInfo.secondaryColor);
    }

}
