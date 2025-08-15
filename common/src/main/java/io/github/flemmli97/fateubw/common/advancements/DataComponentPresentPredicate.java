package io.github.flemmli97.fateubw.common.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DataComponentPresentPredicate implements ItemSubPredicate {

    public static final Codec<DataComponentPresentPredicate> CODEC = BuiltInRegistries.DATA_COMPONENT_TYPE
            .byNameCodec().listOf().xmap(DataComponentPresentPredicate::new, d -> d.expectedComponents);
    public static final Type<DataComponentPresentPredicate> TYPE = new Type<>(DataComponentPresentPredicate.CODEC);

    private final List<DataComponentType<?>> expectedComponents;

    private DataComponentPresentPredicate(List<DataComponentType<?>> expectedComponents) {
        this.expectedComponents = expectedComponents;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean matches(ItemStack stack) {
        for (DataComponentType<?> component : this.expectedComponents) {
            if (!stack.has(component)) {
                return false;
            }
        }
        return true;
    }

    public static class Builder {

        private final List<DataComponentType<?>> expectedComponents = new ArrayList<>();

        public Builder expect(DataComponentType<?> type) {
            this.expectedComponents.add(type);
            return this;
        }

        public DataComponentPresentPredicate build() {
            return new DataComponentPresentPredicate(this.expectedComponents);
        }
    }
}
