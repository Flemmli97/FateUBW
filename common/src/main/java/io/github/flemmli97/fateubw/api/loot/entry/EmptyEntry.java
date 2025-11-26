package io.github.flemmli97.fateubw.api.loot.entry;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.fateubw.api.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.api.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Supplier;

public class EmptyEntry extends GrailLootEntry<EmptyEntry> {

    public static final EmptyEntry INSTANCE = new EmptyEntry();
    public static final MapCodec<EmptyEntry> CODEC = MapCodec.unit(INSTANCE);

    private EmptyEntry() {
        super(List.of());
    }

    @Override
    public Supplier<LootSerializerType<EmptyEntry>> getType() {
        return FateGrailLootSerializer.EMPTY;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
    }
}