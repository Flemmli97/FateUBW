package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.handler.GrailWarHandler;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.utils.ServantUtils;
import com.flemmli97.tenshilib.common.TextHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

//from client to server
public class MessagePlayerServant  implements IMessage{

	private int command;
	
	public MessagePlayerServant(){}
	
	public MessagePlayerServant(int command)
	{
		this.command = command;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		command = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(command);
		
	}
	
	public static class Handler implements IMessageHandler<MessagePlayerServant, IMessage> {

        @Override
        public IMessage onMessage(MessagePlayerServant msg, MessageContext ctx) {
        	EntityPlayer player = Fate.proxy.getPlayerEntity(ctx);
        	IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);     
        	if(capSync.getServant(player)==null)
        	{
        		return null;
        	}
    		//normal mode
    		if(msg.command == 0)
    		{
    			capSync.getServant(player).updateAI(0);
    			player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.attackservant"), TextFormatting.RED));
    		}
    		//aggressive mode
    		else if(msg.command == 1)
    		{
    			capSync.getServant(player).updateAI(1);
    			player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.attackall"), TextFormatting.RED));
    		}
        	//defensive mode
        	else if(msg.command == 2)
        	{
        		capSync.getServant(player).updateAI(2);
        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.defensive"), TextFormatting.RED));
        	}
    		//useNP
        	else if(msg.command == 3)
        	{
        		if(!capSync.getServant(player).forcedNP)
        		{
        			if(!player.capabilities.isCreativeMode)
        			{
        				if(capSync.useMana(player, capSync.getServant(player).props().hogouMana()) && capSync.useCommandSeal(player))
        				{
        					player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.npsuccess"), TextFormatting.RED));	        							
        					capSync.getServant(player).forcedNP = true;
        					capSync.useCommandSeal(player);
        				}
        				else
        				{
        					player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.npfail"), TextFormatting.RED));
        				}
        			}
        			else
        			{
    					player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.npsuccess"), TextFormatting.RED));	        							
        				capSync.getServant(player).forcedNP = true;
        			}
        		}
				else
				{
					player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.npprep"), TextFormatting.RED));
				}
        	}
    		//follow
        	else if(msg.command == 4)
        	{
        		capSync.getServant(player).updateAI(3);
        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.follow"), TextFormatting.RED));
        	}
    		//stay
        	else if(msg.command == 5)
    		{
        		capSync.getServant(player).updateAI(4);
        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.stay"), TextFormatting.RED));
    		}
    		//protect
        	else if(msg.command == 6)
        	{
        		capSync.getServant(player).updateAI(5);
        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.patrol"), TextFormatting.RED));
        	}
    		//kill
        	else if(msg.command == 7)
        	{
        		capSync.getServant(player).attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.kill"), TextFormatting.RED));
        	}
    		//forfeit
        	else if(msg.command == 8)
        	{
        		GrailWarHandler track = GrailWarHandler.get(player.world);
        		if(track.containsPlayer(player))
        		{
	        		track.removePlayer(player);
        		}
        	}
    		//teleport
        	else if(msg.command == 9)
        	{
        		capSync.getServant(player).attemptTeleport(player.posX, player.posY, player.posZ);
        		capSync.getServant(player).setAttackTarget(null);
        		if(ConfigHandler.punishTeleport)
	        		for(EntityServant others : player.world.getEntitiesWithinAABB(EntityServant.class, player.getEntityBoundingBox().grow(32)))
	        			if(others!=capSync.getServant(player) && !ServantUtils.inSameTeam(player, others))
	        			{
	        				others.setAttackTarget(player);
	        				others.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:speed"), 600, 1));
	        			}
        	}
    		//boost
        	else if(msg.command==10)
        	{
        		if(capSync.useCommandSeal(player))
        		{
	        		capSync.getServant(player).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"), 6000, 2, true, false));
	        		capSync.getServant(player).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:regeneration"), 6000, 1, true, false));
	        		capSync.getServant(player).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"), 6000, 2, true, false));
	        		capSync.getServant(player).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:speed"), 6000, 2, true, false));
	        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.spell.success"), TextFormatting.RED));
        		}
        		else
	        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.spell.fail"), TextFormatting.RED));
        	}
			return null;
        	
        }
    }
}
