package io.github.flemmli97.fate.network;

import io.github.flemmli97.fate.common.datapack.DatapackHandler;
import io.github.flemmli97.fate.common.registry.ModItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SGrailReward {

    private final ResourceLocation rewardID;

    public C2SGrailReward(ResourceLocation rewardID) {
        this.rewardID = rewardID;
    }

    public static C2SGrailReward read(PacketBuffer buf) {
        return new C2SGrailReward(buf.readResourceLocation());
    }

    public static void write(C2SGrailReward pkt, PacketBuffer buf) {
        buf.writeResourceLocation(pkt.rewardID);
    }

    public static void handle(C2SGrailReward pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                ItemStack grailItem = ItemStack.EMPTY;
                for (ItemStack stack : player.getHeldEquipment()) {
                    if (stack.getItem() == ModItems.grail.get()) {
                        grailItem = stack;
                        break;
                    }
                }
                if (grailItem.isEmpty())
                    for (ItemStack stack : player.inventory.mainInventory) {
                        if (stack.getItem() == ModItems.grail.get()) {
                            grailItem = stack;
                            break;
                        }
                    }
                if (!grailItem.isEmpty()) {
                    if (!player.abilities.isCreativeMode)
                        grailItem.shrink(1);
                    DatapackHandler.getLootTable(pkt.rewardID).ifPresent(loot -> loot.give(player));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}