package io.github.flemmli97.fateubw.fabric.platform;

import io.github.flemmli97.fateubw.common.network.Packet;
import io.github.flemmli97.fateubw.fabric.common.network.ClientPacketHandler;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class NetworkCallsImpl implements NetworkCalls {

    @Override
    public void sendToClient(Packet packet, ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.write(buf);
        ServerPlayNetworking.send(player, packet.getID(), buf);
    }

    @Override
    public void sendToTracking(Packet packet, Entity entity) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.write(buf);
        PlayerLookup.tracking(entity).forEach(player -> ServerPlayNetworking.send(player, packet.getID(), buf));
    }

    @Override
    public void sendToTracking(Packet packet, ServerLevel level, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.write(buf);
        PlayerLookup.tracking(level, pos).forEach(player -> ServerPlayNetworking.send(player, packet.getID(), buf));
    }

    @Override
    public void sendToServer(Packet packet) {
        ClientPacketHandler.sendToServer(packet);
    }

    @Override
    public void sendToAll(Packet packet, MinecraftServer server) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.write(buf);
        PlayerLookup.all(server).forEach(player -> ServerPlayNetworking.send(player, packet.getID(), buf));
    }
}
