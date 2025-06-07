package io.github.flemmli97.fateubw.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record AttributeHolderProperties(Map<Attribute, Double> attributes) {

    public static final Codec<AttributeHolderProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("attributes").forGetter(d -> d.attributes)
            ).apply(instance, AttributeHolderProperties::new));

    public static final AttributeHolderProperties DEFAULT = new AttributeHolderProperties.Builder()
            .putAttributes(() -> Attributes.MAX_HEALTH, 20).putAttributes(() -> Attributes.ATTACK_DAMAGE, 1)
            .putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.2).putAttributes(ModAttributes.MAGIC_ATTACK, 1).build();

    @Override
    public Map<Attribute, Double> attributes() {
        return ImmutableMap.copyOf(this.attributes);
    }

    public static class Builder {

        private final Map<Supplier<Attribute>, Double> attributes = new LinkedHashMap<>();

        public Builder putAttributes(Supplier<Attribute> att, double val) {
            this.attributes.put(att, val);
            return this;
        }

        public AttributeHolderProperties build() {
            return new AttributeHolderProperties(this.attributes.entrySet().stream().collect(Collectors.toMap(
                    e -> e.getKey().get(),
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
            )));
        }
    }
}