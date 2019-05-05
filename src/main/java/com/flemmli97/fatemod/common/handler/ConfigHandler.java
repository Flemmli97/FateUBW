package com.flemmli97.fatemod.common.handler;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityGilgamesh;
import com.flemmli97.fatemod.common.entity.servant.EntityGilles;
import com.flemmli97.fatemod.common.entity.servant.EntityHassan;
import com.flemmli97.fatemod.common.entity.servant.EntityHeracles;
import com.flemmli97.fatemod.common.entity.servant.EntityIskander;
import com.flemmli97.fatemod.common.entity.servant.EntityLancelot;
import com.flemmli97.fatemod.common.entity.servant.EntityMedea;
import com.flemmli97.fatemod.common.entity.servant.EntityMedusa;
import com.flemmli97.fatemod.common.entity.servant.EntitySasaki;
import com.flemmli97.fatemod.common.utils.ServantProperties;
import com.flemmli97.tenshilib.api.config.IConfigArrayValue;
import com.flemmli97.tenshilib.common.config.ConfigUtils;
import com.flemmli97.tenshilib.common.config.ConfigUtils.LoadState;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ConfigHandler {
	
	public static Configuration config;

	//General
	public static int minPlayer=0;
	public static int maxPlayer=7;
	public static int joinTime=12000;
	public static int rewardDelay=2000;
	public static int charmSpawnRate=1;
	public static int gemSpawnRate=10;
	public static boolean allowDuplicateServant;
	public static boolean allowDuplicateClass;
	public static boolean fillMissingSlots=true;
	//Servants
	public static final Map<Class<? extends Entity>, ServantProperties> attributes = Maps.newHashMap();

	//Minions
	public static int gillesMinionDuration=6000;
	public static float smallMonsterDamage=8;
	public static ServantProperties hassanCopy = new ServantProperties(50, 4, 3.5, 0, 12, 2, 0.34, 0, 10, 5);
	public static float babylonScale = 1;
	public static float eaDamage=13;
	public static float excaliburDamage=19;
	public static float caladBolgDmg = 25;
	public static float magicBeam = 5;
	public static float gaeBolgDmg = 10;
	public static PotionEffectsConfig gaeBolgEffect = new PotionEffectsConfig()
			.addEffect(Potion.getPotionFromResourceLocation("minecraft:wither"), 200, 2)
			.addEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 100, 7)
			.addEffect(Potion.getPotionFromResourceLocation("minecraft:jump_boost"), 100, 128);
	public static float gordiusHealth = 53;
	public static float gordiusDmg = 10;
	public static float pegasusHealth = 53;
	
	public static void load(LoadState state)
	{
		if(config==null)
		{
			config = new Configuration(new File(Loader.instance().getConfigDir(), "fate/main.cfg"));
			config.load();
		}
		
		ConfigCategory general = config.getCategory("general");
		general.setLanguageKey("config.fatemod.general");
		maxPlayer = ConfigUtils.getIntConfig(config, "Max Player", "general", maxPlayer, 0 ,"Maximum of player allowed in a grail war");
		minPlayer = ConfigUtils.getIntConfig(config, "Min Player", "general", minPlayer, maxPlayer, "Minimum of player count required to start a grail war");
		joinTime = ConfigUtils.getIntConfig(config, "Join Time", "general", joinTime, 0 ,"Time buffer to join a grail war after start");
		rewardDelay = ConfigUtils.getIntConfig(config, "Reward Delay", "general", rewardDelay, 0 ,"Delay after an ended grail war for getting the grail");
		charmSpawnRate = ConfigUtils.getIntConfig(config, "Medallion Rate", "general", charmSpawnRate, 0 ,"Customize the spawn rate of the servant medallion ores, higher value means higher spawn rate");
		gemSpawnRate = ConfigUtils.getIntConfig(config, "Magic Gem Rate", "general", gemSpawnRate, 0 ,"Customize the spawn rate of the magic gem ores, higher value means higher spawn rate");
		allowDuplicateServant = config.getBoolean("Allow Duplicate Servants", "general", allowDuplicateServant, "Allow the summoning of duplicate servants during a grail war");
		allowDuplicateClass = config.getBoolean("Allow Duplicate Classes", "general", allowDuplicateClass, "Allow the summoning of duplicate servant classes during a grail war");
		fillMissingSlots = config.getBoolean("Fill Empty Slots", "general", fillMissingSlots, "Fill in missing players till max allowed with npc");

		if(state==LoadState.POSTINIT||state==LoadState.SYNC)
			servants();
		
		ConfigCategory minions = config.getCategory("minions");
		minions.setLanguageKey("config.fatemod.minions");
		gillesMinionDuration = config.get("minions", "Gilles Monster", gillesMinionDuration).getInt();
		if(state==LoadState.POSTINIT||state==LoadState.SYNC)
		{
			ConfigCategory hassan = config.getCategory("minions.hassancopy");
			hassan.setLanguageKey("config.fatemod.hassancopy");
			hassan.setComment("Hassans Clones");
			hassanCopy.config(config, "minions.hassancopy");
			gaeBolgEffect.readFromString(config.getStringList("Gae Bolg Effects", "minion", gaeBolgEffect.writeToString(), gaeBolgEffect.usage()));
			gaeBolgDmg = ConfigUtils.getFloatConfig(config, "Gae Bolg Damage", "minions", gaeBolgDmg, "");
		}
		smallMonsterDamage  = ConfigUtils.getFloatConfig(config, "Small Monster Damage", "minions", smallMonsterDamage, "Monster summoned by Gilles");
		babylonScale = ConfigUtils.getFloatConfig(config, "Babylon Damage Scale", "minions", babylonScale, "Damage scaling for projectiles from the gate of babylon");
		eaDamage = ConfigUtils.getFloatConfig(config, "EA Damage", "minions", eaDamage, "");
		excaliburDamage = ConfigUtils.getFloatConfig(config, "Excalibur Damage", "minions", excaliburDamage, "");
		caladBolgDmg = ConfigUtils.getFloatConfig(config, "Calad Bolg Damage", "minions", caladBolgDmg, "");
		magicBeam = ConfigUtils.getFloatConfig(config, "Magic Beam Damage", "minions", magicBeam, "Magic beams fired by Medea");
		gordiusHealth = ConfigUtils.getFloatConfig(config, "Gordius Wheel Health", "minions", gordiusHealth, "");
		gordiusDmg = ConfigUtils.getFloatConfig(config, "Gordius Wheel Damage", "minions", gordiusDmg, "");
		pegasusHealth = ConfigUtils.getFloatConfig(config, "Pegasus Health", "minions", pegasusHealth, "");
		config.save();
	}
	
	private static void servants()
	{
		ConfigCategory cat = config.getCategory("servants");
		cat.setLanguageKey("config.fatemod.servants");
		cat.setComment("Configure individual servants");
		attributes.forEach((clss, prop)->{
			 prop.config(config, "servants."+EntityList.getKey(clss).toString());
		});
	}
			
	public static class PotionEffectsConfig implements IConfigArrayValue
	{
		Map<Potion,Pair<Integer,Integer>> potions = Maps.newHashMap();
		
		public PotionEffectsConfig addEffect(Potion potion, int duration, int amplifier)
		{
			if(potion!=null)
				potions.put(potion, Pair.of(duration, amplifier));
			return this;
		}
		
		@Override
		public IConfigArrayValue readFromString(String[] s) {
			for(String p : s)
			{
				String[] sub = p.split(",");
				if(sub.length!=3)
					continue;
				this.addEffect(Potion.getPotionFromResourceLocation(sub[0]), Integer.parseInt(sub[1]), Integer.parseInt(sub[2]));
			}
			return this;
		}

		@Override
		public String[] writeToString() {
			String[] s = new String[potions.size()];
			int i = 0;
			for(Entry<Potion, Pair<Integer, Integer>> entry : potions.entrySet())
			{
				Pair<Integer, Integer> p = entry.getValue();
				s[i]=ForgeRegistries.POTIONS.getKey(entry.getKey()).toString()+","+ p.getLeft()+","+ p.getRight();
				i++;
			}
			return s;
		}

		@Override
		public String usage() {
			return "<registry name>,<duration>,<amplifier>";
		}
		
		public PotionEffect[] potions()
		{
			PotionEffect[] effects = new PotionEffect[potions.size()];
			int i = 0;
			for(Entry<Potion, Pair<Integer, Integer>> entry : potions.entrySet())
			{
				Pair<Integer, Integer> p = entry.getValue();
				effects[i]=new PotionEffect(entry.getKey(), p.getLeft(), p.getRight());
				i++;
			}
			return effects;
		}
		
	}
	static
	{
		attributes.put(EntityArthur.class, new ServantProperties(300, 10, 17, 0.7F, 12, 10, 0.3, 100, 10, 5));
		attributes.put(EntityCuchulainn.class, new ServantProperties(275, 7.5, 10, 0, 14, 6, 0.35, 75, 10, 5));
		attributes.put(EntityDiarmuid.class, new ServantProperties(310, 8.5, 12, 0, 13, 7, 0.35, 80, 10, 5));
		attributes.put(EntityEmiya.class, new ServantProperties(250, 7.5, 8, 0, 15.5, 7, 0.33, 66, 10, 5));
		attributes.put(EntityGilgamesh.class, new ServantProperties(250, 10, 9, 0, 12.5, 5, 0.3, 100, 10, 5));
		attributes.put(EntityGilles.class, new ServantProperties(350, 5.5, 7, 0, 5, 14, 0.3, 80, 10, 5));
		attributes.put(EntityHassan.class, new ServantProperties(200, 6, 8.5, 0, 17, 4, 0.34, 1, 10, 5));
		attributes.put(EntityHeracles.class, new ServantProperties(75, 12.5, 10, 0, 17, 9.5, 0.2, 0, 10, 5));
		attributes.put(EntityLancelot.class, new ServantProperties(450, 9, 14, 0, 19, 4, 0.2, 0, 10, 5));
		attributes.put(EntityMedea.class, new ServantProperties(350, 9.5, 5, 0, 4, 17.5, 0.2, 100, 10, 5));
		attributes.put(EntityIskander.class, new ServantProperties(400, 5.5, 10, 0, 9, 9.5, 0.3, 100, 10, 5));
		attributes.put(EntityMedusa.class, new ServantProperties(250, 4.5, 11, 0, 7, 10, 0.3, 80, 10, 5));
		attributes.put(EntitySasaki.class, new ServantProperties(350, 9.5, 9, 0, 8, 8.5, 0.3, 50, 10, 5));
	}
}
