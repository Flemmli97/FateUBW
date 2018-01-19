package com.flemmli97.fatemod.common.init;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityArcherArrow;
import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.entity.EntityCaladBolg;
import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.entity.EntityGem;
import com.flemmli97.fatemod.common.entity.EntityMagicBeam;
import com.flemmli97.fatemod.common.entity.EntityPegasus;
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

import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
	
	public static void mainRegistry(){
		int entityID = 0;
		EntityRegistry.registerModEntity(EntityArthur.class, "arthur", ++entityID, Fate.instance, 64, 3, true);
		//saber class
		//""
		//""
		EntityRegistry.registerModEntity(EntityGilgamesh.class, "gilgamesh", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityEmiya.class, "emiya", ++entityID, Fate.instance, 64, 3, true);
		//archer class
		//""
		EntityRegistry.registerModEntity(EntityDiarmuid.class, "diarmuid", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityCuchulainn.class, "cuchulainn", ++entityID, Fate.instance, 64, 3, true);
		//lancer class
		//""
		EntityRegistry.registerModEntity(EntityIskander.class, "alexander", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityMedusa.class, "medusa", ++entityID, Fate.instance, 64, 3, true);
		//rider class
		//""
		EntityRegistry.registerModEntity(EntityHassan.class, "hassan", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntitySasaki.class, "sasaki", ++entityID, Fate.instance, 64, 3, true);
		//herobrine :O
		EntityRegistry.registerModEntity(EntityLancelot.class, "lancelot", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityHeracles.class, "heracles", ++entityID, Fate.instance, 64, 3, true);
		//berserker class
		//""
		EntityRegistry.registerModEntity(EntityGilles.class, "gilles", ++entityID, Fate.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityMedea.class, "medea", ++entityID, Fate.instance, 64, 3, true);
		//caster class
		//""
		EntityRegistry.registerModEntity(EntityPegasus.class, "pegasus", ++entityID, Fate.instance, 64, 3, true);
		//EntityRegistry.registerModEntity(EntityBukephalos.class, "bukephalos", ++entityID, FateMod.instance, 64, 3, true);
		//pegasus
		//gordius wheel
		//the bulls
		EntityRegistry.registerModEntity(EntityGem.class, "entityGem", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityExcalibur.class, "entityExcalibur", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityGaeBolg.class, "entityGaeBolg", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityArcherArrow.class, "entityArcherArrow", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityBabylonWeapon.class, "entityBabylon", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityCaladBolg.class, "entityCaladBolg", ++entityID, Fate.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityMagicBeam.class, "entityMagicBeam", ++entityID, Fate.instance, 64, 10, true);
	}
}



