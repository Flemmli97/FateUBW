package com.flemmli97.fatemod.proxy;


import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.gen.WorldGen;
import com.flemmli97.fatemod.common.handler.BabylonWeapon;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.handler.GuiHandler;
import com.flemmli97.fatemod.common.handler.ModEventHandler;
import com.flemmli97.fatemod.common.handler.RecipeHandler;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCap;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapNetwork;
import com.flemmli97.fatemod.common.init.ModBlocks;
import com.flemmli97.fatemod.common.init.ModEntities;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.network.CustomDataPacket;
import com.flemmli97.fatemod.network.PacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
	    	ModBlocks.init();
	    	ModItems.init();
	    	ModEntities.mainRegistry();
	    	PacketHandler.registerPackets();
	    	ConfigHandler.loadConfig(e.getModConfigurationDirectory()+"/fate/");
    }
    
    public void init(FMLInitializationEvent e) {
    		CapabilityManager.INSTANCE.register(IPlayer.class, new PlayerCapNetwork(), PlayerCap.class);
    		GameRegistry.registerWorldGenerator(new WorldGen(), 1);
     	NetworkRegistry.INSTANCE.registerGuiHandler(Fate.instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new ModEventHandler());
        RecipeHandler.registerRecipe();
    }

    public void postInit(FMLPostInitializationEvent e) {
    		BabylonWeapon.write();
    		CustomDataPacket.registerData();
    }
    
    public IThreadListener getListener(MessageContext ctx) {
    	return (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
    }
    
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
   	 return ctx.getServerHandler().playerEntity;
   	}
}