package io.github.flemmli97.fateubw.forge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
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
                .add(ModBlocks.gemOre.get(), ModBlocks.artifactOre.get(), ModBlocks.deepSlateGemOre.get(), ModBlocks.deepSlateArtifactOre.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.gemOre.get(), ModBlocks.artifactOre.get(), ModBlocks.deepSlateGemOre.get(), ModBlocks.deepSlateArtifactOre.get());
    }
}
