package io.github.flemmli97.fateubw.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GrailLootTable {

    public static final Codec<GrailLootTable> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("name").forGetter(d -> d.name.getString()),
            Codec.STRING.listOf().optionalFieldOf("descriptions").forGetter(d -> d.descriptions.isEmpty() ? Optional.empty() : Optional.of(d.descriptions.stream().map(Component::getString).toList())),
            GrailLootEntry.CODEC.listOf().fieldOf("loot_pools").forGetter(d -> d.lootPool),
            LootCodecs.LOOT_ITEM_CONDITION.listOf().optionalFieldOf("condition").forGetter(d -> Optional.of(Arrays.stream(d.conditions).toList()))
    ).apply(inst, (name, description, pool, conditions) -> new GrailLootTable(name, description.orElse(List.of()), pool, conditions.map(l -> l.toArray(l.toArray(new LootItemCondition[0]))).orElse(new LootItemCondition[0]))));

    public final Component name;
    public final List<Component> descriptions;

    private final List<GrailLootEntry<?>> lootPool;
    private final LootItemCondition[] conditions;
    private final Predicate<LootContext> combinedConditions;

    public GrailLootTable(String name, List<String> descriptions, List<GrailLootEntry<?>> lootPool, LootItemCondition[] conditions) {
        this.name = new TranslatableComponent(name);
        this.descriptions = descriptions.stream().map(TranslatableComponent::new).collect(Collectors.toUnmodifiableList());
        this.lootPool = lootPool;
        this.conditions = conditions;
        this.combinedConditions = LootItemConditions.andConditions(conditions);
    }

    public boolean isEmpty() {
        return this.lootPool.isEmpty() || this.lootPool.stream().noneMatch(GrailLootEntry::valid);
    }

    public void give(ServerPlayer player) {
        LootContext ctx = new LootContext.Builder(player.getLevel())
                .withLuck(player.getLuck())
                .withRandom(player.getRandom())
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.SELECTOR);
        if (this.combinedConditions.test(ctx))
            this.lootPool.forEach(loot -> loot.give(player, ctx));
    }
}
