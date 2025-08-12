package io.github.flemmli97.fateubw.neoforge.data.tags;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class BlockTagGen extends IntrinsicHolderTagsProvider<Block> {

    public BlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, net.neoforged.neoforge.common.data.ExistingFileHelper fileHelper) {
        super(output, Registries.BLOCK, lookupProvider, block -> BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow(), Fate.MODID, fileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(FateBlocks.GEM_ORE.get(), FateBlocks.ARTIFACT_ORE.get(), FateBlocks.DEEP_SLATE_GEM_ORE.get(), FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(FateBlocks.GEM_ORE.get(), FateBlocks.ARTIFACT_ORE.get(), FateBlocks.DEEP_SLATE_GEM_ORE.get(), FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get());
    }
}
