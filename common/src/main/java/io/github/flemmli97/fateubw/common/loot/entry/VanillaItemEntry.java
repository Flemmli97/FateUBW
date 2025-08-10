package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootCodecs;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class VanillaItemEntry extends GrailLootEntry<VanillaItemEntry> {

    public static final MapCodec<VanillaItemEntry> CODEC = LootCodecs.POOL_ENTRY_CODEC.fieldOf("entry")
            .xmap(VanillaItemEntry::new, e -> e.lootEntry);

    private final LootPoolEntryContainer lootEntry;

    public VanillaItemEntry(LootPoolEntryContainer itemLootEntry) {
        super(new LootItemCondition[0]);
        this.lootEntry = itemLootEntry;
    }

    @Override
    public Supplier<LootSerializerType<VanillaItemEntry>> getType() {
        return FateGrailLootSerializer.VANILLA;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
        Consumer<ItemStack> givePlayer = stack -> {
            if (!player.addItem(stack)) {
                player.spawnAtLocation(stack);
            }
        };
        this.lootEntry.expand(context, gen -> gen.createItemStack(givePlayer, context));
    }
}
