package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootCodecs;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class AttributeEntry extends GrailLootEntry<AttributeEntry> {

    public static final UUID ATTRIBUTE_UUID = UUID.fromString("804c9232-325f-484a-b60f-061b99e46ba2");

    public static final Codec<AttributeEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                    Registry.ATTRIBUTE.byNameCodec().fieldOf("attribute").forGetter(d -> d.att),
                    Codec.DOUBLE.fieldOf("max").forGetter(d -> d.max),
                    LootCodecs.NUMBER_PROVIDER_CODEC.fieldOf("range").forGetter(d -> d.range),
                    LootCodecs.LOOT_ITEM_CONDITION.listOf().optionalFieldOf("conditions").forGetter(d -> d.conditions.length == 0 ? Optional.empty() : Optional.of(Arrays.stream(d.conditions).toList()))
            ).apply(inst, (att, max, range, cond) -> new AttributeEntry(att, max, range, cond.map(l -> l.toArray(l.toArray(new LootItemCondition[0]))).orElse(new LootItemCondition[0])))
    );

    private final Attribute att;
    private final double max;
    private final NumberProvider range;

    public AttributeEntry(Attribute att, double max, NumberProvider range, LootItemCondition... conditions) {
        super(conditions);
        this.att = att;
        this.max = max;
        this.range = range;
    }

    @Override
    public Supplier<LootSerializerType<AttributeEntry>> getType() {
        return GrailLootSerializer.ATTRIBUTE;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
        AttributeInstance inst = player.getAttribute(this.att);
        if (inst != null) {
            AttributeModifier mod = inst.getModifier(ATTRIBUTE_UUID);
            double val = this.range.getFloat(context);
            if (mod != null) {
                val += mod.getAmount();
                inst.removeModifier(ATTRIBUTE_UUID);
            }
            inst.addPermanentModifier(new AttributeModifier(ATTRIBUTE_UUID, "fate.modifier", Math.min(val, this.max), AttributeModifier.Operation.ADDITION));
        }
    }
}