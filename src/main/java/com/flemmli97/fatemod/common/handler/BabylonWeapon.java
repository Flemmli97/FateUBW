package com.flemmli97.fatemod.common.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.flemmli97.fatemod.common.items.weapons.ClassSpear;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BabylonWeapon {
	
	private static List<String> weapons = new ArrayList<String>();
	
	public static void init()
	{
		for(Item item : ForgeRegistries.ITEMS)
		{
			if(item instanceof ItemSword || item instanceof ItemTool || item instanceof ClassSpear)
			{				
				weapons.add(item.getRegistryName().toString());			
			}
		}		
	}

	public static ItemStack getWeapon()
	{
		Random rand = new Random();
        ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(weapons.get(rand.nextInt(weapons.size())))), 1, 0);
        return stack;
	}
}
