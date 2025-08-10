package io.github.flemmli97.fateubw.neoforge;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.specs.ConfigLoader;
import io.github.flemmli97.fateubw.common.config.specs.ConfigSpecs;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.servant.ai.LancelotAttackAI;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.registry.FateCriterionTriggers;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateFeatures;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.neoforge.attachment.CapabilityInsts;
import io.github.flemmli97.fateubw.neoforge.client.ClientEvents;
import io.github.flemmli97.fateubw.neoforge.event.EventHandler;
import io.github.flemmli97.fateubw.neoforge.network.PacketHandler;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(value = Fate.MODID)
public class FateUBWNeoForge {

    public FateUBWNeoForge(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, ConfigSpecs.CLIENT_SPEC, Fate.MODID + "/client.toml");
        container.registerConfig(ModConfig.Type.COMMON, ConfigSpecs.COMMON_SPEC, Fate.MODID + "/common.toml");
        registerContent(modBus);
        modBus.addListener(this::setup);
        modBus.addListener(this::configLoading);
        modBus.addListener(this::configReloading);
        modBus.addListener(CapabilityInsts::register);
        modBus.addListener(this::attributes);

        if (FMLLoader.getDist() == Dist.CLIENT)
            ClientEvents.register(modBus);
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.register(EventHandler.class);
        eventBus.addListener(this::reloadListener);
        eventBus.addListener(this::biomeLoadEvent);

        LancelotAttackAI.register(ModList.get()::isLoaded);
    }

    public static void registerContent(IEventBus modbus) {
        FateBlocks.BLOCKS.registerContent(modbus);
        FateItems.ITEMS.registerContent(modbus);
        FateBlocks.BLOCK_ENTITIES.registerContent(modbus);
        FateEntities.ENTITIES.registerContent(modbus);
        FateGrailLootSerializer.SERIALIZER.register().registerContent(modbus);
        FateParticles.PARTICLES.registerContent(modbus);
        FateAttributes.ATTRIBUTES.registerContent(modbus);
        FateSounds.SOUND_EVENTS.registerContent(modbus);
        FateMobEffects.EFFECTS.registerContent(modbus);
        FateGrailLootSerializer.LOOT_FUNCTION.registerContent(modbus);
    }

    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FateCriterionTriggers.init();
            FateFeatures.register();
        });
        PacketHandler.register();
    }

    public void configLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == ConfigSpecs.COMMON_SPEC)
            ConfigLoader.loadCommon();
        if (event.getConfig().getSpec() == ConfigSpecs.CLIENT_SPEC)
            ConfigLoader.loadClient();
    }

    public void configReloading(ModConfigEvent.Reloading event) {
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
        FateEntities.registeredAttributes()
                .forEach((type, builder) -> event.put(type, builder.build()));
    }

    public void biomeLoadEvent(BiomeLoadingEvent event) {
        if (event.getCategory() != Biome.BiomeCategory.THEEND && event.getCategory() != Biome.BiomeCategory.NETHER) {
            FateFeatures.registerToBiomes((dec, holder) -> event.getGeneration().addFeature(dec, holder));
        }
    }
}
