package io.github.flemmli97.fateubw.neoforge.data.worldgen.dimension;

import io.github.flemmli97.fateubw.common.registry.FateDimensions;
import io.github.flemmli97.fateubw.common.world.chunk.UnlimitedBladeworksChunkGenerator;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class DimensionGen {

    public static void createDimensions(RegistrySetBuilder builder) {
        builder.add(Registries.BIOME, ctx -> {
            ctx.register(FateDimensions.SAND_DUNES_BIOME, new Biome.BiomeBuilder()
                    .hasPrecipitation(false)
                    .temperature(2)
                    .downfall(0)
                    .specialEffects(new BiomeSpecialEffects.Builder()
                            .waterColor(0x3F76E4)
                            .waterFogColor(0x050533)
                            .fogColor(0xC0D8FF)
                            .skyColor(0x6EB1FF)
                            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                            .build())
                    .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                    .generationSettings(new BiomeGenerationSettings.Builder(ctx.lookup(Registries.PLACED_FEATURE), ctx.lookup(Registries.CONFIGURED_CARVER))
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2).build())
                    .build());
            ctx.register(FateDimensions.UNLIMITED_BLADEWORKS_BIOME, new Biome.BiomeBuilder()
                    .hasPrecipitation(false)
                    .temperature(2)
                    .downfall(0)
                    .specialEffects(new BiomeSpecialEffects.Builder()
                            .waterColor(0x3F76E4)
                            .waterFogColor(0x050533)
                            .fogColor(0xC0D8FF)
                            .skyColor(0x945732)
                            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                            .build())
                    .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                    .generationSettings(new BiomeGenerationSettings.Builder(ctx.lookup(Registries.PLACED_FEATURE), ctx.lookup(Registries.CONFIGURED_CARVER)).build())
                    .build());
        });
        List<Consumer<BootstrapContext<NoiseGeneratorSettings>>> noiseSettings = new ArrayList<>();
        List<Consumer<BootstrapContext<DimensionType>>> dimensionTypes = new ArrayList<>();
        List<Consumer<BootstrapContext<LevelStem>>> stems = new ArrayList<>();
        createSimpleDimension(noiseSettings, dimensionTypes, stems, FateDimensions.SAND_DUNES, Blocks.SANDSTONE, Blocks.SAND, FateDimensions.SAND_DUNES_BIOME, NoiseBasedChunkGenerator::new, 6000);
        createSimpleDimension(noiseSettings, dimensionTypes, stems, FateDimensions.UNLIMITED_BLADEWORKS, Blocks.RED_SANDSTONE, Blocks.RED_SAND, FateDimensions.UNLIMITED_BLADEWORKS_BIOME, UnlimitedBladeworksChunkGenerator::new, 6000);
        builder.add(Registries.NOISE_SETTINGS, ctx -> noiseSettings.forEach(c -> c.accept(ctx)));
        builder.add(Registries.DIMENSION_TYPE, ctx -> dimensionTypes.forEach(c -> c.accept(ctx)));
        builder.add(Registries.LEVEL_STEM, ctx -> stems.forEach(c -> c.accept(ctx)));
    }

    private static void createSimpleDimension(List<Consumer<BootstrapContext<NoiseGeneratorSettings>>> noiseSettings, List<Consumer<BootstrapContext<DimensionType>>> dimensionTypes, List<Consumer<BootstrapContext<LevelStem>>> stems,
                                              FateDimensions.DimensionKeys keys, Block block, Block surface, ResourceKey<Biome> biome,
                                              BiFunction<BiomeSource, Holder<NoiseGeneratorSettings>, ChunkGenerator> generator, int time) {
        noiseSettings.add(ctx -> ctx.register(keys.noiseSettings(), createFlatSettingWith(ctx.lookup(Registries.DENSITY_FUNCTION), block, surface)));
        dimensionTypes.add(ctx -> ctx.register(keys.dimensionType(), new DimensionType(OptionalLong.of(time), false, false, true, false, 1,
                false, false, 0, 128, 128, BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 15,
                new DimensionType.MonsterSettings(true, false, ConstantInt.of(15), 15))));
        stems.add(ctx -> ctx.register(keys.stem(), new LevelStem(ctx.lookup(Registries.DIMENSION_TYPE).getOrThrow(keys.dimensionType()),
                generator.apply(new FixedBiomeSource(ctx.lookup(Registries.BIOME).getOrThrow(biome)),
                        ctx.lookup(Registries.NOISE_SETTINGS).getOrThrow(keys.noiseSettings())))));
    }

    private static NoiseGeneratorSettings createFlatSettingWith(HolderGetter<DensityFunction> densityFunctions, Block block, Block surface) {
        return new NoiseGeneratorSettings(new NoiseSettings(0, 128, 1, 2), block.defaultBlockState(), Blocks.WATER.defaultBlockState(),
                new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(),
                        DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(),
                        DensityFunctions.add(
                                DensityFunctions.yClampedGradient(58, 66, 1, -1),
                                new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(NoiseRouterData.RIDGES_FOLDED))
                        ).squeeze(),
                        DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero()),
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(2, false, 2, CaveSurface.FLOOR), SurfaceRules.state(surface.defaultBlockState())),
                        SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), SurfaceRules.state(Blocks.BEDROCK.defaultBlockState()))
                ),
                List.of(),
                0,
                false,
                true,
                true,
                false);
    }
}
