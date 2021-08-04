package io.github.flemmli97.fate.network;

import io.github.flemmli97.fate.client.ClientHandler;
import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.capability.PlayerCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class S2CPlayerCap {

    public final int manaValue, commandSeals;
    public final UUID servantUUID;

    private S2CPlayerCap(int mana, int commandSeals, UUID servantUUID) {
        this.manaValue = mana;
        this.commandSeals = commandSeals;
        this.servantUUID = servantUUID;
    }

    public S2CPlayerCap(PlayerCap cap) {
        this.manaValue = cap.getMana();
        this.commandSeals = cap.getCommandSeals();
        this.servantUUID = cap.getServantUUID();
    }

    public static S2CPlayerCap read(PacketBuffer buf) {
        return new S2CPlayerCap(buf.readInt(), buf.readInt(), buf.readBoolean() ? buf.readUniqueId() : null);
    }

    public static void write(S2CPlayerCap pkt, PacketBuffer buf) {
        buf.writeInt(pkt.manaValue);
        buf.writeInt(pkt.commandSeals);
        boolean nonnull = pkt.servantUUID != null;
        buf.writeBoolean(nonnull);
        if (nonnull) {
            buf.writeUniqueId(pkt.servantUUID);
        }
    }

    public static void handle(S2CPlayerCap pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player != null)
                player.getCapability(CapabilityInsts.PlayerCap, null).ifPresent(cap -> cap.handleClientUpdatePacket(pkt));
        });
        ctx.get().setPacketHandled(true);
    }
}
