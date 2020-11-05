package com.flemmli97.fate;

import com.flemmli97.fate.common.config.ConfigSpecs;
import com.flemmli97.fate.common.registry.ModEntities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = Fate.MODID)
public class Fate {

    public static final String MODID = "fatemod";
    public static final Logger logger = LogManager.getLogger(Fate.MODID);

    public Fate() {
        File file = FMLPaths.CONFIGDIR.get().resolve("fate").toFile();
        if (!file.exists())
            file.mkdir();
        ModEntities.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigSpecs.commonSpec, "fate/common.toml");
    }

    @SubscribeEvent
    static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(()->ModEntities.registerAttributes());
    }

    @SubscribeEvent
    static void conf(ModConfig.ModConfigEvent event) {

    }
}
