package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.entity.MultiPartEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record S2CMultipartDataPkt(int entity, MultiPartEntity.Position offset, boolean smooth) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_multipart_data");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entity);
        buf.writeDouble(this.offset.anchorOffset().x());
        buf.writeDouble(this.offset.anchorOffset().y());
        buf.writeDouble(this.offset.anchorOffset().z());
        buf.writeDouble(this.offset.positionOffset().x());
        buf.writeDouble(this.offset.positionOffset().y());
        buf.writeDouble(this.offset.positionOffset().z());
        buf.writeBoolean(this.smooth);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CMultipartDataPkt read(FriendlyByteBuf buf) {
        return new S2CMultipartDataPkt(buf.readInt(), new MultiPartEntity.Position(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())), buf.readBoolean());
    }

    public static void handle(S2CMultipartDataPkt pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null) {
            Entity entity = player.level.getEntity(pkt.entity);
            if (entity instanceof MultiPartEntity part) {
                part.setOffset(pkt.offset);
                if (pkt.smooth)
                    part.smoothMovement();
            }
        }
    }
}
