package io.github.flemmli97.fate.common.datapack;

import io.github.flemmli97.fate.common.loot.GrailLootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class DatapackHandler {

    private static GrailLootManager lootTables;

    public static void registerDataPackHandler(AddReloadListenerEvent event) {
        event.addListener(lootTables = new GrailLootManager());
    }

    @Nullable
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
