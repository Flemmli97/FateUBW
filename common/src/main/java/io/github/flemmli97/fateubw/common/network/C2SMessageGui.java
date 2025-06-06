package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
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

    public enum Type {
        SERVANT,
        TEAM
    }
}