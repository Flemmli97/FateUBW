package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class S2CItemInUse implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_stack_in_use");

    private final int entityID;
    private final boolean inUse, mainHand;

    public S2CItemInUse(int entityID, boolean flag, boolean mainHand) {
        this.entityID = entityID;
        this.inUse = flag;
        this.mainHand = mainHand;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeBoolean(this.inUse);
        buf.writeBoolean(this.mainHand);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CItemInUse read(FriendlyByteBuf buf) {
        return new S2CItemInUse(buf.readInt(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(S2CItemInUse pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null) {
            Entity e = player.level.getEntity(pkt.entityID);
            if (e instanceof LivingEntity) {
                ItemStack stack = ((LivingEntity) e).getItemInHand(pkt.mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
                Platform.INSTANCE.getItemStackData(stack).ifPresent(cap -> cap.setInUse((LivingEntity) e, pkt.inUse, pkt.mainHand));
            }
        }
    }
}
