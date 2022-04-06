package io.github.flemmli97.fateubw.api.datagen;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.List;

public class GrailLootBuilder {

    private final String name;
    private List<GrailLootEntry<?>> pools = new ArrayList<>();
    private List<LootItemCondition> conditions = new ArrayList<>();

    private GrailLootBuilder(String name) {
        this.name = name;
    }

    public static GrailLootBuilder create(String name) {
        return new GrailLootBuilder(name);
    }

    public GrailLootBuilder addEntry(GrailLootEntry<?> entry) {
        this.pools.add(entry);
        return this;
    }

    public GrailLootBuilder addCondition(LootItemCondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public GrailLootTable build() {
        return new GrailLootTable(this.name, ImmutableList.copyOf(this.pools), this.conditions.toArray(new LootItemCondition[0]));
    }
}
