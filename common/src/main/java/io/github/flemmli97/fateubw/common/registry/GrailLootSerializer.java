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
import io.github.flemmli97.fateubw.common.loot.function.EnchantMaxFunction;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class GrailLootSerializer {

    public static final ResourceKey<Registry<LootSerializerType<?>>> SERIALIZER_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Fate.MODID, "grail_loot_serialzer"));
    public static final PlatformRegistry<LootSerializerType<?>> SERIALIZER = PlatformUtils.INSTANCE.customRegistry(LootSerializerType.CLASS, SERIALIZER_KEY, new ResourceLocation(Fate.MODID, "default_serializer"), true, true);
    public static final PlatformRegistry<LootItemFunctionType> LOOT_FUNCTION = PlatformUtils.INSTANCE.of(Registry.LOOT_FUNCTION_REGISTRY, Fate.MODID);

    public static final RegistryEntrySupplier<LootSerializerType<EmptyEntry>> EMPTY = SERIALIZER.register("default_serializer", () -> new LootSerializerType<>(EmptyEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<VanillaItemEntry>> VANILLA = SERIALIZER.register("vanilla_entry", () -> new LootSerializerType<>(VanillaItemEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<LootTableEntry>> LOOT_TABLE = SERIALIZER.register("loot_table_entry", () -> new LootSerializerType<>(LootTableEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<XPEntry>> XP = SERIALIZER.register("xp_entry", () -> new LootSerializerType<>(XPEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<AttributeEntry>> ATTRIBUTE = SERIALIZER.register("attribute_entry", () -> new LootSerializerType<>(AttributeEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<CommandEntry>> COMMAND = SERIALIZER.register("command_entry", () -> new LootSerializerType<>(CommandEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<ServantEntry>> SERVANT = SERIALIZER.register("servant_entry", () -> new LootSerializerType<>(ServantEntry.CODEC));

    public static final RegistryEntrySupplier<LootItemFunctionType> MAX_ENCHANT = LOOT_FUNCTION.register("max_enchant", () -> new LootItemFunctionType(new EnchantMaxFunction.Serializer()));

}
