package com.flemmli97.fate.data;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.registry.FateTags;
import com.flemmli97.fate.common.registry.ModItems;
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
        this.getOrCreateTagBuilder(FateTags.crystals)
                .add(ModItems.crystalEarth.get())
                .add(ModItems.crystalWater.get())
                .add(ModItems.crystalVoid.get())
                .add(ModItems.crystalFire.get())
                .add(ModItems.crystalWind.get());
    }
}