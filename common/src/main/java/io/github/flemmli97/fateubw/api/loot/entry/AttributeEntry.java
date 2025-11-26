package io.github.flemmli97.fateubw.api.loot.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.api.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class AttributeEntry extends GrailLootEntry<AttributeEntry> {

    public static final ResourceLocation ATTRIBUTE_UUID = Fate.modRes("grail_loot_modifier");

    public static final MapCodec<AttributeEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(d -> d.att),
                    Codec.DOUBLE.fieldOf("max").forGetter(d -> d.max),
                    NumberProviders.CODEC.fieldOf("range").forGetter(d -> d.range),
                    LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions").forGetter(d -> d.conditions.isEmpty() ? Optional.empty() : Optional.of(d.conditions))
            ).apply(inst, (att, max, range, cond) -> new AttributeEntry(att, max, range, cond.orElse(List.of())))
    );

    private final Holder<Attribute> att;
    private final double max;
    private final NumberProvider range;

    public AttributeEntry(Holder<Attribute> att, double max, NumberProvider range, LootItemCondition... conditions) {
        this(att, max, range, List.of(conditions));
    }

    public AttributeEntry(Holder<Attribute> att, double max, NumberProvider range, List<LootItemCondition> conditions) {
        super(conditions);
        this.att = att;
        this.max = max;
        this.range = range;
    }

    @Override
    public Supplier<LootSerializerType<AttributeEntry>> getType() {
        return FateGrailLootSerializer.ATTRIBUTE;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
        AttributeInstance inst = player.getAttribute(this.att);
        if (inst != null) {
            AttributeModifier mod = inst.getModifier(ATTRIBUTE_UUID);
            double val = this.range.getFloat(context);
            if (mod != null) {
                val += mod.amount();
                inst.removeModifier(ATTRIBUTE_UUID);
            }
            inst.addPermanentModifier(new AttributeModifier(ATTRIBUTE_UUID, Math.min(val, this.max), AttributeModifier.Operation.ADD_VALUE));
        }
    }
}