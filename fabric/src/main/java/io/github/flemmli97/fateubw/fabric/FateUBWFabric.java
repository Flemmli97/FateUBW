package io.github.flemmli97.fateubw.fabric;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.commands.CommandHandler;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.event.EventCalls;
import io.github.flemmli97.fateubw.common.registry.AdvancementRegister;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModFeatures;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
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
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            ConfigLoader.loadCommon();
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                return DatapackHandler.lootTables.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(Fate.MODID, "grail_loots");
            }
        });
        ConfigSpecs.initCommonConfig();
    }

    public static void registerContent() {
        ModEntities.ENTITIES.getEntries();
        ModBlocks.BLOCKS.registerContent();
        ModItems.ITEMS.registerContent();
        ModBlocks.TILES.registerContent();
        ModEntities.ENTITIES.registerContent();
        GrailLootSerializer.SERIALIZER.registerContent();
        ModParticles.PARTICLES.registerContent();
        ModAttributes.ATTRIBUTES.registerContent();
        ModFeatures.register();
        ModFeatures.registerToBiomes((dec, holder) -> BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), dec, holder.unwrapKey().orElseThrow()));
    }

    public void setup() {
        ModEntities.registeredAttributes().forEach(FabricDefaultAttributeRegistry::register);
        AdvancementRegister.init();
        ServerPacketHandler.registerServer();
    }
}
