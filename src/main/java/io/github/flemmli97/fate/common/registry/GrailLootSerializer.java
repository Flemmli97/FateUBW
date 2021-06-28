package io.github.flemmli97.fate.common.registry;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.loot.LootSerializerType;
import io.github.flemmli97.fate.common.loot.entry.AstralEntry;
import io.github.flemmli97.fate.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fate.common.loot.entry.EmptyEntry;
import io.github.flemmli97.fate.common.loot.entry.VanillaItemEntry;
import io.github.flemmli97.fate.common.loot.entry.XPEntry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class GrailLootSerializer {

    public static final DeferredRegister<LootSerializerType<?>> SERIALIZER = DeferredRegister.create(LootSerializerType.CLASS, Fate.MODID);


    public static RegistryObject<LootSerializerType<EmptyEntry>> EMPTY = SERIALIZER.register("default_serializer", () -> new LootSerializerType<>(new EmptyEntry.Serializer()));
    public static RegistryObject<LootSerializerType<VanillaItemEntry>> VANILLA = SERIALIZER.register("vanilla_entry", () -> new LootSerializerType<>(new VanillaItemEntry.Serializer()));
    public static RegistryObject<LootSerializerType<XPEntry>> XP = SERIALIZER.register("xp_entry", () -> new LootSerializerType<>(new XPEntry.Serializer()));
    public static RegistryObject<LootSerializerType<AttributeEntry>> ATTRIBUTE = SERIALIZER.register("attribute_entry", () -> new LootSerializerType<>(new AttributeEntry.Serializer()));
    public static RegistryObject<LootSerializerType<AstralEntry>> ASTRAL = SERIALIZER.register("astral_entry", () -> new LootSerializerType<>(new AstralEntry.Serializer()));

}
