package com.flemmli97.fatemod.network;

import java.util.UUID;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.TruceMapHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTruce implements IMessage{
	
	public int type;
	public String uuid;
	public MessageTruce(){}
	
	/**
	 * @param type 0 = send request, 1 = accept request, 2 = dissolve truce
	 * @param uuid
	 */
	public MessageTruce(int type, String uuid)
	{
		this.type = type;
		this.uuid=uuid;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.type=compound.getInteger("Type");
		this.uuid=compound.getString("UUID");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Type", this.type);
		compound.setString("UUID", this.uuid);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<MessageTruce, IMessage> {

        @Override
        public IMessage onMessage(MessageTruce msg, MessageContext ctx) {
    		EntityPlayer player =  Fate.proxy.getPlayerEntity(ctx);
			if(player!=null)
			{
				if(msg.type==1)
					TruceMapHandler.get(player.world).acceptRequest(player, UUID.fromString(msg.uuid));
				else if(msg.type==2)
					TruceMapHandler.get(player.world).disbandTruce(player, UUID.fromString(msg.uuid));
				else if(msg.type==0)
					TruceMapHandler.get(player.world).sendRequest(UUID.fromString(msg.uuid), player);
        		PacketHandler.sendTo(new MessageWarTracker(player.world), (EntityPlayerMP) player);
			}	
            return null;
        }
    }
}
