package com.flemmli97.fatemod.common.handler;

import java.io.File;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;

public class ConfigHandler {

public static Configuration configuration;
public static boolean multiPlayer = false;
public static int charmSpawnRate;
public static int gemSpawnRate;
	
	public static void loadConfig(String configFileDir) 
	{
		general(new Configuration(new File(configFileDir, "main.cfg")));
		servantConfig(configFileDir+"/servant/");
	}
	
	private static void general(Configuration config)
	{
		config.load();
		configuration = config;
		config.addCustomCategoryComment("general", "");
		
		multiPlayer = config.getBoolean("general", "enableMultiPlayerMode", false, "Enables the multiplayer mode which makes for example the summoning table craftable. Currently does nothing");
		
		charmSpawnRate = config.get("general", "medallionSpawnRate", 1,  "Customize the spawn rate of the servant medallion ores, higher value means higher spawn rate. Default is 1").getInt(1);
		
		gemSpawnRate = config.get("general", "magicGemSpawnRate", 10, "Customize the spawn rate of the magic gem ores, higher value means higher spawn rate. Default is 10").getInt(10);
		
		config.save();
	}
	
	private static void servantConfig(String configDir)
	{
		for(Class<? extends Entity> clss : ServantAttributes.attributes.keySet())
		{
			String entity = EntityList.getEntityStringFromClass(clss).replaceAll("fatemod.", "");
			servant(new Configuration(new File(configDir, entity+".cfg")), clss, entity);
		}
	}
	
	private static void servant(Configuration config, Class<? extends Entity> clss, String entity)
	{
		config.load();
		ServantAttribute oldAtt = ServantAttributes.attributes.get(clss);
		ServantAttributes.attributes.put(clss, new ServantAttribute(
				getDoubleConfig(config, "Health", entity, oldAtt.health(), "Health of the servant"), 
				getDoubleConfig(config, "Strength", entity, oldAtt.strength(), "Attack damage of the servant"), 
				getDoubleConfig(config, "Armor", entity, oldAtt.armor(), "Generic armor of the servant"), 
				getDoubleConfig(config, "ProjectProt", entity, oldAtt.projectileProt(), "Projectile armor of the servant"), 
				getDoubleConfig(config, "MagicProt", entity, oldAtt.magicRes(), "Magic protection of the servant"), 
				getDoubleConfig(config, "MoveSpeed", entity, oldAtt.moveSpeed(), "Move speed of the servant"), 
				config.getInt("NobelPhantasmMana", entity, oldAtt.hogouMana(), 0, 100, "Nobel Phantasm mana usage"),
				config.getInt("Drop chance", entity, oldAtt.dropChance(), 0, 100, "Item drop chance upon death. Looting adds 10*level% chance to it")));
		config.save();
	}
	
	protected static double getDoubleConfig(Configuration config, String name, String category, double defaultValue, String comment)
	{
		Property prop = config.get(category, name, Double.toString(defaultValue), name);
        prop.setLanguageKey(name);
        prop.setComment(comment + "[default: " + defaultValue + "]");
        try
        {
            double parseFloat = Double.parseDouble(prop.getString());
            return Math.max(0, parseFloat);
        }
        catch (Exception e)
        {
            FMLLog.getLogger().error("Failed to get double for {}/{}", name, category, e);
        }
        return defaultValue;
	}
}
