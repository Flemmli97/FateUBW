package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record C2SGrailReward(ResourceLocation rewardID) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_grail_reward");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.rewardID);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static C2SGrailReward read(FriendlyByteBuf buf) {
        return new C2SGrailReward(buf.readResourceLocation());
    }

    public static void handle(C2SGrailReward pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        ItemStack grailItem = ItemStack.EMPTY;
        for (ItemStack stack : sender.getHandSlots()) {
            if (stack.getItem() == ModItems.grail.get()) {
                grailItem = stack;
                break;
            }
        }
        if (grailItem.isEmpty())
            for (ItemStack stack : sender.getInventory().items) {
                if (stack.getItem() == ModItems.grail.get()) {
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
}