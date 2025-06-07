package io.github.flemmli97.fateubw.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ServantProperties {

    public static final Codec<ServantProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("attributes").forGetter(d -> d.attributes),
                    Codec.INT.fieldOf("nobel_phantasm_cost").forGetter(d -> d.manaCost),
                    ResourceLocation.CODEC.fieldOf("class").forGetter(d -> d.servantClass),
                    ServantExtraData.CODEC.optionalFieldOf("configs").forGetter(d -> d.extraData.empty() ? Optional.empty() : Optional.of(d.extraData))
            ).apply(instance, ServantProperties::new));

    public static final ServantProperties DEFAULT = new ServantProperties.Builder(BuiltinServantClasses.NONE)
            .putAttributes(() -> Attributes.MAX_HEALTH, 20).putAttributes(() -> Attributes.ATTACK_DAMAGE, 1)
            .putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.2).putAttributes(ModAttributes.MAGIC_ATTACK, 1).build();

    private final Map<Attribute, Double> attributes;
    private final int manaCost;
    private final ResourceLocation servantClass;
    private final ServantExtraData extraData;

    private ServantProperties(Map<Attribute, Double> attributes, int manaCost, ResourceLocation servantClass, Optional<ServantExtraData> extraData) {
        this(attributes, manaCost, servantClass, extraData.orElse(new ServantExtraData(Map.of())));
    }

    public ServantProperties(Map<Attribute, Double> attributes, int manaCost, ResourceLocation servantClass, ServantExtraData extraData) {
        this.attributes = attributes;
        this.manaCost = manaCost;
        this.servantClass = servantClass;
        this.extraData = extraData;
    }

    public Map<Attribute, Double> getAttributes() {
        return ImmutableMap.copyOf(this.attributes);
    }

    public int hogouMana() {
        return this.manaCost;
    }

    public ResourceLocation getServantClass() {
        return this.servantClass;
    }

    public <T> T getConfig(ServantExtraData.DataType<T> type) {
        return this.extraData.get(type);
    }

    public static class Builder {

        private final Map<Supplier<Attribute>, Double> attributes = new LinkedHashMap<>();
        private int manaCost;
        private final ResourceLocation servantClass;
        private final Map<ServantExtraData.DataType<?>, Object> values = new LinkedHashMap<>();

        public Builder(ResourceLocation servantClass) {
            this.servantClass = servantClass;
        }

        public Builder putAttributes(Supplier<Attribute> att, double val) {
            this.attributes.put(att, val);
            return this;
        }

        public Builder npCost(int manaCost) {
            this.manaCost = manaCost;
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
            return new ServantProperties(this.attributes.entrySet().stream().collect(Collectors.toMap(
                    e -> e.getKey().get(),
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
            )), this.manaCost, this.servantClass, new ServantExtraData(this.values));
        }
    }
}