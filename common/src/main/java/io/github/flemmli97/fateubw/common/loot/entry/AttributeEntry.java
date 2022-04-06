package io.github.flemmli97.fateubw.common.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.UUID;
import java.util.function.Supplier;

public class AttributeEntry extends GrailLootEntry<AttributeEntry> {

    public static final UUID attributeUUID = UUID.fromString("804c9232-325f-484a-b60f-061b99e46ba2");
    private final Attribute att;

    public AttributeEntry(Attribute att, NumberProvider range, LootItemCondition... conditions) {
        super(range, conditions);
        this.att = att;
    }

    @Override
    public Supplier<LootSerializerType<AttributeEntry>> getType() {
        return GrailLootSerializer.ATTRIBUTE;
    }

    @Override
    public void accept(ServerPlayer playerEntity, LootContext context) {
        AttributeInstance inst = playerEntity.getAttribute(this.att);
        if (inst != null) {
            AttributeModifier mod = inst.getModifier(attributeUUID);
            float val = this.range.getFloat(context);
            if (mod != null) {
                val += mod.getAmount();
                inst.removeModifier(attributeUUID);
            }
            inst.addPermanentModifier(new AttributeModifier(attributeUUID, "fate.modifier", val, AttributeModifier.Operation.ADDITION));
        }
    }

    public static class Serializer extends BaseSerializer<AttributeEntry> {

        @Override
        public void serialize(JsonObject obj, AttributeEntry entry, JsonSerializationContext context) {
            super.serialize(obj, entry, context);
            ResourceLocation res = PlatformUtils.INSTANCE.attributes().getIDFrom(entry.att);
            obj.addProperty("Attribute", res.toString());
        }

        @Override
        public AttributeEntry deserialize(JsonObject object, JsonDeserializationContext context, NumberProvider range, LootItemCondition[] conditions) {
            return new AttributeEntry(PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(object.get("Attribute").getAsString())), range, conditions);
        }
    }
}