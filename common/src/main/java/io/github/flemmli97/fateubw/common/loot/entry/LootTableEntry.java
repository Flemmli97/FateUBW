package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootCodecs;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class LootTableEntry extends GrailLootEntry<LootTableEntry> {

    public static final Codec<LootTableEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.listOf().fieldOf("loot_tables").forGetter(d -> d.lootTables),
                    LootCodecs.LOOT_ITEM_CONDITION.listOf().optionalFieldOf("conditions").forGetter(d -> d.conditions.length == 0 ? Optional.empty() : Optional.of(Arrays.stream(d.conditions).toList()))
            ).apply(inst, (command, cond) -> new LootTableEntry(command, cond.map(l -> l.toArray(l.toArray(new LootItemCondition[0]))).orElse(new LootItemCondition[0])))
    );

    private final List<ResourceLocation> lootTables;

    public LootTableEntry(List<ResourceLocation> lootTables, LootItemCondition... conditions) {
        super(conditions);
        this.lootTables = lootTables;
    }

    @Override
    public Supplier<LootSerializerType<LootTableEntry>> getType() {
        return GrailLootSerializer.LOOT_TABLE;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
        ResourceLocation lootTable = this.lootTables.get(context.getRandom().nextInt(this.lootTables.size()));
        LootTable table = player.getServer().getLootTables().get(lootTable);
        table.getRandomItems(context, player::addItem);
    }
}
