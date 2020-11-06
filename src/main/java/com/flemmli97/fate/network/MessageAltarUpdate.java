package com.flemmli97.fate.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAltarUpdate {

    private final boolean message;
    private final int x, y, z;

    private MessageAltarUpdate(int x, int y, int z, boolean summoning) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.message = summoning;
    }

    public MessageAltarUpdate(BlockPos tilePos, boolean summoning) {
        this.x = tilePos.getX();
        this.y = tilePos.getY();
        this.z = tilePos.getZ();
        this.message = summoning;
    }

    public static MessageAltarUpdate read(PacketBuffer buf) {
        return new MessageAltarUpdate(buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean());
    }

    public static void write(MessageAltarUpdate pkt, PacketBuffer buf) {
        buf.writeInt(pkt.x);
        buf.writeInt(pkt.y);
        buf.writeInt(pkt.z);
        buf.writeBoolean(pkt.message);
    }

    public static void handle(MessageAltarUpdate pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
			/*
			EntityPlayer player = Fate.proxy.getPlayerEntity(ctx);
        	BlockPos pos = new BlockPos(msg.x, msg.y, msg.z);
    		TileEntity tile = player.world.getTileEntity(pos);
    		if(tile instanceof TileAltar)
    		{
    			TileAltar altar = (TileAltar) tile;
    			altar.updateSummoning(msg.message);
        	}
			 */
        });
        ctx.get().setPacketHandled(true);
    }
}