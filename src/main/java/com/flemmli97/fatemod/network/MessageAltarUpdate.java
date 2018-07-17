package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.blocks.tile.TileAltar;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageAltarUpdate  implements IMessage{

	boolean message;
	int x, y, z;
	BlockPos pos;
	public MessageAltarUpdate(){}
	
	public MessageAltarUpdate(BlockPos tilePos, boolean summoning)
	{
		this.x = tilePos.getX();
		this.y = tilePos.getY();
		this.z = tilePos.getZ();
		message = summoning;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.message = compound.getBoolean("summoning");
		this.x = compound.getInteger("x");
		this.y = compound.getInteger("y");
		this.z = compound.getInteger("z");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("summoning", this.message);
		compound.setInteger("x", this.x);
		compound.setInteger("y", this.y);
		compound.setInteger("z", this.z);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<MessageAltarUpdate, IMessage> {

        @Override
        public IMessage onMessage(MessageAltarUpdate msg, MessageContext ctx) {
        	EntityPlayer player = Fate.proxy.getPlayerEntity(ctx);
        	BlockPos pos = new BlockPos(msg.x, msg.y, msg.z);
    		TileEntity tile = player.world.getTileEntity(pos);
    		if(tile instanceof TileAltar)
    		{
    			TileAltar altar = (TileAltar) tile;
    			altar.updateSummoning(msg.message);
        	}
            return null;
        }
    }
}