package io.github.flemmli97.fateubw.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class GrailLootEntry<T extends GrailLootEntry<T>> implements BiConsumer<ServerPlayer, LootContext> {

    protected final LootItemCondition[] conditions;
    public final Predicate<LootContext> combinedConditions;
    public final NumberProvider range;

    public GrailLootEntry(NumberProvider range, LootItemCondition[] conditions) {
        this.conditions = conditions;
        this.combinedConditions = LootItemConditions.andConditions(conditions);
        this.range = range;
    }

    public void give(ServerPlayer player, LootContext context) {
        if (this.combinedConditions.test(context))
            this.accept(player, context);
    }

    public abstract Supplier<LootSerializerType<T>> getType();

    public boolean valid() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public JsonObject serialize(JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", this.getType().get().getRegistryName().toString());
        this.getType().get().getSerializer().serialize(obj, (T) this, context);
        return obj;
    }

    public static abstract class BaseSerializer<T extends GrailLootEntry<T>> implements Serializer<T> {

        @Override
        public void serialize(JsonObject obj, T entry, JsonSerializationContext context) {
            if (entry.conditions != null && entry.conditions.length > 0) {
                obj.add("conditions", context.serialize(entry.conditions));
            }
            obj.add("range", context.serialize(entry.range));
        }

        @Override
        public T deserialize(JsonObject obj, JsonDeserializationContext context) {
            NumberProvider range = GsonHelper.getAsObject(obj, "range", context, NumberProvider.class);
            LootItemCondition[] conditions = GsonHelper.getAsObject(obj, "conditions", new LootItemCondition[0], context, LootItemCondition[].class);
            return this.deserialize(obj, context, range, conditions);
        }

        public abstract T deserialize(JsonObject object, JsonDeserializationContext context, NumberProvider range, LootItemCondition[] conditions);
    }
}
