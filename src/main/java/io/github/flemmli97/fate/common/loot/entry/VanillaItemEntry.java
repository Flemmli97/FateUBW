package io.github.flemmli97.fate.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.fate.common.loot.GrailLootEntry;
import io.github.flemmli97.fate.common.loot.LootSerializerType;
import io.github.flemmli97.fate.common.registry.GrailLootSerializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class VanillaItemEntry extends GrailLootEntry<VanillaItemEntry> {

    private LootEntry lootEntry;

    public VanillaItemEntry(LootEntry itemLootEntry) {
        super(ConstantRange.of(1), new ILootCondition[0]);
        this.lootEntry = itemLootEntry;
    }

    @Override
    public Supplier<LootSerializerType<VanillaItemEntry>> getType() {
        return GrailLootSerializer.VANILLA;
    }

    @Override
    public void give(ServerPlayerEntity player, LootContext context) {
        Consumer<ItemStack> givePlayer = stack -> player.addItemStackToInventory(stack);
        this.lootEntry.expand(context, gen -> gen.func_216188_a(givePlayer, context));
    }

    @Override
    public void accept(PlayerEntity playerEntity, LootContext context) {
        Consumer<ItemStack> givePlayer = stack -> playerEntity.addItemStackToInventory(stack);
        this.lootEntry.expand(context, gen -> gen.func_216188_a(givePlayer, context));
    }

    public static class Serializer implements ILootSerializer<VanillaItemEntry> {

        @Override
        public void serialize(JsonObject obj, VanillaItemEntry entry, JsonSerializationContext ctx) {
            JsonElement val = ctx.serialize(entry.lootEntry);
            obj.add("Entry", val);
        }

        @Override
        public VanillaItemEntry deserialize(JsonObject obj, JsonDeserializationContext ctx) {
            LootEntry entry = ctx.deserialize(obj.get("Entry"), LootEntry.class);
            return new VanillaItemEntry(entry);
        }
    }
}
