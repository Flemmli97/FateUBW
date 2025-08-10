package io.github.flemmli97.fateubw.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServantProperties {

    public static final Codec<ServantProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("attributes").forGetter(d -> d.attributes),
                    Codec.INT.fieldOf("nobel_phantasm_cost").forGetter(ServantProperties::hogouMana),
                    Codec.INT.fieldOf("weight").forGetter(ServantProperties::weight),
                    ResourceLocation.CODEC.fieldOf("class").forGetter(ServantProperties::getServantClass),
                    ServantExtraData.CODEC.optionalFieldOf("configs").forGetter(d -> d.extraData.empty() ? Optional.empty() : Optional.of(d.extraData))
            ).apply(instance, ServantProperties::new));

    public static final ServantProperties DEFAULT = new ServantProperties.Builder(BuiltinServantClasses.NONE)
            .putAttributes(Attributes.MAX_HEALTH, 20).putAttributes(Attributes.ATTACK_DAMAGE, 1)
            .putAttributes(Attributes.MOVEMENT_SPEED, 0.2).putAttributes(FateAttributes.MAGIC_ATTACK.asHolder(), 1).build();

    private final Map<Holder<Attribute>, Double> attributes;
    private final int manaCost, weight;
    private final ResourceLocation servantClass;
    private final ServantExtraData extraData;

    private ServantProperties(Map<Holder<Attribute>, Double> attributes, int manaCost, int weight, ResourceLocation servantClass, Optional<ServantExtraData> extraData) {
        this(attributes, manaCost, weight, servantClass, extraData.orElse(new ServantExtraData(Map.of())));
    }

    public ServantProperties(Map<Holder<Attribute>, Double> attributes, int manaCost, int weight, ResourceLocation servantClass, ServantExtraData extraData) {
        this.attributes = attributes;
        this.manaCost = manaCost;
        this.weight = weight;
        this.servantClass = servantClass;
        this.extraData = extraData;
    }

    public Map<Holder<Attribute>, Double> getAttributes() {
        return ImmutableMap.copyOf(this.attributes);
    }

    public int hogouMana() {
        return this.manaCost;
    }

    public int weight() {
        return this.weight;
    }

    public ResourceLocation getServantClass() {
        return this.servantClass;
    }

    public <T> T getConfig(ServantExtraData.DataType<T> type) {
        return this.extraData.get(type);
    }

    public static class Builder {

        private final Map<Holder<Attribute>, Double> attributes = new HashMap<>();
        private int manaCost;
        private int weight = 1;
        private final ResourceLocation servantClass;
        private final Map<ServantExtraData.DataType<?>, Object> values = new HashMap<>();

        public Builder(ResourceLocation servantClass) {
            this.servantClass = servantClass;
        }

        public Builder putAttributes(Holder<Attribute> att, double val) {
            this.attributes.put(att, val);
            return this;
        }

        public Builder npCost(int manaCost) {
            this.manaCost = manaCost;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public <T> Builder withConfigData(ServantExtraData.DataType<T> type) {
            return this.withConfigData(type, type.defaultValue());
        }

        public <T> Builder withConfigData(ServantExtraData.DataType<T> type, T value) {
            this.values.put(type, value);
            return this;
        }

        public ServantProperties build() {
            return new ServantProperties(this.attributes, this.manaCost, this.weight, this.servantClass, new ServantExtraData(this.values));
        }
    }
}