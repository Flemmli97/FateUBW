package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.Fate;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class FateTags {

    public static Tags.IOptionalNamedTag<Item> crystals = ItemTags.createOptional(new ResourceLocation(Fate.MODID, "crystals"));

}
