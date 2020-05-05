package com.flemmli97.fatemod.common.handler;

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
import com.flemmli97.fatemod.common.init.AdvancementRegister;
import com.flemmli97.fatemod.common.init.ModEntities;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.network.MessageWarTracker;
import com.flemmli97.fatemod.network.PacketHandler;
import com.flemmli97.fatemod.proxy.CommonProxy;
import com.flemmli97.tenshilib.common.TextHelper;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class GrailWarHandler extends WorldSavedData{

	private static final String identifier = "GrailWarTracker";

	private Map<UUID, String> players = Maps.newLinkedHashMap();
	private Map<UUID,ResourceLocation> activeServants = Maps.newLinkedHashMap();
	
	private Set<ResourceLocation> servants = Sets.newHashSet();
	private Set<EnumServantType> servantClasses = Sets.newHashSet();
	
	private State state = State.NOTHING;

	private int joinTicker, winningDelay, timeToNextServant, spawnedServants;

	private List<UUID> shedulePlayerRemoval = Lists.newArrayList();

	public GrailWarHandler(String identifier) {
		super(identifier);
	}
	
	public GrailWarHandler()
	{
		this(identifier);
	}
	
	public static GrailWarHandler get(World world)
	{
		MapStorage storage = world.getMapStorage();
		GrailWarHandler data = (GrailWarHandler)storage.getOrLoadData(GrailWarHandler.class, identifier);
		if (data == null)
		{
			data = new GrailWarHandler();
			storage.setData(identifier, data);
		}
		return data;
	}
	
	public boolean join(EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			UUID uuid=player.getUniqueID();
			if(!this.players.containsKey(uuid) && this.spawnedServants <ConfigHandler.maxPlayer)
			{
				EntityServant servant = player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player);
				if(this.addServant(servant))
				{
					this.players.put(uuid, player.getName());
				}
				if(this.state==State.NOTHING)
				{
					this.joinTicker=ConfigHandler.joinTime;
					player.world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.init", this.joinTicker/20), TextFormatting.RED));
					this.state=State.JOIN;
				}
				this.markDirty();
				return true;
			}
		}
		return false;
	}
	
	public boolean canJoin(EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			if(!this.players.containsKey(player.getUniqueID()) && this.state!=State.RUN)
			{
				return true;
			}
		}
		return false;
	}
	
	public void removePlayer(EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			this.players.remove(player.getUniqueID());
			IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
			cap.setCommandSeals(player, 0);
			cap.setServant(player, null);
			TruceMapHandler.get(player.world).disbandAllTruce(player);
			player.world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.playerout", player.getName()), TextFormatting.RED));
			if(!player.world.isRemote)
				this.checkWinCondition((WorldServer) player.world, true);
			
			this.markDirty();
		}
	}
	
	public void checkWinCondition(WorldServer world, boolean announce)
	{
		if(this.state==State.RUN)
		{
			if(this.activeServants.size() == 1 && this.spawnedServants>=ConfigHandler.maxPlayer && this.players.size()==1)
			{
				this.winningDelay=ConfigHandler.rewardDelay;
				this.state=State.FINISH;
				for(Entry<UUID, String> entry : this.players.entrySet())
				{
					world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.win", entry.getValue()), TextFormatting.RED));
					EntityPlayerMP player = EntityUtil.findFromUUID(EntityPlayerMP.class, world, entry.getKey());
					if(player!=null)
						AdvancementRegister.grailWarTrigger.trigger(player, false);
				}
			}
			else if(this.players.size()==0)
				this.reset(world);
		}
	}
	
	public boolean containsPlayer(EntityPlayer player)
	{
		return this.players.containsKey(player.getUniqueID());
	}
	
	public String getPlayerNameFromUUID(UUID uuid)
	{
		return this.players.get(uuid);
	}
	
	public void updatePlayerName(EntityPlayer player)
	{
		if(this.players.containsKey(player.getUniqueID()))
		{
			this.players.put(player.getUniqueID(), player.getName());
			this.markDirty();
		}
	}
	
	public void removeServant(EntityServant servant)
	{
		boolean flag=this.activeServants.remove(servant.getUniqueID())!=null;
		if(flag)
		{
			if(!servant.world.isRemote)
				this.checkWinCondition((WorldServer) servant.world, true);
			if(servant.getOwner() != null)
			{
				IPlayer servantprop = servant.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
				servantprop.setServant(servant.getOwner(), null);
				
				this.removePlayer(servant.getOwner());
			}
			else if(servant.hasOwner())
			{
				this.shedulePlayerRemoval.add(servant.ownerUUID());
			}
		}
		this.markDirty();
	}
	
	public boolean shouldSeverConnection(EntityPlayer player)
	{
		return this.shedulePlayerRemoval.contains(player.getUniqueID());
	}
	
	public boolean addServant(EntityServant servant)
	{
		if(!this.activeServants.containsKey(servant.getUniqueID()))
		{
			this.activeServants.put(servant.getUniqueID(), EntityList.getKey(servant));
			this.servants.add(EntityList.getKey(servant));
			this.servantClasses.add(servant.getServantType());
			this.markDirty();
			this.spawnedServants++;
			return true;
		}
		return false;
	}
	
	public Set<Entry<UUID, String>> players()
	{
		return ImmutableSet.copyOf(this.players.entrySet());
	}
	
	@Nullable
	public EntityPlayer getWinningPlayer(WorldServer world)
	{
		return EntityUtil.findFromUUID(EntityPlayer.class, world, this.players.keySet().iterator().next());
	}
	
	public void start(WorldServer world)
	{
		this.state=State.RUN;
		if(this.players.size()>=ConfigHandler.minPlayer)
		{
			world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.start"), TextFormatting.RED));
		}
		else if(this.players.size()==0)
			this.reset(world);
		else
		{
			this.joinTicker=ConfigHandler.joinTime;
			this.state=State.JOIN;
			world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.missingplayer"), TextFormatting.RED));
		}
		this.markDirty();
	}
	
	public void tick(WorldServer world)
	{
		if(this.state==State.JOIN)
		{
			if(--this.joinTicker==0)
				this.start(world);
		}
		else if(this.state==State.RUN)
		{
			if(ConfigHandler.fillMissingSlots && this.spawnedServants<ConfigHandler.maxPlayer && --this.timeToNextServant<=0)
	  		{
				List<EntityPlayer> players = Lists.newArrayList();
				world.playerEntities.forEach(player->{
					if(this.players.containsKey(player.getUniqueID()))
						players.add(player);
				});
				int spawns = world.rand.nextInt(ConfigHandler.maxServantCircle)+1;
				for(int i = 0; i < spawns; i++)
				{
					if(!players.isEmpty())
					{
						EntityPlayer player = players.remove(world.rand.nextInt(players.size()));
						List<ChunkPos> chunks = Lists.newArrayList();
						int chunkX = MathHelper.floor(player.posX / 16.0D);
	                    int chunkZ = MathHelper.floor(player.posZ / 16.0D);

	                    for (int x = -9; x <= 9; ++x)
	                    {
	                        for (int z = -9; z <= 9; ++z)
	                        {
	                            ChunkPos chunkpos = new ChunkPos(x + chunkX, z + chunkZ);
	                            if (!chunks.contains(chunkpos) && world.getWorldBorder().contains(chunkpos))
	                            {
	                            	chunks.add(chunkpos);
	                            }
	                        }
	                    }	             
	                    ChunkPos pos = chunks.get(world.rand.nextInt(chunks.size()));
	                    Chunk chunk = world.getChunkFromChunkCoords(pos.x, pos.z);
	    	  			int x = chunk.x * 16 + world.rand.nextInt(16);
	    	  	        int z = chunk.z * 16 + world.rand.nextInt(16);
	    	  	        int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(x, 0, z)) + 1, 16);
	    	  	        int y = world.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
	    	  	        if(!world.isAnyPlayerWithinRangeAt(x, y, z, 48))
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
	    		                	this.addServant(servant);
	    		                	this.timeToNextServant=MathHelper.getInt(world.rand, ConfigHandler.servantMinSpawnDelay, ConfigHandler.servantMaxSpawnDelay);
	    		                	if(ConfigHandler.notifyList.contains(EntityList.getKey(servant)))
	    		                		if(ConfigHandler.notifyAll)
	    		                			world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.spawn", player.getName()), TextFormatting.GOLD));
	    		                		else
	    		                			player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.spawn", player.getName()), TextFormatting.GOLD));
	    		                }
	    	  	  	        }
	    	  	        }
					}
				}
	  		}
		}
		else if(this.state==State.FINISH)
		{
			if(--this.winningDelay<=0)
	  		{
	  			EntityPlayer player = this.getWinningPlayer(world);
	  			if(player!=null)
	  			{
    	  			EntityItem holyGrail = new EntityItem(player.world, player.posX+world.rand.nextInt(9)-4, player.posY, player.posZ+ world.rand.nextInt(9)-4, new ItemStack(ModItems.grail));
    	  			holyGrail.lifespan = 6000;
    	  			holyGrail.setOwner(player.getName());
    	  			holyGrail.setEntityInvulnerable(true);
    	  			holyGrail.setGlowing(true);
    	  			holyGrail.setEntityInvulnerable(true);
    	  			world.spawnEntity(holyGrail);
					player.getCapability(PlayerCapProvider.PlayerCap, null).saveServant(player);
	  			}
  				this.reset(world);
	  		}
		}
		this.markDirty();
	}
	
	public void reset(World world)
	{
		if(!world.isRemote)
		{
			this.players.clear();
			this.joinTicker = 0;
			this.winningDelay=0;
			this.timeToNextServant=0;
			this.state=State.NOTHING;
			this.activeServants.forEach((uuid,res)->{
				EntityServant servant = EntityUtil.findFromUUID(EntityServant.class, world, uuid);
				if(servant!=null)
					servant.attackEntityFrom(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
			});
			this.activeServants.clear();
			this.servants.clear();
			this.servantClasses.clear();
			this.spawnedServants=0;
			world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.grailwar.end"), TextFormatting.RED));
			PacketHandler.sendToAll(new MessageWarTracker(world));
		}
		this.markDirty();
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
			for(EnumServantType type : this.servantClasses)
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
			return !this.servantClasses.contains(entity.getServantType());
		return true;
	}
	
	private EntityServant tryGetServant(World world)
	{
		if(this.canSpawnOtherServants())
		{
			EnumServantType type = EnumServantType.values()[world.rand.nextInt(EnumServantType.values().length-1)];
			if(ConfigHandler.allowDuplicateClass || !this.servantClasses.contains(type))
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
				return this.tryGetServant(world);
		}
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound tag = nbt.getCompoundTag("Players");
		tag.getKeySet().forEach(s->this.players.put(UUID.fromString(s), tag.getString(s)));
		NBTTagCompound tag2 = nbt.getCompoundTag("ActiveServants");
		tag2.getKeySet().forEach(s->this.activeServants.put(UUID.fromString(s), new ResourceLocation(tag2.getString(s))));
		NBTTagList list = nbt.getTagList("Servants", Constants.NBT.TAG_STRING);
		list.forEach(s->this.servants.add(new ResourceLocation(((NBTTagString)s).getString())));
		NBTTagList list2 = nbt.getTagList("ServantClasses", Constants.NBT.TAG_STRING);
		list2.forEach(s->this.servantClasses.add(EnumServantType.valueOf(((NBTTagString)s).getString())));
		NBTTagList list3 = nbt.getTagList("ToRemove", Constants.NBT.TAG_STRING);
		list3.forEach(s->this.shedulePlayerRemoval.add(UUID.fromString(((NBTTagString)s).getString())));
		this.joinTicker = nbt.getInteger("Ticker");
		this.winningDelay = nbt.getInteger("WinDelay");
		this.timeToNextServant= nbt.getInteger("SpawnTick");
		this.state=State.valueOf(nbt.getString("State"));
		this.spawnedServants=nbt.getInteger("SpawnedServants");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		this.players.forEach((uuid,name)->tag.setString(uuid.toString(), name));
		compound.setTag("Players", tag);
		NBTTagCompound tag2 = new NBTTagCompound();
		this.activeServants.forEach((uuid,res)->tag2.setString(uuid.toString(), res.toString()));
		compound.setTag("ActiveServants", tag2);
		NBTTagList list = new NBTTagList();
		this.servants.forEach(res->list.appendTag(new NBTTagString(res.toString())));
		compound.setTag("Servants", list);
		NBTTagList list2 = new NBTTagList();
		this.servantClasses.forEach(e->list2.appendTag(new NBTTagString(e.toString())));
		compound.setTag("ServantClasses", list2);
		NBTTagList list3 = new NBTTagList();
		this.shedulePlayerRemoval.forEach(uuid->list3.appendTag(new NBTTagString(uuid.toString())));
		compound.setTag("ToRemove", list3);
		compound.setInteger("Ticker", this.joinTicker);
		compound.setInteger("WinDelay", this.winningDelay);
		compound.setInteger("SpawnTick", this.timeToNextServant);
		compound.setString("State", this.state.toString());
		compound.setInteger("SpawnedServants", this.spawnedServants);
		return compound;
	}
	
	private enum State
	{
		JOIN,
		RUN,
		FINISH,
		NOTHING;
	}
}
