package io.github.flemmli97.fateubw.forge.common.event;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.commands.CommandHandler;
import io.github.flemmli97.fateubw.common.event.EventCalls;
import io.github.flemmli97.fateubw.common.network.S2CPlayerCap;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.forge.common.capability.PlayerCap;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    public static final ResourceLocation playerCap = new ResourceLocation(Fate.MODID, "player_cap");
    public static final ResourceLocation stackCap = new ResourceLocation(Fate.MODID, "itemstack_cap");

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player)
            event.addCapability(playerCap, new PlayerCap());
    }

    @SubscribeEvent
    public static void command(RegisterCommandsEvent event) {
        CommandHandler.reg(event.getDispatcher());
    }

    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
            EventCalls.joinWorld(serverPlayer);
    }

    @SubscribeEvent
    public static void updateGrailWar(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.world instanceof ServerLevel serverLevel && serverLevel.dimension() == Level.OVERWORLD) {
            GrailWarHandler.get(serverLevel.getServer()).tick(serverLevel);
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            boolean rev = Platform.INSTANCE.getPlayerData(event.getOriginal()).isPresent();
            if (!rev)
                event.getOriginal().reviveCaps();
            Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> data.from(Platform.INSTANCE.getPlayerData(event.getOriginal()).orElseThrow(() -> new NullPointerException("Capability of old player is null!"))));
            NetworkCalls.INSTANCE.sendToClient(new S2CPlayerCap(Platform.INSTANCE.getPlayerData(serverPlayer).orElseThrow(() -> new NullPointerException("Capability of player is null!"))), serverPlayer);
            if (!rev)
                event.getOriginal().invalidateCaps();
        }
    }
}
