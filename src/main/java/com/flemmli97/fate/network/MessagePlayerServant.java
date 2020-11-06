/*package com.flemmli97.fate.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.handler.GrailWarHandler;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.utils.ServantUtils;
import com.flemmli97.tenshilib.common.TextHelper;
import com.flemmli97.tenshilib.common.world.RayTraceUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
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

        this.command = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.command);
		
	}
	
	public static class Handler implements IMessageHandler<MessagePlayerServant, IMessage> {

        @Override
        public IMessage onMessage(MessagePlayerServant msg, MessageContext ctx) {
        	EntityPlayer player = Fate.proxy.getPlayerEntity(ctx);
        	IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
        	EntityServant servant = capSync.getServant(player);
        	if(servant==null)
        	{
        		return null;
        	}
    		//normal mode
    		if(msg.command == 0)
    		{
    			servant.updateAI(0);
    			player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.attackservant"), TextFormatting.RED));
    		}
    		//aggressive mode
    		else if(msg.command == 1)
    		{
    			servant.updateAI(1);
    			player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.attackall"), TextFormatting.RED));
    		}
        	//defensive mode
        	else if(msg.command == 2)
        	{
        		servant.updateAI(2);
        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.defensive"), TextFormatting.RED));
        	}
    		//useNP
        	else if(msg.command == 3)
        	{
        		if(!servant.forcedNP)
        		{
        			if(!player.capabilities.isCreativeMode)
        			{
        				if(capSync.useMana(player, servant.props().hogouMana()) && capSync.useCommandSeal(player))
        				{
        					player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.npsuccess"), TextFormatting.RED));	        							
        					servant.forcedNP = true;
        				}
        				else
        				{
        					player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.npfail"), TextFormatting.RED));
        				}
        			}
        			else
        			{
    					player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.npsuccess"), TextFormatting.RED));	        							
        				servant.forcedNP = true;
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
        		servant.updateAI(3);
        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.follow"), TextFormatting.RED));
        	}
    		//stay
        	else if(msg.command == 5)
    		{
        		servant.updateAI(4);
        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.stay"), TextFormatting.RED));
    		}
    		//protect
        	else if(msg.command == 6)
        	{
        		servant.updateAI(5);
        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.patrol"), TextFormatting.RED));
        	}
    		//kill
        	else if(msg.command == 7)
        	{
        		servant.onKillOrder(player, capSync.useCommandSeal(player));
        	}
    		//forfeit
        	else if(msg.command == 8)
        	{
        		GrailWarHandler track = GrailWarHandler.get(player.world);
        		if(track.containsPlayer(player))
        		{
	        		track.removePlayer(player);
	        		servant.onForfeit(player);
        		}
        	}
    		//teleport
        	else if(msg.command == 9)
        	{
        		servant.attemptTeleport(player.posX, player.posY, player.posZ);
        		servant.setAttackTarget(null);
        		if(ConfigHandler.punishTeleport)
	        		for(EntityServant others : player.world.getEntitiesWithinAABB(EntityServant.class, player.getEntityBoundingBox().grow(32)))
	        			if(others!=servant && !ServantUtils.inSameTeam(player, others))
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
	        		servant.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"), 6000, 2, true, false));
	        		servant.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:regeneration"), 6000, 1, true, false));
	        		servant.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"), 6000, 2, true, false));
	        		servant.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:speed"), 6000, 2, true, false));
	        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.spell.success"), TextFormatting.RED));
        		}
        		else
	        		player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.command.spell.fail"), TextFormatting.RED));
        	}
    		//target
        	else if(msg.command==11)
        	{
        		RayTraceResult res = RayTraceUtils.calculateEntityFromLook(player, 16, EntityLivingBase.class);
        		if(res!=null)
        		{
        			EntityLivingBase e = (EntityLivingBase) res.entityHit;
        			if(e instanceof EntityServant)
        			{
        				if(!ServantUtils.inSameTeam(player, (EntityServant) e));
        					servant.setAttackTarget(e);
        			}
        			else if(e instanceof EntityPlayer)
        			{
        				if(!ServantUtils.inSameTeam(player, (EntityPlayer) e));
        					servant.setAttackTarget(e);
        			}
        			else
        				servant.setAttackTarget(e);
        		}
        	}
			return null;
        	
        }
    }
}
*/