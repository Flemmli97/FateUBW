package io.github.flemmli97.fateubw.neoforge.data.tags;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BiomeTagGen extends TagsProvider<Biome> {

    public BiomeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, ExistingFileHelper existingFileHelper) {
        super(output, Registries.BIOME, completableFuture, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(FateTags.Biomes.FATE_ORE_GEN)
                .addTag(BiomeTags.IS_OVERWORLD);
    }

    @SafeVarargs
    protected final void tag(ResourceKey<Biome> key, TagKey<Biome>... tags) {
        for (TagKey<Biome> tag : tags) {
            this.tag(tag).add(key);
        }
    }
}
