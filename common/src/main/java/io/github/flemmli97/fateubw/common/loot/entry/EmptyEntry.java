package io.github.flemmli97.fateubw.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.Supplier;

public class EmptyEntry extends GrailLootEntry<EmptyEntry> {

    public EmptyEntry() {
        super(ConstantValue.exactly(1), new LootItemCondition[0]);
    }

    @Override
    public Supplier<LootSerializerType<EmptyEntry>> getType() {
        return GrailLootSerializer.EMPTY;
    }

    @Override
    public void accept(ServerPlayer playerEntity, LootContext context) {

    }

    public static class SerializerImpl implements Serializer<EmptyEntry> {

        @Override
        public void serialize(JsonObject obj, EmptyEntry entry, JsonSerializationContext ctx) {
        }

        @Override
        public EmptyEntry deserialize(JsonObject obj, JsonDeserializationContext ctx) {
            return new EmptyEntry();
        }
    }
}