package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fateubw.common.loot.entry.CommandEntry;
import io.github.flemmli97.fateubw.common.loot.entry.EmptyEntry;
import io.github.flemmli97.fateubw.common.loot.entry.LootTableEntry;
import io.github.flemmli97.fateubw.common.loot.entry.ServantEntry;
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

    public static RegistryEntrySupplier<LootSerializerType<EmptyEntry>> EMPTY = SERIALIZER.register("default_serializer", () -> new LootSerializerType<>(EmptyEntry.CODEC));
    public static RegistryEntrySupplier<LootSerializerType<VanillaItemEntry>> VANILLA = SERIALIZER.register("vanilla_entry", () -> new LootSerializerType<>(VanillaItemEntry.CODEC));
    public static RegistryEntrySupplier<LootSerializerType<LootTableEntry>> LOOT_TABLE = SERIALIZER.register("loot_table_entry", () -> new LootSerializerType<>(LootTableEntry.CODEC));
    public static RegistryEntrySupplier<LootSerializerType<XPEntry>> XP = SERIALIZER.register("xp_entry", () -> new LootSerializerType<>(XPEntry.CODEC));
    public static RegistryEntrySupplier<LootSerializerType<AttributeEntry>> ATTRIBUTE = SERIALIZER.register("attribute_entry", () -> new LootSerializerType<>(AttributeEntry.CODEC));
    public static RegistryEntrySupplier<LootSerializerType<CommandEntry>> COMMAND = SERIALIZER.register("command_entry", () -> new LootSerializerType<>(CommandEntry.CODEC));
    public static RegistryEntrySupplier<LootSerializerType<ServantEntry>> SERVANT = SERIALIZER.register("servant_entry", () -> new LootSerializerType<>(ServantEntry.CODEC));

}
