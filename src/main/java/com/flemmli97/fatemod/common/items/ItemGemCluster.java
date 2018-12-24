package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemGemCluster extends Item{
		
	public ItemGemCluster()
    {
        this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "gem_cluster"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
