package com.flemmli97.fate.data;

import com.flemmli97.fate.Fate;
import com.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ModelsResourceUtil;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (SpawnEgg egg : SpawnEgg.getEggs())
            this.withExistingParent(egg.getRegistryName().getPath(), ModelsResourceUtil.getMinecraftNamespacedItem("template_spawn_egg"));
    }
}
