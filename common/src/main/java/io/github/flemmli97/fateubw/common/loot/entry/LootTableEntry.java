package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Supplier;

public class LootTableEntry extends GrailLootEntry<LootTableEntry> {

    public static final Codec<LootTableEntry> CODEC = ResourceLocation.CODEC.fieldOf("loot_table")
            .xmap(LootTableEntry::new, e -> e.lootTable).codec();

    private final ResourceLocation lootTable;

    public LootTableEntry(ResourceLocation lootTable) {
        super(new LootItemCondition[0]);
        this.lootTable = lootTable;
    }

    @Override
    public Supplier<LootSerializerType<LootTableEntry>> getType() {
        return GrailLootSerializer.LOOT_TABLE;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
        LootTable table = player.getServer().getLootTables().get(this.lootTable);
        table.getRandomItems(context, player::addItem);
    }
}
