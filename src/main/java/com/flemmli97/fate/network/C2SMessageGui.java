package com.flemmli97.fate.network;

import com.flemmli97.fate.common.capability.IPlayer;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SMessageGui {

    private final Type message;

    public C2SMessageGui(Type i) {
        this.message = i;
    }

    public static C2SMessageGui read(PacketBuffer buf) {
        return new C2SMessageGui(Type.values()[buf.readInt()]);
    }

    public static void write(C2SMessageGui pkt, PacketBuffer buf) {
        buf.writeInt(pkt.message.ordinal());
    }

    public static void handle(C2SMessageGui pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null)
                return;
            if (pkt.message == Type.SERVANT || pkt.message == Type.ALL) {
                IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap).orElse(null);
                if (cap == null)
                    return;
                if (cap.getServant(player) != null)
                    PacketHandler.sendToClient(new S2CServantSync(cap.getServant(player)), player);
            }
            if (pkt.message == Type.GRAIL || pkt.message == Type.ALL)
                PacketHandler.sendToClient(new S2CWarData(player.getServerWorld()), player);
            if (pkt.message == Type.TRUCE || pkt.message == Type.ALL)
                PacketHandler.sendToClient(new S2CTruceData(player.getServerWorld(), player), player);
        });
        ctx.get().setPacketHandled(true);
    }

    public enum Type {
        SERVANT,
        GRAIL,
        TRUCE,
        ALL
    }
}