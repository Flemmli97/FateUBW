package io.github.flemmli97.fate.data;

import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.api.datapack.GrailLootBuilder;
import io.github.flemmli97.fate.api.datapack.GrailLootProvider;
import io.github.flemmli97.fate.common.loot.entry.AstralEntry;
import io.github.flemmli97.fate.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fate.common.loot.entry.VanillaItemEntry;
import io.github.flemmli97.fate.common.loot.entry.XPEntry;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.conditions.TimeCheck;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GrailLoottables extends GrailLootProvider {

    public GrailLoottables(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void add() {
        this.addLootTable(new ResourceLocation(Fate.MODID, "valuables"), GrailLootBuilder.create("Riches")
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.IRON_INGOT)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(45, 64)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.GOLD_INGOT)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(32, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.DIAMOND)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(8, 14)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.EMERALD)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(17, 25)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.NETHERITE_INGOT)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(4, 7)))
                        .build())));

        this.addLootTable(new ResourceLocation(Fate.MODID, "xp"), GrailLootBuilder.create("Knowledge")
                .addEntry(new XPEntry(RandomValueRange.of(5000, 10000)))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.EXPERIENCE_BOTTLE)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(32, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.EXPERIENCE_BOTTLE)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(32, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.EXPERIENCE_BOTTLE)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(16, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(Items.EXPERIENCE_BOTTLE)
                        .acceptFunction(SetCount.builder(ConstantRange.of(64)))
                        .build())));

        this.addLootTable(new ResourceLocation(Fate.MODID, "power"), GrailLootBuilder.create("Power")
                .addEntry(new AttributeEntry(Attributes.MAX_HEALTH, RandomValueRange.of(1, 2)))
                .addEntry(new AttributeEntry(Attributes.ATTACK_DAMAGE, RandomValueRange.of(0.2f, 0.4f))));

        this.addLootTable(new ResourceLocation(Fate.MODID, "astral"), GrailLootBuilder.create("Astral")
                .addEntry(new AstralEntry(ConstantRange.of(1)))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(ItemsAS.AQUAMARINE)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(32, 48)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(ItemsAS.STARMETAL_INGOT)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(10, 16)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(ItemsAS.RESONATING_GEM)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(8, 12)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(ItemsAS.PERK_GEM_DAY)
                        .acceptFunction(SetCount.builder(ConstantRange.of(1)))
                        .acceptCondition(new TimeCheckBuilder(24000L, RandomValueRange.of(23658, 23999))
                                .alternative(new TimeCheckBuilder(24000L, RandomValueRange.of(0, 12342))))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(ItemsAS.PERK_GEM_DAY)
                        .acceptFunction(SetCount.builder(ConstantRange.of(1)))
                        .acceptCondition(new TimeCheckBuilder(24000L, RandomValueRange.of(23658, 23999))
                                .alternative(new TimeCheckBuilder(24000L, RandomValueRange.of(0, 12342))))
                        .acceptCondition(RandomChance.builder(0.5f))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(ItemsAS.PERK_GEM_NIGHT)
                        .acceptFunction(SetCount.builder(ConstantRange.of(1)))
                        .acceptCondition(new TimeCheckBuilder(24000L, RandomValueRange.of(14743, 21257)))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(ItemsAS.PERK_GEM_NIGHT)
                        .acceptFunction(SetCount.builder(ConstantRange.of(1)))
                        .acceptCondition(new TimeCheckBuilder(24000L, RandomValueRange.of(14743, 21257)))
                        .acceptCondition(RandomChance.builder(0.5f))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(ItemsAS.PERK_GEM_SKY)
                        .acceptFunction(SetCount.builder(ConstantRange.of(1)))
                        .acceptCondition(new TimeCheckBuilder(24000L, RandomValueRange.of(12343, 14742))
                                .alternative(new TimeCheckBuilder(24000L, RandomValueRange.of(21258, 23657))))
                        .build()))
                .addEntry(new VanillaItemEntry(ItemLootEntry.builder(ItemsAS.PERK_GEM_SKY)
                        .acceptFunction(SetCount.builder(ConstantRange.of(1)))
                        .acceptCondition(new TimeCheckBuilder(24000L, RandomValueRange.of(12343, 14742))
                                .alternative(new TimeCheckBuilder(24000L, RandomValueRange.of(21258, 23657))))
                        .acceptCondition(RandomChance.builder(0.5f))
                        .build())));
    }

    /**
     * Cause vanilla doesnt have one... why mojang why...
     */
    private static class TimeCheckBuilder implements ILootCondition.IBuilder {

        private final Long mod;
        private final RandomValueRange range;

        public TimeCheckBuilder(Long mod, RandomValueRange range) {
            this.mod = mod;
            this.range = range;
        }

        @Override
        public ILootCondition build() {
            return this.create(this.mod, this.range);
        }

        private TimeCheck create(Long mod, RandomValueRange range) {
            try {
                Constructor<TimeCheck> constructor = TimeCheck.class.getDeclaredConstructor(Long.class, RandomValueRange.class);
                constructor.setAccessible(true);
                TimeCheck check = constructor.newInstance(mod, range);
                constructor.setAccessible(false);
                return check;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
