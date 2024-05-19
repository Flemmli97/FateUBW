package io.github.flemmli97.fateubw.common.loot;

import com.google.gson.Gson;
import com.mojang.serialization.Codec;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class LootCodecs {

    private static final Gson GSON = Deserializers.createFunctionSerializer()
            .setPrettyPrinting().disableHtmlEscaping().create();

    public static Codec<LootPoolEntryContainer> POOL_ENTRY_CODEC = CodecUtils.jsonCodecBuilder(GSON, LootPoolEntryContainer.class, "LootPoolEntryContainer");
    public static Codec<LootItemCondition> LOOT_ITEM_CONDITION = CodecUtils.jsonCodecBuilder(GSON, LootItemCondition.class, "LootItemCondition");
    public static Codec<NumberProvider> NUMBER_PROVIDER_CODEC = CodecUtils.jsonCodecBuilder(GSON, NumberProvider.class, "NumberProvider");
}
