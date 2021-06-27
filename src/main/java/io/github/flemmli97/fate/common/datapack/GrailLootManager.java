package io.github.flemmli97.fate.common.datapack;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.loot.GrailLootTable;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class GrailLootManager extends JsonReloadListener {

    private Map<ResourceLocation, GrailLootTable> lootTables = ImmutableMap.of();

    public GrailLootManager() {
        super(GrailLootTable.GSON, "grail_loot_tables");
    }

    public GrailLootTable get(ResourceLocation res) {
        return this.lootTables.get(res);
    }

    public Collection<ResourceLocation> getAll() {
        return this.lootTables.keySet();
    }

    public Map<ResourceLocation, String> clientTableMap() {
        return this.lootTables.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().name));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, IResourceManager manager, IProfiler profiler) {
        ImmutableMap.Builder<ResourceLocation, GrailLootTable> builder = ImmutableMap.builder();
        data.forEach((res, el) -> {
            try {
                GrailLootTable table = GrailLootTable.GSON.fromJson(el, GrailLootTable.class);
                if (!table.isEmpty())
                    builder.put(res, table);
            } catch (JsonSyntaxException | IllegalStateException ex) {
                Fate.logger.error("Couldnt parse grail loottable json {}", res);
                ex.printStackTrace();
            }
        });
        this.lootTables = builder.build();
    }

}
