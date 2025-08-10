package io.github.flemmli97.fateubw.common.loot;

import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class GrailLootEntry<T extends GrailLootEntry<T>> implements BiConsumer<ServerPlayer, LootContext> {

    public static final Codec<GrailLootEntry<?>> CODEC = FateGrailLootSerializer.SERIALIZER.registry().byNameCodec()
            .dispatch(e -> e.getType().get(), LootSerializerType::codec);

    protected final List<LootItemCondition> conditions;
    public final Predicate<LootContext> combinedConditions;

    public GrailLootEntry(List<LootItemCondition> conditions) {
        this.conditions = conditions;
        this.combinedConditions = AllOfCondition.allOf(conditions);
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
