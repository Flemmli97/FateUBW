package com.flemmli97.fatemod.common.init;

import java.util.Map;
import java.util.Set;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityArcherArrow;
import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.entity.EntityCaladBolg;
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
import com.flemmli97.fatemod.common.lib.LibReference;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
	
	private static final Map<EnumServantType,Set<ResourceLocation>> map = Maps.newHashMap();
	
	public static void mainRegistry(){
		int entityID = 0;
		registerServant(new ResourceLocation(LibReference.MODID, "arthur"), EntityArthur.class, ++entityID, EnumServantType.SABER);
		//saber class
		//""
		//""
		registerServant(new ResourceLocation(LibReference.MODID, "gilgamesh"), EntityGilgamesh.class, ++entityID, EnumServantType.ARCHER);
		registerServant(new ResourceLocation(LibReference.MODID, "emiya"), EntityEmiya.class, ++entityID, EnumServantType.ARCHER);
		//archer class
		//""
		registerServant(new ResourceLocation(LibReference.MODID, "diarmuid"), EntityDiarmuid.class, ++entityID, EnumServantType.LANCER);
		registerServant(new ResourceLocation(LibReference.MODID, "cuchulainn"), EntityCuchulainn.class, ++entityID, EnumServantType.LANCER);
		//lancer class
		//""
		registerServant(new ResourceLocation(LibReference.MODID, "alexander"), EntityIskander.class, ++entityID, EnumServantType.RIDER);
		registerServant(new ResourceLocation(LibReference.MODID, "medusa"), EntityMedusa.class, ++entityID, EnumServantType.RIDER);
		//rider class
		//""
		registerServant(new ResourceLocation(LibReference.MODID, "hassan"), EntityHassan.class, ++entityID, EnumServantType.ASSASSIN);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "hassan_copy"), EntityHassanCopy.class, "hassan_copy", ++entityID, Fate.instance, 64, 3, true);

		registerServant(new ResourceLocation(LibReference.MODID, "sasaki"), EntitySasaki.class, ++entityID, EnumServantType.ASSASSIN);
		//herobrine :O
		registerServant(new ResourceLocation(LibReference.MODID, "lancelot"), EntityLancelot.class, ++entityID, EnumServantType.BERSERKER);
		registerServant(new ResourceLocation(LibReference.MODID, "heracles"), EntityHeracles.class, ++entityID, EnumServantType.BERSERKER);
		//berserker class
		//""
		registerServant(new ResourceLocation(LibReference.MODID, "gilles"), EntityGilles.class, ++entityID, EnumServantType.CASTER);
		registerServant(new ResourceLocation(LibReference.MODID, "medea"), EntityMedea.class, ++entityID, EnumServantType.CASTER);
		//caster class
		//""
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "pegasus"), EntityPegasus.class, "pegasus", ++entityID, Fate.instance, 64, 3, true);
		//EntityRegistry.registerModEntity(EntityBukephalos.class, "bukephalos", ++entityID, FateMod.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "gordius"), EntityGordiusWheel.class, "gordius", ++entityID, Fate.instance, 64, 3, true);

		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "entity_gem"), EntityGem.class, "entity_gem", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "excalibur"), EntityExcalibur.class, "excalibur", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "gae_bolg"), EntityGaeBolg.class, "gae_bolg", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "archer_arrow"), EntityArcherArrow.class, "archer_arrow", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "babylon"), EntityBabylonWeapon.class, "babylon", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "caladbolg"), EntityCaladBolg.class, "caladbolg", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "magic_beam"), EntityMagicBeam.class, "magic_beam", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ea"), EntityEnumaElish.class, "ea", ++entityID, Fate.instance, 64, 10, true);
		
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "monster_small"), EntityLesserMonster.class, "monster_small", ++entityID, Fate.instance, 64, 3, true);

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



