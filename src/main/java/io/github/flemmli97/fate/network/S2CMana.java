package io.github.flemmli97.fate.network;

import io.github.flemmli97.fate.client.ClientHandler;
import io.github.flemmli97.fate.common.capability.IPlayer;
import io.github.flemmli97.fate.common.capability.PlayerCapProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CMana {

    private final int manaValue;

    private S2CMana(int mana) {
        this.manaValue = mana;
    }

    public S2CMana(IPlayer playerMana) {
        this.manaValue = playerMana.getMana();
    }

    public static S2CMana read(PacketBuffer buf) {
        return new S2CMana(buf.readInt());
    }

    public static void write(S2CMana pkt, PacketBuffer buf) {
        buf.writeInt(pkt.manaValue);
    }

    public static void handle(S2CMana pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player != null)
                player.getCapability(PlayerCapProvider.PlayerCap, null).ifPresent(cap -> cap.setMana(player, pkt.manaValue));
        });
        ctx.get().setPacketHandled(true);
    }
}
