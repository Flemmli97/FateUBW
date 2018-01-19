package com.flemmli97.fatemod.common.handler;

import com.flemmli97.fatemod.common.init.ModBlocks;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHandler {

	static boolean multiyPlayer = ConfigHandler.multiPlayer;
	public static final void registerRecipe()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.crystalCluster), new Object[]{	
        		new ItemStack(ModItems.crystal, 1, 0), 
        		new ItemStack(ModItems.crystal, 1, 1),
        		new ItemStack(ModItems.crystal, 1, 2),
        		new ItemStack(ModItems.crystal, 1, 3),
        		new ItemStack(ModItems.crystal, 1, 4)});
		GameRegistry.addRecipe(new ItemStack(ModBlocks.grailWarKey), new Object[] {
				"LGL", "GDG", "LGL",
				'L', new ItemStack(Blocks.LAPIS_BLOCK),
				'G', new ItemStack(ModItems.crystalCluster),
				'D', Blocks.DIAMOND_BLOCK});
		GameRegistry.addRecipe(new ItemStack(ModItems.chalk), new Object[] {
				"  B", " S ", "C  ",
				'B', new ItemStack(Items.DYE, 1 , 15),
				'S', new ItemStack(Items.STICK),
				'C', new ItemStack(ModItems.crystal, 1, OreDictionary.WILDCARD_VALUE)});
	}
}
