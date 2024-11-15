package io.github.flemmli97.fateubw.forge;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.servant.ai.LancelotAttackAI;
import io.github.flemmli97.fateubw.common.registry.AdvancementRegister;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModFeatures;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.registry.ModSounds;
import io.github.flemmli97.fateubw.forge.client.ClientEvents;
import io.github.flemmli97.fateubw.forge.common.capability.CapabilityInsts;
import io.github.flemmli97.fateubw.forge.common.config.ConfigLoader;
import io.github.flemmli97.fateubw.forge.common.config.ConfigSpecs;
import io.github.flemmli97.fateubw.forge.common.event.EventHandler;
import io.github.flemmli97.fateubw.forge.common.network.PacketHandler;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = Fate.MODID)
public class FateUBWForge {

    public FateUBWForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        File file = FMLPaths.CONFIGDIR.get().resolve(Fate.MODID).toFile();
        if (!file.exists())
            file.mkdir();
        registerContent();
        modBus.addListener(this::setup);
        modBus.addListener(this::conf);
        modBus.addListener(CapabilityInsts::register);
        modBus.addListener(this::attributes);

        if (FMLLoader.getDist() == Dist.CLIENT)
            ClientEvents.register();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(EventHandler.class);
        forgeBus.addListener(this::reloadListener);
        forgeBus.addListener(this::biomeLoadEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigSpecs.CLIENT_SPEC, Fate.MODID + "/client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigSpecs.COMMON_SPEC, Fate.MODID + "/common.toml");

        LancelotAttackAI.register(ModList.get()::isLoaded);
    }

    public static void registerContent() {
        ModBlocks.BLOCKS.registerContent();
        ModItems.ITEMS.registerContent();
        ModBlocks.TILES.registerContent();
        ModEntities.ENTITIES.registerContent();
        GrailLootSerializer.SERIALIZER.registerContent();
        ModParticles.PARTICLES.registerContent();
        ModAttributes.ATTRIBUTES.registerContent();
        ModSounds.SOUND_EVENTS.registerContent();
    }

    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            AdvancementRegister.init();
            ModFeatures.register();
        });
        PacketHandler.register();
    }

    public void conf(ModConfigEvent event) {
        if (event.getConfig().getSpec() == ConfigSpecs.COMMON_SPEC)
            ConfigLoader.loadCommon();
        if (event.getConfig().getSpec() == ConfigSpecs.CLIENT_SPEC)
            ConfigLoader.loadClient();
    }

    public void reloadListener(AddReloadListenerEvent event) {
        event.addListener(DatapackHandler.LOOT_TABLES);
        event.addListener(DatapackHandler.SERVANT_PROPS);
    }

    public void attributes(EntityAttributeCreationEvent event) {
        ModEntities.registeredAttributes()
                .forEach((type, builder) -> event.put(type, builder.build()));
    }

    public void biomeLoadEvent(BiomeLoadingEvent event) {
        if (event.getCategory() != Biome.BiomeCategory.THEEND && event.getCategory() != Biome.BiomeCategory.NETHER) {
            ModFeatures.registerToBiomes((dec, holder) -> event.getGeneration().addFeature(dec, holder));
        }
    }
}
