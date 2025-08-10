package io.github.flemmli97.fateubw.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;

public record AttributeHolderProperties(Map<Holder<Attribute>, Double> attributes) {

    public static final Codec<AttributeHolderProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("attributes").forGetter(d -> d.attributes)
            ).apply(instance, AttributeHolderProperties::new));

    public static final AttributeHolderProperties DEFAULT = new AttributeHolderProperties.Builder()
            .putAttributes(Attributes.MAX_HEALTH, 20).putAttributes(Attributes.ATTACK_DAMAGE, 1)
            .putAttributes(Attributes.MOVEMENT_SPEED, 0.2).putAttributes(FateAttributes.MAGIC_ATTACK.asHolder(), 1).build();

    @Override
    public Map<Holder<Attribute>, Double> attributes() {
        return ImmutableMap.copyOf(this.attributes);
    }

    public static class Builder {

        private final Map<Holder<Attribute>, Double> attributes = new HashMap<>();

        public Builder putAttributes(Holder<Attribute> att, double val) {
            this.attributes.put(att, val);
            return this;
        }

        public AttributeHolderProperties build() {
            return new AttributeHolderProperties(this.attributes);
        }
    }
}