package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemIcon extends Item{

	public ItemIcon(int i) {
		this.setMaxStackSize(1);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "icon_"+i));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
}
