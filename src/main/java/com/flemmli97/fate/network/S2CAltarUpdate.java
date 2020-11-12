package com.flemmli97.fate.network;

import com.flemmli97.fate.client.ClientHandler;
import com.flemmli97.fate.common.blocks.tile.TileAltar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CAltarUpdate {

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

    public static S2CAltarUpdate read(PacketBuffer buf) {
        return new S2CAltarUpdate(buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean());
    }

    public static void write(S2CAltarUpdate pkt, PacketBuffer buf) {
        buf.writeInt(pkt.x);
        buf.writeInt(pkt.y);
        buf.writeInt(pkt.z);
        buf.writeBoolean(pkt.message);
    }

    public static void handle(S2CAltarUpdate pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player == null)
                return;
            BlockPos pos = new BlockPos(pkt.x, pkt.y, pkt.z);
            TileEntity tile = player.world.getTileEntity(pos);
            if (tile instanceof TileAltar)
                ((TileAltar) tile).updateSummoning(pkt.message);
        });
        ctx.get().setPacketHandled(true);
    }
}