package com.flemmli97.fate;

import com.flemmli97.fate.client.ClientHandler;
import com.flemmli97.fate.common.capability.IPlayer;
import com.flemmli97.fate.common.capability.PlayerCap;
import com.flemmli97.fate.common.capability.PlayerCapNetwork;
import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.config.ConfigSpecs;
import com.flemmli97.fate.common.registry.AdvancementRegister;
import com.flemmli97.fate.common.registry.ModBlocks;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.fate.common.utils.CachedWeaponList;
import com.flemmli97.fate.network.PacketHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        File file = FMLPaths.CONFIGDIR.get().resolve("fate").toFile();
        if (!file.exists())
            file.mkdir();
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.TILES.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigSpecs.commonSpec, "fate/common.toml");
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModEntities.registerAttributes();
            AdvancementRegister.init();
        });
        CachedWeaponList.init();
        PacketHandler.register();
        CapabilityManager.INSTANCE.register(IPlayer.class, new PlayerCapNetwork(), () -> new PlayerCap());
    }

    @SubscribeEvent
    public static void client(FMLClientSetupEvent event) {
        ClientHandler.registerRenderer();
    }

    @SubscribeEvent
    public static void conf(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == ConfigSpecs.commonSpec)
            Config.Common.load();
    }

    public static final ItemGroup TAB = new ItemGroup("fate") {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(ModItems.randomIcon.get());
        }
    };
}
