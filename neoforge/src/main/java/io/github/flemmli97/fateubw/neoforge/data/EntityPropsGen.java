package io.github.flemmli97.fateubw.neoforge.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.datapack.EntityPropsManager;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public record EntityPropsGen(DataGenerator gen) implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void run(HashCache cache) {
        ModEntities.getServantProperties().forEach((res, prop) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + EntityPropsManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = ServantProperties.CODEC.encodeStart(JsonOps.INSTANCE, prop.build())
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save entity properties {}", path, e);
            }
        });
        ModEntities.getEntityProps().forEach((res, prop) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + EntityPropsManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = AttributeHolderProperties.CODEC.encodeStart(JsonOps.INSTANCE, prop.build())
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