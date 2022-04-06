package io.github.flemmli97.fateubw.common.event;

import io.github.flemmli97.fateubw.common.network.S2CPlayerCap;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.common.world.TruceHandler;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

public class EventCalls {

    public static void joinWorld(ServerPlayer player) {
        GrailWarHandler handler = GrailWarHandler.get(player.getServer());
        if (handler.removeConnection(player))
            handler.removePlayer(player);
        Platform.INSTANCE.getPlayerData(player).ifPresent(cap -> NetworkCalls.INSTANCE.sendToClient(new S2CPlayerCap(cap), player));
        TruceHandler.get(player.getServer()).pending(player).forEach(uuid -> {
            player.getServer().getProfileCache().get(uuid)
                    .ifPresent(prof ->
                            player.sendMessage(new TranslatableComponent("chat.truce.pending", prof.getName()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID));
        });
    }
}