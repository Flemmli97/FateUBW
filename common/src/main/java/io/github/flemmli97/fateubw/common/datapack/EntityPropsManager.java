package io.github.flemmli97.fateubw.common.datapack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EntityPropsManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = Fate.modRes("entity_properties");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());
    private static final Gson GSON = new GsonBuilder().create();

    private Map<EntityType<?>, ServantProperties> props = ImmutableMap.of();
    private Map<EntityType<?>, AttributeHolderProperties> genericProps = ImmutableMap.of();

    private Set<EntityTypeAndID> servants = ImmutableSet.of();

    private HolderLookup.Provider provider;

    public EntityPropsManager() {
        super(GSON, DIRECTORY);
    }

    public ServantProperties get(EntityType<?> type) {
        return this.props.getOrDefault(type, ServantProperties.DEFAULT);
    }

    public AttributeHolderProperties getGeneric(EntityType<?> type) {
        return this.genericProps.getOrDefault(type, AttributeHolderProperties.DEFAULT);
    }

    public Set<EntityTypeAndID> getServants() {
        return this.servants;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<EntityType<?>, ServantProperties> builder = ImmutableMap.builder();
        ImmutableMap.Builder<EntityType<?>, AttributeHolderProperties> attBuilder = ImmutableMap.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((fres, el) -> {
            try {
                BuiltInRegistries.ENTITY_TYPE.getOptional(fres).ifPresent(type -> {
                    Optional<ServantProperties> servantProps = ServantProperties.CODEC.parse(ops, el).result();
                    if (servantProps.isPresent()) {
                        builder.put(type, servantProps.get());
                    } else {
                        AttributeHolderProperties props = AttributeHolderProperties.CODEC.parse(ops, el).getOrThrow();
                        attBuilder.put(type, props);
                    }
                });
            } catch (Exception ex) {
                Fate.LOGGER.error("Couldn't parse entity properties json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.props = builder.build();
        this.genericProps = attBuilder.build();
        Set<EntityTypeAndID> servants = new HashSet<>();
        this.props.forEach((type, prop) -> {
            if (!prop.servantClass().equals(BuiltinServantClasses.NONE)) {
                servants.add(new EntityTypeAndID(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type), prop.servantClass(), prop.weight()));
            }
        });
        this.servants = ImmutableSet.copyOf(servants);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }

    public record EntityTypeAndID(Holder<EntityType<?>> type, ResourceLocation servantClass,
                                  Weight weight) implements WeightedEntry {

        public EntityTypeAndID(Holder<EntityType<?>> type, ResourceLocation servantClass, int weight) {
            this(type, servantClass, Weight.of(weight));
        }

        public EntityTypeAndID updatedWeight(@Nullable ItemStack stack) {
            if (stack == null)
                return this;
            ResourceLocation clss = stack.get(FateDataComponents.CLASS_RELIC.get());
            ResourceLocation servant = stack.get(FateDataComponents.SERVANT_RELIC.get());
            int weight = this.weight.asInt();
            if (this.servantClass().equals(clss)) {
                weight *= 2;
            }
            if (this.type().is(servant)) {
                weight *= 2;
            }
            if (this.weight.asInt() == weight)
                return this;
            return new EntityTypeAndID(this.type(), this.servantClass(), Weight.of(weight));
        }

        public ResourceLocation id() {
            return this.type().unwrapKey().orElseThrow().location();
        }

        @Override
        public Weight getWeight() {
            return this.weight();
        }
    }
}
