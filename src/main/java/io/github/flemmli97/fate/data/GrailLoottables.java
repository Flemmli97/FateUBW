package io.github.flemmli97.fate.data;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.api.datapack.GrailLootBuilder;
import io.github.flemmli97.fate.api.datapack.GrailLootProvider;
import io.github.flemmli97.fate.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fate.common.loot.entry.VanillaItemEntry;
import io.github.flemmli97.fate.common.loot.entry.XPEntry;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

public class GrailLoottables extends GrailLootProvider {

    public GrailLoottables(DataGenerator gen, String modid) {
        super(gen, modid);
    }

    @Override
    protected void add() {
        this.addLootTable(new ResourceLocation(Fate.MODID, "valuables"), GrailLootBuilder.create("Riches")
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.DIAMOND)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(4, 7)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.GOLD_INGOT)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(14, 18)))
                        .build())));

        this.addLootTable(new ResourceLocation(Fate.MODID, "xp"), GrailLootBuilder.create("Knowledge")
                .addEntry(new XPEntry(RandomValueRange.of(4, 7)))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.EXPERIENCE_BOTTLE)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(32, 48)))
                        .build())));

        this.addLootTable(new ResourceLocation(Fate.MODID, "power"), GrailLootBuilder.create("Power")
                .addEntry(new AttributeEntry(Attributes.MAX_HEALTH, RandomValueRange.of(1, 2)))
                .addEntry(new AttributeEntry(Attributes.ATTACK_DAMAGE, RandomValueRange.of(1, 2))));
    }
}
