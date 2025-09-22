package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CMana implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CMana> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_mana"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMana> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CMana decode(RegistryFriendlyByteBuf buf) {
            return new S2CMana(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CMana pkt) {
            buf.writeInt(pkt.manaValue);
        }
    };

    private final int manaValue;

    private S2CMana(int mana) {
        this.manaValue = mana;
    }

    public S2CMana(PlayerData playerMana) {
        this.manaValue = playerMana.getMana();
    }

    public static void handle(S2CMana pkt, Player player) {
        Platform.INSTANCE.getPlayerData(player).setMana(pkt.manaValue);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
