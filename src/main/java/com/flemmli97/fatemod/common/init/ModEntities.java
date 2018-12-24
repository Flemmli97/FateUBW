package com.flemmli97.fatemod.common.init;

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
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
	
	public static void mainRegistry(){
		int entityID = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "arthur"), EntityArthur.class, "arthur", ++entityID, Fate.instance, 64, 3, true);
		//saber class
		//""
		//""
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "gilgamesh"), EntityGilgamesh.class, "gilgamesh", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "emiya"), EntityEmiya.class, "emiya", ++entityID, Fate.instance, 64, 3, true);
		//archer class
		//""
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "diarmuid"), EntityDiarmuid.class, "diarmuid", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "cuchulainn"), EntityCuchulainn.class, "cuchulainn", ++entityID, Fate.instance, 64, 3, true);
		//lancer class
		//""
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "alexander"), EntityIskander.class, "alexander", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "medusa"), EntityMedusa.class, "medusa", ++entityID, Fate.instance, 64, 3, true);
		//rider class
		//""
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "hassan"), EntityHassan.class, "hassan", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "hassanCopy"), EntityHassanCopy.class, "hassanCopy", ++entityID, Fate.instance, 64, 3, true);

		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "sasaki"), EntitySasaki.class, "sasaki", ++entityID, Fate.instance, 64, 3, true);
		//herobrine :O
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "lancelot"), EntityLancelot.class, "lancelot", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "heracles"), EntityHeracles.class, "heracles", ++entityID, Fate.instance, 64, 3, true);
		//berserker class
		//""
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "gilles"), EntityGilles.class, "gilles", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "medea"), EntityMedea.class, "medea", ++entityID, Fate.instance, 64, 3, true);
		//caster class
		//""
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "pegasus"), EntityPegasus.class, "pegasus", ++entityID, Fate.instance, 64, 3, true);
		//EntityRegistry.registerModEntity(EntityBukephalos.class, "bukephalos", ++entityID, FateMod.instance, 64, 3, true);
		//pegasus
		//gordius wheel
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "gordius"), EntityGordiusWheel.class, "gordius", ++entityID, Fate.instance, 64, 3, true);

		//the bulls
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "entityGem"), EntityGem.class, "entityGem", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "entityExcalibur"), EntityExcalibur.class, "entityExcalibur", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "entityGaeBolg"), EntityGaeBolg.class, "entityGaeBolg", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "entityArcherArrow"), EntityArcherArrow.class, "entityArcherArrow", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "entityBabylon"), EntityBabylonWeapon.class, "entityBabylon", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "entityCaladBolg"), EntityCaladBolg.class, "entityCaladBolg", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "entityMagicBeam"), EntityMagicBeam.class, "entityMagicBeam", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "entityEA"), EntityEnumaElish.class, "entityEA", ++entityID, Fate.instance, 64, 10, true);
		
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "monsterSmall"), EntityLesserMonster.class, "monsterSmall", ++entityID, Fate.instance, 64, 3, true);

	}
}



