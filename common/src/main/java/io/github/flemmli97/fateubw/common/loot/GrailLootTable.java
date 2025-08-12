package io.github.flemmli97.fateubw.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GrailLootTable {

    public static final Codec<GrailLootTable> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("name").forGetter(d -> d.name.getString()),
            Codec.STRING.listOf().optionalFieldOf("descriptions").forGetter(d -> d.descriptions.isEmpty() ? Optional.empty() : Optional.of(d.descriptions.stream().map(Component::getString).toList())),
            GrailLootEntry.CODEC.listOf().fieldOf("loot_pools").forGetter(d -> d.lootPool),
            LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions").forGetter(d -> d.conditions.isEmpty() ? Optional.empty() : Optional.of(d.conditions))
    ).apply(inst, (name, description, pool, conditions) -> new GrailLootTable(name, description.orElse(List.of()), pool, conditions.orElse(List.of()))));

    public final Component name;
    public final List<Component> descriptions;

    private final List<GrailLootEntry<?>> lootPool;
    private final List<LootItemCondition> conditions;
    private final Predicate<LootContext> combinedConditions;

    public GrailLootTable(String name, List<String> descriptions, List<GrailLootEntry<?>> lootPool, List<LootItemCondition> conditions) {
        this.name = Component.translatable(name);
        this.descriptions = descriptions.stream().map(Component::translatable).collect(Collectors.toUnmodifiableList());
        this.lootPool = lootPool;
        this.conditions = conditions;
        this.combinedConditions = AllOfCondition.allOf(conditions);
    }

    public boolean isEmpty() {
        return this.lootPool.isEmpty() || this.lootPool.stream().noneMatch(GrailLootEntry::valid);
    }

    public void give(ServerPlayer player) {
        LootParams params = new LootParams.Builder(player.serverLevel())
                .withLuck(player.getLuck())
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.SELECTOR);
        LootContext ctx = new LootContext.Builder(params)
                .withOptionalRandomSource(player.getRandom())
                .create(Optional.empty());
        if (this.combinedConditions.test(ctx))
            this.lootPool.forEach(loot -> loot.give(player, ctx));
    }
}
