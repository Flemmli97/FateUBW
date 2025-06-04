package io.github.flemmli97.fateubw.common.event;

import io.github.flemmli97.fateubw.common.network.S2CPlayerCap;
import io.github.flemmli97.fateubw.common.registry.ModEffects;
import io.github.flemmli97.fateubw.common.world.GrailTeam;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class EventCalls {

    public static void joinWorld(ServerPlayer player) {
        GrailWarHandler handler = GrailWarHandler.get(player.getServer());
        if (handler.removeConnection(player))
            handler.removePlayer(player, true);
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> NetworkCalls.INSTANCE.sendToClient(new S2CPlayerCap(data), player));
        TeamHandler teamHandler = TeamHandler.get(player.getServer());
        List<GrailTeam.ShortTeamInfo> invites = teamHandler.fetchInvitesFor(player);
        if (!invites.isEmpty()) {
            player.sendMessage(new TranslatableComponent("fateubw.chat.team.invite.pending", invites.toString()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
        }
        List<GrailTeam.ShortTeamInfo> requests = teamHandler.fetchRequestsFor(player, teamHandler.getTeamFor(player));
        if (!requests.isEmpty()) {
            player.sendMessage(new TranslatableComponent("fateubw.chat.team.request.pending", requests.toString()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
        }
    }

    public static void tick(LivingEntity entity) {
        if (entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.tick(player));
    }

    public static boolean canHeal(LivingEntity entity) {
        return !entity.hasEffect(ModEffects.GAE_BUIDHE.get());
    }
}