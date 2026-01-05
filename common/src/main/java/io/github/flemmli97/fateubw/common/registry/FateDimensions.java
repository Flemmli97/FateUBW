package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class FateDimensions {

    public static final DimensionKeys SAND_DUNES = new DimensionKeys(Fate.modRes("sand_dunes"));
    public static final ResourceKey<Biome> SAND_DUNES_BIOME = ResourceKey.create(Registries.BIOME, Fate.modRes("sand_dunes"));
    public static final DimensionKeys UNLIMITED_BLADEWORKS = new DimensionKeys(Fate.modRes("unlimited_blade_works"));
    public static final ResourceKey<Biome> UNLIMITED_BLADEWORKS_BIOME = ResourceKey.create(Registries.BIOME, Fate.modRes("unlimited_blade_works"));

    public record DimensionKeys(ResourceKey<NoiseGeneratorSettings> noiseSettings,
                                ResourceKey<DimensionType> dimensionType, ResourceKey<LevelStem> stem,
                                ResourceKey<Level> dimension) {

        public DimensionKeys(ResourceLocation id) {
            this(ResourceKey.create(Registries.NOISE_SETTINGS, id), ResourceKey.create(Registries.DIMENSION_TYPE, id), ResourceKey.create(Registries.LEVEL_STEM, id), ResourceKey.create(Registries.DIMENSION, id));
        }
    }
}
