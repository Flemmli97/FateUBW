package io.github.flemmli97.fateubw.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.RecordBuilder;
import io.github.flemmli97.fateubw.Fate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServantExtraData {

    private static final Map<ResourceLocation, DataType<?>> REGISTRY = new HashMap<>();

    public static final DataType<Float> LANCELOT_REFLECT_CHANCE = register(Fate.MODID, "lancelot_reflect_chance", Codec.FLOAT, 0.4f);
    public static final DataType<Integer> HASSAN_COPIES = register(Fate.MODID, "hassan_copies", Codec.INT, 5);
    public static final DataType<Integer> MEDEA_CIRCLE_DURATION = register(Fate.MODID, "medea_circle_duration", ExtraCodecs.POSITIVE_INT, 2000);
    public static final DataType<Float> MEDEA_CIRCLE_RANGE = register(Fate.MODID, "medea_circle_range", Codec.FLOAT, 24f);
    public static final DataType<Integer> GILLES_MONSTER_DURATION = register(Fate.MODID, "gilles_monster_duration", ExtraCodecs.POSITIVE_INT, 6000);
    public static final DataType<Integer> GILLES_MONSTER_MAX = register(Fate.MODID, "gilles_monster_max", ExtraCodecs.NON_NEGATIVE_INT, 7);
    public static final DataType<Integer> HERACLES_DEATH_MAX = register(Fate.MODID, "heracles_death_max", ExtraCodecs.POSITIVE_INT, 2);

    public static final Codec<ServantExtraData> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<ServantExtraData, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).setLifecycle(Lifecycle.stable())
                    .flatMap(map -> {
                        ImmutableMap.Builder<DataType<?>, Object> values = ImmutableMap.builder();
                        List<String> errors = new ArrayList<>();
                        map.entries().forEach((pair) -> {
                            // Impl allowing dynamic codec based on map key which is not possible in vanilla
                            DataResult<Unit> val = ResourceLocation.CODEC.parse(ops, pair.getFirst())
                                    .flatMap(id -> {
                                        DataType<?> type = REGISTRY.get(id);
                                        if (type == null)
                                            return DataResult.error(() -> "No such type " + id);
                                        DataResult<?> value = type.codec.parse(ops, pair.getSecond());
                                        return value.map(v -> {
                                            values.put(type, v);
                                            return Unit.INSTANCE;
                                        });
                                    });
                            val.error().ifPresent(e -> errors.add(e.message()));
                        });
                        ServantExtraData extraData = new ServantExtraData(values.build());
                        if (!errors.isEmpty()) {
                            return DataResult.error(() -> "Error during parsing: " + String.join("\n", errors), extraData);
                        }
                        return DataResult.success(extraData);
                    }).map(r -> Pair.of(r, input));
        }

        @Override
        public <T> DataResult<T> encode(ServantExtraData input, DynamicOps<T> ops, T prefix) {
            RecordBuilder<T> builder = ops.mapBuilder();
            for (DataType<?> type : input.values.keySet()) {
                builder.add(ResourceLocation.CODEC.encodeStart(ops, type.id()), input.encode(ops, type));
            }
            return builder.build(prefix);
        }
    };

    public static synchronized <T> DataType<T> register(String namespace, String path, Codec<T> codec, T defaultValue) {
        DataType<T> type = new DataType<>(ResourceLocation.fromNamespaceAndPath(namespace, path), codec, defaultValue);
        if (REGISTRY.put(type.id(), type) != null) {
            throw new IllegalStateException("Type with " + type.id() + " already registered");
        }
        return type;
    }

    private final Map<DataType<?>, Object> values;

    public ServantExtraData(Map<DataType<?>, Object> map) {
        this.values = map;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(DataType<T> type) {
        T val = (T) this.values.get(type);
        if (val == null)
            return type.defaultValue();
        return val;
    }

    public boolean empty() {
        return this.values.isEmpty();
    }

    private <R, T> DataResult<R> encode(DynamicOps<R> ops, DataType<T> type) {
        return type.codec.encodeStart(ops, this.get(type));
    }

    public record DataType<T>(ResourceLocation id, Codec<T> codec, T defaultValue) {

        public DataType(String namespace, String path, Codec<T> codec, T defaultValue) {
            this(ResourceLocation.fromNamespaceAndPath(namespace, path), codec, defaultValue);
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || (obj instanceof DataType<?> other && this.id.equals(other.id));
        }

        @Override
        public int hashCode() {
            return this.id.hashCode();
        }

        @Override
        @NotNull
        public String toString() {
            return this.id.toString();
        }
    }
}
