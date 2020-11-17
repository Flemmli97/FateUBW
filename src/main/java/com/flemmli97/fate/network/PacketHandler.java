package com.flemmli97.fate.network;

import com.flemmli97.fate.Fate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
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
        dispatcher.registerMessage(id++, S2CAltarUpdate.class, S2CAltarUpdate::write, S2CAltarUpdate::read, S2CAltarUpdate::handle);
        dispatcher.registerMessage(id++, S2CCommandSeals.class, S2CCommandSeals::write, S2CCommandSeals::read, S2CCommandSeals::handle);
        dispatcher.registerMessage(id++, C2SMessageGui.class, C2SMessageGui::write, C2SMessageGui::read, C2SMessageGui::handle);
        dispatcher.registerMessage(id++, S2CMana.class, S2CMana::write, S2CMana::read, S2CMana::handle);
        dispatcher.registerMessage(id++, C2SServantCommand.class, C2SServantCommand::write, C2SServantCommand::read, C2SServantCommand::handle);
        dispatcher.registerMessage(id++, C2SServantSpecial.class, C2SServantSpecial::write, C2SServantSpecial::read, C2SServantSpecial::handle);
        dispatcher.registerMessage(id++, S2CServantSync.class, S2CServantSync::write, S2CServantSync::read, S2CServantSync::handle);
        dispatcher.registerMessage(id++, S2CWarData.class, S2CWarData::write, S2CWarData::read, S2CWarData::handle);
        dispatcher.registerMessage(id++, C2STruceMessage.class, C2STruceMessage::write, C2STruceMessage::read, C2STruceMessage::handle);
        dispatcher.registerMessage(id++, S2CTruceData.class, S2CTruceData::write, S2CTruceData::read, S2CTruceData::handle);
    }

    public static <T> void sendToClient(T message, ServerPlayerEntity player) {
        dispatcher.sendTo(message, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void vanillaChunkPkt(IPacket<?> pkt, ServerWorld world, BlockPos pos){
        PacketDistributor.TRACKING_CHUNK.with(()->world.getChunkAt(pos)).send(pkt);
    }

    public static <T> void sendToServer(T message) {
        dispatcher.sendToServer(message);
    }

    public static <T> void sendToAll(T message) {
        dispatcher.send(PacketDistributor.ALL.noArg(), message);
    }
}
