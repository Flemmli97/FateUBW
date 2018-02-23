package com.flemmli97.fatemod.common.handler.capabilities;

import java.util.Set;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.network.MessageCommandSeals;
import com.flemmli97.fatemod.network.MessageMana;
import com.flemmli97.fatemod.network.PacketHandler;
import com.google.common.collect.Sets;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerCap implements IPlayer  {

	private float currentMana = 0;
	private EntityServant servant;
	private Set<EntityPlayer> truceSet = Sets.<EntityPlayer>newLinkedHashSet();
	private Set<EntityServant> truceServant = Sets.<EntityServant>newLinkedHashSet();
	private int commandSeals = 0;
	private EntityPlayer requestPlayer;
	
	public void update(EntityPlayer player)
	{
		if(player!=null &&!player.worldObj.isRemote && player instanceof EntityPlayerMP)
		{
			//IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null); 
			PacketHandler.sendTo(new MessageMana(), (EntityPlayerMP) player);
		}
	}
	
	@Override
    public void setMana(EntityPlayer player, float mana) {
        this.currentMana = mana;
        if(player!=null &&!player.worldObj.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageMana(this), (EntityPlayerMP) player);
		}
    }

	@Override
    public float getMana() {
        return this.currentMana;
    }
    
	@Override
    public boolean useMana(EntityPlayer player, float amount) {
		if(currentMana < amount) {
			if(!player.worldObj.isRemote && player instanceof EntityPlayerMP)
			{
				PacketHandler.sendTo(new MessageMana(this), (EntityPlayerMP) player);
			}
			return false;
		}
		else
		{
			currentMana -= amount;
			if(!player.worldObj.isRemote && player instanceof EntityPlayerMP)
			{
				PacketHandler.sendTo(new MessageMana(this), (EntityPlayerMP) player);
			}
			return true;
		}
		
	}

	@Override
	public EntityServant getServant() {
		return this.servant;
	}

	@Override
	public void setServant(EntityServant servant) {
		this.servant = servant;		
	}

	@Override
	public String getServantName() {
		if(servant!=null)
		{
			return I18n.format(servant.getName());
		}
		return "No servant";
	}

	@Override
	public String getServantSpecial() {
		return servant.getSpecialName();
	}

	@Override
	public double getServantDmg() {
		double dmg = servant.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		return dmg;
	}

	@Override
	public double getServantArmor() {
		double armor = servant.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue();
		return armor;
	}

	@Override
	public void addPlayerTruce(EntityPlayer player) {
		truceSet.add(player);
		IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
		truceServant.add(capSync.getServant());
	}

	@Override
	public boolean isPlayerTruce(EntityPlayer player) {
		if(truceSet.contains(player))
			return true;
		return false;
	}

	@Override
	public void removePlayerTruce(EntityPlayer player) {
		truceSet.remove(player);
		IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
		truceServant.remove(capSync.getServant());
	}

	@Override
	public Set<EntityPlayer> getTruceList()
	{
		return this.truceSet;
	}
	
	public Set<EntityServant> getServantTruce()
	{
		return this.truceServant;
	}

	@Override
	public int getCommandSeals() {
		return this.commandSeals;
	}

	@Override
	public boolean useCommandSeal(EntityPlayer player) {
		boolean flag = false;
		if(this.commandSeals>0)
		{
			this.commandSeals--;
			flag = true;
		}
		if(!player.worldObj.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageCommandSeals(this), (EntityPlayerMP) player);
		}
		return flag;
	}

	@Override
	public void setCommandSeals(EntityPlayer player, int amount) {
		if(amount<=3)
		this.commandSeals=amount;
		if(player!=null && !player.worldObj.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageCommandSeals(this), (EntityPlayerMP) player);
		}
	}

	@Override
	public void setTruceRequest(EntityPlayer player) {
		this.requestPlayer = player;
	}

	@Override
	public String getRequestPlayerString() {
		return this.requestPlayer.getUniqueID().toString();
	}
}
