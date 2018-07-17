package com.flemmli97.fatemod.common.init;

import com.flemmli97.fatemod.client.render.RenderAltar;
import com.flemmli97.fatemod.client.render.RenderArcherArrow;
import com.flemmli97.fatemod.client.render.RenderBabylon;
import com.flemmli97.fatemod.client.render.RenderCaladBolg;
import com.flemmli97.fatemod.client.render.RenderEA;
import com.flemmli97.fatemod.client.render.RenderExcalibur;
import com.flemmli97.fatemod.client.render.RenderGaeBolg;
import com.flemmli97.fatemod.client.render.RenderGem;
import com.flemmli97.fatemod.client.render.RenderGordiusWheel;
import com.flemmli97.fatemod.client.render.RenderMagicBeam;
import com.flemmli97.fatemod.client.render.RenderPegasus;
import com.flemmli97.fatemod.client.render.servant.RenderArthur;
import com.flemmli97.fatemod.client.render.servant.RenderCuchulainn;
import com.flemmli97.fatemod.client.render.servant.RenderDiarmuid;
import com.flemmli97.fatemod.client.render.servant.RenderEmiya;
import com.flemmli97.fatemod.client.render.servant.RenderGilgamesh;
import com.flemmli97.fatemod.client.render.servant.RenderGilles;
import com.flemmli97.fatemod.client.render.servant.RenderHassan;
import com.flemmli97.fatemod.client.render.servant.RenderHassanCopy;
import com.flemmli97.fatemod.client.render.servant.RenderHeracles;
import com.flemmli97.fatemod.client.render.servant.RenderIskander;
import com.flemmli97.fatemod.client.render.servant.RenderLancelot;
import com.flemmli97.fatemod.client.render.servant.RenderMedea;
import com.flemmli97.fatemod.client.render.servant.RenderMedusa;
import com.flemmli97.fatemod.client.render.servant.RenderSasaki;
import com.flemmli97.fatemod.common.blocks.tile.TileAltar;
import com.flemmli97.fatemod.common.entity.EntityArcherArrow;
import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.entity.EntityCaladBolg;
import com.flemmli97.fatemod.common.entity.EntityEnumaElish;
import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.entity.EntityGem;
import com.flemmli97.fatemod.common.entity.EntityGordiusWheel;
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

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public final class ModRender {
	
	public static final void registerRenderers(){
		RenderingRegistry.registerEntityRenderingHandler(EntityArthur.class, RenderArthur::new);
		//saber class
		//""
		//""
		RenderingRegistry.registerEntityRenderingHandler(EntityGilgamesh.class, RenderGilgamesh::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityEmiya.class, RenderEmiya::new);
		//archer class
		//""
		RenderingRegistry.registerEntityRenderingHandler(EntityDiarmuid.class,RenderDiarmuid::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCuchulainn.class,RenderCuchulainn::new);
		//lancer class
		//""
		RenderingRegistry.registerEntityRenderingHandler(EntityIskander.class, RenderIskander::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMedusa.class, RenderMedusa::new);
		//rider class
		//""
		RenderingRegistry.registerEntityRenderingHandler(EntityHassan.class, RenderHassan::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHassanCopy.class, RenderHassanCopy::new);

		RenderingRegistry.registerEntityRenderingHandler(EntitySasaki.class, RenderSasaki::new);
		//assassin class
		//""
		RenderingRegistry.registerEntityRenderingHandler(EntityLancelot.class, RenderLancelot::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHeracles.class, RenderHeracles::new);
		//berserker class
		//""
		RenderingRegistry.registerEntityRenderingHandler(EntityGilles.class, RenderGilles::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMedea.class, RenderMedea::new);
		//caster class
		//""
		RenderingRegistry.registerEntityRenderingHandler(EntityPegasus.class, RenderPegasus::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGordiusWheel.class, RenderGordiusWheel::new);

		/*RenderingRegistry.registerEntityRenderingHandler(EntityBukephalos.class, new RenderBukephalos(new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGem.class, new RenderGem(ModItems.gemShard));*/
		RenderingRegistry.registerEntityRenderingHandler(EntityGaeBolg.class, RenderGaeBolg::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityExcalibur.class, RenderExcalibur::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityArcherArrow.class, RenderArcherArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBabylonWeapon.class, RenderBabylon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGem.class, RenderGem::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCaladBolg.class, RenderCaladBolg::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMagicBeam.class, RenderMagicBeam::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityEnumaElish.class, RenderEA::new);

        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderAltar());
	}

}
