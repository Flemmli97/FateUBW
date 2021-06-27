package io.github.flemmli97.fate.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.fate.common.loot.GrailLootEntry;
import io.github.flemmli97.fate.common.loot.LootSerializerType;
import io.github.flemmli97.fate.common.registry.GrailLootSerializer;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.Supplier;

public class AttributeEntry extends GrailLootEntry<AttributeEntry> {

    private static final UUID attributeUUID = UUID.fromString("804c9232-325f-484a-b60f-061b99e46ba2");
    private final Attribute att;

    public AttributeEntry(Attribute att, IRandomRange range, ILootCondition... conditions) {
        super(range, conditions);
        this.att = att;
    }

    @Override
    public Supplier<LootSerializerType<AttributeEntry>> getType() {
        return GrailLootSerializer.ATTRIBUTE;
    }

    @Override
    public void accept(PlayerEntity playerEntity, LootContext context) {
        ModifiableAttributeInstance inst = playerEntity.getAttribute(this.att);
        if (inst != null) {
            AttributeModifier mod = inst.getModifier(attributeUUID);
            double val;
            if (this.range instanceof RandomValueRange)
                val = ((RandomValueRange) this.range).generateFloat(context.getRandom());
            else
                val = this.range.generateInt(context.getRandom());
            if (mod != null) {
                val += mod.getAmount();
                inst.removeModifier(attributeUUID);
            }
            inst.applyPersistentModifier(new AttributeModifier(attributeUUID, "fate.modifier", val, AttributeModifier.Operation.ADDITION));
        }
    }

    public static class Serializer extends BaseSerializer<AttributeEntry> {

        @Override
        public void serialize(JsonObject obj, AttributeEntry entry, JsonSerializationContext context) {
            super.serialize(obj, entry, context);
            ResourceLocation res = entry.att.getRegistryName();
            obj.addProperty("Attribute", res.toString());
        }

        @Override
        public AttributeEntry deserialize(JsonObject object, JsonDeserializationContext context, IRandomRange range, ILootCondition[] conditions) {
            return new AttributeEntry(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(object.get("Attribute").getAsString())), range, conditions);
        }
    }
}