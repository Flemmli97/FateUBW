package io.github.flemmli97.fate.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.fate.common.loot.GrailLootEntry;
import io.github.flemmli97.fate.common.loot.LootSerializerType;
import io.github.flemmli97.fate.common.registry.GrailLootSerializer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.function.Supplier;

public class EmptyEntry extends GrailLootEntry<EmptyEntry> {

    public EmptyEntry() {
        super(ConstantRange.of(1), new ILootCondition[0]);
    }

    @Override
    public Supplier<LootSerializerType<EmptyEntry>> getType() {
        return GrailLootSerializer.EMPTY;
    }

    @Override
    public void accept(ServerPlayerEntity playerEntity, LootContext context) {

    }

    public static class Serializer implements ILootSerializer<EmptyEntry> {

        @Override
        public void serialize(JsonObject obj, EmptyEntry entry, JsonSerializationContext ctx) {
        }

        @Override
        public EmptyEntry deserialize(JsonObject obj, JsonDeserializationContext ctx) {
            return new EmptyEntry();
        }
    }
}