package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.BiConsumer;

public class ModFeatures {

    public static Holder<ConfiguredFeature<?, ?>> charmFeature;
    public static Holder<ConfiguredFeature<?, ?>> gemFeature;

    public static Holder<PlacedFeature> charmPlacedFeature;
    public static Holder<PlacedFeature> gemPlacedFeature;

    public static void register() {
        charmFeature = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Fate.MODID, "charm_ore"),
                new ConfiguredFeature<>(OreFeature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.artifactOre.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.deepSlateArtifactOre.get().defaultBlockState())), 2)));
        gemFeature = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Fate.MODID, "gem_ores"),
                new ConfiguredFeature<>(OreFeature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.gemOre.get().defaultBlockState()),
                        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.deepSlateGemOre.get().defaultBlockState())), 9)));

        charmPlacedFeature = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(Fate.MODID, "charm_ore"), new PlacedFeature(charmFeature,
                List.of(CountPlacement.of(4),
                        InSquarePlacement.spread(),
                        BiomeFilter.biome(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-80), VerticalAnchor.absolute(32)))));
        gemPlacedFeature = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(Fate.MODID, "gem_ores"), new PlacedFeature(gemFeature,
                List.of(CountPlacement.of(9),
                        InSquarePlacement.spread(),
                        BiomeFilter.biome(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-33), VerticalAnchor.absolute(50)))));
    }

    public static void registerToBiomes(BiConsumer<GenerationStep.Decoration, Holder<PlacedFeature>> cons) {
        cons.accept(GenerationStep.Decoration.UNDERGROUND_ORES, charmPlacedFeature);
        cons.accept(GenerationStep.Decoration.UNDERGROUND_ORES, gemPlacedFeature);
    }
}
