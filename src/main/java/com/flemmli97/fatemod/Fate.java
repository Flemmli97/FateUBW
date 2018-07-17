package com.flemmli97.fatemod;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Fate.MODID, name = Fate.MODNAME, version = Fate.VERSION)
public class Fate {

	public static final String MODID = "fatemod";
	public static final String MODNAME = "Fate/Unlimited Block Works";
	public static final String VERSION = "a0.1.0";
	public static final Logger logger = LogManager.getLogger(Fate.MODID);

	@Instance
	public static Fate instance = new Fate();

	@SidedProxy(clientSide = "com.flemmli97.fatemod.proxy.ClientProxy", serverSide = "com.flemmli97.fatemod.proxy.ServerProxy")
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

	public static CreativeTabs customTab = new CreativeTabs("fatecraft") {

		@Override
		public Item getTabIconItem() {
			return ModItems.icon;
		}

		@Override
		public int getIconItemDamage() {
			Random rand = new Random();
			return rand.nextInt(4);
		}
	};

}