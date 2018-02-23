package com.flemmli97.fatemod.common.handler;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

public static Configuration configuration;
public static boolean multiPlayer = false;
public static int charmSpawnRate;
public static int gemSpawnRate;
	
	public static void loadConfig(Configuration config) {
		config.load();
		configuration = config;
		config.addCustomCategoryComment("general", "");
		
		multiPlayer = config.get("general", "enableMultiPlayerMode", false, "Enables the multiplayer mode which makes for example the summoning table craftable. Currently does nothing").getBoolean(false);
		
		charmSpawnRate = config.get("general", "medallionSpawnRate", 1,  "Customize the spawn rate of the servant medallion ores, higher value means higher spawn rate. Default is 1").getInt(1);
		
		gemSpawnRate = config.get("general", "magicGemSpawnRate", 10, "Customize the spawn rate of the magic gem ores, higher value means higher spawn rate. Default is 10").getInt(10);
		
		config.save();
	 }
}
