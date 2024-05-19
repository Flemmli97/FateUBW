package io.github.flemmli97.fateubw.common.loot;

import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class GrailLootEntry<T extends GrailLootEntry<T>> implements BiConsumer<ServerPlayer, LootContext> {

    public static final Codec<GrailLootEntry<?>> CODEC = CodecUtils.registryCodec(GrailLootSerializer.SERIALIZER_KEY,
            c -> c.dispatch(e -> e.getType().get(), LootSerializerType::getCodec));

    protected final LootItemCondition[] conditions;
    public final Predicate<LootContext> combinedConditions;

    public GrailLootEntry(LootItemCondition[] conditions) {
        this.conditions = conditions;
        this.combinedConditions = LootItemConditions.andConditions(conditions);
    }

    public void give(ServerPlayer player, LootContext context) {
        if (this.combinedConditions.test(context))
            this.accept(player, context);
    }

    public abstract Supplier<LootSerializerType<T>> getType();

    public boolean valid() {
        return true;
    }
}
