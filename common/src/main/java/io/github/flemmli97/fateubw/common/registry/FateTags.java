package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FateTags {

    public static TagKey<Item> crystals = PlatformUtils.INSTANCE.itemTag(new ResourceLocation(Fate.MODID, "crystals"));
    public static TagKey<Item> fabricDyeTag = PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", "dye_any"));
    public static TagKey<Item> fabricStickTag = PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", "wood_sticks"));
    public static TagKey<Item> fabricLapisBlock = PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", "diamond_blocks"));
    public static TagKey<Item> fabricDiamondBlock = PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", "lapis_blocks"));

}
