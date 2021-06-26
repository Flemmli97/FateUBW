package io.github.flemmli97.fate.network;

import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SServantSpecial {

    private final String specialID;

    public C2SServantSpecial(String specialID) {
        this.specialID = specialID;
    }

    public static C2SServantSpecial read(PacketBuffer buf) {
        return new C2SServantSpecial(buf.readString());
    }

    public static void write(C2SServantSpecial pkt, PacketBuffer buf) {
        buf.writeString(pkt.specialID);
    }

    public static void handle(C2SServantSpecial pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player == null)
                return;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                if (cap.getServant(player) != null)
                    cap.getServant(player).doSpecialCommand(pkt.specialID);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
