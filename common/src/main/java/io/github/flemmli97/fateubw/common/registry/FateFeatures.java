package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.world.features.SwordDisplayFeature;
import io.github.flemmli97.fateubw.common.world.features.config.SwordDisplayFeatureConfig;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
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
import java.util.function.Consumer;
import java.util.function.Function;

public class FateFeatures {

    public static final LoaderRegister<Feature<?>> FEATURES = LoaderRegistryAccess.INSTANCE.of(Registries.FEATURE, Fate.MODID);

    public static final RegistryEntrySupplier<Feature<?>, Feature<SwordDisplayFeatureConfig>> SWORD_FEATURE = FEATURES.register("sword_feature", () -> new SwordDisplayFeature(SwordDisplayFeatureConfig.CODEC));

    public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_CLASS_ARTIFACT_ORE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Fate.modRes("class_artifact_ore"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_GEM_ORES = ResourceKey.create(Registries.CONFIGURED_FEATURE, Fate.modRes("gem_ores"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_UBW_SWORDS = ResourceKey.create(Registries.CONFIGURED_FEATURE, Fate.modRes("ubw_swords"));

    public static final ResourceKey<PlacedFeature> CLASS_ARTIFACT_ORE = ResourceKey.create(Registries.PLACED_FEATURE, Fate.modRes("class_artifact_ore"));
    public static final ResourceKey<PlacedFeature> GEM_ORES = ResourceKey.create(Registries.PLACED_FEATURE, Fate.modRes("gem_ores"));
    public static final ResourceKey<PlacedFeature> UBW_SWORDS = ResourceKey.create(Registries.PLACED_FEATURE, Fate.modRes("ubw_swords"));

    public static void createFeatures(@Nullable FeatureRegister register,
                                      Consumer<FeatureBiomeModifier> placedFeatureHandler) {
        if (register != null) {
            register.registerConfigured(FateFeatures.CONFIGURED_CLASS_ARTIFACT_ORE, provider -> new ConfiguredFeature<>(OreFeature.ORE,
                    new OreConfiguration(List.of(OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), FateBlocks.ARTIFACT_ORE.get().defaultBlockState()),
                            OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get().defaultBlockState())), 2)));
            register.registerConfigured(FateFeatures.CONFIGURED_GEM_ORES, provider -> new ConfiguredFeature<>(OreFeature.ORE,
                    new OreConfiguration(List.of(OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), FateBlocks.GEM_ORE.get().defaultBlockState()),
                            OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), FateBlocks.DEEP_SLATE_GEM_ORE.get().defaultBlockState())), 9)));
            register.registerPlaced(FateFeatures.CLASS_ARTIFACT_ORE, provider ->
                    new PlacedFeature(provider.get(FateFeatures.CONFIGURED_CLASS_ARTIFACT_ORE),
                            List.of(CountPlacement.of(4),
                                    InSquarePlacement.spread(),
                                    BiomeFilter.biome(),
                                    HeightRangePlacement.uniform(VerticalAnchor.absolute(-80), VerticalAnchor.absolute(32)))));
            register.registerPlaced(FateFeatures.GEM_ORES, provider ->
                    new PlacedFeature(provider.get(FateFeatures.CONFIGURED_GEM_ORES),
                            List.of(CountPlacement.of(9),
                                    InSquarePlacement.spread(),
                                    BiomeFilter.biome(),
                                    HeightRangePlacement.uniform(VerticalAnchor.absolute(-33), VerticalAnchor.absolute(50)))));
            register.registerConfigured(FateFeatures.CONFIGURED_UBW_SWORDS, provider -> new ConfiguredFeature<>(SWORD_FEATURE.get(),
                    new SwordDisplayFeatureConfig(List.of(WeightedEntry.wrap(new ItemStack(Items.IRON_SWORD), 3),
                            WeightedEntry.wrap(new ItemStack(Items.STONE_SWORD), 8),
                            WeightedEntry.wrap(new ItemStack(Items.GOLDEN_SWORD), 5),
                            WeightedEntry.wrap(new ItemStack(Items.DIAMOND_SWORD), 1)))));
            register.registerPlaced(FateFeatures.UBW_SWORDS, provider ->
                    new PlacedFeature(provider.get(FateFeatures.CONFIGURED_UBW_SWORDS),
                            List.of(InSquarePlacement.spread(),
                                    PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
        }
        placedFeatureHandler.accept(new FeatureBiomeModifier(FateTags.Biomes.FATE_ORE_GEN, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, FateFeatures.CLASS_ARTIFACT_ORE.location())));
        placedFeatureHandler.accept(new FeatureBiomeModifier(FateTags.Biomes.FATE_ORE_GEN, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, FateFeatures.GEM_ORES.location())));
    }

    public interface FeatureRegister {

        void registerConfigured(ResourceKey<ConfiguredFeature<?, ?>> id, Function<HolderGetterLookup, ConfiguredFeature<?, ?>> register);

        void registerPlaced(ResourceKey<PlacedFeature> id, Function<HolderGetterLookup, PlacedFeature> register);
    }

    public interface HolderGetterLookup {

        default <S> Holder<S> get(ResourceKey<S> key) {
            return this.lookup(key.registryKey())
                    .getOrThrow(key);
        }

        <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key);
    }

    public record FeatureBiomeModifier(TagKey<Biome> tag, GenerationStep.Decoration decoration,
                                       ResourceKey<PlacedFeature> placedFeature) {

    }
}
