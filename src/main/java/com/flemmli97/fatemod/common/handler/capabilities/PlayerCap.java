package com.flemmli97.fatemod.common.handler.capabilities;

import java.util.Set;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.network.MessageCommandSeals;
import com.flemmli97.fatemod.network.MessageMana;
import com.flemmli97.fatemod.network.MessageServantSync;
import com.flemmli97.fatemod.network.PacketHandler;
import com.google.common.collect.Sets;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

public class PlayerCap implements IPlayer{
	
	private int currentMana = 0;
	private EntityServant servant;
	private int commandSeals = 0;
	private Set<String> truceSet = Sets.<String>newLinkedHashSet();
	private String requestPlayer = "";
	
	public PlayerCap() {}
	
	@Override
    public void setMana(EntityPlayer player, int mana) {
        this.currentMana = mana;
        if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageMana(this), (EntityPlayerMP) player);
		}
    }

	@Override
    public int getMana() {
        return this.currentMana;
    }
    
	@Override
    public boolean useMana(EntityPlayer player, int amount) {
		boolean flag = currentMana<amount;
		if(!flag)
			currentMana -= amount;
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageMana(this), (EntityPlayerMP) player);
		}
		return flag;
	}
	
	@Override
	public EntityServant getServant() {
		return this.servant;
	}

	@Override
	public void setServant(EntityPlayer player, EntityServant servant) {
		this.servant = servant;	
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageServantSync(servant), (EntityPlayerMP) player);
		}
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
	public int getCommandSeals() {
		return this.commandSeals;
	}

	@Override
	public boolean useCommandSeal(EntityPlayer player) {
		boolean flag = this.commandSeals>0;
		if(flag)
			this.commandSeals--;
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageCommandSeals(this), (EntityPlayerMP) player);
		}
		return flag;
	}

	@Override
	public void setCommandSeals(EntityPlayer player, int amount) {
		this.commandSeals=Math.min(amount, 3);
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageCommandSeals(this), (EntityPlayerMP) player);
		}
	}
	
	@Override
	public boolean isPlayerTruce(EntityPlayer player) {
		if(truceSet.contains(player.getCachedUniqueIdString()))
			return true;
		return false;
	}
	
	@Override
	public void addPlayerTruce(EntityPlayer player) {
		truceSet.add(player.getCachedUniqueIdString());		
	}

	@Override
	public void removePlayerTruce(EntityPlayer player) {
		truceSet.remove(player.getCachedUniqueIdString());
	}

	@Override
	public Set<String> getTruceList()
	{
		return this.truceSet;
	}
	
	@Override
	public void setTruceRequest(EntityPlayer player) {
		this.requestPlayer = player.getCachedUniqueIdString();
	}
	
	@Override
	public String currentRequest()
	{
		return this.requestPlayer;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("Mana", this.currentMana);
		compound.setInteger("CommandSeal", this.commandSeals);
		if(this.servant!=null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			this.servant.writeToNBT(nbt);
			compound.setTag("Servant", nbt);
		}
		NBTTagList truce = new NBTTagList();
		for(String s : this.truceSet)
			truce.appendTag(new NBTTagString(s));
		compound.setTag("Truce", truce);
		compound.setString("CurrentReq", this.requestPlayer);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.currentMana=compound.getInteger("Mana");
		this.commandSeals=compound.getInteger("CommandSeal");
		for(NBTBase s : compound.getTagList("Truce", Constants.NBT.TAG_STRING))
			this.truceSet.add(((NBTTagString)s).getString());
		this.requestPlayer=compound.getString("CurrentReq");
	}
}
