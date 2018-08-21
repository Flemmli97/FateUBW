package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
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
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.id=compound.getInteger("Id");
		this.isNull=compound.getBoolean("IsNull");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Id", this.id);
		compound.setBoolean("IsNull", this.isNull);
		ByteBufUtils.writeTag(buf, compound);
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
