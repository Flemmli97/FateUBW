package com.flemmli97.fatemod.common.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.commons.io.IOUtils;

import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.fatemod.proxy.CommonProxy;
import com.flemmli97.tenshilib.common.config.JsonConfig;
import com.flemmli97.tenshilib.common.item.ItemUtil;
import com.flemmli97.tenshilib.common.javahelper.ResourceStream;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

public class GrailReward {

	private static Map<String,List<Loot>> loots = Maps.newLinkedHashMap();
	private static final Gson GSON  = new GsonBuilder().create();
	private static JsonConfig<JsonObject> lootConf;
	
	public static void load()
	{
		loots.clear();
		File conf = new File(Loader.instance().getConfigDir(), "fate/grail.json");
		if(!conf.exists())
		{
			try 
			{
				conf.createNewFile();
				OutputStream out = new FileOutputStream(conf);
				InputStream in = ResourceStream.getStream(LibReference.MODID, "configs", "grail.json");
				IOUtils.copy(in, out);
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		if(lootConf==null)
			lootConf = new JsonConfig<JsonObject>(conf, JsonObject.class, null).setGson(GSON);
		JsonObject obj = lootConf.getElement();
		obj.entrySet().forEach((entry)->{
			if(entry.getValue() instanceof JsonArray)
			{
				List<Loot> list = Lists.newArrayList();
				((JsonArray)entry.getValue()).forEach(element->{
					if(element instanceof JsonObject)
					{
						JsonObject lootObj = (JsonObject) element;
						LootType type = LootType.valueOf(lootObj.get("type").getAsString().toUpperCase());
						switch(type)
						{
							case ITEM:
								NBTTagCompound tag = ItemUtil.stackCompoundFromJson(lootObj);
								if(tag==null)
									tag = new NBTTagCompound();
								tag.setString("id", lootObj.get("item").getAsString());
								tag.setInteger("Damage", lootObj.has("meta")?lootObj.get("meta").getAsInt():0);
								ItemStack stack = new ItemStack(tag);
								list.add(new Loot((loot,player)->
								{
									stack.setCount(loot.getRange().generateInt(player.getRNG()));
									player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, stack.copy()));
								})
									.setRange(new RandomValueRange(lootObj.get("min").getAsInt(), lootObj.get("max").getAsInt()))
									.setChance(lootObj.has("chance")?lootObj.get("chance").getAsFloat():1));
								break;
							case SERVANT:
								list.add(new Loot((loot,player)->
								{
									player.getCapability(PlayerCapProvider.PlayerCap, null).restoreServant(player);
								}));
								break;
							case XP:
								list.add(new Loot((loot,player)->player.addExperience(loot.getRange().generateInt(player.getRNG())))
									.setRange(new RandomValueRange(lootObj.get("min").getAsInt(), lootObj.get("max").getAsInt()))
									.setChance(lootObj.has("chance")?lootObj.get("chance").getAsFloat():1));
								break;
							case ASTRALSORCERY:
								if(CommonProxy.astralSorcery)
									list.add(new Loot((loot,player)-> 
									{
										String token = "fate:astralToken";
										int i = 1;
										PlayerProgress prog = ResearchManager.getProgress(player, Side.SERVER);
										if(prog.isValid())
										{
											while(!ResearchManager.grantFreePerkPoint(player, token+i))
												i++;
										}
									})
										.setChance(lootObj.has("chance")?lootObj.get("chance").getAsFloat():1));
								break;
							case THAUMCRAFT:
								break;
							case BOTANIA:
								break;
							case BLOODMAGIC:
								break;
						}
					}
				});
				if(!list.isEmpty())
					loots.put(entry.getKey(), list);
			}
		});
	}

	public static void giveRewards(String type, EntityPlayer player)
	{
		if(loots.containsKey(type))
			loots.get(type).forEach(loot->{if(player.getRNG().nextFloat()<loot.getChance()) loot.give(player);});
	}
	
	public static Set<String> lootSets()
	{
		return ImmutableSet.copyOf(loots.keySet());
	}
	
	public static class Loot
	{
		private RandomValueRange val;
		private BiConsumer<Loot,EntityPlayer> func;
		private float chance = 1;
		public Loot(BiConsumer<Loot,EntityPlayer> func)
		{
			this.func=func;
		}
		
		public Loot setRange(RandomValueRange range)
		{
			this.val=range;
			return this;
		}
		
		public Loot setChance(float chance)
		{
			this.chance = chance;
			return this;
		}
		
		public RandomValueRange getRange()
		{
			return this.val;
		}
		
		public float getChance()
		{
			return this.chance;
		}
		
		public void give(EntityPlayer player) {
			this.func.accept(this, player);
		}		
	}
	
	public static enum LootType
	{
		XP,
		ITEM,
		//PLAYERSTAT
		SERVANT,
		ASTRALSORCERY,
		BOTANIA,
		THAUMCRAFT,
		BLOODMAGIC;
	}
}
