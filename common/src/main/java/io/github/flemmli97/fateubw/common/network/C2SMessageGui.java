package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SMessageGui(C2SMessageGui.Type message) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_gui_message");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.message);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static C2SMessageGui read(FriendlyByteBuf buf) {
        return new C2SMessageGui(buf.readEnum(Type.class));
    }

    public static void handle(C2SMessageGui pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        if (pkt.message == Type.SERVANT || pkt.message == Type.ALL) {
            PlayerData cap = Platform.INSTANCE.getPlayerData(sender).orElse(null);
            if (cap == null)
                return;
            if (cap.getServant(sender) != null)
                NetworkCalls.INSTANCE.sendToClient(new S2CServantSync(cap.getServant(sender)), sender);
        }
        if (pkt.message == Type.GRAIL || pkt.message == Type.ALL)
            NetworkCalls.INSTANCE.sendToClient(new S2CWarData(sender.getLevel()), sender);
        if (pkt.message == Type.TRUCE || pkt.message == Type.ALL)
            NetworkCalls.INSTANCE.sendToClient(new S2CTruceData(sender.getLevel(), sender), sender);
    }

    public enum Type {
        SERVANT,
        GRAIL,
        TRUCE,
        ALL
    }
}