package io.github.flemmli97.fateubw.common.datapack;

import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class DatapackHandler {

    public static final GrailLootManager LOOT_TABLES = new GrailLootManager();

    public static Optional<GrailLootTable> getLootTable(ResourceLocation res) {
        return Optional.ofNullable(LOOT_TABLES.get(res));
    }

    public static Collection<ResourceLocation> getAllTables() {
        return LOOT_TABLES.getAll();
    }

    public static Map<ResourceLocation, String> getTablesForClient() {
        return LOOT_TABLES.clientTableMap();
    }
}
