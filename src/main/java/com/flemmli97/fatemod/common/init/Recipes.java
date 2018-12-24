package com.flemmli97.fatemod.common.init;

import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class Recipes {

	@SubscribeEvent
	public static final void registerRecipe(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(new ShapelessOreRecipe(new ResourceLocation(LibReference.MODID, "crystal_cluster"), new ItemStack(ModItems.crystalCluster), new Object[]{	
        		new ItemStack(ModItems.crystalWind), 
        		new ItemStack(ModItems.crystalFire),
        		new ItemStack(ModItems.crystalVoid),
        		new ItemStack(ModItems.crystalEarth),
        		new ItemStack(ModItems.crystalWater)}).setRegistryName(new ResourceLocation(LibReference.MODID, "crystal_cluster")));
		
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(LibReference.MODID, "altar"), new ItemStack(ModBlocks.grailWarKey), new Object[] {
				"LGL", "GDG", "LGL",
				'L', new ItemStack(Blocks.LAPIS_BLOCK),
				'G', new ItemStack(ModItems.crystalCluster),
				'D', Blocks.DIAMOND_BLOCK}).setRegistryName(new ResourceLocation(LibReference.MODID, "altar")));
		
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(LibReference.MODID, "chalk"), new ItemStack(ModItems.chalk), new Object[] {
				"  B", " S ", "C  ",
				'B', new ItemStack(Items.DYE, 1 , 15),
				'S', new ItemStack(Items.STICK),
				'C', Ingredient.fromItems(ModItems.crystals)}).setRegistryName(new ResourceLocation(LibReference.MODID, "chalk")));
	}
}
