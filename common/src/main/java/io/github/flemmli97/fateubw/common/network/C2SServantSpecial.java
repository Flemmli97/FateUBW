package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2SServantSpecial implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_servant_special");

    private final String specialID;

    public C2SServantSpecial(String specialID) {
        this.specialID = specialID;
    }

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
        Platform.INSTANCE.getPlayerData(sender).ifPresent(cap -> {
            if (cap.getServant(sender) != null)
                cap.getServant(sender).doSpecialCommand(pkt.specialID);
        });
    }
}
