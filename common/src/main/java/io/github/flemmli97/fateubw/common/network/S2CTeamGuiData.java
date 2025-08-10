package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.world.GrailTeam;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class S2CTeamGuiData implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CTeamGuiData> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_team_gui"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CTeamGuiData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CTeamGuiData decode(RegistryFriendlyByteBuf buf) {
            return new S2CTeamGuiData(buf.readBoolean(), GrailTeam.ClientTeamInfo.read(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CTeamGuiData pkt) {
            buf.writeBoolean(pkt.open);
            pkt.info.write(buf);
        }
    };

    private final boolean open;
    private final GrailTeam.ClientTeamInfo info;

    private S2CTeamGuiData(boolean open, GrailTeam.ClientTeamInfo info) {
        this.open = open;
        this.info = info;
    }

    private S2CTeamGuiData(ServerPlayer player, boolean open) {
        this.open = open;
        TeamHandler handler = TeamHandler.get(player.getServer());
        handler.listenChanges(player);
        this.info = GrailTeam.ClientTeamInfo.create(player, handler);
    }

    public static void sendTeamData(ServerPlayer player, boolean open) {
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CTeamGuiData(player, open), player);
    }

    public static void handle(S2CTeamGuiData pkt) {
        ClientHandler.openTeamGui(pkt.open, pkt.info);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}