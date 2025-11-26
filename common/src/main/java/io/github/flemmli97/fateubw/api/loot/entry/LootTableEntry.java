package io.github.flemmli97.fateubw.api.loot.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.api.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.api.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class LootTableEntry extends GrailLootEntry<LootTableEntry> {

    public static final MapCodec<LootTableEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    ResourceKey.codec(Registries.LOOT_TABLE).listOf().fieldOf("loot_tables").forGetter(d -> d.lootTables),
                    LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions").forGetter(d -> d.conditions.isEmpty() ? Optional.empty() : Optional.of(d.conditions))
            ).apply(inst, (command, cond) -> new LootTableEntry(command, cond.orElse(List.of())))
    );

    private final List<ResourceKey<LootTable>> lootTables;

    public LootTableEntry(List<ResourceKey<LootTable>> lootTables, LootItemCondition... conditions) {
        this(lootTables, List.of(conditions));
    }

    public LootTableEntry(List<ResourceKey<LootTable>> lootTables, List<LootItemCondition> conditions) {
        super(conditions);
        this.lootTables = lootTables;
    }

    @Override
    public Supplier<LootSerializerType<LootTableEntry>> getType() {
        return FateGrailLootSerializer.LOOT_TABLE;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
        ResourceKey<LootTable> lootTable = this.lootTables.get(context.getRandom().nextInt(this.lootTables.size()));
        LootTable table = player.getServer().reloadableRegistries().getLootTable(lootTable);
        table.getRandomItems(context, player::addItem);
    }
}
