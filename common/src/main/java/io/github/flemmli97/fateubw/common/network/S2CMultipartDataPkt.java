package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.MultiPartEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record S2CMultipartDataPkt(int entity, MultiPartEntity.Position offset,
                                  boolean smooth) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CMultipartDataPkt> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_multipart_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMultipartDataPkt> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CMultipartDataPkt decode(RegistryFriendlyByteBuf buf) {
            return new S2CMultipartDataPkt(buf.readInt(), new MultiPartEntity.Position(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                    new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CMultipartDataPkt pkt) {
            buf.writeInt(pkt.entity);
            buf.writeDouble(pkt.offset.anchorOffset().x());
            buf.writeDouble(pkt.offset.anchorOffset().y());
            buf.writeDouble(pkt.offset.anchorOffset().z());
            buf.writeDouble(pkt.offset.positionOffset().x());
            buf.writeDouble(pkt.offset.positionOffset().y());
            buf.writeDouble(pkt.offset.positionOffset().z());
            buf.writeBoolean(pkt.smooth);
        }
    };

    public static void handle(S2CMultipartDataPkt pkt, Player player) {
        Entity entity = player.level().getEntity(pkt.entity);
        if (entity instanceof MultiPartEntity part) {
            part.setOffset(pkt.offset);
            if (pkt.smooth)
                part.smoothMovement();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
