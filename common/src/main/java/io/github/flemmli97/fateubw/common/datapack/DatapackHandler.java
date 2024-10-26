package io.github.flemmli97.fateubw.common.datapack;

import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class DatapackHandler {

    public static final GrailLootManager LOOT_TABLES = new GrailLootManager();
    public static final ServantPropManager SERVANT_PROPS = new ServantPropManager();

    public static Optional<GrailLootTable> getLootTable(ResourceLocation res) {
        return Optional.ofNullable(LOOT_TABLES.get(res));
    }

    public static ServantProperties getServantProp(EntityType<?> entityType) {
        return SERVANT_PROPS.get(entityType);
    }

    public static Collection<ResourceLocation> getAllTables() {
        return LOOT_TABLES.getAll();
    }

    public static Map<ResourceLocation, Component> getTablesForClient() {
        return LOOT_TABLES.clientTableMap();
    }
}
