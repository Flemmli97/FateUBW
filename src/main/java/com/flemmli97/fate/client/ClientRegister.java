package com.flemmli97.fate.client;

import com.flemmli97.fate.client.render.RenderArcherArrow;
import com.flemmli97.fate.client.render.RenderBabylon;
import com.flemmli97.fate.client.render.RenderCaladbolg;
import com.flemmli97.fate.client.render.RenderEA;
import com.flemmli97.fate.client.render.RenderExcalibur;
import com.flemmli97.fate.client.render.RenderGaeBolg;
import com.flemmli97.fate.client.render.RenderGem;
import com.flemmli97.fate.client.render.RenderStarfish;
import com.flemmli97.fate.client.render.servant.RenderArthur;
import com.flemmli97.fate.client.render.servant.RenderCuchulainn;
import com.flemmli97.fate.client.render.servant.RenderDiarmuid;
import com.flemmli97.fate.client.render.servant.RenderEmiya;
import com.flemmli97.fate.client.render.servant.RenderGilgamesh;
import com.flemmli97.fate.client.render.servant.RenderGilles;
import com.flemmli97.fate.client.render.servant.RenderLancelot;
import com.flemmli97.fate.common.registry.ModEntities;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientRegister {

    public static void registerRenderer() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.arthur.get(), RenderArthur::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.cuchulainn.get(), RenderCuchulainn::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.diarmuid.get(), RenderDiarmuid::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.emiya.get(), RenderEmiya::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gilgamesh.get(), RenderGilgamesh::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gilles.get(), RenderGilles::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.lancelot.get(), RenderLancelot::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.excalibur.get(), RenderExcalibur::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gaebolg.get(), RenderGaeBolg::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.archerArrow.get(), RenderArcherArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.caladbolg.get(), RenderCaladbolg::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.babylon.get(), RenderBabylon::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ea.get(), RenderEA::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.lesserMonster.get(), RenderStarfish::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gem.get(), RenderGem::new);
    }
}
