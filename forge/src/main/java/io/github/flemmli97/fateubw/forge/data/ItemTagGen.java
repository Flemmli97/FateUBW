package io.github.flemmli97.fateubw.forge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateTags;
import io.github.flemmli97.fateubw.common.registry.ModItems;
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
        this.tag(FateTags.crystals)
                .add(ModItems.crystalEarth.get())
                .add(ModItems.crystalWater.get())
                .add(ModItems.crystalVoid.get())
                .add(ModItems.crystalFire.get())
                .add(ModItems.crystalWind.get());
        TagAppender<Item> dyes = this.tag(FateTags.fabricDyeTag)
                .addTag(Tags.Items.DYES);
        this.add(dyes::add);
        this.tag(FateTags.fabricStickTag)
                .add(Items.STICK)
                .addTag(Tags.Items.RODS_WOODEN);
        this.tag(FateTags.fabricLapisBlock)
                .add(Items.LAPIS_BLOCK)
                .addTag(Tags.Items.STORAGE_BLOCKS_LAPIS);
        this.tag(FateTags.fabricDiamondBlock)
                .add(Items.DIAMOND_BLOCK)
                .addTag(Tags.Items.STORAGE_BLOCKS_DIAMOND);
    }

    private void add(Consumer<Item> consumer) {
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation key = new ResourceLocation("minecraft", "{color}_dye".replace("{color}", color.getName()));
            Item item = ForgeRegistries.ITEMS.getValue(key);
            consumer.accept(item);
        }
    }
}
