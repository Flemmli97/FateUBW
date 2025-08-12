package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class XPEntry extends GrailLootEntry<XPEntry> {

    public static final MapCodec<XPEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    NumberProviders.CODEC.fieldOf("range").forGetter(d -> d.range),
                    LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions").forGetter(d -> d.conditions.isEmpty() ? Optional.empty() : Optional.of(d.conditions))
            ).apply(inst, (range, cond) -> new XPEntry(range, cond.orElse(List.of())))
    );

    private final NumberProvider range;

    public XPEntry(NumberProvider range, LootItemCondition... conditions) {
        this(range, List.of(conditions));
    }

    private XPEntry(NumberProvider range, List<LootItemCondition> conditions) {
        super(conditions);
        this.range = range;
    }

    @Override
    public Supplier<LootSerializerType<XPEntry>> getType() {
        return FateGrailLootSerializer.XP;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
        player.giveExperiencePoints(this.range.getInt(context));
    }
}