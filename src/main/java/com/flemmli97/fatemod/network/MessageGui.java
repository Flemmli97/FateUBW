package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGui  implements IMessage{

	int message;
	public MessageGui(){}
	
	/** 1=ServantInfo, 2=GrailWarTracker*/
	public MessageGui(int i)
	{
		message = i;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		message = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(message);
	}
	
	public static class Handler implements IMessageHandler<MessageGui, IMessage> {

        @Override
        public IMessage onMessage(MessageGui msg, MessageContext ctx) {
        	EntityPlayer player = Fate.proxy.getPlayerEntity(ctx);
        	if(msg.message==1)
        	{
        		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
        		if(cap.getServant(player)!=null)
        			PacketHandler.sendTo(new MessageServantSync(cap.getServant(player)), (EntityPlayerMP) player);
        	}
        	else if(msg.message==2)
        	{
        		PacketHandler.sendTo(new MessageWarTracker(player.world), (EntityPlayerMP) player);
        	}
            return null;
        }
    }
}