package io.github.flemmli97.fateubw.common.datapack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServantPropManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "servant_properties";
    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, ServantProperties> props = ImmutableMap.of();
    private Map<ResourceLocation, List<EntityTypeAndID>> classServantMap = ImmutableMap.of();
    private Set<EntityTypeAndID> servants = ImmutableSet.of();
    private boolean built;

    public ServantPropManager() {
        super(GSON, DIRECTORY);
    }

    public ServantProperties get(ResourceLocation entityType) {
        return this.props.getOrDefault(entityType, ServantProperties.DEFAULT);
    }

    public Set<ResourceLocation> getServantClasses() {
        return this.classServantMap.keySet();
    }

    public Set<EntityTypeAndID> getServants(ServerLevel level) {
        this.computeData(level);
        return this.servants;
    }

    public List<EntityTypeAndID> getServantsFromClass(ServerLevel level, ResourceLocation servantClass) {
        this.computeData(level);
        return this.classServantMap.getOrDefault(servantClass, List.of());
    }

    @SuppressWarnings("unchecked")
    public void computeData(ServerLevel level) {
        if (!this.built) {
            Map<ResourceLocation, List<EntityTypeAndID>> classes = new HashMap<>();
            Set<EntityTypeAndID> servants = new HashSet<>();
            this.props.forEach((id, prop) -> {
                EntityType<?> type = Registry.ENTITY_TYPE.get(id);
                Entity entity = type.create(level);
                if (!prop.getServantClass().equals(BuiltinServantClasses.NONE) && entity instanceof BaseServant) {
                    EntityTypeAndID entry = new EntityTypeAndID((EntityType<? extends BaseServant>) type, id);
                    classes.merge(prop.getServantClass(), Lists.newArrayList(entry), (old, val) -> {
                        old.add(entry);
                        return old;
                    });
                    servants.add(entry);
                }
            });
            this.classServantMap = ImmutableMap.copyOf(classes);
            this.servants = ImmutableSet.copyOf(servants);
            this.built = true;
        }
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
        this.built = false;
    }

    public record EntityTypeAndID(EntityType<? extends BaseServant> type, ResourceLocation id) {
    }
}
