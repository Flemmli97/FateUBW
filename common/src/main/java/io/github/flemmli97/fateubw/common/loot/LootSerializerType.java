package io.github.flemmli97.fateubw.common.loot;

import com.mojang.serialization.MapCodec;

public record LootSerializerType<T extends GrailLootEntry<T>>(MapCodec<T> codec) {

}
