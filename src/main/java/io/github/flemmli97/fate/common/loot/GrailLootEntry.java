package io.github.flemmli97.fate.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.RandomRanges;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LootConditionManager;
import net.minecraft.util.JSONUtils;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class GrailLootEntry<T extends GrailLootEntry<T>> implements BiConsumer<ServerPlayerEntity, LootContext> {

    protected final ILootCondition[] conditions;
    public final Predicate<LootContext> combinedConditions;
    public final IRandomRange range;

    public GrailLootEntry(IRandomRange range, ILootCondition[] conditions) {
        this.conditions = conditions;
        this.combinedConditions = LootConditionManager.and(conditions);
        this.range = range;
    }

    public void give(ServerPlayerEntity player, LootContext context) {
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

    public static abstract class BaseSerializer<T extends GrailLootEntry<T>> implements ILootSerializer<T> {

        @Override
        public void serialize(JsonObject obj, T entry, JsonSerializationContext context) {
            if (entry.conditions != null && entry.conditions.length > 0) {
                obj.add("conditions", context.serialize(entry.conditions));
            }
            obj.add("range", RandomRanges.serialize(entry.range, context));
        }

        @Override
        public T deserialize(JsonObject obj, JsonDeserializationContext context) {
            IRandomRange range = RandomRanges.deserialize(obj.get("range"), context);
            ILootCondition[] conditions = JSONUtils.deserializeClass(obj, "conditions", new ILootCondition[0], context, ILootCondition[].class);
            return this.deserialize(obj, context, range, conditions);
        }

        public abstract T deserialize(JsonObject object, JsonDeserializationContext context, IRandomRange range, ILootCondition[] conditions);
    }
}
