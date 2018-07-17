package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.GrailWarPlayerTracker;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
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
        	if(capSync.getServant() != null || msg.command == 8)
        	{
        		//normal mode
        		if(msg.command == 0)
        		{
        			capSync.getServant().updateAI(0);
        			player.sendMessage(new TextComponentString(TextFormatting.RED + "Your servant now only attacks other servants"));
        		}
        		//aggressive mode
        		else if(msg.command == 1)
        		{
        			capSync.getServant().updateAI(1);
        			player.sendMessage(new TextComponentString(TextFormatting.RED + "Your servant now attacks every mob"));
        		}
	        	//defensive mode
	        	else if(msg.command == 2)
	        	{
	        		capSync.getServant().updateAI(2);
	        		player.sendMessage(new TextComponentString(TextFormatting.RED + "Your servant now only fights back when attacked"));
	        	}
        		//useNP
	        	else if(msg.command == 3)
	        	{
	        		if(!capSync.getServant().forcedNP)
	        		{
	        			if(!player.capabilities.isCreativeMode)
	        			{
	        				if(capSync.useMana(player, capSync.getServant().props().hogouMana()) && capSync.useCommandSeal(player))
	        				{
	        					player.sendMessage(new TextComponentString(TextFormatting.RED + "You commanded your servant to use a Nobel Phantasm"));	        							
	        					capSync.getServant().forcedNP = true;
	        					capSync.useCommandSeal(player);
	        				}
	        				else
	        				{
	        					player.sendMessage(new TextComponentString(TextFormatting.RED + "Seems like you don't have enough mana or command spells"));
	        				}
	        			}
	        			else
	        			{
        					player.sendMessage(new TextComponentString(TextFormatting.RED + "You commanded your servant to use a Nobel Phantasm"));	        							
	        				capSync.getServant().forcedNP = true;
	        			}
	        		}
					else
					{
						player.sendMessage(new TextComponentString(TextFormatting.RED + "Your servant is already preparing for an attack"));
					}
	        	}
        		//follow
	        	else if(msg.command == 4)
	        	{
	        		capSync.getServant().updateAI(3);
	        		player.sendMessage(new TextComponentString(TextFormatting.RED + "Your servant now follows you"));
	        	}
        		//stay
	        	else if(msg.command == 5)
        		{
	        		capSync.getServant().updateAI(4);
	        		player.sendMessage(new TextComponentString(TextFormatting.RED + "You told your servant to hold their position"));
        		}
        		//protect
	        	else if(msg.command == 6)
	        	{
	        		capSync.getServant().updateAI(5);
	        		player.sendMessage(new TextComponentString(TextFormatting.RED + "Your servant now protects this area"));
	        	}
        		//kill
	        	else if(msg.command == 7)
	        	{
	        		capSync.getServant().attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
	        		player.sendMessage(new TextComponentString(TextFormatting.RED + "You killed your servant"));
	        	}
        		//forfeit
	        	else if(msg.command == 8)
	        	{
	        		GrailWarPlayerTracker track = GrailWarPlayerTracker.get(player.world);
	        		if(track.containsPlayer(player))
	        		{
		        		track.removePlayer(player);
	        		}
	        	}
        		//teleport
	        	else if(msg.command == 9)
	        	{
	        		capSync.getServant().attemptTeleport(player.posX, player.posY, player.posZ);
	        		capSync.getServant().setAttackTarget(null);
	        	}
        		//boost
	        	else if(msg.command==10)
	        	{
	        		if(capSync.useCommandSeal(player))
	        		{
		        		capSync.getServant().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"), 6000, 2, true, false));
		        		capSync.getServant().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:regeneration"), 6000, 1, true, false));
		        		capSync.getServant().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"), 6000, 2, true, false));
		        		capSync.getServant().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:speed"), 6000, 2, true, false));
		        		player.sendMessage(new TextComponentString(TextFormatting.RED + "You buffed your servant using a command spell"));
	        		}
	        		else
		        		player.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have any command spells anymore"));
	        	}
        	}
			return null;
        	
        }
    }
}
