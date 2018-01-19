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
	public static Map<String, Class<? extends Entity>> stringToClassMapping = new HashMap<String, Class<? extends Entity>>();

	/** Provides a mapping between a string and an entity classes */
	public static Map<Class<? extends Entity>, String> classToStringMapping = new HashMap<Class<? extends Entity>, String>();

	/** provides a mapping between an entityID and an Entity Class */
	public static Map<Integer, Class<? extends Entity>> IDtoClassMapping = new HashMap<Integer, Class<? extends Entity>>();

	/** provides a mapping between an Entity Class and an entity ID */
	private static Map<Class<? extends Entity>, Integer> classToIDMapping = new HashMap<Class<? extends Entity>, Integer>();

	/** Maps entity names to their numeric identifiers */
	private static Map<String, Integer> stringToIDMapping = new HashMap<String, Integer>();

	/** This is a HashMap of the Creative Entity Eggs/Spawners. */
	public static HashMap<String, EntityEggInfo> entityEggs = new LinkedHashMap<String, EntityEggInfo>();

    /**
     * adds a mapping between Entity classes and both a string representation and an ID
     */
	private static void addMapping(Class<? extends Entity> parEntityclass, String parEntityName, int parInt)
    {
        if (stringToClassMapping.containsKey(parEntityName))
        {
            throw new IllegalArgumentException("ID is already registered: " + parEntityName);
        }
        else if (IDtoClassMapping.containsKey(Integer.valueOf(parInt)))
        {
            throw new IllegalArgumentException("ID is already registered: " + parInt);
        }
        else
        {
            stringToClassMapping.put(parEntityName, parEntityclass);
            classToStringMapping.put(parEntityclass, parEntityName);
            IDtoClassMapping.put(Integer.valueOf(parInt), parEntityclass);
            classToIDMapping.put(parEntityclass, Integer.valueOf(parInt));
            stringToIDMapping.put(parEntityName, Integer.valueOf(parInt));
        }
    }

    /**
     * Adds a entity mapping with egg info.
     */
    private static void addMapping(Class<? extends Entity> entity, String entityName, int metadata, int eggfirstcolour, int eggsecondcolour)
    {
    	String entityNameFull = "fatemod." + entityName;
        addMapping(entity, entityNameFull, metadata);
        entityEggs.put(entityNameFull, new SpawnEntityCustomList.EntityEggInfo(entityNameFull, eggfirstcolour, eggsecondcolour));
    }

    static
    {
        addMapping(EntityArthur.class, "arthur", 0, 0x048dd0, 0xecee37);
        //saber class
        //""
        //""
        addMapping(EntityGilgamesh.class, "gilgamesh", 4, 0xfff400, 0xffdb00);
        addMapping(EntityEmiya.class, "emiya", 5, 0x9f0707, 0x000000);
        //archer class
        //""
        addMapping(EntityDiarmuid.class, "diarmuid", 8, 0x000000, 0x2a079a);
        addMapping(EntityCuchulainn.class, "cuchulainn", 9, 0x0038ff, 0xb6c0c1);
        //lancer class
        //""
        addMapping(EntityIskander.class, "alexander", 12, 0xd40000, 0x8d0101);
        addMapping(EntityMedusa.class, "medusa", 13, 0x000000, 0xf234ea);
        //rider class
        //""
        addMapping(EntityGilles.class, "gilles", 16, 0x100460, 0x600453);
        addMapping(EntityMedea.class, "medea", 17, 0x6f086b, 0x4a8be5);
        //caster class
        //""
        addMapping(EntityHassan.class, "hassan", 20, 0x000, 0x3a393a);
        addMapping(EntitySasaki.class, "sasaki", 21, 0x4e04c3, 0xa77cec);
        //assassin class
        //""       
        addMapping(EntityLancelot.class, "lancelot", 24, 0x071a33, 0x1d4f94);
        addMapping(EntityHeracles.class, "heracles", 25, 0x3c1d06, 0x5e3c22);
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

    /**
     * Create a new instance of an entity in the world by using an entity ID.
     */
    public static Entity createEntityByID(int entityID, World world) {
		return createEntity(getClassFromID(entityID), world);
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
     * gets the entityID of a specific entity
     */
    public static int getEntityID(Entity parEntity)
    {
    	Class<? extends Entity> oclass = parEntity.getClass();
		return classToIDMapping.containsKey(oclass) ? classToIDMapping.get(oclass) : 0;
    }

    /**
     * Return the class assigned to this entity ID.
     */
    public static Class<? extends Entity> getClassFromID(int entityID) {
		return IDtoClassMapping.get(entityID);
	}

    /**
     * Gets the string representation of a specific entity.
     */
    public static String getEntityString(Entity parEntity)
    {
        return (String)classToStringMapping.get(parEntity.getClass());
    }

    /**
     * Finds the class using IDtoClassMapping and classToStringMapping
     */
    public static String getStringFromID(int entityID) {
		Class<? extends Entity> oclass = getClassFromID(entityID);
		return oclass != null ? classToStringMapping.get(oclass) : null;
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