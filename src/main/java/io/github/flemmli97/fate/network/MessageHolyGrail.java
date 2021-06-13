/*package com.flemmli97.fate.network;

import java.util.function.Predicate;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.GrailReward;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.EntityUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHolyGrail  implements IMessage{

	private String reward;
	public MessageHolyGrail(){}
	
	public MessageHolyGrail(String reward)
	{
		this.reward = reward;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.reward = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.reward);
	}
	
	public static class Handler implements IMessageHandler<MessageHolyGrail, IMessage> {

		private static final Predicate<ItemStack> grailPred = (stack)->stack.getItem()==ModItems.grail;
		
        @Override
        public IMessage onMessage(MessageHolyGrail msg, MessageContext ctx) {
        	EntityPlayer player = Fate.proxy.getPlayerEntity(ctx);
        	ItemStack grail = EntityUtil.findItem(player, grailPred, false, false);
        	if(!grail.isEmpty())
        	{
        		GrailReward.giveRewards(msg.reward, player);
        		if(!player.capabilities.isCreativeMode)
        			grail.shrink(1);
        	}
            return null;
        }
    }
}*/