package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootCodecs;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class XPEntry extends GrailLootEntry<XPEntry> {

    public static final Codec<XPEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                    LootCodecs.NUMBER_PROVIDER_CODEC.fieldOf("range").forGetter(d -> d.range),
                    LootCodecs.LOOT_ITEM_CONDITION.listOf().optionalFieldOf("conditions").forGetter(d -> d.conditions.length == 0 ? Optional.empty() : Optional.of(Arrays.stream(d.conditions).toList()))
            ).apply(inst, (range, cond) -> new XPEntry(range, cond.map(l -> l.toArray(l.toArray(new LootItemCondition[0]))).orElse(new LootItemCondition[0])))
    );

    private final NumberProvider range;

    public XPEntry(NumberProvider range, LootItemCondition... conditions) {
        super(conditions);
        this.range = range;
    }

    @Override
    public Supplier<LootSerializerType<XPEntry>> getType() {
        return GrailLootSerializer.XP;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
        player.giveExperiencePoints(this.range.getInt(context));
    }
}