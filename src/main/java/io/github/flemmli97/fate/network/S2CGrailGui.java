package io.github.flemmli97.fate.network;

import io.github.flemmli97.fate.client.ClientHandler;
import io.github.flemmli97.fate.common.datapack.DatapackHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class S2CGrailGui {

    private final Map<ResourceLocation, String> rewards;

    private S2CGrailGui(Map<ResourceLocation, String> rewards) {
        this.rewards = rewards;
    }

    public S2CGrailGui() {
        this.rewards = DatapackHandler.getTablesForClient();
    }

    public static S2CGrailGui read(PacketBuffer buf) {
        Map<ResourceLocation, String> rewards = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            rewards.put(buf.readResourceLocation(), buf.readString());
        }
        return new S2CGrailGui(rewards);
    }

    public static void write(S2CGrailGui pkt, PacketBuffer buf) {
        buf.writeInt(pkt.rewards.size());
        pkt.rewards.forEach((res, s) -> {
            buf.writeResourceLocation(res);
            buf.writeString(s);
        });
    }

    public static void handle(S2CGrailGui pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player == null)
                return;
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.openGrailGui(pkt.rewards));
        });
        ctx.get().setPacketHandled(true);
    }
}