package io.github.flemmli97.fateubw.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.function.Supplier;

public class XPEntry extends GrailLootEntry<XPEntry> {

    public XPEntry(NumberProvider range, LootItemCondition... conditions) {
        super(range, conditions);
    }

    @Override
    public Supplier<LootSerializerType<XPEntry>> getType() {
        return GrailLootSerializer.XP;
    }

    @Override
    public void accept(ServerPlayer playerEntity, LootContext context) {
        playerEntity.giveExperiencePoints(this.range.getInt(context));
    }

    public static class Serializer extends BaseSerializer<XPEntry> {

        @Override
        public XPEntry deserialize(JsonObject object, JsonDeserializationContext context, NumberProvider range, LootItemCondition[] conditions) {
            return new XPEntry(range, conditions);
        }
    }
}