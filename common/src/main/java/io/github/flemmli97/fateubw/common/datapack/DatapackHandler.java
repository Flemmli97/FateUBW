package io.github.flemmli97.fateubw.common.datapack;

import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class DatapackHandler {

    public static final GrailLootManager LOOT_TABLES = new GrailLootManager();
    public static final ServantPropManager SERVANT_PROPS = new ServantPropManager();

    public static Optional<GrailLootTable> getLootTable(ResourceLocation res) {
        return Optional.ofNullable(LOOT_TABLES.get(res));
    }

    public static ServantProperties getServantProp(ResourceLocation entityTypeId) {
        return SERVANT_PROPS.get(entityTypeId);
    }

    public static Collection<ResourceLocation> getAllTables() {
        return LOOT_TABLES.getAll();
    }

    public static Map<ResourceLocation, Component> getTablesForClient() {
        return LOOT_TABLES.clientTableMap();
    }
}
