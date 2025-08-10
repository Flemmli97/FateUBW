package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.components.ServantSpawneggData;
import io.github.flemmli97.fateubw.common.items.FateEgg;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class C2SSpawnEgg implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SSpawnEgg> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("c2s_spawn_egg"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSpawnEgg> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SSpawnEgg decode(RegistryFriendlyByteBuf buf) {
            return new C2SSpawnEgg(buf.readEnum(InteractionHand.class), buf.readBoolean(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SSpawnEgg pkt) {
            buf.writeEnum(pkt.hand);
            buf.writeBoolean(pkt.master);
            buf.writeBoolean(pkt.war);
        }
    };

    private final InteractionHand hand;
    private final boolean master, war;

    public C2SSpawnEgg(InteractionHand hand, boolean master, boolean war) {
        this.hand = hand;
        this.master = master;
        this.war = war;
    }

    public static void handle(C2SSpawnEgg pkt, ServerPlayer sender) {
        if (sender != null) {
            ItemStack stack = sender.getItemInHand(pkt.hand);
            if (stack.getItem() instanceof FateEgg) {
                stack.set(FateDataComponents.SERVANT_EGG_DATA.get(), new ServantSpawneggData(pkt.master, pkt.war));
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
