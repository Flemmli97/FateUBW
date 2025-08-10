package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.blocks.entity.AltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class S2CAltarUpdate implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CAltarUpdate> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_altar_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CAltarUpdate> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CAltarUpdate decode(RegistryFriendlyByteBuf buf) {
            return new S2CAltarUpdate(buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CAltarUpdate pkt) {
            buf.writeInt(pkt.x);
            buf.writeInt(pkt.y);
            buf.writeInt(pkt.z);
            buf.writeBoolean(pkt.summoning);
        }
    };

    private final boolean summoning;
    private final int x, y, z;

    private S2CAltarUpdate(int x, int y, int z, boolean summoning) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.summoning = summoning;
    }

    public S2CAltarUpdate(BlockPos tilePos, boolean summoning) {
        this.x = tilePos.getX();
        this.y = tilePos.getY();
        this.z = tilePos.getZ();
        this.summoning = summoning;
    }

    public static void handle(S2CAltarUpdate pkt, Player player) {
        BlockPos pos = new BlockPos(pkt.x, pkt.y, pkt.z);
        BlockEntity tile = player.level().getBlockEntity(pos);
        if (tile instanceof AltarBlockEntity altar)
            altar.updateSummoning(pkt.summoning);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}