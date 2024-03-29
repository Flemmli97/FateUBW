package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SServantSpecial(String specialID) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_servant_special");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.specialID);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static C2SServantSpecial read(FriendlyByteBuf buf) {
        return new C2SServantSpecial(buf.readUtf());
    }

    public static void handle(C2SServantSpecial pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        Platform.INSTANCE.getPlayerData(sender).ifPresent(data -> {
            if (data.getServant(sender) != null)
                data.getServant(sender).doSpecialCommand(pkt.specialID);
        });
    }
}
