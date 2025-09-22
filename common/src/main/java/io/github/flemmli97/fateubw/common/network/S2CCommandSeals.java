package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CCommandSeals implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CCommandSeals> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_command_seal"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CCommandSeals> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CCommandSeals decode(RegistryFriendlyByteBuf buf) {
            return new S2CCommandSeals(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CCommandSeals pkt) {
            buf.writeInt(pkt.commandSeals);
        }
    };

    private final int commandSeals;

    private S2CCommandSeals(int commandSeals) {
        this.commandSeals = commandSeals;
    }

    public S2CCommandSeals(PlayerData playerCap) {
        this.commandSeals = playerCap.getCommandSeals();
    }

    public static void handle(S2CCommandSeals pkt, Player player) {
        Platform.INSTANCE.getPlayerData(player).setCommandSeals(pkt.commandSeals);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
