package com.flemmli97.fatemod.common.entity.servant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SpawnEntityCustomList
{
    private static final Logger logger = LogManager.getLogger();

    /** Provides a mapping between entity classes and a string */
	private static Map<String, Class<? extends Entity>> stringToClassMapping = new HashMap<String, Class<? extends Entity>>();

	/** Provides a mapping between a string and an entity classes */
	private static Map<Class<? extends Entity>, String> classToStringMapping = new HashMap<Class<? extends Entity>, String>();

	/** This is a HashMap of the Creative Entity Eggs/Spawners. */
	public static HashMap<String, EntityEggInfo> entityEggs = new LinkedHashMap<String, EntityEggInfo>();

    /**
     * adds a mapping between Entity classes and a string representation
     */
	private static void addMapping(Class<? extends Entity> parEntityclass, String parEntityName)
    {
        if (stringToClassMapping.containsKey(parEntityName))
        {
            throw new IllegalArgumentException("ID is already registered: " + parEntityName);
        }
        else
        {
            stringToClassMapping.put(parEntityName, parEntityclass);
            classToStringMapping.put(parEntityclass, parEntityName);
        }
    }

    /**
     * Adds a entity mapping with egg info.
     */
    private static void addMapping(Class<? extends Entity> entity, String entityName, int eggfirstcolour, int eggsecondcolour)
    {
    	String entityNameFull = "fatemod." + entityName;
        addMapping(entity, entityNameFull);
        entityEggs.put(entityNameFull, new SpawnEntityCustomList.EntityEggInfo(entityNameFull, eggfirstcolour, eggsecondcolour));
    }

    static
    {
        addMapping(EntityArthur.class, "arthur", 0x048dd0, 0xecee37);
        //saber class
        //""
        //""
        addMapping(EntityGilgamesh.class, "gilgamesh", 0xfff400, 0xffdb00);
        addMapping(EntityEmiya.class, "emiya", 0x9f0707, 0x000000);
        //archer class
        //""
        addMapping(EntityDiarmuid.class, "diarmuid", 0x000000, 0x2a079a);
        addMapping(EntityCuchulainn.class, "cuchulainn", 0x0038ff, 0xb6c0c1);
        //lancer class
        //""
        addMapping(EntityIskander.class, "alexander", 0xd40000, 0x8d0101);
        addMapping(EntityMedusa.class, "medusa", 0x000000, 0xf234ea);
        //rider class
        //""
        addMapping(EntityGilles.class, "gilles", 0x100460, 0x600453);
        addMapping(EntityMedea.class, "medea", 0x6f086b, 0x4a8be5);
        //caster class
        //""
        addMapping(EntityHassan.class, "hassan", 0x000, 0x3a393a);
        addMapping(EntitySasaki.class, "sasaki", 0x4e04c3, 0xa77cec);
        //assassin class
        //""       
        addMapping(EntityLancelot.class, "lancelot", 0x071a33, 0x1d4f94);
        addMapping(EntityHeracles.class, "heracles", 0x3c1d06, 0x5e3c22);
        //berserker class
        //""
    }

    /**
     * Create a new instance of an entity in the world by using the entity name.
     */
    public static Entity createEntityByName(String entityName, World world)
    {
    	return createEntity(stringToClassMapping.get(entityName), world);
    }

    /**
     * create a new instance of an entity from NBT store
     */
    public static Entity createEntityFromNBT(NBTTagCompound compound, World world) {
		Entity entity = createEntityByName(compound.getString("id"), world);
		if (entity != null) {
			try {
				entity.readFromNBT(compound);
			} catch (Exception e) {
				logger.error(String.format("An Entity %s has thrown an exception during loading, its state cannot be restored. Report this to the mod author", compound.getString("id")));
				e.printStackTrace();
				entity = null;
			}
		} else {
			logger.warn("Skipping Entity with id " + compound.getString("id"));
		}

		return entity;
	}
    
    public static Entity createEntity(Class<? extends Entity> oclass, World world) {
		Entity entity = null;
		try {
			if (oclass != null) {
				entity = oclass.getConstructor(World.class).newInstance(world);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return entity;
	}

    /**
     * Gets the string representation of a specific entity.
     */
    public static String getEntityString(Entity parEntity)
    {
        return (String)classToStringMapping.get(parEntity.getClass());
    }

    public static class EntityEggInfo
    {
        public final String spawnedID;
        public final int primaryColor;
        public final int secondaryColor;

        public EntityEggInfo(String entityID, int eggfirstcolour, int eggsecondcolour)
        {
            this.spawnedID = entityID;
            this.primaryColor = eggfirstcolour;
            this.secondaryColor = eggsecondcolour;
        }
    }
}