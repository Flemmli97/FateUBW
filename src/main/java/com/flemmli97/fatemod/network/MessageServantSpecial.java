package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServantSpecial implements IMessage{

	private String specialID;
	
	public MessageServantSpecial(){}
	
	public MessageServantSpecial(String specialID)
	{
		this.specialID = specialID;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
        this.specialID = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.specialID);
	}
	
	public static class Handler implements IMessageHandler<MessageServantSpecial, IMessage> {
		
		@Override
        public IMessage onMessage(MessageServantSpecial msg, MessageContext ctx) {
			EntityPlayer player = Fate.proxy.getPlayerEntity(ctx);
        	IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
        	EntityServant servant = capSync.getServant(player);
        	if(servant==null)
        	{
        		return null;
        	}
        	servant.doSpecialCommand(msg.specialID);
        	return null;
		}
	}
}
