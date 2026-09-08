package io.github.flemmli97.fateubw.common.world.features.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record SwordDisplayFeatureConfig(
        List<WeightedEntry.Wrapper<ItemStack>> weapons) implements FeatureConfiguration {

    public static final Codec<SwordDisplayFeatureConfig> CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    WeightedEntry.Wrapper.codec(ItemStack.CODEC).listOf().fieldOf("weapons").forGetter(SwordDisplayFeatureConfig::weapons))
            .apply(codec, SwordDisplayFeatureConfig::new));
}
