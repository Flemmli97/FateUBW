package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SServantSpecial(String specialID, int entityId) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_servant_special");

    public static C2SServantSpecial read(FriendlyByteBuf buf) {
        return new C2SServantSpecial(buf.readUtf(), buf.readInt());
    }

    public static void handle(C2SServantSpecial pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        BaseServant servant = C2SServantCommand.getServant(sender, pkt.entityId);
        if (servant != null)
            servant.doSpecialCommand(pkt.specialID);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.specialID);
        buf.writeInt(this.entityId);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
