package io.github.flemmli97.fateubw.neoforge;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.specs.ConfigLoader;
import io.github.flemmli97.fateubw.common.config.specs.ConfigSpecs;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.servant.ai.LancelotAttackAI;
import io.github.flemmli97.fateubw.common.registry.AdvancementRegister;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModEffects;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModFeatures;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.registry.ModSounds;
import io.github.flemmli97.fateubw.neoforge.attachment.CapabilityInsts;
import io.github.flemmli97.fateubw.neoforge.client.ClientEvents;
import io.github.flemmli97.fateubw.neoforge.event.EventHandler;
import io.github.flemmli97.fateubw.neoforge.network.PacketHandler;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.io.File;

@Mod(value = Fate.MODID)
public class FateUBWNeoForge {

    public FateUBWNeoForge(IEventBus modBus, ModContainer container) {
        File file = FMLPaths.CONFIGDIR.get().resolve(Fate.MODID).toFile();
        if (!file.exists())
            file.mkdir();
        registerContent();
        modBus.addListener(this::setup);
        modBus.addListener(this::conf);
        modBus.addListener(CapabilityInsts::register);
        modBus.addListener(this::attributes);
        modBus.addGenericListener(Attribute.class, this::registry);

        if (FMLLoader.getDist() == Dist.CLIENT)
            ClientEvents.register(modBus);
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
        ModEffects.EFFECTS.registerContent();
    }

    public void registry(RegistryEvent.Register<Attribute> event) {
        // Is vanilla reg
        GrailLootSerializer.LOOT_FUNCTION.registerContent();
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
