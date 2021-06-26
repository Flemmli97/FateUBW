package io.github.flemmli97.fate.network;

import io.github.flemmli97.fate.client.ClientHandler;
import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CItemInUse {

    private final int entityID;
    private final boolean inUse, mainHand;

    public S2CItemInUse(int entityID, boolean flag, boolean mainHand) {
        this.entityID = entityID;
        this.inUse = flag;
        this.mainHand = mainHand;
    }

    public static S2CItemInUse read(PacketBuffer buf) {
        return new S2CItemInUse(buf.readInt(), buf.readBoolean(), buf.readBoolean());
    }

    public static void write(S2CItemInUse pkt, PacketBuffer buf) {
        buf.writeInt(pkt.entityID);
        buf.writeBoolean(pkt.inUse);
        buf.writeBoolean(pkt.mainHand);
    }

    public static void handle(S2CItemInUse pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player != null) {
                Entity e = player.world.getEntityByID(pkt.entityID);
                if (e instanceof LivingEntity) {
                    ItemStack stack = ((LivingEntity) e).getHeldItem(pkt.mainHand ? Hand.MAIN_HAND : Hand.OFF_HAND);
                    stack.getCapability(CapabilityInsts.ItemStackCap).ifPresent(cap -> cap.setInUse((LivingEntity) e, pkt.inUse, pkt.mainHand));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
