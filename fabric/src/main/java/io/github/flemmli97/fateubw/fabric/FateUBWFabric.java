package io.github.flemmli97.fateubw.fabric;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.common.commands.CommandHandler;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.servant.ai.LancelotAttackAI;
import io.github.flemmli97.fateubw.common.event.EventCalls;
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
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.fabric.common.config.ConfigLoader;
import io.github.flemmli97.fateubw.fabric.common.config.ConfigSpecs;
import io.github.flemmli97.fateubw.fabric.common.network.ServerPacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FateUBWFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        registerContent();
        this.setup();
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> CommandHandler.reg(dispatcher)));
        ServerEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
            if (entity instanceof ServerPlayer serverPlayer)
                EventCalls.joinWorld(serverPlayer);
        }));
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.dimension() == Level.OVERWORLD) {
                GrailWarHandler.get(world.getServer()).tick(world);
            }
        });
        ServerLifecycleEvents.SERVER_STARTING.register(server -> ConfigLoader.loadCommon());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                return DatapackHandler.LOOT_TABLES.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(Fate.MODID, "grail_loots");
            }
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                return DatapackHandler.SERVANT_PROPS.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(Fate.MODID, "servant_props");
            }
        });
        ConfigSpecs.initCommonConfig();
        LancelotAttackAI.register(FabricLoader.getInstance()::isModLoaded);
    }

    public static void registerContent() {
        FateEntities.ENTITIES.getEntries();
        FateBlocks.BLOCKS.registerContent();
        FateItems.ITEMS.registerContent();
        FateBlocks.BLOCK_ENTITIES.registerContent();
        FateEntities.ENTITIES.registerContent();
        FateGrailLootSerializer.SERIALIZER.registerContent();
        FateGrailLootSerializer.LOOT_FUNCTION.registerContent();
        FateParticles.PARTICLES.registerContent();
        FateAttributes.ATTRIBUTES.registerContent();
        FateFeatures.register();
        FateSounds.SOUND_EVENTS.registerContent();
        FateMobEffects.EFFECTS.registerContent();
        FateFeatures.registerToBiomes((dec, holder) -> BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), dec, holder.unwrapKey().orElseThrow()));
    }

    public void setup() {
        FateEntities.registeredAttributes().forEach(FabricDefaultAttributeRegistry::register);
        FateCriterionTriggers.init();
        ServerPacketHandler.registerServer();
    }

    public static void entityTick(LivingEntity entity) {
        EventCalls.tick(entity);
        if (entity.level.isClientSide)
            ClientCalls.tick(entity);
    }
}
