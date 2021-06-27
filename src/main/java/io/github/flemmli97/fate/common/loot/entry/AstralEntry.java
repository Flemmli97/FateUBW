package io.github.flemmli97.fate.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.loot.GrailLootEntry;
import io.github.flemmli97.fate.common.loot.LootSerializerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.function.Supplier;

public class AstralEntry extends GrailLootEntry<AstralEntry> {

    public AstralEntry(IRandomRange range, ILootCondition... conditions) {
        super(range, conditions);
    }

    @Override
    public Supplier<LootSerializerType<AstralEntry>> getType() {
        return null;
    }

    @Override
    public void accept(PlayerEntity playerEntity, LootContext context) {
        if (Fate.astralSorcery) {

        }
    }

    public static class Serializer extends BaseSerializer<AstralEntry> {

        @Override
        public AstralEntry deserialize(JsonObject object, JsonDeserializationContext context, IRandomRange range, ILootCondition[] conditions) {
            return new AstralEntry(range, conditions);
        }
    }
}
