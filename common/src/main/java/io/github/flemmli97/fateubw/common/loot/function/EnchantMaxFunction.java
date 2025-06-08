package io.github.flemmli97.fateubw.common.loot.function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class EnchantMaxFunction extends LootItemConditionalFunction {

    private final List<Enchantment> enchantments;

    private EnchantMaxFunction(LootItemCondition[] conditions, Collection<Enchantment> possibleEnchantments) {
        super(conditions);
        this.enchantments = ImmutableList.copyOf(possibleEnchantments);
    }

    @Override
    public LootItemFunctionType getType() {
        return GrailLootSerializer.MAX_ENCHANT.get();
    }

    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        if (this.enchantments.isEmpty()) {
            return stack;
        }
        return enchantItem(stack, this.enchantments.get(context.getRandom().nextInt(this.enchantments.size())));
    }

    private static ItemStack enchantItem(ItemStack stack, Enchantment enchantment) {
        if (stack.is(Items.BOOK)) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(enchantment, enchantment.getMaxLevel()));
        } else {
            stack.enchant(enchantment, enchantment.getMaxLevel());
        }
        return stack;
    }

    public static EnchantMaxFunction.Builder builder() {
        return new EnchantMaxFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final Set<Enchantment> enchantments = Sets.newHashSet();

        public Builder withEnchantment(Enchantment enchantment) {
            this.enchantments.add(enchantment);
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

    public static class Serializer extends LootItemConditionalFunction.Serializer<EnchantMaxFunction> {

        @Override
        public void serialize(JsonObject json, EnchantMaxFunction value, JsonSerializationContext serializationContext) {
            super.serialize(json, value, serializationContext);
            JsonArray jsonArray = new JsonArray();
            value.enchantments.forEach(enchantment -> {
                ResourceLocation id = Registry.ENCHANTMENT.getKey(enchantment);
                if (id == null) {
                    throw new IllegalArgumentException("Don't know how to serialize enchantment " + enchantment);
                }
                jsonArray.add(id.toString());
            });
            json.add("enchantments", jsonArray);
        }

        @Override
        public EnchantMaxFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            ArrayList<Enchantment> list = Lists.newArrayList();
            JsonArray arr = GsonHelper.getAsJsonArray(object, "enchantments", new JsonArray());
            arr.forEach(e -> {
                Enchantment enchantment = Registry.ENCHANTMENT.getOptional(new ResourceLocation(e.getAsString())).orElseThrow(() -> new JsonSyntaxException("Unknown enchantment '" + e + "'"));
                list.add(enchantment);
            });
            return new EnchantMaxFunction(conditions, list);
        }
    }
}

