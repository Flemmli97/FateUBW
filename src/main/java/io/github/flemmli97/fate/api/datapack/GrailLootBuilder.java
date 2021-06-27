package io.github.flemmli97.fate.api.datapack;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.fate.common.loot.GrailLootEntry;
import io.github.flemmli97.fate.common.loot.GrailLootTable;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.ArrayList;
import java.util.List;

public class GrailLootBuilder {

    private final String name;
    private List<GrailLootEntry<?>> pools = new ArrayList<>();
    private List<ILootCondition> conditions = new ArrayList<>();

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

    public GrailLootBuilder addCondition(ILootCondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public GrailLootTable build() {
        return new GrailLootTable(this.name, ImmutableList.copyOf(this.pools), this.conditions.toArray(new ILootCondition[0]));
    }
}
