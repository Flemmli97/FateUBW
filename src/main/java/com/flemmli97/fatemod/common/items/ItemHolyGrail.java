package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemHolyGrail extends Item{

	//Rewards: xp, items, keep servant
	public ItemHolyGrail() {
    	this.setCreativeTab(Fate.customTab);
    	this.setMaxStackSize(1);
    	this.setRegistryName(new ResourceLocation(LibReference.MODID, "holy_grail"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
