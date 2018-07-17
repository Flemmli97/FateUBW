package com.flemmli97.fatemod.common.handler;

import java.io.File;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.init.ServantAttributes;
import com.flemmli97.fatemod.common.utils.ServantProperties;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ConfigHandler {

public static int minPlayer;
public static int participants;
public static int joinTime;
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
		config.addCustomCategoryComment("general", "");
		minPlayer = config.getInt("Min Player", "general", 0, 0, 7, "Minimum of player count required to start a grail war");
		participants = config.getInt("Min Player", "general", 7, 0, 7, "Maximum of participants in a grail war");
		joinTime = config.get("general", "Join Time", 12000).getInt();
		charmSpawnRate = config.get("general", "Medallion rate", 1,  "Customize the spawn rate of the servant medallion ores, higher value means higher spawn rate. Default is 1").getInt(1);
		gemSpawnRate = config.get("general", "Magic gem rate", 10, "Customize the spawn rate of the magic gem ores, higher value means higher spawn rate. Default is 10").getInt(10);
		
		config.save();
	}
	
	private static void servantConfig(String configDir)
	{
		for (EntityEntry entry : ForgeRegistries.ENTITIES.getValues()) 
        {
            if (EntityServant.class.isAssignableFrom(entry.getEntityClass())) 
            {
            	String entity = entry.getRegistryName().getResourcePath();
    			servant(new Configuration(new File(configDir, entity+".cfg")), entry.getEntityClass(), entity);
            }
        }
	}
	
	private static void servant(Configuration config, Class<? extends Entity> clss, String entity)
	{
		config.load();
		ServantProperties oldAtt = ServantAttributes.attributes.get(clss);
		ServantAttributes.attributes.put(clss, new ServantProperties(
				getDoubleConfig(config, "Health", entity, oldAtt.health(), "Health of the servant"), 
				getDoubleConfig(config, "Strength", entity, oldAtt.strength(), "Attack damage of the servant"), 
				getDoubleConfig(config, "Armor", entity, oldAtt.armor(), "Generic armor of the servant"), 
				getDoubleConfig(config, "ProjectProt", entity, oldAtt.projectileProt(), "Projectile armor of the servant"), 
				getDoubleConfig(config, "MagicProt", entity, oldAtt.magicRes(), "Magic protection of the servant"), 
				getDoubleConfig(config, "MoveSpeed", entity, oldAtt.moveSpeed(), "Move speed of the servant"), 
				config.getInt("NobelPhantasmMana", entity, oldAtt.hogouMana(), 0, 100, "Nobel Phantasm mana usage"),
				config.getInt("Drop chance", entity, oldAtt.dropChance(), 0, 100, "Item drop chance upon death. Looting adds 10*level% chance to it"),
				config.getInt("Looting add", entity, oldAtt.looting(), 0, 100, "Chance per level of looting adding on to the drop chance")));
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
            FMLLog.log.error("Failed to get double for {}/{}", name, category, e);
        }
        return defaultValue;
	}
}
