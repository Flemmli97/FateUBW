package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.loot.entry.AstralEntry;
import io.github.flemmli97.fateubw.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fateubw.common.loot.entry.EmptyEntry;
import io.github.flemmli97.fateubw.common.loot.entry.VanillaItemEntry;
import io.github.flemmli97.fateubw.common.loot.entry.XPEntry;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class GrailLootSerializer {

    public static final ResourceKey<Registry<LootSerializerType<?>>> SERIALIZER_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Fate.MODID, "grail_loot_serialzer"));
    public static final PlatformRegistry<LootSerializerType<?>> SERIALIZER = PlatformUtils.INSTANCE.customRegistry(LootSerializerType.CLASS, SERIALIZER_KEY, new ResourceLocation(Fate.MODID, "default_serializer"), true, true);

    public static RegistryEntrySupplier<LootSerializerType<EmptyEntry>> EMPTY = SERIALIZER.register("default_serializer", () -> new LootSerializerType<>(new EmptyEntry.SerializerImpl()));
    public static RegistryEntrySupplier<LootSerializerType<VanillaItemEntry>> VANILLA = SERIALIZER.register("vanilla_entry", () -> new LootSerializerType<>(new VanillaItemEntry.SerializerImpl()));
    public static RegistryEntrySupplier<LootSerializerType<XPEntry>> XP = SERIALIZER.register("xp_entry", () -> new LootSerializerType<>(new XPEntry.Serializer()));
    public static RegistryEntrySupplier<LootSerializerType<AttributeEntry>> ATTRIBUTE = SERIALIZER.register("attribute_entry", () -> new LootSerializerType<>(new AttributeEntry.Serializer()));
    public static RegistryEntrySupplier<LootSerializerType<AstralEntry>> ASTRAL = SERIALIZER.register("astral_entry", () -> new LootSerializerType<>(new AstralEntry.Serializer()));

}
