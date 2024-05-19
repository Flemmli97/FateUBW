package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Supplier;

public class EmptyEntry extends GrailLootEntry<EmptyEntry> {

    public static final EmptyEntry INSTANCE = new EmptyEntry();
    public static final Codec<EmptyEntry> CODEC = Codec.unit(INSTANCE);

    private EmptyEntry() {
        super(new LootItemCondition[0]);
    }

    @Override
    public Supplier<LootSerializerType<EmptyEntry>> getType() {
        return GrailLootSerializer.EMPTY;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
    }
}