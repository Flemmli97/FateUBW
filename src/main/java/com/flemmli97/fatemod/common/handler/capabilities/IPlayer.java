package com.flemmli97.fatemod.common.handler.capabilities;

import java.util.Set;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.entity.player.EntityPlayer;

public interface IPlayer{
		
	public float getMana();
	
	public void setMana(EntityPlayer player, float f);
	
	public boolean useMana(EntityPlayer player, float amount);
		
	public EntityServant getServant();
	
	public void setServant(EntityServant servant);

	public String getServantName();

	public String getServantSpecial();

	public double getServantDmg();

	public double getServantArmor();
	
	public void addPlayerTruce(EntityPlayer player);
	
	public boolean isPlayerTruce(EntityPlayer player);
	
	public void removePlayerTruce(EntityPlayer player);
	
	public Set<EntityPlayer> getTruceList();
	
	public Set<EntityServant> getServantTruce();
	
	public int getCommandSeals();
	
	public boolean useCommandSeal(EntityPlayer player);
	
	public void setCommandSeals(EntityPlayer player, int amount);
	
	public void setTruceRequest(EntityPlayer player);
	
	public String getRequestPlayerString();
}
