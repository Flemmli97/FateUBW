package com.flemmli97.fatemod.common.handler;

import java.io.File;
import java.util.Map;

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
import com.flemmli97.tenshilib.common.config.ConfigUtils;
import com.flemmli97.tenshilib.common.config.ConfigUtils.LoadState;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class ConfigHandler {
	
	public static Configuration config;

	//General
	public static int minPlayer;
	public static int maxPlayer;
	public static int joinTime;
	public static int rewardDelay;
	public static int charmSpawnRate;
	public static int gemSpawnRate;
	public static boolean allowDuplicateServant;
	public static boolean allowDuplicateClass;
	
	//Servants
	public static final Map<Class<? extends Entity>, ServantProperties> attributes = Maps.newHashMap();

	//Minions
	public static int gillesMinionDuration;
	public static ServantProperties hassanCopy = new ServantProperties(50, 4, 3.5, 0, 12, 2, 0.34, 0, 10, 5);

	public static void load(LoadState state)
	{
		if(config==null)
		{
			config = new Configuration(new File(Loader.instance().getConfigDir(), "fate/main.cfg"));
			config.load();
		}
		
		ConfigCategory general = config.getCategory("general");
		general.setLanguageKey("config.fatemod.general");
		maxPlayer = ConfigUtils.getIntConfig(config, "Max Player", "general", 7, 0 ,"Maximum of player allowed in a grail war");
		minPlayer = ConfigUtils.getIntConfig(config, "Min Player", "general", 0, maxPlayer, "Minimum of player count required to start a grail war");
		joinTime = ConfigUtils.getIntConfig(config, "Join Time", "general", 12000, 0 ,"Time buffer to join a grail war after start");
		rewardDelay = ConfigUtils.getIntConfig(config, "Reward Delay", "general", 2000, 0 ,"Delay after an ended grail war for getting the grail");
		charmSpawnRate = ConfigUtils.getIntConfig(config, "Medallion Rate", "general", 1, 0 ,"Customize the spawn rate of the servant medallion ores, higher value means higher spawn rate");
		gemSpawnRate = ConfigUtils.getIntConfig(config, "Magic Gem Rate", "general", 10, 0 ,"Customize the spawn rate of the magic gem ores, higher value means higher spawn rate");
		allowDuplicateServant = config.getBoolean("Allow Duplicate Servants", "general", false, "Allow the summoning of duplicate servants during a grail war");
		allowDuplicateClass = config.getBoolean("Allow Duplicate Classes", "general", false, "Allow the summoning of duplicate servant classes during a grail war");
		
		if(state==LoadState.POSTINIT||state==LoadState.SYNC)
			servants();
		
		ConfigCategory minions = config.getCategory("minions");
		minions.setLanguageKey("config.fatemod.minions");
		gillesMinionDuration = config.get("minions", "Gilles Monster", 4).getInt();
		if(state==LoadState.POSTINIT||state==LoadState.SYNC)
		{
			ConfigCategory hassan = config.getCategory("minions.hassancopy");
			hassan.setLanguageKey("config.fatemod.hassancopy");
			hassan.setComment("Hassans Clones");
			hassanCopy = hassanCopy.config(config, hassanCopy, "minions.hassancopy");
		}
		config.save();
	}
	
	private static void servants()
	{
		ConfigCategory cat = config.getCategory("servants");
		cat.setLanguageKey("config.fatemod.servants");
		cat.setComment("Configure individual servants");
		attributes.replaceAll((clss, prop)->{
			return prop.config(config, prop, "servants."+EntityList.getKey(clss).toString());
		});
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
