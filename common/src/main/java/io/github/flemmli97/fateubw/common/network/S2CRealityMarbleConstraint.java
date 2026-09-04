package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.attachment.RealityMarbleConstraint;
import io.github.flemmli97.fateubw.common.registry.FateAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CRealityMarbleConstraint implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CRealityMarbleConstraint> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_reality_marble_constraint"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CRealityMarbleConstraint> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CRealityMarbleConstraint decode(RegistryFriendlyByteBuf buf) {
            return new S2CRealityMarbleConstraint(buf.readInt(), RealityMarbleConstraint.Data.STREAM_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CRealityMarbleConstraint pkt) {
            buf.writeInt(pkt.entity);
            RealityMarbleConstraint.Data.STREAM_CODEC.encode(buf, pkt.data);
        }
    };

    public final int entity;
    public final RealityMarbleConstraint.Data data;

    private S2CRealityMarbleConstraint(int entity, RealityMarbleConstraint.Data data) {
        this.entity = entity;
        this.data = data;
    }

    public S2CRealityMarbleConstraint(Entity entity) {
        this(entity.getId(), FateAttachments.REALITY_MARBLE_CONSTRAINT.get().get(entity).pack());
    }

    public static void handle(S2CRealityMarbleConstraint pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null) {
            Entity entity = player.level().getEntity(pkt.entity);
            if (entity != null) {
                FateAttachments.REALITY_MARBLE_CONSTRAINT.get().get(entity).setConstraint(pkt.data.center().orElse(null), pkt.data.radius());
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
