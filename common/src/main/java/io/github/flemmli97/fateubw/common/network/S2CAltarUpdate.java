package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.blocks.tile.AltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class S2CAltarUpdate implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "altar");

    private final boolean message;
    private final int x, y, z;

    private S2CAltarUpdate(int x, int y, int z, boolean summoning) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.message = summoning;
    }

    public S2CAltarUpdate(BlockPos tilePos, boolean summoning) {
        this.x = tilePos.getX();
        this.y = tilePos.getY();
        this.z = tilePos.getZ();
        this.message = summoning;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeBoolean(this.message);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CAltarUpdate read(FriendlyByteBuf buf) {
        return new S2CAltarUpdate(buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean());
    }

    public static class Handler {
        public static void handle(S2CAltarUpdate pkt) {
            Player player = ClientHandler.clientPlayer();
            if (player == null)
                return;
            BlockPos pos = new BlockPos(pkt.x, pkt.y, pkt.z);
            BlockEntity tile = player.level.getBlockEntity(pos);
            if (tile instanceof AltarBlockEntity)
                ((AltarBlockEntity) tile).updateSummoning(pkt.message);
        }
    }
}