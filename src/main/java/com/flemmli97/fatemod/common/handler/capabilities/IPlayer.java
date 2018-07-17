package com.flemmli97.fatemod.common.handler.capabilities;

import java.util.Set;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayer {

	//Mana
	public int getMana();
	
	public void setMana(EntityPlayer player, int f);
	
	public boolean useMana(EntityPlayer player, int amount);
		
	//Servant
	public EntityServant getServant();
	
	public void setServant(EntityPlayer player, EntityServant servant);
	
	public String getServantName();
	
	//Command seals
	public int getCommandSeals();
	
	public boolean useCommandSeal(EntityPlayer player);
	
	public void setCommandSeals(EntityPlayer player, int amount);
	
	//Truce
	
	public void addPlayerTruce(EntityPlayer player);
	
	public boolean isPlayerTruce(EntityPlayer player);
	
	public void removePlayerTruce(EntityPlayer player);
	
	public Set<String> getTruceList();
		
	public void setTruceRequest(EntityPlayer player);

	public String currentRequest();

	//NBT
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound);
	
	public void readFromNBT(NBTTagCompound compound);
}
