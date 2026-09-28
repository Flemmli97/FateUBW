package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.entity.ServantLike;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record C2SServantSpecial(String specialID,
                                C2SServantCommand.EntityData entityData) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SServantSpecial> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("c2s_servant_special"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SServantSpecial> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SServantSpecial decode(RegistryFriendlyByteBuf buf) {
            return new C2SServantSpecial(buf.readUtf(), C2SServantCommand.EntityData.STREAM_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SServantSpecial pkt) {
            buf.writeUtf(pkt.specialID);
            C2SServantCommand.EntityData.STREAM_CODEC.encode(buf, pkt.entityData());
        }
    };

    public C2SServantSpecial(String specialID, S2CServantGui.ServantMetaData data) {
        this(specialID, data != null ? new C2SServantCommand.EntityData(data.entityId(), data.dimension()) : null);
    }

    public static void handle(C2SServantSpecial pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        ServantLike<?> servant = C2SServantCommand.getServant(sender, pkt.entityData);
        if (servant != null)
            servant.doSpecialCommand(sender, pkt.specialID);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
