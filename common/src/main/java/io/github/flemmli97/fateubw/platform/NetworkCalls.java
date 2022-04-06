package io.github.flemmli97.fateubw.platform;

import io.github.flemmli97.fateubw.common.network.Packet;
import io.github.flemmli97.tenshilib.platform.InitUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface NetworkCalls {

    NetworkCalls INSTANCE = InitUtil.getPlatformInstance(NetworkCalls.class,
            "io.github.flemmli97.fateubw.fabric.platform.NetworkCallsImpl",
            "io.github.flemmli97.fateubw.forge.platform.NetworkCallsImpl");

    void sendToClient(Packet packet, ServerPlayer player);

    void sendToTracking(Packet packet, Entity entity);

    void sendToTracking(Packet packet, ServerLevel level, BlockPos pos);

    void sendToServer(Packet packet);

    void sendToAll(Packet packet, MinecraftServer server);
}
