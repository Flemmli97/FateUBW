package io.github.flemmli97.fate.common.loot;

import net.minecraft.loot.ILootSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class LootSerializerType<T extends GrailLootEntry<T>> extends ForgeRegistryEntry<LootSerializerType<?>> {

    /**
     * Use this instead of LootSerializerType.class for deferred register cause generics
     */
    @SuppressWarnings("unchecked")
    public static final Class<LootSerializerType<?>> CLASS = (Class<LootSerializerType<?>>) ((Class<?>) LootSerializerType.class);

    private final ILootSerializer<T> serializer;

    public LootSerializerType(ILootSerializer<T> serializer) {
        this.serializer = serializer;
    }

    public ILootSerializer<T> getSerializer() {
        return this.serializer;
    }
}
