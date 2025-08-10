package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagGen extends BlockTagsProvider {

    public BlockTagGen(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, Fate.MODID, existingFileHelper);
    }

    @Override
    public void addTags() {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(FateBlocks.GEM_ORE.get(), FateBlocks.ARTIFACT_ORE.get(), FateBlocks.DEEP_SLATE_GEM_ORE.get(), FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(FateBlocks.GEM_ORE.get(), FateBlocks.ARTIFACT_ORE.get(), FateBlocks.DEEP_SLATE_GEM_ORE.get(), FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get());
    }
}
