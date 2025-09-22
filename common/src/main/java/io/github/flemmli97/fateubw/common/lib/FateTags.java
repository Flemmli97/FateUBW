package io.github.flemmli97.fateubw.common.lib;

import io.github.flemmli97.fateubw.Fate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class FateTags {

    public static class Items {

        public static final TagKey<Item> CRYSTALS = tag("crystals");

        public static final TagKey<Item> SPEARS = tagCommon("tools/spears");
        public static final TagKey<Item> MOD_SPEARS = tag("spears");
    }

    public static class Blocks {

        public static final TagKey<Block> GENERIC_INCORRECT = TagKey.create(Registries.BLOCK, Fate.modRes("generic_incorrect_tool_tag"));
    }

    public static class EntityTypes {

        public static final TagKey<EntityType<?>> SERVANT = TagKey.create(Registries.ENTITY_TYPE, Fate.modRes("servant"));
        public static final TagKey<EntityType<?>> STRONG_MOB = TagKey.create(Registries.ENTITY_TYPE, Fate.modRes("strong_mob"));
        public static final TagKey<EntityType<?>> MANA_LEECHING_PROJECTILE = TagKey.create(Registries.ENTITY_TYPE, Fate.modRes("mana_leeching_projectile"));
        public static final TagKey<EntityType<?>> MANA_LEECHING_SUMMONS = TagKey.create(Registries.ENTITY_TYPE, Fate.modRes("mana_leeching_summons"));
    }

    public static class DamageTypes {

        public static final TagKey<DamageType> IS_MAGIC = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("c", "is_magic"));
    }

    public static class Biomes {

        public static final TagKey<Biome> FATE_ORE_GEN = TagKey.create(Registries.BIOME, Fate.modRes("ore_generation"));
    }

    public static TagKey<Item> tagCommon(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    public static TagKey<Item> tag(String name) {
        return TagKey.create(Registries.ITEM, Fate.modRes(name));
    }
}
