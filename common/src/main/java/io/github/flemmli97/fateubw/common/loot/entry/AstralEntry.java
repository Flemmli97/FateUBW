package io.github.flemmli97.fateubw.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.integration.AstralSorcery;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.function.Supplier;

public class AstralEntry extends GrailLootEntry<AstralEntry> {

    public AstralEntry(NumberProvider range, LootItemCondition... conditions) {
        super(range, conditions);
    }

    @Override
    public Supplier<LootSerializerType<AstralEntry>> getType() {
        return GrailLootSerializer.ASTRAL;
    }

    @Override
    public void accept(ServerPlayer playerEntity, LootContext context) {
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
        public AstralEntry deserialize(JsonObject object, JsonDeserializationContext context, NumberProvider range, LootItemCondition[] conditions) {
            return new AstralEntry(range, conditions);
        }
    }
}
