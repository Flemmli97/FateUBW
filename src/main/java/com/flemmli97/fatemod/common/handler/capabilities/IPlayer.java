package com.flemmli97.fatemod.common.handler.capabilities;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayer {

	//Mana
	public int getMana();
	
	public void setMana(EntityPlayer player, int f);
	
	public boolean useMana(EntityPlayer player, int amount);
		
	//Servant
	public EntityServant getServant(EntityPlayer player);
	
	public void setServant(EntityPlayer player, EntityServant servant);
	
	public String getServantName();
			
	//Command seals
	public int getCommandSeals();
	
	public boolean useCommandSeal(EntityPlayer player);
	
	public void setCommandSeals(EntityPlayer player, int amount);

	//NBT
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound);
	
	public void readFromNBT(NBTTagCompound compound);
}
