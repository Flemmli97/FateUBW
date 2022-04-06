package io.github.flemmli97.fateubw.common.loot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class GrailLootTable {

    private static final Map<String, GrailLootTable> loots = new HashMap<>();
    public static final Gson GSON = Deserializers.createFunctionSerializer()
            .registerTypeAdapter(NumberProvider.class, NumberProviders.createGsonAdapter())
            .registerTypeHierarchyAdapter(GrailLootTable.class, new GrailLootTable.Serializer())
            .setPrettyPrinting().disableHtmlEscaping().create();

    public final String name;

    private final List<GrailLootEntry<?>> lootPool;
    private final LootItemCondition[] conditions;
    private final Predicate<LootContext> combinedConditions;

    public GrailLootTable(String name, List<GrailLootEntry<?>> lootPool, LootItemCondition[] conditions) {
        this.name = name;
        this.lootPool = lootPool;
        this.conditions = conditions;
        this.combinedConditions = LootItemConditions.andConditions(conditions);
    }

    public boolean isEmpty() {
        return this.lootPool.isEmpty() || this.lootPool.stream().noneMatch(GrailLootEntry::valid);
    }

    public void give(ServerPlayer player) {
        LootContext ctx = new LootContext.Builder(player.getLevel())
                .withLuck(player.getLuck())
                .withRandom(player.getRandom())
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.SELECTOR);
        if (this.combinedConditions.test(ctx))
            this.lootPool.forEach(loot -> loot.give(player, ctx));
    }

    public static class Serializer implements JsonSerializer<GrailLootTable>, JsonDeserializer<GrailLootTable> {

        @Override
        public GrailLootTable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                List<GrailLootEntry<?>> lootEntries = new ArrayList<>();
                JsonArray arr = GsonHelper.getAsJsonArray(obj, "pools", new JsonArray());
                arr.forEach(e -> {
                    JsonObject val = e.getAsJsonObject();
                    ResourceLocation type = new ResourceLocation(val.get("type").getAsString());
                    lootEntries.add(PlatformUtils.INSTANCE.registry(GrailLootSerializer.SERIALIZER_KEY).getFromId(type)
                            .getSerializer().deserialize(val, context));
                });
                LootItemCondition[] conditions = GsonHelper.getAsObject(obj, "conditions", new LootItemCondition[0], context, LootItemCondition[].class);
                return new GrailLootTable(GsonHelper.getAsString(obj, "default_name", "NONAME"), lootEntries, conditions);
            } else
                throw new UnsupportedOperationException("Object " + json + " can't be deserialized");
        }

        @Override
        public JsonElement serialize(GrailLootTable src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            JsonArray pools = new JsonArray();
            obj.addProperty("default_name", src.name);
            src.lootPool.forEach(entry -> pools.add(entry.serialize(context)));
            obj.add("pools", pools);
            if (src.conditions != null && src.conditions.length > 0)
                obj.add("conditions", context.serialize(src.conditions));
            return obj;
        }
    }
}
