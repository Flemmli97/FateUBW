package io.github.flemmli97.fate.common.loot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.flemmli97.fate.Fate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootSerializers;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LootConditionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class GrailLootTable {

    private static final Map<String, GrailLootTable> loots = new HashMap<>();
    public static final Gson GSON = LootSerializers.func_237387_b_()
            //.registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
            //.registerTypeAdapter(BinomialRange.class, new BinomialRange.Serializer())
            //.registerTypeAdapter(ConstantRange.class, new ConstantRange.Serializer())
            //.registerTypeAdapter(IntClamper.class, new IntClamper.Serializer())
            //.registerTypeHierarchyAdapter(ILootCondition.class, LootConditionManager.func_237474_a_())
            //.registerTypeAdapter(ILootFunction.class, LootFunctionManager.func_237450_a_())
            //.registerTypeHierarchyAdapter(LootEntry.class, LootEntryManager.func_237418_a_())
            .registerTypeHierarchyAdapter(GrailLootTable.class, new GrailLootTable.Serializer())
            .setPrettyPrinting().disableHtmlEscaping().create();

    private final String name;

    private final List<GrailLootEntry<?>> lootPool;
    private final ILootCondition[] conditions;
    private final Predicate<LootContext> combinedConditions;

    public GrailLootTable(String name, List<GrailLootEntry<?>> lootPool, ILootCondition[] conditions) {
        this.name = name;
        this.lootPool = lootPool;
        this.conditions = conditions;
        this.combinedConditions = LootConditionManager.and(conditions);
    }

    public boolean isEmpty() {
        return this.lootPool.isEmpty();
    }

    public void give(ServerPlayerEntity player) {
        LootContext ctx = new LootContext.Builder(player.getServerWorld())
                .withLuck(player.getLuck())
                .withRandom(player.getRNG())
                .withParameter(LootParameters.ORIGIN, player.getPositionVec())
                .withParameter(LootParameters.THIS_ENTITY, player)
                .build(LootParameterSets.SELECTOR);
        if (this.combinedConditions.test(ctx))
            this.lootPool.forEach(loot -> loot.give(player, ctx));
    }

    public static class Serializer implements JsonSerializer<GrailLootTable>, JsonDeserializer<GrailLootTable> {

        @Override
        public GrailLootTable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                List<GrailLootEntry<?>> lootEntries = new ArrayList<>();
                JsonArray arr = obj.getAsJsonArray("pools");
                arr.forEach(e -> {
                    JsonObject val = e.getAsJsonObject();
                    ResourceLocation type = new ResourceLocation(val.get("type").getAsString());
                    lootEntries.add(Fate.getGrailSerializers().getValue(type)
                            .getSerializer().deserialize(val, context));
                });
                ILootCondition[] conditions = JSONUtils.deserializeClass(obj, "conditions", new ILootCondition[0], context, ILootCondition[].class);

                return new GrailLootTable(obj.get("name").toString(), lootEntries, conditions);
            } else
                throw new UnsupportedOperationException("Object " + json + " can't be deserialized");
        }

        @Override
        public JsonElement serialize(GrailLootTable src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            JsonArray pools = new JsonArray();
            src.lootPool.forEach(entry -> {
                pools.add(entry.serialize(context));
            });
            obj.add("pools", pools);
            if (src.conditions != null && src.conditions.length > 0)
                obj.add("conditions", context.serialize(src.conditions));
            obj.addProperty("name", src.name);
            return obj;
        }
    }
}
