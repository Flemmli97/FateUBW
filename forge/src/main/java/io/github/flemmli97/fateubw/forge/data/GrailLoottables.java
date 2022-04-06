package io.github.flemmli97.fateubw.forge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datagen.GrailLootBuilder;
import io.github.flemmli97.fateubw.api.datagen.GrailLootProvider;
import io.github.flemmli97.fateubw.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fateubw.common.loot.entry.VanillaItemEntry;
import io.github.flemmli97.fateubw.common.loot.entry.XPEntry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class GrailLoottables extends GrailLootProvider {

    public GrailLoottables(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void add() {
        this.addLootTable(new ResourceLocation(Fate.MODID, "valuables"), GrailLootBuilder.create("Riches")
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.IRON_INGOT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(45, 64)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.GOLD_INGOT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(32, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.DIAMOND)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 14)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.EMERALD)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(17, 25)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.NETHERITE_INGOT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7)))
                        .build())));

        this.addLootTable(new ResourceLocation(Fate.MODID, "rare_dungeon"), GrailLootBuilder.create("Rare Dungeon Loot")
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.DIAMOND)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 12)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.HEART_OF_THE_SEA)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(-1, 2)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.PIGLIN_BANNER_PATTERN)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.MUSIC_DISC_PIGSTEP)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.MUSIC_DISC_OTHERSIDE)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .build())));

        this.addLootTable(new ResourceLocation(Fate.MODID, "xp"), GrailLootBuilder.create("Knowledge")
                .addEntry(new XPEntry(UniformGenerator.between(5000, 10000)))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(32, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(32, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(16, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))
                        .build())));

        this.addLootTable(new ResourceLocation(Fate.MODID, "power"), GrailLootBuilder.create("Power")
                .addEntry(new AttributeEntry(Attributes.MAX_HEALTH, UniformGenerator.between(1, 2)))
                .addEntry(new AttributeEntry(Attributes.ATTACK_DAMAGE, UniformGenerator.between(0.2f, 0.4f))));

        this.addLootTable(new ResourceLocation(Fate.MODID, "armor"), GrailLootBuilder.create("Increased Armor")
                .addEntry(new AttributeEntry(Attributes.MAX_HEALTH, UniformGenerator.between(0, 2)))
                .addEntry(new AttributeEntry(Attributes.ARMOR, UniformGenerator.between(0.3f, 0.5f)))
                .addEntry(new AttributeEntry(Attributes.ARMOR_TOUGHNESS, UniformGenerator.between(0.2f, 0.5f))));

        /*this.addLootTable(new ResourceLocation(Fate.MODID, "astral"), GrailLootBuilder.create("Astral")
                .addEntry(new AstralEntry(ConstantValue.exactly(1)))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(ItemsAS.AQUAMARINE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(32, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(ItemsAS.STARMETAL_INGOT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 16)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(ItemsAS.RESONATING_GEM)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(ItemsAS.PERK_GEM_DAY)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(new TimeCheck.Builder(24000L, UniformGenerator.between(23658, 23999))
                                .or(new TimeCheck.Builder(24000L, UniformGenerator.between(0, 12342))))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(ItemsAS.PERK_GEM_DAY)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(new TimeCheck.Builder(24000L, UniformGenerator.between(23658, 23999))
                                .or(new TimeCheck.Builder(24000L, UniformGenerator.between(0, 12342))))
                        .when(LootItemRandomChanceCondition.randomChance(0.5f))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(ItemsAS.PERK_GEM_NIGHT)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(new TimeCheck.Builder(24000L, UniformGenerator.between(14743, 21257)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(ItemsAS.PERK_GEM_NIGHT)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(new TimeCheck.Builder(24000L, UniformGenerator.between(14743, 21257)))
                        .when(LootItemRandomChanceCondition.randomChance(0.5f))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(ItemsAS.PERK_GEM_SKY)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(new TimeCheck.Builder(IntRange.range(12343, 14742)).setPeriod(24000L)
                                .or(new TimeCheck.Builder(UniformGenerator.between(21258, 23657)).setPeriod(24000L)))
                        .build()))
                .addEntry(new VanillaItemEntry(LootItem.lootTableItem(ItemsAS.PERK_GEM_SKY)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(new TimeCheck.Builder(IntRange.range(12343, 14742)).setPeriod(24000L)
                                .or(new TimeCheck.Builder(IntRange.range(21258, 23657)).setPeriod(24000L)))
                        .when(LootItemRandomChanceCondition.randomChance(0.5f))
                        .build())));*/
    }
}
