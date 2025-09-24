package io.github.flemmli97.fateubw.neoforge.data.tags;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

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
        this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE)
                .addTag(FateTags.Items.MOD_SPEARS);
        this.tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTag(FateTags.Items.MOD_SPEARS);
        this.tag(ItemTags.TRIDENT_ENCHANTABLE)
                .addTag(FateTags.Items.MOD_SPEARS);

        this.tag(ItemTags.SWORDS)
                .add(FateItems.INVISEXCALIBUR.get())
                .add(FateItems.EXCALIBUR.get())
                .add(FateItems.KANSHOU.get())
                .add(FateItems.BAKUYA.get())
                .add(FateItems.BAKUYA.get())
                .add(FateItems.ENUMAELISH.get())
                .add(FateItems.RULE_BREAKER.get())
                .add(FateItems.ARONDIGHT.get())
                .add(FateItems.KUPRIOTS.get())
                .add(FateItems.MEDUSA_DAGGER.get())
                .add(FateItems.ASSASSIN_DAGGER.get())
                .add(FateItems.MONOHOSHI_ZAO.get());
        this.tag(ItemTags.AXES)
                .add(FateItems.HERACLES_AXE.get());
        this.tag(Tags.Items.TOOLS_BOW)
                .add(FateItems.EMIYAS_BOW.get());
        this.tag(ItemTags.BOW_ENCHANTABLE)
                .add(FateItems.EMIYAS_BOW.get());
        this.tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(FateTags.Items.MOD_SPEARS)
                .add(FateItems.EMIYAS_BOW.get());
    }
}
