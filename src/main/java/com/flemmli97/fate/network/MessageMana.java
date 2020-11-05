package com.flemmli97.fate.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMana  implements IMessage{

	public int manaValue;
	
	public MessageMana(){}
	
	public MessageMana(IPlayer playerMana)
	{
		this.manaValue = playerMana.getMana();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
        this.manaValue = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.manaValue);
	}
	
	public static class Handler implements IMessageHandler<MessageMana, IMessage> {

        @Override
        public IMessage onMessage(MessageMana msg, MessageContext ctx) {
        	Fate.proxy.getListener(ctx).addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayer player =  Fate.proxy.getPlayerEntity(ctx);
					if(player!=null)
					{
						player.getCapability(PlayerCapProvider.PlayerCap, null).setMana(player, msg.manaValue);				     	
					}
				}
        	});	
            return null;
        }
    }
}
