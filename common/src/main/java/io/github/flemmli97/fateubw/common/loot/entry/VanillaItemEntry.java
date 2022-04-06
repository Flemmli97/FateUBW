package io.github.flemmli97.fateubw.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class VanillaItemEntry extends GrailLootEntry<VanillaItemEntry> {

    private LootPoolEntryContainer lootEntry;

    public VanillaItemEntry(LootPoolEntryContainer itemLootEntry) {
        super(ConstantValue.exactly(1), new LootItemCondition[0]);
        this.lootEntry = itemLootEntry;
    }

    @Override
    public Supplier<LootSerializerType<VanillaItemEntry>> getType() {
        return GrailLootSerializer.VANILLA;
    }

    @Override
    public void give(ServerPlayer player, LootContext context) {
        Consumer<ItemStack> givePlayer = player::addItem;
        this.lootEntry.expand(context, gen -> gen.createItemStack(givePlayer, context));
    }

    @Override
    public void accept(ServerPlayer playerEntity, LootContext context) {
        Consumer<ItemStack> givePlayer = playerEntity::addItem;
        this.lootEntry.expand(context, gen -> gen.createItemStack(givePlayer, context));
    }

    public static class SerializerImpl implements Serializer<VanillaItemEntry> {

        @Override
        public void serialize(JsonObject obj, VanillaItemEntry entry, JsonSerializationContext ctx) {
            JsonElement val = ctx.serialize(entry.lootEntry);
            obj.add("Entry", val);
        }

        @Override
        public VanillaItemEntry deserialize(JsonObject obj, JsonDeserializationContext ctx) {
            LootPoolEntryContainer entry = ctx.deserialize(obj.get("Entry"), LootPoolEntryContainer.class);
            return new VanillaItemEntry(entry);
        }
    }
}
