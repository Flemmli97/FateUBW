package io.github.flemmli97.fateubw.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.commands.CommandHandler;
import io.github.flemmli97.fateubw.common.config.specs.ConfigLoader;
import io.github.flemmli97.fateubw.common.config.specs.ConfigSpecs;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.datapack.EntityPropsManager;
import io.github.flemmli97.fateubw.common.datapack.GrailLootManager;
import io.github.flemmli97.fateubw.common.event.EventCalls;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.registry.FateCreativeTab;
import io.github.flemmli97.fateubw.common.registry.FateCriterionTriggers;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateFeatures;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.fabric.network.PacketHandler;
import io.github.flemmli97.tenshilib.fabric.loader.events.CommonSetupEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.neoforged.fml.config.ModConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FateUBWFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        this.registerContent();
        NeoForgeModConfigEvents.loading(Fate.MODID).register(config -> {
            if (config.getSpec() == ConfigSpecs.CLIENT_SPEC)
                ConfigLoader.loadClient();
            if (config.getSpec() == ConfigSpecs.COMMON_SPEC)
                ConfigLoader.loadCommon();
        });
        NeoForgeModConfigEvents.reloading(Fate.MODID).register(config -> {
            if (config.getSpec() == ConfigSpecs.CLIENT_SPEC)
                ConfigLoader.loadClient();
            if (config.getSpec() == ConfigSpecs.COMMON_SPEC)
                ConfigLoader.loadCommon();
        });
        NeoForgeConfigRegistry.INSTANCE.register(Fate.MODID, ModConfig.Type.CLIENT, ConfigSpecs.CLIENT_SPEC, Fate.MODID + "/client.toml");
        NeoForgeConfigRegistry.INSTANCE.register(Fate.MODID, ModConfig.Type.COMMON, ConfigSpecs.COMMON_SPEC, Fate.MODID + "/common.toml");

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, sel) -> CommandHandler.reg(dispatcher)));
        ServerEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
            if (entity instanceof ServerPlayer serverPlayer)
                EventCalls.joinWorld(serverPlayer);
        }));
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.dimension() == Level.OVERWORLD) {
                GrailWarHandler.get(world.getServer()).tick(world);
            }
        });
        PacketHandler.register();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                return DatapackHandler.LOOT_TABLES.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }

            @Override
            public ResourceLocation getFabricId() {
                return GrailLootManager.ID;
            }
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                return DatapackHandler.SERVANT_PROPS.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }

            @Override
            public ResourceLocation getFabricId() {
                return EntityPropsManager.ID;
            }
        });
        FateFeatures.createFeatures(null, feat ->
                BiomeModifications.addFeature(ctx -> ctx.getBiomeRegistryEntry().is(feat.tag()),
                        feat.decoration(), ResourceKey.create(Registries.PLACED_FEATURE, feat.placedFeature())));
//        LancelotAttackAI.register(FabricLoader.getInstance()::isModLoaded);

        CommonSetupEvent.EVENT.register(listener -> listener.enqueue(Fate.MODID, () -> {
            FateEntities.registeredAttributes().forEach(FabricDefaultAttributeRegistry::register);
        }));
    }

    public void registerContent() {
        FateAttributes.ATTRIBUTES.registerContent();
        FateBlocks.BLOCK_ENTITIES.registerContent();
        FateBlocks.BLOCKS.registerContent();
        FateCreativeTab.TABS.registerContent();
        FateCriterionTriggers.TRIGGERS.registerContent();
        FateDataComponents.DATA_COMPONENTS.registerContent();
        FateEntities.ENTITIES.registerContent();
        FateGrailLootSerializer.LOOT_FUNCTION.registerContent();
        FateGrailLootSerializer.SERIALIZER.register().registerContent();
        FateItems.ITEMS.registerContent();
        FateMobEffects.EFFECTS.registerContent();
        FateParticles.PARTICLES.registerContent();
        FateSounds.SOUND_EVENTS.registerContent();
    }
}
