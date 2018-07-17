package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.servant.SpawnEntityCustomList;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServantInfo  implements IMessage{

	public String servantName="NoServant";
	public ItemStack heldItem;
	public double damage;
	public double armor;
	
	public MessageServantInfo(){}
	
	public MessageServantInfo(IPlayer playerCap)
	{
		if(playerCap.getServant()!=null)
		{
			servantName = SpawnEntityCustomList.getEntityString(playerCap.getServant());
			heldItem = playerCap.getServant().getHeldItemMainhand();
			damage = playerCap.getServantDmg();
			armor = playerCap.getServantArmor();
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		servantName = compound.getString("Name");	
		if(compound.hasKey("Item"))
		{
			heldItem = ItemStack.loadItemStackFromNBT((NBTTagCompound) compound.getTag("Item"));
		}
		damage = compound.getDouble("Damage");
		armor = compound.getDouble("Armor");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		compound.setString("Name", servantName);
		if(heldItem!=null)
		{
			compound.setTag("Item", heldItem.writeToNBT(tag));
		}
		compound.setDouble("Damage", damage);
		compound.setDouble("Armor", armor);
		ByteBufUtils.writeTag(buf, compound);

	}
	
	public static class Handler implements IMessageHandler<MessageServantInfo, IMessage> {

        @Override
        public IMessage onMessage(MessageServantInfo msg, MessageContext ctx) {
    		EntityPlayer player =  Fate.proxy.getPlayerEntity(ctx);
    		
			if(player!=null)
			{
				IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null); 
				if(capSync != null)
			       {
						if(!msg.servantName.equals("NoServant"))
						{
							EntityServant servant = (EntityServant) SpawnEntityCustomList.createEntityByName(msg.servantName, Minecraft.getMinecraft().theWorld);
							servant.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, msg.heldItem); 
							servant.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(msg.armor);
							servant.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(msg.damage);
							capSync.setServant(servant);
						}
						else
							capSync.setServant(null);
			       }					
			}
            return null;
        }
    }
}
