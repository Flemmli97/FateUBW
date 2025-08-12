package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CPlayerCap implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CPlayerCap> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_player_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CPlayerCap> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CPlayerCap decode(RegistryFriendlyByteBuf buf) {
            return new S2CPlayerCap(buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CPlayerCap pkt) {
            buf.writeInt(pkt.manaValue);
            buf.writeInt(pkt.commandSeals);
        }
    };

    public final int manaValue, commandSeals;

    private S2CPlayerCap(int mana, int commandSeals) {
        this.manaValue = mana;
        this.commandSeals = commandSeals;
    }

    public S2CPlayerCap(PlayerData cap) {
        this.manaValue = cap.getMana();
        this.commandSeals = cap.getCommandSeals();
    }

    public static void handle(S2CPlayerCap pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null)
            Platform.INSTANCE.getPlayerData(player).handleClientUpdatePacket(pkt);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
