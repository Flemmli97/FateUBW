package com.flemmli97.fatemod.common.handler;

import java.util.UUID;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.gui.ManaBar;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.fatemod.common.utils.ServantUtils;
import com.flemmli97.fatemod.network.MessageMana;
import com.flemmli97.fatemod.network.MessagePlayerServant;
import com.flemmli97.fatemod.network.MessageServantSync;
import com.flemmli97.fatemod.network.PacketHandler;
import com.flemmli97.fatemod.proxy.ClientProxy;
import com.flemmli97.tenshilib.common.config.ConfigUtils.LoadState;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
//TODO death capabilites
public class ModEventHandler {
	
	public static final ResourceLocation PlayerCap = new ResourceLocation(LibReference.MODID, "playerCap");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
		    event.addCapability(PlayerCap, new PlayerCapProvider());
        }
    }
    
    @SubscribeEvent
    public void login(PlayerLoggedInEvent event)
    {
    	if(!event.player.world.isRemote)
    	{
			GrailWarPlayerTracker.get(event.player.world).updatePlayerName(event.player);
    	}
    }
    
    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote)
        {
        	EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
        	if(GrailWarPlayerTracker.get(player.world).shouldSeverConnection(player))
        	{
        		GrailWarPlayerTracker.get(player.world).removePlayer(player);
        	}
			PacketHandler.sendTo(new MessageMana(player.getCapability(PlayerCapProvider.PlayerCap, null)), (EntityPlayerMP) player);
			for(UUID uuid : TruceMapHandler.get(player.world).getRequests(player))
			{
				String name = GrailWarPlayerTracker.get(player.world).getPlayerNameFromUUID(uuid);
				player.sendMessage(ServantUtils.setColor(new TextComponentTranslation("chat.truce.pending", name), TextFormatting.GOLD));
			}
			if(player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player)!=null)
				this.trackEntity(player, player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player));
        }
    }
    
    @SubscribeEvent
    public void tracking(PlayerEvent.StartTracking event)
    {
    	if(event.getTarget() instanceof EntityServant)
    	{
    		EntityServant servant = (EntityServant) event.getTarget();
    		if(servant.getOwner()!=null)
    		{
				PacketHandler.sendTo(new MessageServantSync(servant), (EntityPlayerMP) servant.getOwner());
    		}
    	}
    }
    
    @SubscribeEvent
    public void reTracking(PlayerEvent.StopTracking event)
    {
    	if(event.getTarget() instanceof EntityServant)
    	{
            this.trackEntity((EntityPlayerMP) event.getEntityPlayer(), event.getTarget());
    	}
    }
    
    private void trackEntity(EntityPlayerMP player, Entity e)
    {
    	Packet<?> packet = net.minecraftforge.fml.common.network.internal.FMLNetworkHandler.getEntitySpawningPacket(e);
    	player.addEntity(e);
    	player.connection.sendPacket(packet);
        if (!e.getDataManager().isEmpty())
        {
        	player.connection.sendPacket(new SPacketEntityMetadata(e.getEntityId(), e.getDataManager(), true));
        }
    }

    @SubscribeEvent
	public void onEatEvent(LivingEntityUseItemEvent.Finish event){
	    	ItemStack stack = event.getItem();
	    	if(stack!=null && stack.getItem() instanceof ItemFood)
	    	{
	    		ItemFood item = (ItemFood) stack.getItem();
	    		float foodValue = item.getHealAmount(stack);
	    		if (event.getEntityLiving() instanceof EntityPlayer && !event.getEntity().world.isRemote)
	    		{
	    			IPlayer cap = event.getEntityLiving().getCapability(PlayerCapProvider.PlayerCap, null);
	    			int mana = (int) (cap.getMana()+foodValue*5);
	    			if(mana>100)
	    			{
	    				mana=100;
	    			}
	    			cap.setMana((EntityPlayer) event.getEntityLiving(), mana);
	    		}
	    	}
    }
    
    @SubscribeEvent
    public void updateGrailWar(WorldTickEvent event)
    {
		if(event.phase == Phase.END && !event.world.isRemote && event.world.provider.getDimension()==0)
		{
			GrailWarPlayerTracker.get(event.world).updateGrailWar((WorldServer) event.world);;
		}
    }
    
    @SubscribeEvent
    public void clone(PlayerEvent.Clone event) 
    {
        if (event.isWasDeath()) 
        {
            IPlayer capSync = event.getOriginal().getCapability(PlayerCapProvider.PlayerCap, null);
            NBTTagCompound oldNBT = new NBTTagCompound();
            capSync.writeToNBT(oldNBT);
            event.getEntityPlayer().getCapability(PlayerCapProvider.PlayerCap, null).readFromNBT(oldNBT);
        }
    }
    
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void keyEvent(KeyInputEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		World world = Minecraft.getMinecraft().world;
		if(ClientProxy.command.isPressed())
		{
        	player.openGui(Fate.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);  
        }
		if(ClientProxy.special.isPressed())
		{
			PacketHandler.sendToServer(new MessagePlayerServant(3));
		}
		if(ClientProxy.boost.isPressed())
		{
			PacketHandler.sendToServer(new MessagePlayerServant(10));
		}
    }	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderManaBar(RenderGameOverlayEvent.Post event)
	{				
		if (event.isCancelable() || event.getType() != ElementType.EXPERIENCE)return;
		{
			new ManaBar(Minecraft.getMinecraft());
		}
	}
	
	@SubscribeEvent
	public void config(OnConfigChangedEvent event)
	{
		if(event.getModID().equals(LibReference.MODID))
			ConfigHandler.load(LoadState.SYNC);
	}
}
