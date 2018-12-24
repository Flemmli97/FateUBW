package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCommandSeals implements IMessage{
	
	public int commandSeals;
	
	public MessageCommandSeals(){}
	
	public MessageCommandSeals(IPlayer playerCap)
	{
		this.commandSeals = playerCap.getCommandSeals();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.commandSeals = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.commandSeals);		
	}
	
	public static class Handler implements IMessageHandler<MessageCommandSeals, IMessage> {

        @Override
        public IMessage onMessage(MessageCommandSeals msg, MessageContext ctx) {
    		EntityPlayer player =  Fate.proxy.getPlayerEntity(ctx);
		if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).setCommandSeals(player, msg.commandSeals);
		}	
            return null;
        }
    }
}
