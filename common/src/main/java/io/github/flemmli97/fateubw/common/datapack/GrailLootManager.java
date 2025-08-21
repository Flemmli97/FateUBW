package io.github.flemmli97.fateubw.common.datapack;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class GrailLootManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = Fate.modRes("grail_loot_tables");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, GrailLootTable> lootTables = ImmutableMap.of();

    private HolderLookup.Provider provider;

    public GrailLootManager() {
        super(GSON, DIRECTORY);
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
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((res, el) -> {
            try {
                GrailLootTable table = GrailLootTable.CODEC.parse(ops, el).getOrThrow();
                if (!table.isEmpty())
                    builder.put(res, table);
            } catch (Exception ex) {
                Fate.LOGGER.error("Couldn't parse grail loottable json {}", res, ex);
            }
        });
        this.lootTables = builder.build();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }
}
