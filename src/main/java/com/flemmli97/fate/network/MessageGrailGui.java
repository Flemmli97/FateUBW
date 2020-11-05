package com.flemmli97.fate.network;

import java.util.Set;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.GrailReward;
import com.google.common.collect.Sets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGrailGui  implements IMessage{

	private Set<String> rewards;
	public MessageGrailGui(){}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.rewards = Sets.newLinkedHashSet();
		int ix = buf.readInt();
		for(int i = 0; i < ix; i++)
		{
			this.rewards.add(ByteBufUtils.readUTF8String(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		Set<String> rew = GrailReward.lootSets();
		buf.writeInt(rew.size());
		for(String s : rew)
			ByteBufUtils.writeUTF8String(buf, s);
	}
	
	public static class Handler implements IMessageHandler<MessageGrailGui, IMessage> {
		
        @Override
        public IMessage onMessage(MessageGrailGui msg, MessageContext ctx) {
        	Fate.proxy.openGrailGui(msg.rewards);
            return null;
        }
    }
}