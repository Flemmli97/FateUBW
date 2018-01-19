package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMana  implements IMessage{

	public float manaValue;
	
	public MessageMana(){}
	
	public MessageMana(IPlayer playerMana)
	{
		this.manaValue = playerMana.getMana();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		manaValue = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(manaValue);		
	}
	
	public static class Handler implements IMessageHandler<MessageMana, IMessage> {

        @Override
        public IMessage onMessage(MessageMana msg, MessageContext ctx) {
    		float mana = msg.manaValue;
    		EntityPlayer player =  Fate.proxy.getPlayerEntity(ctx);
		if(player!=null)
		{
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(capSync != null)
		    {
				capSync.setMana(player, mana);				     	
		    }					
		}	
            return null;
        }
    }
}
