package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServantSync  implements IMessage{

	public int id;
	//Since i have no idea what range the entityId goes from.
	public boolean isNull;
	
	public MessageServantSync(){}
	
	public MessageServantSync(EntityServant servant)
	{
		if(servant==null)
			this.isNull=true;
		else
			this.id=servant.getEntityId();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.id=buf.readInt();
		this.isNull=buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.id);
		buf.writeBoolean(isNull);
	}
	
	public static class Handler implements IMessageHandler<MessageServantSync, IMessage> {

        @Override
        public IMessage onMessage(MessageServantSync msg, MessageContext ctx) {
    		EntityPlayer player =  Fate.proxy.getPlayerEntity(ctx);
			if(player!=null)
			{
				IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null); 
				Entity e = player.world.getEntityByID(msg.id);
				if(capSync != null)
				{
					if(e instanceof EntityServant)
						capSync.setServant(player, (EntityServant) e);
					else if(msg.isNull)
						capSync.setServant(player, null);
				}					
			}
            return null;
        }
    }
}
