package io.github.flemmli97.fateubw.common.loot;

import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.world.level.storage.loot.Serializer;

public class LootSerializerType<T extends GrailLootEntry<T>> extends CustomRegistryEntry<LootSerializerType<?>> {

    /**
     * Use this instead of LootSerializerType.class for deferred register cause generics
     */
    @SuppressWarnings("unchecked")
    public static final Class<LootSerializerType<?>> CLASS = (Class<LootSerializerType<?>>) ((Class<?>) LootSerializerType.class);

    private final Serializer<T> serializer;

    public LootSerializerType(Serializer<T> serializer) {
        this.serializer = serializer;
    }

    public Serializer<T> getSerializer() {
        return this.serializer;
    }
}
