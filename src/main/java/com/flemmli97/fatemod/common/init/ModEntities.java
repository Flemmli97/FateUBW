package com.flemmli97.fatemod.common.init;

import java.util.Map;
import java.util.Set;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityArcherArrow;
import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.entity.EntityCaladBolg;
import com.flemmli97.fatemod.common.entity.EntityCasterCircle;
import com.flemmli97.fatemod.common.entity.EntityEnumaElish;
import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.entity.EntityGem;
import com.flemmli97.fatemod.common.entity.EntityGordiusWheel;
import com.flemmli97.fatemod.common.entity.EntityLesserMonster;
import com.flemmli97.fatemod.common.entity.EntityMagicBeam;
import com.flemmli97.fatemod.common.entity.EntityPegasus;
import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityGilgamesh;
import com.flemmli97.fatemod.common.entity.servant.EntityGilles;
import com.flemmli97.fatemod.common.entity.servant.EntityHassan;
import com.flemmli97.fatemod.common.entity.servant.EntityHassanCopy;
import com.flemmli97.fatemod.common.entity.servant.EntityHeracles;
import com.flemmli97.fatemod.common.entity.servant.EntityIskander;
import com.flemmli97.fatemod.common.entity.servant.EntityLancelot;
import com.flemmli97.fatemod.common.entity.servant.EntityMedea;
import com.flemmli97.fatemod.common.entity.servant.EntityMedusa;
import com.flemmli97.fatemod.common.entity.servant.EntitySasaki;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.EnumServantType;
import com.flemmli97.fatemod.common.lib.LibEntities;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
	
	private static final Map<EnumServantType,Set<ResourceLocation>> map = Maps.newHashMap();
	
	public static void mainRegistry(){
		int entityID = 0;
		registerServant(LibEntities.arthur, EntityArthur.class, ++entityID, EnumServantType.SABER);
		//saber class
		//""
		//""
		registerServant(LibEntities.gilgamesh, EntityGilgamesh.class, ++entityID, EnumServantType.ARCHER);
		registerServant(LibEntities.emiya, EntityEmiya.class, ++entityID, EnumServantType.ARCHER);
		//archer class
		//""
		registerServant(LibEntities.diarmuid, EntityDiarmuid.class, ++entityID, EnumServantType.LANCER);
		registerServant(LibEntities.cuchulainn, EntityCuchulainn.class, ++entityID, EnumServantType.LANCER);
		//lancer class
		//""
		registerServant(LibEntities.alexander, EntityIskander.class, ++entityID, EnumServantType.RIDER);
		registerServant(LibEntities.medusa, EntityMedusa.class, ++entityID, EnumServantType.RIDER);
		//rider class
		//""
		registerServant(LibEntities.hassan, EntityHassan.class, ++entityID, EnumServantType.ASSASSIN);
		EntityRegistry.registerModEntity(LibEntities.hassan_copy, EntityHassanCopy.class, LibEntities.hassan_copy.getResourcePath(), ++entityID, Fate.instance, 64, 3, true);

		registerServant(LibEntities.sasaki, EntitySasaki.class, ++entityID, EnumServantType.ASSASSIN);
		//herobrine :O
		registerServant(LibEntities.lancelot, EntityLancelot.class, ++entityID, EnumServantType.BERSERKER);
		registerServant(LibEntities.heracles, EntityHeracles.class, ++entityID, EnumServantType.BERSERKER);
		//berserker class
		//""
		registerServant(LibEntities.gilles, EntityGilles.class, ++entityID, EnumServantType.CASTER);
		registerServant(LibEntities.medea, EntityMedea.class, ++entityID, EnumServantType.CASTER);
		//caster class
		//""
		EntityRegistry.registerModEntity(LibEntities.pegasus, EntityPegasus.class, LibEntities.pegasus.getResourcePath(), ++entityID, Fate.instance, 64, 3, true);
		//EntityRegistry.registerModEntity(EntityBukephalos.class, "bukephalos", ++entityID, FateMod.instance, 64, 3, true);
		EntityRegistry.registerModEntity(LibEntities.gordius, EntityGordiusWheel.class, LibEntities.gordius.getResourcePath(), ++entityID, Fate.instance, 64, 3, true);

		EntityRegistry.registerModEntity(LibEntities.entity_gem, EntityGem.class, LibEntities.entity_gem.getResourcePath(), ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntities.excalibur, EntityExcalibur.class, LibEntities.excalibur.getResourcePath(), ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntities.gae_bolg, EntityGaeBolg.class, LibEntities.gae_bolg.getResourcePath(), ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntities.archer_arrow, EntityArcherArrow.class, LibEntities.archer_arrow.getResourcePath(), ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntities.babylon, EntityBabylonWeapon.class, LibEntities.babylon.getResourcePath(), ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntities.caladbolg, EntityCaladBolg.class, LibEntities.caladbolg.getResourcePath(), ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntities.magic_beam, EntityMagicBeam.class, LibEntities.magic_beam.getResourcePath(), ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntities.ea, EntityEnumaElish.class, LibEntities.ea.getResourcePath(), ++entityID, Fate.instance, 64, 10, true);
		
		EntityRegistry.registerModEntity(LibEntities.monster_small, EntityLesserMonster.class, LibEntities.monster_small.getResourcePath(), ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(LibEntities.medea_circle, EntityCasterCircle.class, LibEntities.medea_circle.getResourcePath(), ++entityID, Fate.instance, 64, 3, true);

	}
	
	private static void registerServant(ResourceLocation res, Class<? extends EntityServant> clss, int id, EnumServantType type)
	{
		EntityRegistry.registerModEntity(res, clss, res.getResourcePath(), id, Fate.instance, 64, 3, true);
		map.merge(type, Sets.newHashSet(res), (old,list)->{old.addAll(list);return old;});
	}
	
	public static Set<ResourceLocation> getAllServant(EnumServantType type)
	{
		return ImmutableSet.copyOf(map.get(type));
	}
}



