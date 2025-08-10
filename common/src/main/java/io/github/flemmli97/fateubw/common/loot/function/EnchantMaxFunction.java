package io.github.flemmli97.fateubw.common.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class EnchantMaxFunction extends LootItemConditionalFunction {

    public static final MapCodec<EnchantMaxFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantments").forGetter(d -> d.enchantments))
                    .apply(instance, EnchantMaxFunction::new)
    );
    private final HolderSet<Enchantment> enchantments;

    private EnchantMaxFunction(List<LootItemCondition> conditions, HolderSet<Enchantment> possibleEnchantments) {
        super(conditions);
        this.enchantments = possibleEnchantments;
    }

    @Override
    public LootItemFunctionType getType() {
        return FateGrailLootSerializer.MAX_ENCHANT.get();
    }

    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        if (this.enchantments.size() == 0) {
            return stack;
        }
        return enchantItem(stack, this.enchantments.get(context.getRandom().nextInt(this.enchantments.size())));
    }

    private static ItemStack enchantItem(ItemStack stack, Holder<Enchantment> enchantment) {
        if (stack.is(Items.BOOK)) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, enchantment.value().getMaxLevel()));
        } else {
            stack.enchant(enchantment, enchantment.value().getMaxLevel());
        }
        return stack;
    }

    public static EnchantMaxFunction.Builder builder(HolderLookup.Provider provider) {
        return new EnchantMaxFunction.Builder(provider);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {

        private final HolderGetter<Enchantment> getter;

        private HolderSet<Enchantment> enchantments = HolderSet.empty();

        private Builder(HolderLookup.Provider provider) {
            this.getter = provider.lookupOrThrow(Registries.ENCHANTMENT);
        }

        @SafeVarargs
        public final Builder withEnchantment(ResourceKey<Enchantment>... enchantment) {
            this.enchantments = HolderSet.direct(this.getter::getOrThrow, enchantment);
            return this;
        }

        public Builder withEnchants(HolderSet<Enchantment> options) {
            this.enchantments = options;
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new EnchantMaxFunction(this.getConditions(), this.enchantments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}

