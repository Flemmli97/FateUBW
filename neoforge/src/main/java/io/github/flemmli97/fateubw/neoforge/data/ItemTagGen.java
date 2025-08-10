package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class ItemTagGen extends ItemTagsProvider {

    public ItemTagGen(DataGenerator generator, BlockTagsProvider provider, ExistingFileHelper existingFileHelper) {
        super(generator, provider, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(FateTags.CRYSTALS)
                .add(FateItems.CRYSTAL_YELLOW.get())
                .add(FateItems.CRYSTAL_BLUE.get())
                .add(FateItems.CRYSTAL_BLACK.get())
                .add(FateItems.CRYSTAL_RED.get())
                .add(FateItems.CRYSTAL_GREEN.get());
        TagAppender<Item> dyes = this.tag(FateTags.FABRIC_DYE_TAG)
                .addOptional(Tags.Items.DYES.location());
        this.add(dyes::add);
        this.tag(FateTags.FABRIC_DYE_RED)
                .add(Items.RED_DYE)
                .addOptional(Tags.Items.DYES_RED.location());
        this.tag(FateTags.FABRIC_STICK_TAG)
                .add(Items.STICK)
                .addOptional(Tags.Items.RODS_WOODEN.location());
        this.tag(FateTags.FABRIC_LAPIS_BLOCK)
                .add(Items.LAPIS_BLOCK)
                .addOptional(Tags.Items.STORAGE_BLOCKS_LAPIS.location());
        this.tag(FateTags.FABRIC_DIAMOND_BLOCK)
                .add(Items.DIAMOND_BLOCK)
                .addOptional(Tags.Items.STORAGE_BLOCKS_DIAMOND.location());
    }

    private void add(Consumer<Item> consumer) {
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation key = new ResourceLocation("minecraft", "{color}_dye".replace("{color}", color.getName()));
            Item item = ForgeRegistries.ITEMS.getValue(key);
            consumer.accept(item);
        }
    }
}
