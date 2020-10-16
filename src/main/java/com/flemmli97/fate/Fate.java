package com.flemmli97.fate;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = Fate.MODID)
public class Fate {

    public static final String MODID = "fatemod";
    public static final Logger logger = LogManager.getLogger(Fate.MODID);

    public Fate() {

    }

    @SubscribeEvent
    static void setup(FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    static void conf(ModConfig.ModConfigEvent event) {

    }
}
