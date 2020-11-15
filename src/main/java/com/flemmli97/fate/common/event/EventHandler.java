package com.flemmli97.fate.common.event;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.commands.CommandHandler;
import com.flemmli97.fate.common.world.GrailWarHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fate.MODID)
public class EventHandler {

    public static final ResourceLocation cap = new ResourceLocation(Fate.MODID, "fate_cap");

    @SubscribeEvent
    public static void capability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity)
            event.addCapability(cap, new PlayerCapProvider());
    }

    @SubscribeEvent
    public static void serverStart(RegisterCommandsEvent event) {
        CommandHandler.reg(event.getDispatcher());
    }

    /*
     @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote)
        {
        	EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
        	GrailWarHandler handler =GrailWarHandler.get(player.world);
        	if(handler.shouldSeverConnection(player))
        	{
        		handler.removePlayer(player);
        	}
			PacketHandler.sendTo(new MessageMana(player.getCapability(PlayerCapProvider.PlayerCap, null)), (EntityPlayerMP) player);
			for(UUID uuid : TruceMapHandler.get(player.world).getRequests(player))
			{
				String name = GrailWarHandler.get(player.world).getPlayerNameFromUUID(uuid);
				player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.truce.pending", name), TextFormatting.GOLD));
			}
			if(player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player)!=null)
				this.trackEntity(player, player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player));
        }
    }
     */

    @SubscribeEvent
    public static void updateGrailWar(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.world.isRemote && event.world.getRegistryKey() == World.OVERWORLD) {
            GrailWarHandler.get((ServerWorld) event.world).tick((ServerWorld) event.world);
        }
    }
}
