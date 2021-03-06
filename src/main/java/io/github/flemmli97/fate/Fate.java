package io.github.flemmli97.fate;

import io.github.flemmli97.fate.client.ClientEvents;
import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.capability.IPlayer;
import io.github.flemmli97.fate.common.capability.ItemStackCap;
import io.github.flemmli97.fate.common.capability.PlayerCap;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.config.ConfigSpecs;
import io.github.flemmli97.fate.common.datapack.DatapackHandler;
import io.github.flemmli97.fate.common.event.EventHandler;
import io.github.flemmli97.fate.common.loot.LootSerializerType;
import io.github.flemmli97.fate.common.registry.AdvancementRegister;
import io.github.flemmli97.fate.common.registry.GrailLootSerializer;
import io.github.flemmli97.fate.common.registry.ModBlocks;
import io.github.flemmli97.fate.common.registry.ModEntities;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.registry.ModParticles;
import io.github.flemmli97.fate.common.utils.CachedWeaponList;
import io.github.flemmli97.fate.network.PacketHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = Fate.MODID)
public class Fate {

    public static final String MODID = "fateubw";
    public static final Logger logger = LogManager.getLogger(Fate.MODID);

    public static boolean astralSorcery;

    private static IForgeRegistry<LootSerializerType<?>> grailSerializers;

    public Fate() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        File file = FMLPaths.CONFIGDIR.get().resolve("fate").toFile();
        if (!file.exists())
            file.mkdir();
        registerContent(modBus);
        modBus.addListener(this::setup);
        modBus.addListener(this::conf);
        modBus.addListener(this::newRegistry);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEvents::register);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(EventHandler.class);
        forgeBus.addListener(DatapackHandler::registerDataPackHandler);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigSpecs.commonSpec, "fate/common.toml");
        astralSorcery = ModList.get().isLoaded("astralsorcery");
    }

    public static void registerContent(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.TILES.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        GrailLootSerializer.SERIALIZER.register(modBus);
        ModParticles.PARTICLES.register(modBus);
    }

    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModEntities.registerAttributes();
            AdvancementRegister.init();
        });
        CachedWeaponList.init();
        PacketHandler.register();
        CapabilityManager.INSTANCE.register(IPlayer.class, new CapabilityInsts.PlayerCapNetwork(), PlayerCap::new);
        CapabilityManager.INSTANCE.register(ItemStackCap.class, new CapabilityInsts.ItemStackNetwork(), ItemStackCap::new);
    }

    public void newRegistry(RegistryEvent.NewRegistry event) {
        grailSerializers = new RegistryBuilder<LootSerializerType<?>>().setName(new ResourceLocation(Fate.MODID, "grail_loot_serialzer"))
                .setType(LootSerializerType.CLASS).setDefaultKey(new ResourceLocation(Fate.MODID, "default_serializer"))
                .create();
    }

    public void conf(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == ConfigSpecs.commonSpec)
            Config.Common.load();
        if (event.getConfig().getSpec() == ConfigSpecs.clientSpec)
            Config.Client.load();
    }

    public static IForgeRegistry<LootSerializerType<?>> getGrailSerializers() {
        if (grailSerializers == null)
            throw new IllegalStateException("Registry not initialized yet");
        return grailSerializers;
    }

    public static final ItemGroup TAB = new ItemGroup("fate") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.randomIcon.get());
        }
    };
}
