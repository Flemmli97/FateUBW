package com.flemmli97.fatemod.network;

import java.util.UUID;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.item.IExtendedReach;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageExtendedHit  implements IMessage{

	public String entityUUID;
	
	public MessageExtendedHit(){}
	
	public MessageExtendedHit(String uuid)
	{
		this.entityUUID = uuid;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		entityUUID = compound.getString("UUID");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("UUID", entityUUID);
		ByteBufUtils.writeTag(buf, compound);

	}
	
	public static class Handler implements IMessageHandler<MessageExtendedHit, IMessage> {

        @Override
        public IMessage onMessage(MessageExtendedHit msg, MessageContext ctx) {
    		EntityPlayer player =  Fate.proxy.getPlayerEntity(ctx);
    		Entity entity = null;
    		for (final Object obj : player.worldObj.loadedEntityList)
			{
        		if(obj instanceof Entity)
        		{
        			Entity entity1 = (Entity) obj;
        			if(entity1.getUniqueID().equals(UUID.fromString(msg.entityUUID)))
        				entity = entity1;
        		}
			}
    		if(player!=null && entity!=null)
			{
				ItemStack stack = player.getHeldItemMainhand();
				if(stack!=null)
					if(stack.getItem() instanceof IExtendedReach)
					{
						IExtendedReach item = (IExtendedReach) stack.getItem();
						double distance = player.getDistanceSqToEntity(entity);
						double rangeSq = item.getRange()*item.getRange();
						if(rangeSq>=distance)
						{
							player.attackTargetEntityWithCurrentItem(entity);
							player.resetCooldown();
						}
					}
			}
            return null;
        }
    }
}