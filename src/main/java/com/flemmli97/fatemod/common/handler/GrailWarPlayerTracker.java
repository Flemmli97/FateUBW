package com.flemmli97.fatemod.common.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GrailWarPlayerTracker extends WorldSavedData {

	private static String identifier = "GrailWarTracker";
	private List<String> playerList = new ArrayList<String>();
	public int joinTicker, winningDelay;
	
	public GrailWarPlayerTracker(String identifier) {
		super(identifier);
	}
	
	public GrailWarPlayerTracker()
	{
		this(identifier);
	}
	
	public static GrailWarPlayerTracker get(World world)
	{
		MapStorage storage = world.getMapStorage();
		GrailWarPlayerTracker data = (GrailWarPlayerTracker)storage.getOrLoadData(GrailWarPlayerTracker.class, identifier);
		if (data == null)
		{
			data = new GrailWarPlayerTracker();
			storage.setData(identifier, data);
		}
		return data;
	}
	
	public boolean addPlayer(EntityPlayer player)
	{
		if(!player.worldObj.isRemote)
		{
			String uuid=player.getUniqueID().toString();
			if(!playerList.contains(uuid))
			{
				if(playerList.isEmpty())
				{
					//start
					this.joinTicker=12000;
					MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
					minecraftserver.getPlayerList().sendChatMsg(new TextComponentString(TextFormatting.RED + "A grailwar has been initialized. You have half a day to join it."));
				}
				if(this.joinTicker>0)
				{
					playerList.add(uuid);
				}
				this.markDirty();
				return true;
			}
		}
		return false;
	}
	
	public boolean containsPlayer(EntityPlayer player)
	{
		return this.playerList.contains(player.getUniqueID().toString());
	}
	
	public void removePlayer(EntityPlayer player)
	{
		playerList.remove(player.getUniqueID().toString());
		MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
		minecraftserver.getPlayerList().sendChatMsg(new TextComponentString(TextFormatting.RED + player.getName() +" is out"));
		this.checkWinCondition();
		this.markDirty();
	}
	
	private void checkWinCondition()
	{
		if(this.playerList.size()==1 && this.joinTicker==-1)
		{
			this.winningDelay=1000;
			EntityPlayer player = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(playerList.get(0)));
			MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
			minecraftserver.getPlayerList().sendChatMsg(new TextComponentString(TextFormatting.RED + player.getName() +" won the grail war"));
		}
	}
	
	public EntityPlayer getWinningPlayer()
	{
		return (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(playerList.get(0)));
	}
	
	public void startGrailWar(World world)
	{
		if(this.playerList.size()>1)
		{
			this.joinTicker=-1;
			MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
			minecraftserver.getPlayerList().sendChatMsg(new TextComponentString(TextFormatting.RED + "Joining is now locked"));
			this.markDirty();
		}
		else
		{
			this.joinTicker=12000;
			MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
			minecraftserver.getPlayerList().sendChatMsg(new TextComponentString(TextFormatting.RED + "Not enough player, restarting timer"));
			this.markDirty();
		}
	}
	
	public boolean isWarOccurring()
	{
		return this.joinTicker==-1 && !this.playerList.isEmpty();
	}
	
	public void reset()
	{
		playerList.clear();
		this.joinTicker = 0;
		this.markDirty();
	}
	
	public int playerCount()
	{
		return playerList.size();
	}
	
	public EntityPlayer getPlayer(int i)
	{
		UUID uuid = UUID.fromString(playerList.get(i));
		//TODO doesnt work on server
		EntityPlayer player = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(uuid);
		if(player!=null)
			return player;
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagCompound tag = (NBTTagCompound) compound.getTag("PlayerList");
		int count = compound.getInteger("PlayerCount");
		if(count!=0)
		{
			for(int i = 1; i <= compound.getInteger("PlayerCount"); i ++)
			{
				playerList.add(tag.getString("Player " + i));
			}
		}
		this.joinTicker = compound.getInteger("Ticker");
		this.winningDelay = compound.getInteger("WinDelay");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(!playerList.isEmpty())
		{
			compound.setInteger("PlayerCount", playerList.size());
			NBTTagCompound tag = new NBTTagCompound();
			for(int i = 1; i <= playerList.size(); i ++)
			{
				tag.setString("Player " + i, playerList.get(i-1));
			}
			compound.setTag("PlayerList", tag);
		}	
		compound.setInteger("Ticker", joinTicker);
		compound.setInteger("WinDelay", winningDelay);
		return compound;
	}
	
}
