package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record C2SServantSpecial(String specialID, int entityId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SServantSpecial> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("c2s_servant_special"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SServantSpecial> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SServantSpecial decode(RegistryFriendlyByteBuf buf) {
            return new C2SServantSpecial(buf.readUtf(), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SServantSpecial pkt) {
            buf.writeUtf(pkt.specialID);
            buf.writeInt(pkt.entityId);
        }
    };

    public static void handle(C2SServantSpecial pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        BaseServant servant = C2SServantCommand.getServant(sender, pkt.entityId);
        if (servant != null)
            servant.doSpecialCommand(pkt.specialID);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
