package io.github.flemmli97.fateubw.forge.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.datapack.ServantPropManager;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
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

public record ServantPropGen(DataGenerator gen) implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void run(HashCache cache) {
        Map<ResourceLocation, ServantProperties> props = new HashMap<>(ModEntities.getDefaultMobProperties());
        props.forEach((res, prop) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + ServantPropManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = ServantProperties.CODEC.encodeStart(JsonOps.INSTANCE, prop)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save entity properties {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "EntityProperties";
    }
}