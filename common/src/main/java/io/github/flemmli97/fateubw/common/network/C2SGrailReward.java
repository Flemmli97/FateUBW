package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record C2SGrailReward(ResourceLocation rewardID) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SGrailReward> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("c2s_grail_reward"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SGrailReward> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SGrailReward decode(RegistryFriendlyByteBuf buf) {
            return new C2SGrailReward(buf.readResourceLocation());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SGrailReward pkt) {
            buf.writeResourceLocation(pkt.rewardID);
        }
    };

    public static void handle(C2SGrailReward pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        ItemStack grailItem = ItemStack.EMPTY;
        for (ItemStack stack : sender.getHandSlots()) {
            if (stack.getItem() == FateItems.GRAIL.get()) {
                grailItem = stack;
                break;
            }
        }
        if (grailItem.isEmpty())
            for (ItemStack stack : sender.getInventory().items) {
                if (stack.getItem() == FateItems.GRAIL.get()) {
                    grailItem = stack;
                    break;
                }
            }
        if (!grailItem.isEmpty()) {
            if (!sender.isCreative())
                grailItem.shrink(1);
            DatapackHandler.getLootTable(pkt.rewardID).ifPresent(loot -> loot.give(sender));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}