package com.flemmli97.fate.network;

import com.flemmli97.fate.Fate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final SimpleChannel dispatcher =
            NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Fate.MODID, "packets"))
                    .clientAcceptedVersions(a -> true)
                    .serverAcceptedVersions(a -> true)
                    .networkProtocolVersion(() -> "v1.0").simpleChannel();

    public static void register() {
        int id = 0;
    }

    public static <T> void sendToClient(T message, ServerPlayerEntity player) {
        dispatcher.sendTo(message, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T> void sendToServer(T message) {
        dispatcher.sendToServer(message);
    }

    public static <T> void sendToAll(T message) {
        dispatcher.send(PacketDistributor.ALL.noArg(), message);
    }
}
