package com.flemmli97.fatemod.common.handler;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.tenshilib.common.TextHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class TruceMapHandler extends WorldSavedData{

	private static final String identifier = "TruceData";

	private SetMultimap<UUID, UUID> truceMap = HashMultimap.create();
	
	private SetMultimap<UUID, UUID> pendingRequests = HashMultimap.create();
	
	public TruceMapHandler(String identifier) {
		super(identifier);
	}
	
	public TruceMapHandler()
	{
		this(identifier);
	}
	
	public static TruceMapHandler get(World world)
	{
		MapStorage storage = world.getMapStorage();
		TruceMapHandler data = (TruceMapHandler)storage.getOrLoadData(TruceMapHandler.class, identifier);
		if (data == null)
		{
			data = new TruceMapHandler();
			storage.setData(identifier, data);
		}
		return data;
	}
	
	public boolean sendRequest(UUID to, EntityPlayer from)
	{
		if(this.pendingRequests.put(to, from.getUniqueID()))
		{
			this.markDirty();
			from.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.truce.send", GrailWarHandler.get(from.world).getPlayerNameFromUUID(to)), TextFormatting.GOLD));
			EntityPlayer player = from.world.getPlayerEntityByUUID(to);
			if(player!=null)
				player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.truce.request", from.getName()), TextFormatting.GOLD));
			return true;
		}
		return false;
	}
	
	public boolean hasRequest(UUID to, EntityPlayer from)
	{
		return this.pendingRequests.get(to).contains(from.getUniqueID());
	}
	
	public Set<UUID> getRequests(EntityPlayer player)
	{
		return this.pendingRequests.get(player.getUniqueID());
	}
	
	public void acceptRequest(EntityPlayer player, UUID request)
	{
		if(this.pendingRequests.containsValue(request))
		{
			this.pendingRequests.remove(player.getUniqueID(), request);
			this.truceMap.put(player.getUniqueID(), request);
			this.truceMap.put(request, player.getUniqueID());
			player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.truce.accept", GrailWarHandler.get(player.world).getPlayerNameFromUUID(request)), TextFormatting.GOLD));
			if(player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player)!=null)
				player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(player).setAttackTarget(null);
			EntityPlayer other= player.world.getPlayerEntityByUUID(request);
			if(other!=null)
			{
				other.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.truce.requestsuccess", player.getName()), TextFormatting.GOLD));
				if(other.getCapability(PlayerCapProvider.PlayerCap, null).getServant(other)!=null)
					other.getCapability(PlayerCapProvider.PlayerCap, null).getServant(other).setAttackTarget(null);
			}
			this.markDirty();
		}
	}
	
	public Set<UUID> playerTruces(EntityPlayer player)
	{
		return this.truceMap.get(player.getUniqueID());
	}
	
	public void disbandTruce(EntityPlayer player, UUID other)
	{
		if(this.truceMap.get(player.getUniqueID()).contains(other))
		{
			this.truceMap.remove(player.getUniqueID(), other);
			this.truceMap.remove(other, player.getUniqueID());
			this.markDirty();
		}
	}
	
	public void disbandAllTruce(EntityPlayer player)
	{
		for(UUID a: this.truceMap.get(player.getUniqueID()))
		{
			this.truceMap.remove(a, player.getUniqueID());
		}
		this.truceMap.removeAll(player.getUniqueID());
		this.markDirty();
	}
	
	public void reset()
	{
		this.pendingRequests.clear();
		this.truceMap.clear();
		this.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound requests = nbt.getCompoundTag("Requests");
		for(String s : requests.getKeySet())
		{
			NBTTagList list = requests.getTagList(s, Constants.NBT.TAG_STRING);
			list.forEach(value -> {
				this.pendingRequests.put(UUID.fromString(s), UUID.fromString(((NBTTagString)value).getString()));
			});
		}
		NBTTagCompound truce = nbt.getCompoundTag("Truce");
		for(String s : truce.getKeySet())
		{
			NBTTagList list = truce.getTagList(s, Constants.NBT.TAG_STRING);
			list.forEach(value -> {
				this.truceMap.put(UUID.fromString(s), UUID.fromString(((NBTTagString)value).getString()));
			});
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound requests = new NBTTagCompound();
		for(Entry<UUID, Collection<UUID>> entry : this.pendingRequests.asMap().entrySet())
		{
			NBTTagList list = new NBTTagList();
			for(UUID uuid : entry.getValue())
				list.appendTag(new NBTTagString(uuid.toString()));
			requests.setTag(entry.getKey().toString(), list);
		}
		compound.setTag("Requests", requests);
		NBTTagCompound truce = new NBTTagCompound();
		for(Entry<UUID, Collection<UUID>> entry : this.truceMap.asMap().entrySet())
		{
			NBTTagList list = new NBTTagList();
			for(UUID uuid : entry.getValue())
				list.appendTag(new NBTTagString(uuid.toString()));
			truce.setTag(entry.getKey().toString(), list);
		}
		compound.setTag("Truce", truce);
		return compound;
	}
}
