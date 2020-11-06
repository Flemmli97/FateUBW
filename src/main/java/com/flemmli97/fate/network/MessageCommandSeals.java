package com.flemmli97.fate.network;

import com.flemmli97.fate.common.capability.IPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCommandSeals {

    private final int commandSeals;

    private MessageCommandSeals(int seals) {
        this.commandSeals = seals;
    }

    public MessageCommandSeals(IPlayer playerCap) {
        this.commandSeals = playerCap.getCommandSeals();
    }

    public static MessageCommandSeals read(PacketBuffer buf) {
        return new MessageCommandSeals(buf.readInt());
    }

    public static void write(MessageCommandSeals pkt, PacketBuffer buf) {
        buf.writeInt(pkt.commandSeals);
    }

    public static void handle(MessageCommandSeals pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
			/*PlayerEntity player =  Fate.proxy.getPlayerEntity(ctx);
			if(player!=null)
			{
				player.getCapability(PlayerCapProvider.PlayerCap, null).setCommandSeals(player, msg.commandSeals);
			}*/
        });
        ctx.get().setPacketHandled(true);
    }
}
