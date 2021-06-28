package io.github.flemmli97.fate.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.integration.AstralSorcery;
import io.github.flemmli97.fate.common.loot.GrailLootEntry;
import io.github.flemmli97.fate.common.loot.LootSerializerType;
import io.github.flemmli97.fate.common.registry.GrailLootSerializer;
import net.minecraft.entity.player.ServerPlayerEntity;
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
        return GrailLootSerializer.ASTRAL;
    }

    @Override
    public void accept(ServerPlayerEntity playerEntity, LootContext context) {
        if (Fate.astralSorcery) {
            AstralSorcery.grantFreePerkPoint(playerEntity);
        }
    }

    @Override
    public boolean valid() {
        return Fate.astralSorcery;
    }

    public static class Serializer extends BaseSerializer<AstralEntry> {

        @Override
        public AstralEntry deserialize(JsonObject object, JsonDeserializationContext context, IRandomRange range, ILootCondition[] conditions) {
            return new AstralEntry(range, conditions);
        }
    }
}
