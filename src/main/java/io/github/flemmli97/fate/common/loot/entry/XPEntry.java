package io.github.flemmli97.fate.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import io.github.flemmli97.fate.common.loot.GrailLootEntry;
import io.github.flemmli97.fate.common.loot.LootSerializerType;
import io.github.flemmli97.fate.common.registry.GrailLootSerializer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.function.Supplier;

public class XPEntry extends GrailLootEntry<XPEntry> {

    public XPEntry(IRandomRange range, ILootCondition... conditions) {
        super(range, conditions);
    }

    @Override
    public Supplier<LootSerializerType<XPEntry>> getType() {
        return GrailLootSerializer.XP;
    }

    @Override
    public void accept(ServerPlayerEntity playerEntity, LootContext context) {
        playerEntity.giveExperiencePoints(this.range.generateInt(context.getRandom()));
    }

    public static class Serializer extends BaseSerializer<XPEntry> {

        @Override
        public XPEntry deserialize(JsonObject object, JsonDeserializationContext context, IRandomRange range, ILootCondition[] conditions) {
            return new XPEntry(range, conditions);
        }
    }
}