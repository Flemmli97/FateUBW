package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FateFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_CLASS_ARTIFACT_ORE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Fate.modRes("class_artifact_ore"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_GEM_ORES = ResourceKey.create(Registries.CONFIGURED_FEATURE, Fate.modRes("gem_ores"));

    public static final ResourceKey<PlacedFeature> CLASS_ARTIFACT_ORE = ResourceKey.create(Registries.PLACED_FEATURE, Fate.modRes("class_artifact_ore"));
    public static final ResourceKey<PlacedFeature> GEM_ORES = ResourceKey.create(Registries.PLACED_FEATURE, Fate.modRes("gem_ores"));

    public static void createFeatures(@Nullable FeatureRegister register,
                                      Consumer<FeatureBiomeModifier> placedFeatureHandler) {
        if (register != null) {
            register.registerConfigured(FateFeatures.CONFIGURED_CLASS_ARTIFACT_ORE.location(), provider -> new ConfiguredFeature<>(OreFeature.ORE,
                    new OreConfiguration(List.of(OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), FateBlocks.ARTIFACT_ORE.get().defaultBlockState()),
                            OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get().defaultBlockState())), 2)));
            register.registerConfigured(FateFeatures.CONFIGURED_GEM_ORES.location(), provider -> new ConfiguredFeature<>(OreFeature.ORE,
                    new OreConfiguration(List.of(OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), FateBlocks.GEM_ORE.get().defaultBlockState()),
                            OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), FateBlocks.DEEP_SLATE_GEM_ORE.get().defaultBlockState())), 9)));
            register.registerPlaced(FateFeatures.CLASS_ARTIFACT_ORE.location(), FateFeatures.CONFIGURED_CLASS_ARTIFACT_ORE.location(), (provider, configured) ->
                    new PlacedFeature(configured,
                            List.of(CountPlacement.of(4),
                                    InSquarePlacement.spread(),
                                    BiomeFilter.biome(),
                                    HeightRangePlacement.uniform(VerticalAnchor.absolute(-80), VerticalAnchor.absolute(32)))));
            register.registerPlaced(FateFeatures.GEM_ORES.location(), FateFeatures.CONFIGURED_GEM_ORES.location(), (provider, configured) ->
                    new PlacedFeature(configured,
                            List.of(CountPlacement.of(9),
                                    InSquarePlacement.spread(),
                                    BiomeFilter.biome(),
                                    HeightRangePlacement.uniform(VerticalAnchor.absolute(-33), VerticalAnchor.absolute(50)))));
        }
        placedFeatureHandler.accept(new FeatureBiomeModifier(FateTags.Biomes.FATE_ORE_GEN, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                FateFeatures.CLASS_ARTIFACT_ORE.location()));
        placedFeatureHandler.accept(new FeatureBiomeModifier(FateTags.Biomes.FATE_ORE_GEN, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                FateFeatures.GEM_ORES.location()));
    }

    public interface FeatureRegister {

        void registerConfigured(ResourceLocation id, Function<HolderLookup.Provider, ConfiguredFeature<?, ?>> feature);

        void registerPlaced(ResourceLocation id, ResourceLocation configuredID, BiFunction<HolderLookup.Provider, Holder<ConfiguredFeature<?, ?>>, PlacedFeature> placed);
    }

    public record FeatureBiomeModifier(TagKey<Biome> tag, GenerationStep.Decoration decoration,
                                       ResourceLocation placedFeature) {

    }
}
