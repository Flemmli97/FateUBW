package com.flemmli97.fate.common.event;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.capability.IPlayer;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.commands.CommandHandler;
import com.flemmli97.fate.common.world.GrailWarHandler;
import com.flemmli97.fate.common.world.TruceHandler;
import com.flemmli97.fate.network.PacketHandler;
import com.flemmli97.fate.network.S2CMana;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
    public static void command(RegisterCommandsEvent event) {
        CommandHandler.reg(event.getDispatcher());
    }

    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
            GrailWarHandler handler = GrailWarHandler.get(player.getServerWorld());
            if (handler.removeConnection(player))
                handler.removePlayer(player);
            IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap).orElse(null);
            if (cap != null)
                PacketHandler.sendToClient(new S2CMana(cap), player);
            TruceHandler.get(player.getServerWorld()).pending(player).forEach(uuid -> {
                GameProfile prof = player.getServer().getPlayerProfileCache().getProfileByUUID(uuid);
                if (prof != null)
                    player.sendMessage(new TranslationTextComponent("chat.truce.pending", prof.getName()).formatted(TextFormatting.GOLD), Util.NIL_UUID);
            });
			/*if(player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player)!=null)
				this.trackEntity(player, player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player));*/
        }
    }

    @SubscribeEvent
    public static void updateGrailWar(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.world.isRemote && event.world.getRegistryKey() == World.OVERWORLD) {
            GrailWarHandler.get((ServerWorld) event.world).tick((ServerWorld) event.world);
        }
    }
}