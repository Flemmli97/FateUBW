/*package com.flemmli97.fatemod.common.handler;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.EnumServantType;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.init.ModEntities;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.network.MessageWarTracker;
import com.flemmli97.fatemod.network.PacketHandler;
import com.flemmli97.fatemod.proxy.CommonProxy;
import com.flemmli97.tenshilib.common.TextHelper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class GrailWarHandler2 extends WorldSavedData {

	private Map<UUID, String> uuidNameMap = Maps.newLinkedHashMap();

	private List<ResourceLocation> servants = Lists.newArrayList();
	private List<EnumServantType> servantClass = Lists.newArrayList();

	private List<UUID> removePlayersServant = Lists.newArrayList();
	
	private int joinTicker, winningDelay;
	
	public GrailWarHandler2(String identifier) {
		super(identifier);
	}
	
	public GrailWarHandler2()
	{
		this(identifier);
	}
	
	public static GrailWarHandler2 get(World world)
	{
		MapStorage storage = world.getMapStorage();
		GrailWarHandler2 data = (GrailWarHandler2)storage.getOrLoadData(GrailWarHandler2.class, identifier);
		if (data == null)
		{
			data = new GrailWarHandler2();
			storage.setData(identifier, data);
		}
		return data;
	}
	
	public boolean canJoin(EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			if(!this.uuidNameMap.containsKey(player.getUniqueID()) && this.joinTicker!=-1)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean addPlayer(EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			UUID uuid=player.getUniqueID();
			if(!this.uuidNameMap.containsKey(uuid))
			{
				if(this.uuidNameMap.isEmpty() && this.joinTicker==0)
				{
					//start
					this.joinTicker=ConfigHandler.joinTime;
					player.world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.init", this.joinTicker/20), TextFormatting.RED));
				}
				if(this.joinTicker>0 && this.uuidNameMap.size()<ConfigHandler.maxPlayer)
				{
					this.uuidNameMap.put(uuid, player.getName());
					//Player joining grailwar should have a servant
					this.servants.add(EntityList.getKey(player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player)));
					this.servantClass.add(player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player).getServantType());
					this.markDirty();
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	public List<ResourceLocation> currentServants()
	{
		return Lists.newArrayList(this.servants);
	}
	
	public boolean containsPlayer(EntityPlayer player)
	{
		return this.uuidNameMap.containsKey(player.getUniqueID());
	}
	
	public String getPlayerNameFromUUID(UUID uuid)
	{
		return this.uuidNameMap.get(uuid);
	}		
	
	public void removePlayer(EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			this.uuidNameMap.remove(player.getUniqueID());
			IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
			cap.setCommandSeals(player, 0);
			cap.setServant(player, null);
			TruceMapHandler.get(player.world).disbandAllTruce(player);
			player.world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.playerout", player.getName()), TextFormatting.RED));
			this.checkWinCondition((WorldServer) player.world);
			this.markDirty();
		}
	}
	
	public boolean canSpawnOtherServants()
	{
		if(!ConfigHandler.allowDuplicateServant)
		{
			List<ResourceLocation> noDups = Lists.newArrayList();
			for(ResourceLocation res : this.servants)
			{
				if(!noDups.contains(res))
					noDups.add(res);
			}
			return noDups.size()<CommonProxy.servants();
		}
		if(!ConfigHandler.allowDuplicateClass)
		{
			List<EnumServantType> noDups = Lists.newArrayList();
			for(EnumServantType type : this.servantClass)
			{
				if(!noDups.contains(type))
					noDups.add(type);
			}
			return noDups.size()<EnumServantType.values().length-1;
		}
		return true;
	}
	
	public boolean canSpawnServant(EntityServant entity)
	{
		if(!ConfigHandler.allowDuplicateServant)
			return !this.servants.contains(EntityList.getKey(entity));
		if(!ConfigHandler.allowDuplicateClass)
			return !this.servantClass.contains(entity.getServantType());
		return true;
	}
	
	private void checkWinCondition(WorldServer world)
	{
		if(this.uuidNameMap.size()==1 && this.joinTicker==-1)
		{
			this.winningDelay=ConfigHandler.rewardDelay;
			for(Entry<UUID, String> entry : this.uuidNameMap.entrySet())
			{
				world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.win", entry.getValue()), TextFormatting.RED));
			}
		}
		else if(this.uuidNameMap.isEmpty())
			this.reset(world);
	}

	@Nullable
	public EntityPlayer getWinningPlayer(WorldServer world)
	{
		for(UUID uuid : this.uuidNameMap.keySet())
		{
			EntityPlayer player = (EntityPlayer) world.getMinecraftServer().getEntityFromUuid(uuid);
			return player;
		}
		return null;
	}
	
	public void startGrailWar(WorldServer world)
	{
		if(this.uuidNameMap.size()>=ConfigHandler.minPlayer)
		{
			this.joinTicker=-1;
			world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.start"), TextFormatting.RED));
			if(ConfigHandler.fillMissingSlots)
			{
				int diff = ConfigHandler.maxPlayer-this.uuidNameMap.size();
				for(int i = 0; i < diff; i++)
				{
					
				}
			}
			this.markDirty();
		}
		else
		{
			this.joinTicker=ConfigHandler.joinTime;
			world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.missingplayer"), TextFormatting.RED));
			this.markDirty();
		}
	}
	
	public boolean isWarOccurring()
	{
		return this.joinTicker==-1 && !this.uuidNameMap.isEmpty();
	}
	
	public void reset(World world)
	{
		this.uuidNameMap.clear();
		this.servants.clear();
		this.servantClass.clear();
		this.joinTicker = 0;
		this.winningDelay=0;
		world.loadedEntityList.forEach(e-> {
			if(e instanceof EntityServant)
			{
				EntityServant servant = (EntityServant) e;
				if(servant.hasOwner())
					servant.attackEntityFrom(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
			}
		});
		if(!world.isRemote)
		{
			world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.end"), TextFormatting.RED));
			PacketHandler.sendToAll(new MessageWarTracker(world));
		}
		this.markDirty();
	}
	
	public void updatePlayerName(EntityPlayer player)
	{
		if(this.uuidNameMap.containsKey(player.getUniqueID()))
		{
			this.uuidNameMap.put(player.getUniqueID(), player.getName());
			this.markDirty();
		}
	}
	
	public Set<Entry<UUID, String>> players()
	{
		return ImmutableSet.copyOf(this.uuidNameMap.entrySet());
	}
	
	public void updateGrailWar(WorldServer world)
	{
  		if(this.joinTicker>0)
  		{
	  		this.joinTicker--;
	  		if(this.joinTicker==0)
	  		{
	  			this.startGrailWar(world);
	  		}
  		}
  		if(this.joinTicker==-1 && ConfigHandler.fillMissingSlots && this.servants.size()<ConfigHandler.maxPlayer && world.getTotalWorldTime()%800==0)
  		{
  			List<Chunk> list = Lists.newArrayList(world.getChunkProvider().getLoadedChunks());
  			Chunk chunk = list.get(world.rand.nextInt(list.size()));
  			int x = chunk.x * 16 + world.rand.nextInt(16);
  	        int z = chunk.z * 16 + world.rand.nextInt(16);
  	        int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(x, 0, z)) + 1, 16);
  	        int y = world.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
  	        if(!world.isAnyPlayerWithinRangeAt(x, y, z, 32))
  	        {
  	  	        EntityServant servant = this.tryGetServant(world); 	  	    
  	  	        if(servant!=null)
  	  	        {
  	  	        	servant.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0);
	  	  	        net.minecraftforge.fml.common.eventhandler.Event.Result canSpawn = net.minecraftforge.event.ForgeEventFactory.canEntitySpawn(servant, world, x, y, z, null);
	                if (canSpawn == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW || (canSpawn == net.minecraftforge.fml.common.eventhandler.Event.Result.DEFAULT && (servant.getCanSpawnHere() && servant.isNotColliding())))
	                {
	                	servant.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(servant)), null);
	                	world.spawnEntity(servant);
	                }
  	  	        }
  	        }
  		}
  		if(this.winningDelay>0)
  		{
  			this.winningDelay--;
  			if(this.winningDelay==0)
	  		{
	  			EntityPlayer player = this.getWinningPlayer(world);
	  			if(player!=null)
	  			{
    	  			EntityItem holyGrail = new EntityItem(player.world, player.posX+world.rand.nextInt(9)-4, player.posY, player.posZ+ world.rand.nextInt(9)-4, new ItemStack(ModItems.grail));
    	  			holyGrail.lifespan = 6000;
    	  			holyGrail.setOwner(player.getName());
    	  			holyGrail.setEntityInvulnerable(true);
    	  			holyGrail.setGlowing(true);
    	  			world.spawnEntity(holyGrail);
	  			}
  				this.reset(world);
	  		}
  		}
	}
	
	private EntityServant tryGetServant(World world)
	{
		if(this.canSpawnOtherServants())
		{
			EnumServantType type = EnumServantType.values()[world.rand.nextInt(EnumServantType.values().length-1)];
			if(ConfigHandler.allowDuplicateClass || !this.servantClass.contains(type))
			{
				List<ResourceLocation> list = Lists.newArrayList(ModEntities.getAllServant(type));
				ResourceLocation res = list.get(world.rand.nextInt(list.size()));
				if(ConfigHandler.allowDuplicateServant || !this.servants.contains(res))
				{
					Entity e = EntityList.createEntityByIDFromName(res, world);
					if(e instanceof EntityServant)
						return (EntityServant) e;
				}
			}
			else 
				return tryGetServant(world);
		}
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagCompound tag = compound.getCompoundTag("Players");
		for(String s :tag.getKeySet())
		{
			this.uuidNameMap.put(UUID.fromString(s), tag.getString(s));
		}
		NBTTagList list = compound.getTagList("Servants", Constants.NBT.TAG_STRING);
		list.forEach(string -> this.servants.add(new ResourceLocation( ((NBTTagString)string).getString() )));

		NBTTagList list2 = compound.getTagList("ServantClasses", Constants.NBT.TAG_INT);
		list2.forEach(id -> this.servantClass.add(EnumServantType.values()[((NBTTagInt)id).getInt()]));
		
		this.joinTicker = compound.getInteger("Ticker");
		this.winningDelay = compound.getInteger("WinDelay");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		for(Entry<UUID, String> entry : this.uuidNameMap.entrySet())
		{
			tag.setString(entry.getKey().toString(), entry.getValue());
		}
		compound.setTag("Players", tag);
		NBTTagList list = new NBTTagList();
		for(ResourceLocation res : this.servants)
		{
			list.appendTag(new NBTTagString(res.toString()));
		}
		compound.setTag("Servants", list);
		NBTTagList list2 = new NBTTagList();
		for(EnumServantType entry : this.servantClass)
		{
			list2.appendTag(new NBTTagInt(entry.ordinal()));
		}
		compound.setTag("ServantClasses", list2);
		compound.setInteger("Ticker", this.joinTicker);
		compound.setInteger("WinDelay", this.winningDelay);
		return compound;
	}

	public void removeServantOwner(UUID fromString) {
		this.removePlayersServant.add(fromString);
		this.markDirty();
	}
	
	public boolean shouldSeverConnection(EntityPlayer player)
	{
		return this.removePlayersServant.contains(player.getUniqueID());
	}
}*/
