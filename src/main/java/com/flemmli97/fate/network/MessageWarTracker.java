/*package com.flemmli97.fate.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.GrailWarHandler;
import com.flemmli97.fatemod.common.handler.TruceMapHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageWarTracker  implements IMessage{

	public NBTTagCompound grailWarTracker = new NBTTagCompound();
	public NBTTagCompound truceMap = new NBTTagCompound();

	public MessageWarTracker(){}
	
	public MessageWarTracker(World world)
	{
		GrailWarHandler.get(world).writeToNBT(this.grailWarTracker);
		TruceMapHandler.get(world).writeToNBT(this.truceMap);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.grailWarTracker=compound.getCompoundTag("GrailWarTracker");
		this.truceMap=compound.getCompoundTag("TruceMap");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("GrailWarTracker", this.grailWarTracker);
		compound.setTag("TruceMap", this.truceMap);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<MessageWarTracker, IMessage> {

        @Override
        public IMessage onMessage(MessageWarTracker msg, MessageContext ctx) {
        	World world = Fate.proxy.getPlayerEntity(ctx).world;

			GrailWarHandler tracker = GrailWarHandler.get(world);
				tracker.reset(world);
				tracker.readFromNBT(msg.grailWarTracker);
			TruceMapHandler truce = TruceMapHandler.get(world);
				truce.reset();
				truce.readFromNBT(msg.truceMap);
			Fate.proxy.updateGuiTruce();
            return null;
        }
    }
}*/