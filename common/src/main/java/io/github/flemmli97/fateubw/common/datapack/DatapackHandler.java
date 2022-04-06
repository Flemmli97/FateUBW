package io.github.flemmli97.fateubw.common.datapack;

import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class DatapackHandler {

    public static final GrailLootManager lootTables = new GrailLootManager();

    public static Optional<GrailLootTable> getLootTable(ResourceLocation res) {
        return Optional.ofNullable(lootTables.get(res));
    }

    public static Collection<ResourceLocation> getAllTables() {
        return lootTables.getAll();
    }

    public static Map<ResourceLocation, String> getTablesForClient() {
        return lootTables.clientTableMap();
    }
}
