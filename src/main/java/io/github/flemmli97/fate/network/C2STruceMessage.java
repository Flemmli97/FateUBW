package io.github.flemmli97.fate.network;

import io.github.flemmli97.fate.common.world.TruceHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2STruceMessage {

    public Type type;
    public UUID uuid;

    public C2STruceMessage(Type type, UUID uuid) {
        this.type = type;
        this.uuid = uuid;
    }

    public static C2STruceMessage read(PacketBuffer buf) {
        return new C2STruceMessage(Type.values()[buf.readByte()], buf.readUniqueId());
    }

    public static void write(C2STruceMessage pkt, PacketBuffer buf) {
        buf.writeByte(pkt.type.ordinal());
        buf.writeUniqueId(pkt.uuid);
    }

    public static void handle(C2STruceMessage pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null)
                return;
            switch (pkt.type) {
                case ACCEPT:
                    TruceHandler.get(player.getServerWorld()).accept(player, pkt.uuid);
                    break;
                case SEND:
                    TruceHandler.get(player.getServerWorld()).sendRequest(player, pkt.uuid);
                    break;
                case DENY:
                    TruceHandler.get(player.getServerWorld()).disband(player, pkt.uuid);
                    break;
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public enum Type {
        ACCEPT,
        SEND,
        DENY
    }
}