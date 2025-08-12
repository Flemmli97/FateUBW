package io.github.flemmli97.fateubw.neoforge.data.tags;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ItemTagGen extends ItemTagsProvider {

    public ItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, Fate.MODID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(FateTags.Items.CRYSTALS)
                .add(FateItems.CRYSTAL_YELLOW.get())
                .add(FateItems.CRYSTAL_BLUE.get())
                .add(FateItems.CRYSTAL_BLACK.get())
                .add(FateItems.CRYSTAL_RED.get())
                .add(FateItems.CRYSTAL_GREEN.get());

        this.tag(FateTags.Items.MOD_SPEARS)
                .add(FateItems.GAEBOLG.get())
                .add(FateItems.GAEBUIDHE.get())
                .add(FateItems.GAEDEARG.get());
        this.tag(FateTags.Items.SPEARS)
                .addTag(FateTags.Items.MOD_SPEARS);
        this.tag(ItemTags.SWORD_ENCHANTABLE)
                .addTag(FateTags.Items.MOD_SPEARS);
        this.tag(ItemTags.TRIDENT_ENCHANTABLE)
                .addTag(FateTags.Items.MOD_SPEARS);
    }
}
