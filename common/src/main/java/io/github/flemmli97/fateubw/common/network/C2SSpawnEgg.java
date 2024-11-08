package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.items.FateEgg;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class C2SSpawnEgg implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_spawn_egg");

    private final InteractionHand hand;
    private final boolean master, war;

    public C2SSpawnEgg(InteractionHand hand, boolean master, boolean war) {
        this.hand = hand;
        this.master = master;
        this.war = war;
    }

    public static C2SSpawnEgg read(FriendlyByteBuf buf) {
        return new C2SSpawnEgg(buf.readEnum(InteractionHand.class), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(C2SSpawnEgg pkt, ServerPlayer sender) {
        if (sender != null) {
            ItemStack stack = sender.getItemInHand(pkt.hand);
            if (stack.getItem() instanceof FateEgg) {
                FateEgg.withMaster(stack, pkt.master);
                FateEgg.setJoinWar(stack, pkt.war);
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.hand);
        buf.writeBoolean(this.master);
        buf.writeBoolean(this.war);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
