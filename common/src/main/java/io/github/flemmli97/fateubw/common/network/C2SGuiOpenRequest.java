package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.entity.ServantLike;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class C2SGuiOpenRequest implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SGuiOpenRequest> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("c2s_gui_open_request"));
    public static final C2SGuiOpenRequest INSTANCE = new C2SGuiOpenRequest();
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SGuiOpenRequest> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private C2SGuiOpenRequest() {
    }

    public static void handle(C2SGuiOpenRequest pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        ServantLike<?> servant = GrailWarHandler.get(sender.getServer()).getServant(sender)
                .orElse(null);
        S2CServantGui.sendServantGui(sender, servant);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}