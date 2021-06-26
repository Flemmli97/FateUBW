package io.github.flemmli97.fate.network;

import io.github.flemmli97.fate.client.ClientHandler;
import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CServantSync {

    private int entityID;
    private final boolean none;

    private S2CServantSync(boolean none, int id) {
        this.entityID = id;
        this.none = none;
    }

    public S2CServantSync(EntityServant servant) {
        this.none = servant == null;
        if (servant != null)
            this.entityID = servant.getEntityId();
    }

    public static S2CServantSync read(PacketBuffer buf) {
        return new S2CServantSync(buf.readBoolean(), buf.readInt());
    }

    public static void write(S2CServantSync pkt, PacketBuffer buf) {
        buf.writeBoolean(pkt.none);
        buf.writeInt(pkt.entityID);
    }

    public static void handle(S2CServantSync pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player != null) {
                Entity fromId = pkt.none ? null : player.world.getEntityByID(pkt.entityID);
                if (fromId instanceof EntityServant)
                    player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.setServant(player, (EntityServant) fromId));
                else
                    player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.setServant(player, null));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}