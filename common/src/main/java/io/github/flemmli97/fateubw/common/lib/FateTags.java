package io.github.flemmli97.fateubw.common.lib;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class FateTags {

    public static final TagKey<Item> CRYSTALS = PlatformUtils.INSTANCE.itemTag(new ResourceLocation(Fate.MODID, "crystals"));
    public static final TagKey<Item> FABRIC_DYE_TAG = PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", "dye_any"));
    public static final TagKey<Item> FABRIC_STICK_TAG = PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", "wood_sticks"));
    public static final TagKey<Item> FABRIC_LAPIS_BLOCK = PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", "lapis_blocks"));
    public static final TagKey<Item> FABRIC_DIAMOND_BLOCK = PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", "diamond_blocks"));

    public static final TagKey<EntityType<?>> SERVANT = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Fate.MODID, "servant"));
}
