package io.github.flemmli97.fateubw.api.datapack.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.GrailLootBuilder;
import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class GrailLootProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, GrailLootTable> data = new HashMap<>();

    private final DataGenerator gen;

    public GrailLootProvider(DataGenerator gen) {
        this.gen = gen;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.data.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/grail_loot_tables/" + res.getPath() + ".json");
            try {
                JsonElement obj = GrailLootTable.CODEC.encodeStart(JsonOps.INSTANCE, builder).getOrThrow(false, Fate.LOGGER::error);
                DataProvider.save(GSON, cache, obj, path);
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