package com.flemmli97.fate.network;

import com.flemmli97.fate.client.ClientHandler;
import com.flemmli97.fate.common.capability.IPlayer;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CCommandSeals {

    private final int commandSeals;

    private S2CCommandSeals(int seals) {
        this.commandSeals = seals;
    }

    public S2CCommandSeals(IPlayer playerCap) {
        this.commandSeals = playerCap.getCommandSeals();
    }

    public static S2CCommandSeals read(PacketBuffer buf) {
        return new S2CCommandSeals(buf.readInt());
    }

    public static void write(S2CCommandSeals pkt, PacketBuffer buf) {
        buf.writeInt(pkt.commandSeals);
    }

    public static void handle(S2CCommandSeals pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player != null)
                player.getCapability(PlayerCapProvider.PlayerCap, null).ifPresent(cap -> cap.setCommandSeals(player, pkt.commandSeals));
        });
        ctx.get().setPacketHandled(true);
    }
}
