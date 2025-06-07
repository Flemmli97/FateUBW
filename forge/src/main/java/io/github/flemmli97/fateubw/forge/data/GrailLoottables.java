package io.github.flemmli97.fateubw.forge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.GrailLootBuilder;
import io.github.flemmli97.fateubw.api.datapack.provider.GrailLootProvider;
import io.github.flemmli97.fateubw.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fateubw.common.loot.entry.ServantEntry;
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
        this.addLootTable(new ResourceLocation(Fate.MODID, "valuables"), GrailLootBuilder.create("fateubw.loot.valuables")
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

        this.addLootTable(new ResourceLocation(Fate.MODID, "rare_dungeon"), GrailLootBuilder.create("fateubw.loot.rare_dungeon")
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

        this.addLootTable(new ResourceLocation(Fate.MODID, "xp"), GrailLootBuilder.create("fateubw.loot.knowledge")
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

        this.addLootTable(new ResourceLocation(Fate.MODID, "power"), GrailLootBuilder.create("fateubw.loot.power")
                .addEntry(new AttributeEntry(Attributes.MAX_HEALTH, UniformGenerator.between(1, 2)))
                .addEntry(new AttributeEntry(Attributes.ATTACK_DAMAGE, UniformGenerator.between(0.2f, 0.4f))));

        this.addLootTable(new ResourceLocation(Fate.MODID, "armor"), GrailLootBuilder.create("fateubw.loot.armor")
                .addEntry(new AttributeEntry(Attributes.MAX_HEALTH, UniformGenerator.between(0, 2)))
                .addEntry(new AttributeEntry(Attributes.ARMOR, UniformGenerator.between(0.3f, 0.5f)))
                .addEntry(new AttributeEntry(Attributes.ARMOR_TOUGHNESS, UniformGenerator.between(0.2f, 0.5f))));

        this.addLootTable(new ResourceLocation(Fate.MODID, "resummon"), GrailLootBuilder.create("fateubw.loot.resummon")
                .addEntry(new ServantEntry(false)));

        this.addLootTable(new ResourceLocation(Fate.MODID, "servant_loot"), GrailLootBuilder.create("fateubw.loot.servant_loot")
                .addEntry(new ServantEntry(true)));
    }
}
