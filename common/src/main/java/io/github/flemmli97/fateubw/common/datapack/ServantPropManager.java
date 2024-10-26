package io.github.flemmli97.fateubw.common.datapack;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ServantPropManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "servant_properties";
    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, ServantProperties> props = ImmutableMap.of();

    public ServantPropManager() {
        super(GSON, DIRECTORY);
    }

    @Nullable
    public ServantProperties get(ResourceLocation entityType) {
        return this.props.getOrDefault(entityType, ServantProperties.DEFAULT);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, ServantProperties> builder = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                ServantProperties props = ServantProperties.CODEC.parse(JsonOps.INSTANCE, el)
                        .getOrThrow(false, Fate.LOGGER::error);
                builder.put(fres, props);
            } catch (Exception ex) {
                Fate.LOGGER.error("Couldnt parse crop properties json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.props = builder.build();
    }
}
