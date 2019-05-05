package com.flemmli97.fatemod.proxy;


import java.util.List;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.gui.GuiHandler;
import com.flemmli97.fatemod.common.entity.servant.EntityHassanCopy;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.gen.WorldGen;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.handler.ModEventHandler;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCap;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapNetwork;
import com.flemmli97.fatemod.common.init.AdvancementRegister;
import com.flemmli97.fatemod.common.init.ModEntities;
import com.flemmli97.fatemod.network.CustomDataPacket;
import com.flemmli97.fatemod.network.PacketHandler;
import com.flemmli97.tenshilib.common.config.ConfigUtils.LoadState;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    private static int servants;

	public void preInit(FMLPreInitializationEvent e) {
    	ConfigHandler.load(LoadState.PREINIT);
    	ModEntities.mainRegistry();
    	PacketHandler.registerPackets();
    	AdvancementRegister.init();
    }
    
    public void init(FMLInitializationEvent e) {
		CapabilityManager.INSTANCE.register(IPlayer.class, new PlayerCapNetwork(), PlayerCap::new);
		GameRegistry.registerWorldGenerator(new WorldGen(), 1);
     	NetworkRegistry.INSTANCE.registerGuiHandler(Fate.instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new ModEventHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
    	ConfigHandler.load(LoadState.POSTINIT);
		CustomDataPacket.registerData();
		ForgeChunkManager.setForcedChunkLoadingCallback(Fate.instance, new LoadingCallback() {
			@Override
			public void ticketsLoaded(List<Ticket> tickets, World world) {
				for(Ticket ticket : tickets)
				{
					if(ticket.getEntity()!=null && ticket.getEntity().isEntityAlive())
					{
						if(ticket.getModData().hasKey("Chunk"))
						{
							int[] arr = ticket.getModData().getIntArray("Chunk");
							ForgeChunkManager.forceChunk(ticket, new ChunkPos(arr[0], arr[1]));
						}
					}
				}
			}});
		ForgeRegistries.ENTITIES.forEach(entry->{
			if(EntityServant.class.isAssignableFrom(entry.getEntityClass()) && !entry.getEntityClass().equals(EntityHassanCopy.class))
			{
				++servants;
			}
		});
    }
    
    public static int servants()
    {
    	return servants;
    }
    
    public IThreadListener getListener(MessageContext ctx) {
    	return (WorldServer) ctx.getServerHandler().player.world;
    }
    
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
   	 return ctx.getServerHandler().player;
   	}
    
    public String entityName(EntityServant servant)
    {
    	return servant.getRealName();
    }
    
    public void updateGuiTruce()
    {
    	
    }
    
    public IAnimationStateMachine getASM(ResourceLocation res)
    {
    	return null;
    }
}