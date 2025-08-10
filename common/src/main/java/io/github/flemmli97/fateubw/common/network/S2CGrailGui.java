package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class S2CGrailGui implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CGrailGui> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_grail_reward_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CGrailGui> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CGrailGui decode(RegistryFriendlyByteBuf buf) {
            Map<ResourceLocation, Component> rewards = new HashMap<>();
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                rewards.put(buf.readResourceLocation(), ComponentSerialization.STREAM_CODEC.decode(buf));
            }
            return new S2CGrailGui(rewards);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CGrailGui pkt) {
            buf.writeInt(pkt.rewards.size());
            pkt.rewards.forEach((res, s) -> {
                buf.writeResourceLocation(res);
                ComponentSerialization.STREAM_CODEC.encode(buf, s);
            });
        }
    };

    private final Map<ResourceLocation, Component> rewards;

    private S2CGrailGui(Map<ResourceLocation, Component> rewards) {
        this.rewards = rewards;
    }

    public S2CGrailGui() {
        this.rewards = DatapackHandler.getTablesForClient();
    }

    public static void handle(S2CGrailGui pkt) {
        ClientHandler.openGrailGui(pkt.rewards);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}