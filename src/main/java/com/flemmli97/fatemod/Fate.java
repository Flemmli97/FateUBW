package com.flemmli97.fatemod;

import java.util.Random;

import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.fatemod.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LibReference.MODID, name = LibReference.MODNAME, version = LibReference.VERSION)
public class Fate {

	@Instance
	public static Fate instance = new Fate();

	@SidedProxy(clientSide = "com.flemmli97.fatemod.proxy.ClientProxy", serverSide = "com.flemmli97.fatemod.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

	public static CreativeTabs customTab = new CreativeTabs("fate") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.icon, 1, new Random().nextInt(4));
		}
	};

}