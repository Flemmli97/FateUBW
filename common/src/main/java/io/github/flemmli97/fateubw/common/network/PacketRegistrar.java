package io.github.flemmli97.fateubw.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PacketRegistrar {

    public static void registerServerPackets(ServerPacketRegister register) {
        register.register(C2SGrailReward.TYPE, C2SGrailReward.STREAM_CODEC, C2SGrailReward::handle);
        register.register(C2SGuiOpenRequest.TYPE, C2SGuiOpenRequest.STREAM_CODEC, C2SGuiOpenRequest::handle);
        register.register(C2SMessageGui.TYPE, C2SMessageGui.STREAM_CODEC, C2SMessageGui::handle);
        register.register(C2SServantCommand.TYPE, C2SServantCommand.STREAM_CODEC, C2SServantCommand::handle);
        register.register(C2SSpawnEgg.TYPE, C2SSpawnEgg.STREAM_CODEC, C2SSpawnEgg::handle);
        register.register(C2STeamMessage.TYPE, C2STeamMessage.STREAM_CODEC, C2STeamMessage::handle);
        register.register(C2STeamUuidMessage.TYPE, C2STeamUuidMessage.STREAM_CODEC, C2STeamUuidMessage::handle);
    }

    public static void registerClientPackets(ClientPacketRegister register) {
        register.register(S2CAltarUpdate.TYPE, S2CAltarUpdate.STREAM_CODEC, S2CAltarUpdate::handle);
        register.register(S2CAttackDebug.TYPE, S2CAttackDebug.STREAM_CODEC, S2CAttackDebug::handle);
        register.register(S2CCommandSeals.TYPE, S2CCommandSeals.STREAM_CODEC, S2CCommandSeals::handle);
        register.register(S2CGrailGui.TYPE, S2CGrailGui.STREAM_CODEC, S2CGrailGui::handle);
        register.register(S2CMana.TYPE, S2CMana.STREAM_CODEC, S2CMana::handle);
        register.register(S2CMultipartDataPkt.TYPE, S2CMultipartDataPkt.STREAM_CODEC, S2CMultipartDataPkt::handle);
        register.register(S2CPlayerCap.TYPE, S2CPlayerCap.STREAM_CODEC, S2CPlayerCap::handle);
        register.register(S2CScreenShake.TYPE, S2CScreenShake.STREAM_CODEC, S2CScreenShake::handle);
        register.register(S2CServantGui.TYPE, S2CServantGui.STREAM_CODEC, S2CServantGui::handle);
        register.register(S2CSpawnEggScreen.TYPE, S2CSpawnEggScreen.STREAM_CODEC, S2CSpawnEggScreen::handle);
        register.register(S2CTeamGuiData.TYPE, S2CTeamGuiData.STREAM_CODEC, S2CTeamGuiData::handle);
    }

    public interface ServerPacketRegister {

        <P extends CustomPacketPayload> void register(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, ServerPlayer> handler);
    }

    public interface ClientPacketRegister {

        default <P extends CustomPacketPayload> void register(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, Consumer<P> handler) {
            this.register(type, codec, (pkt, p) -> handler.accept(pkt));
        }

        <P extends CustomPacketPayload> void register(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, Player> handler);
    }
}
