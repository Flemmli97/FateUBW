package io.github.flemmli97.fate.common.event;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.capability.IPlayer;
import io.github.flemmli97.fate.common.capability.PlayerCap;
import io.github.flemmli97.fate.common.commands.CommandHandler;
import io.github.flemmli97.fate.common.world.GrailWarHandler;
import io.github.flemmli97.fate.common.world.TruceHandler;
import io.github.flemmli97.fate.network.PacketHandler;
import io.github.flemmli97.fate.network.S2CMana;
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

public class EventHandler {

    public static final ResourceLocation cap = new ResourceLocation(Fate.MODID, "fate_cap");

    @SubscribeEvent
    public static void capability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity)
            event.addCapability(cap, new PlayerCap());
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
            IPlayer cap = player.getCapability(CapabilityInsts.PlayerCap).orElse(null);
            if (cap != null)
                PacketHandler.sendToClient(new S2CMana(cap), player);
            TruceHandler.get(player.getServerWorld()).pending(player).forEach(uuid -> {
                GameProfile prof = player.getServer().getPlayerProfileCache().getProfileByUUID(uuid);
                if (prof != null)
                    player.sendMessage(new TranslationTextComponent("chat.truce.pending", prof.getName()).mergeStyle(TextFormatting.GOLD), Util.DUMMY_UUID);
            });
			/*if(player.getCapability(CapabilityInsts.PlayerCap, null).getServant(player)!=null)
				this.trackEntity(player, player.getCapability(CapabilityInsts.PlayerCap, null).getServant(player));*/
        }
    }

    @SubscribeEvent
    public static void updateGrailWar(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.world.isRemote && event.world.getDimensionKey() == World.OVERWORLD) {
            GrailWarHandler.get((ServerWorld) event.world).tick((ServerWorld) event.world);
        }
    }
}