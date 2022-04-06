package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class S2CGrailGui implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_grail_reward_data");

    private final Map<ResourceLocation, String> rewards;

    private S2CGrailGui(Map<ResourceLocation, String> rewards) {
        this.rewards = rewards;
    }

    public S2CGrailGui() {
        this.rewards = DatapackHandler.getTablesForClient();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.rewards.size());
        this.rewards.forEach((res, s) -> {
            buf.writeResourceLocation(res);
            buf.writeUtf(s);
        });
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CGrailGui read(FriendlyByteBuf buf) {
        Map<ResourceLocation, String> rewards = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            rewards.put(buf.readResourceLocation(), buf.readUtf());
        }
        return new S2CGrailGui(rewards);
    }

    public static class Handler {
        public static void handle(S2CGrailGui pkt) {
            ClientHandler.openGrailGui(pkt.rewards);
        }
    }
}