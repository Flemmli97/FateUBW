package io.github.flemmli97.fateubw.forge.platform;

import io.github.flemmli97.fateubw.common.network.Packet;
import io.github.flemmli97.fateubw.forge.common.network.PacketHandler;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class NetworkCallsImpl implements NetworkCalls {

    @Override
    public void sendToClient(Packet packet, ServerPlayer player) {
        PacketHandler.sendToClient(packet, player);
    }

    @Override
    public void sendToTracking(Packet packet, Entity entity) {
        PacketHandler.sendToTracking(packet, entity);
    }

    @Override
    public void sendToTracking(Packet packet, ServerLevel level, BlockPos pos) {
        PacketHandler.vanillaChunkPkt(packet, level, pos);
    }

    @Override
    public void sendToServer(Packet packet) {
        PacketHandler.sendToServer(packet);
    }

    @Override
    public void sendToAll(Packet packet, MinecraftServer server) {
        PacketHandler.sendToAll(packet);
    }
}
