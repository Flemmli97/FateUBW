package io.github.flemmli97.fate.data;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.registry.FateTags;
import io.github.flemmli97.fate.common.registry.ModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagGen extends ItemTagsProvider {

    public ItemTagGen(DataGenerator generator, BlockTagsProvider provider, ExistingFileHelper existingFileHelper) {
        super(generator, provider, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(FateTags.crystals)
                .addItemEntry(ModItems.crystalEarth.get())
                .addItemEntry(ModItems.crystalWater.get())
                .addItemEntry(ModItems.crystalVoid.get())
                .addItemEntry(ModItems.crystalFire.get())
                .addItemEntry(ModItems.crystalWind.get());
    }
}
