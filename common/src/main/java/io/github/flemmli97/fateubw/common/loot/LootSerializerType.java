package io.github.flemmli97.fateubw.common.loot;

import com.mojang.serialization.Codec;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;

public class LootSerializerType<T extends GrailLootEntry<T>> extends CustomRegistryEntry<LootSerializerType<?>> {

    /**
     * Use this instead of LootSerializerType.class for deferred register cause generics
     */
    @SuppressWarnings("unchecked")
    public static final Class<LootSerializerType<?>> CLASS = (Class<LootSerializerType<?>>) ((Class<?>) LootSerializerType.class);

    private final Codec<T> codec;

    public LootSerializerType(Codec<T> serializer) {
        this.codec = serializer;
    }

    public Codec<T> getCodec() {
        return this.codec;
    }
}
