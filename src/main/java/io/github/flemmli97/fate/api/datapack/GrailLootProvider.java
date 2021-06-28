package io.github.flemmli97.fate.api.datapack;

import com.google.gson.JsonElement;
import io.github.flemmli97.fate.common.loot.GrailLootTable;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class GrailLootProvider implements IDataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<ResourceLocation, GrailLootTable> data = new HashMap<>();

    private final DataGenerator gen;

    public GrailLootProvider(DataGenerator gen) {
        this.gen = gen;
    }

    protected abstract void add();

    @Override
    public void act(DirectoryCache cache) {
        this.add();
        this.data.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/grail_loot_tables/" + res.getPath() + ".json");
            try {
                JsonElement obj = GrailLootTable.GSON.toJsonTree(builder);
                IDataProvider.save(GrailLootTable.GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save grail loot table {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "GrailLootTables";
    }

    public void addLootTable(ResourceLocation res, GrailLootBuilder builder) {
        this.data.put(res, builder.build());
    }
}