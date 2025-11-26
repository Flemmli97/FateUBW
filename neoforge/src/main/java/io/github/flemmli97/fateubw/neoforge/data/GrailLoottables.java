package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.GrailLootBuilder;
import io.github.flemmli97.fateubw.api.datapack.provider.GrailLootProvider;
import io.github.flemmli97.fateubw.api.loot.entry.AttributeEntry;
import io.github.flemmli97.fateubw.api.loot.entry.LootTableEntry;
import io.github.flemmli97.fateubw.api.loot.entry.ServantEntry;
import io.github.flemmli97.fateubw.api.loot.entry.VanillaItemEntry;
import io.github.flemmli97.fateubw.api.loot.entry.XPEntry;
import io.github.flemmli97.fateubw.api.loot.function.EnchantMaxFunction;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GrailLoottables extends GrailLootProvider {

    public GrailLoottables(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Fate.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.addLootTable(Fate.modRes("grails_blessing"), GrailLootBuilder.create("fateubw.loot.grails_blessing")
                .addEntry(new XPEntry(UniformGenerator.between(9000, 15000)))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.GOLD_INGOT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(32, 64)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.GOLD_BLOCK)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.DIAMOND)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.NETHERITE_INGOT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(FateItems.MANA_BOTTLE.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantWithLevelsFunction.enchantWithLevels(provider, UniformGenerator.between(25, 30)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantWithLevelsFunction.enchantWithLevels(provider, UniformGenerator.between(25, 30)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantWithLevelsFunction.enchantWithLevels(provider, UniformGenerator.between(25, 30)))
                        .build())));

        this.addLootTable(Fate.modRes("explorers_dream"), GrailLootBuilder.create("fateubw.loot.explorers_dream")
                .addEntry(new XPEntry(UniformGenerator.between(10000, 17000)))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.DIAMOND)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.HEART_OF_THE_SEA)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.PIGLIN_BANNER_PATTERN)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.MUSIC_DISC_PIGSTEP)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.ELYTRA)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                        .build()))
                .addEntry(new LootTableEntry(List.of(
                        ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("chests/end_city_treasure")),
                        ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("chests/buried_treasure")),
                        ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("chests/shipwreck_treasure"))))));

        this.addLootTable(Fate.modRes("grail_empowerment"), GrailLootBuilder.create("fateubw.loot.grail_empowerment")
                .addEntry(new XPEntry(UniformGenerator.between(5000, 9000)))
                .addEntry(new AttributeEntry(Attributes.MAX_HEALTH, 20, UniformGenerator.between(1, 2)))
                .addEntry(new AttributeEntry(Attributes.ATTACK_DAMAGE, 5, UniformGenerator.between(0.25f, 0.5f)))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantMaxFunction.builder(provider)
                                .withEnchantment(Enchantments.SHARPNESS, Enchantments.UNBREAKING,
                                        Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.IMPALING))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantMaxFunction.builder(provider)
                                .withEnchantment(Enchantments.SHARPNESS, Enchantments.UNBREAKING, Enchantments.SMITE,
                                        Enchantments.BANE_OF_ARTHROPODS, Enchantments.IMPALING))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantMaxFunction.builder(provider)
                                .withEnchantment(Enchantments.SHARPNESS, Enchantments.UNBREAKING, Enchantments.SMITE,
                                        Enchantments.BANE_OF_ARTHROPODS, Enchantments.IMPALING))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantMaxFunction.builder(provider)
                                .withEnchantment(Enchantments.SHARPNESS, Enchantments.UNBREAKING, Enchantments.SMITE,
                                        Enchantments.BANE_OF_ARTHROPODS, Enchantments.IMPALING))
                        .build())));

        this.addLootTable(Fate.modRes("divine_protection"), GrailLootBuilder.create("fateubw.loot.divine_protection")
                .addEntry(new XPEntry(UniformGenerator.between(5000, 9000)))
                .addEntry(new AttributeEntry(Attributes.MAX_HEALTH, 20, UniformGenerator.between(0, 2)))
                .addEntry(new AttributeEntry(Attributes.ARMOR, 5, UniformGenerator.between(0.25f, 0.5f)))
                .addEntry(new AttributeEntry(Attributes.ARMOR_TOUGHNESS, 5, UniformGenerator.between(0.2f, 0.4f)))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantMaxFunction.builder(provider)
                                .withEnchantment(Enchantments.PROTECTION, Enchantments.UNBREAKING, Enchantments.FEATHER_FALLING, Enchantments.PROJECTILE_PROTECTION,
                                        Enchantments.FIRE_PROTECTION, Enchantments.BLAST_PROTECTION))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantMaxFunction.builder(provider)
                                .withEnchantment(Enchantments.PROTECTION, Enchantments.UNBREAKING, Enchantments.FEATHER_FALLING, Enchantments.PROJECTILE_PROTECTION,
                                        Enchantments.FIRE_PROTECTION, Enchantments.BLAST_PROTECTION))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantMaxFunction.builder(provider)
                                .withEnchantment(Enchantments.PROTECTION, Enchantments.UNBREAKING, Enchantments.FEATHER_FALLING, Enchantments.PROJECTILE_PROTECTION,
                                        Enchantments.FIRE_PROTECTION, Enchantments.BLAST_PROTECTION))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantMaxFunction.builder(provider)
                                .withEnchantment(Enchantments.PROTECTION, Enchantments.UNBREAKING, Enchantments.FEATHER_FALLING, Enchantments.PROJECTILE_PROTECTION,
                                        Enchantments.FIRE_PROTECTION, Enchantments.BLAST_PROTECTION))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.BOOK)
                        .apply(EnchantMaxFunction.builder(provider)
                                .withEnchantment(Enchantments.PROTECTION, Enchantments.UNBREAKING, Enchantments.FEATHER_FALLING, Enchantments.PROJECTILE_PROTECTION,
                                        Enchantments.FIRE_PROTECTION, Enchantments.BLAST_PROTECTION))
                        .build())));

        this.addLootTable(Fate.modRes("eternal_pact"), GrailLootBuilder.create("fateubw.loot.eternal_pact")
                .addEntry(new XPEntry(UniformGenerator.between(5000, 9000)))
                .addEntry(new ServantEntry(false))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.SPLASH_POTION)
                        .apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.SPLASH_POTION)
                        .apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.SPLASH_POTION)
                        .apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.SPLASH_POTION)
                        .apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.SPLASH_POTION)
                        .apply(SetPotionFunction.setPotion(Potions.STRONG_STRENGTH))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.SPLASH_POTION)
                        .apply(SetPotionFunction.setPotion(Potions.STRONG_STRENGTH))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.SPLASH_POTION)
                        .apply(SetPotionFunction.setPotion(Potions.STRONG_STRENGTH))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                        .build())));

        this.addLootTable(Fate.modRes("legendary_armaments"), GrailLootBuilder.create("fateubw.loot.legendary_armaments")
                .addEntry(new XPEntry(UniformGenerator.between(5000, 9000)))
                .addEntry(new ServantEntry(true))
                .addEntry(new AttributeEntry(Attributes.ARMOR, 3, UniformGenerator.between(0.2f, 0.4f)))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.DIAMOND_HELMET)
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE)
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS)
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.DIAMOND_BOOTS)
                        .build())));
    }
}
