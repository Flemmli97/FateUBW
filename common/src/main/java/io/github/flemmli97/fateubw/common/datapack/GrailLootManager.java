package io.github.flemmli97.fateubw.common.datapack;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class GrailLootManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, GrailLootTable> lootTables = ImmutableMap.of();

    public GrailLootManager() {
        super(GSON, "grail_loot_tables");
    }

    public GrailLootTable get(ResourceLocation res) {
        return this.lootTables.get(res);
    }

    public Collection<ResourceLocation> getAll() {
        return this.lootTables.keySet();
    }

    public Map<ResourceLocation, Component> clientTableMap() {
        return this.lootTables.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().name));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, GrailLootTable> builder = ImmutableMap.builder();
        data.forEach((res, el) -> {
            try {
                GrailLootTable table = GrailLootTable.CODEC.parse(JsonOps.INSTANCE, el)
                        .getOrThrow(false, Fate.LOGGER::error);
                if (!table.isEmpty())
                    builder.put(res, table);
            } catch (JsonSyntaxException | IllegalStateException ex) {
                Fate.LOGGER.error("Couldn't parse grail loottable json {}", res, ex);
            }
        });
        this.lootTables = builder.build();
    }

}
