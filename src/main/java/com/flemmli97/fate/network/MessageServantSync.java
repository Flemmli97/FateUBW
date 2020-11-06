/*package com.flemmli97.fate.network;

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

	//Since i have no idea what range the entityId goes from.
	public NBTTagCompound servant = new NBTTagCompound();
	public int entityID;
	public MessageServantSync(){}
	
	public MessageServantSync(EntityServant servant)
	{
		if(servant!=null)
		{
			servant.writeToNBTAtomically(this.servant);
			this.entityID=servant.getEntityId();
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.servant=compound.getCompoundTag("Entity");
		this.entityID=compound.getInteger("ID");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("Entity", this.servant);
		compound.setInteger("ID", this.entityID);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<MessageServantSync, IMessage> {

        @Override
        public IMessage onMessage(MessageServantSync msg, MessageContext ctx) {
        	Fate.proxy.getListener(ctx).addScheduledTask(new Runnable() {
				@Override
				public void run() 
				{
					EntityPlayer player =  Fate.proxy.getPlayerEntity(ctx);
					if(player!=null)
					{
						IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null); 
						Entity fromId = player.world.getEntityByID(msg.entityID);
						if(fromId instanceof EntityServant)
							capSync.setServant(player, (EntityServant) fromId);
						else
							capSync.setServant(player, null);
					
					}
				}});
            return null;
        }
    }
}
*/