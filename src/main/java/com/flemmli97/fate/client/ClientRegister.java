package com.flemmli97.fate.client;

import com.flemmli97.fate.client.render.RenderExcalibur;
import com.flemmli97.fate.client.render.RenderGaeBolg;
import com.flemmli97.fate.client.render.servant.RenderArthur;
import com.flemmli97.fate.client.render.servant.RenderCuchulainn;
import com.flemmli97.fate.common.registry.ModEntities;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientRegister {

    public static void registerRenderer() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.arthur.get(), RenderArthur::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.cuchulainn.get(), RenderCuchulainn::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.excalibur.get(), RenderExcalibur::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gaebolg.get(), RenderGaeBolg::new);
    }
}
