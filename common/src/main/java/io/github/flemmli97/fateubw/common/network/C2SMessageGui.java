package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record C2SMessageGui(C2SMessageGui.Type message) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SMessageGui> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("c2s_gui_message"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageGui> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SMessageGui decode(RegistryFriendlyByteBuf buf) {
            return new C2SMessageGui(buf.readEnum(Type.class));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SMessageGui pkt) {
            buf.writeEnum(pkt.message);
        }
    };

    public static void handle(C2SMessageGui pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        if (pkt.message == Type.SERVANT) {
            GrailWarHandler grailWar = GrailWarHandler.get(sender.getServer());
            if (grailWar.getServant(sender) != null) {
                S2CServantGui.sendServantGui(sender, grailWar.getServant(sender));
            }
        }
        if (pkt.message == Type.TEAM) {
            S2CTeamGuiData.sendTeamData(sender, true);
        }
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Type {
        SERVANT,
        TEAM
    }
}