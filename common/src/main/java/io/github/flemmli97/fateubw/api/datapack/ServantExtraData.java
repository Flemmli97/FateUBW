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
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServantExtraData {

    private static final Map<ResourceLocation, DataType<?>> REGISTRY = new HashMap<>();

    public static final DataType<Float> PROJECTILE_REFLECT_CHANCE = register(Fate.MODID, "projectile_reflect_chance", Codec.FLOAT, 0.4f);
    public static final DataType<Integer> HASSAN_COPIES = register(Fate.MODID, "hassan_copies", Codec.INT, 4);
    public static final DataType<Integer> MAGIC_CIRCLE_DURATION = register(Fate.MODID, "magic_circle_duration", ExtraCodecs.POSITIVE_INT, 2000);
    public static final DataType<Float> MAGIC_CIRCLE_RANGE = register(Fate.MODID, "magic_circle_range", Codec.FLOAT, 24f);
    public static final DataType<Integer> SUMMONED_MONSTER_DURATION = register(Fate.MODID, "summoned_monster_duration", ExtraCodecs.POSITIVE_INT, 6000);
    public static final DataType<Integer> SUMMONED_MONSTER_MAX = register(Fate.MODID, "summoned_monster_max", ExtraCodecs.NON_NEGATIVE_INT, 7);
    public static final DataType<Float> TENTACLE_DAMAGE = register(Fate.MODID, "tentacle_damage", Codec.FLOAT, 15f);
    public static final DataType<Integer> LIVES = register(Fate.MODID, "lives", ExtraCodecs.POSITIVE_INT, 2);
    public static final DataType<NumberProvider> CALADBOLG_COOLDOWN = register(Fate.MODID, "caladbolg_cooldown", NumberProviders.CODEC, UniformGenerator.between(200, 500));
    public static final DataType<NumberProvider> MOUNT_SUMMON_COOLDOWN = register(Fate.MODID, "mount_summon_cooldown", NumberProviders.CODEC, UniformGenerator.between(250, 600));
    public static final DataType<NumberProvider> ARMY_SUMMON_COOLDOWN = register(Fate.MODID, "army_summon_cooldown", NumberProviders.CODEC, UniformGenerator.between(200, 500));
    public static final DataType<Integer> MAX_NEARBY_ARMY = register(Fate.MODID, "max_nearby_army", Codec.INT, 12);
    public static final DataType<Float> STRONG_HOPLITE_CHANCE = register(Fate.MODID, "strong_hoplite_chance", Codec.FLOAT, 0.1f);

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
