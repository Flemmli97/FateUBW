package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.GrailWarPlayerTracker;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageWarTracker  implements IMessage{

	public NBTTagCompound compound = new NBTTagCompound();
	public MessageWarTracker(){}
	
	public MessageWarTracker(World world)
	{
		GrailWarPlayerTracker track = GrailWarPlayerTracker.get(world);
		if(track!=null&& track.playerCount()>0)
			track.writeToNBT(compound);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<MessageWarTracker, IMessage> {

        @Override
        public IMessage onMessage(MessageWarTracker msg, MessageContext ctx) {
        	World world = Fate.proxy.getPlayerEntity(ctx).world;

			GrailWarPlayerTracker tracker = GrailWarPlayerTracker.get(world);
			if(tracker != null && msg.compound!=null)
			{
				tracker.reset();
				tracker.readFromNBT(msg.compound);
			}      	
            return null;
        }
    }
}