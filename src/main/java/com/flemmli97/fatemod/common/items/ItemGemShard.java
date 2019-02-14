package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemGemShard extends Item{
		
	private ShardType type;
	
	public ItemGemShard(ShardType type)
    {
		this.type=type;
        this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "gem_shard_"+type.getName()));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
	
	public static Item getItemFromType(ShardType type)
	{
		switch(type)
		{
			case EARTH:return ModItems.crystalEarth;
			case FIRE:return ModItems.crystalFire;
			case VOID:return ModItems.crystalVoid;
			case WATER:return ModItems.crystalWater;
			case WIND:return ModItems.crystalWind;
		}
		return ModItems.crystalFire;
	}
	
	public ShardType getType()
	{
		return this.type;
	}

	public static enum ShardType
	{
		FIRE("fire"),
		WATER("water"),
		EARTH("earth"),
		WIND("wind"),
		VOID("void");
		
		String name;
		ShardType(String s)
		{
			this.name=s;
		}
		
		protected String getName()
		{
			return this.name;
		}
	}
}
