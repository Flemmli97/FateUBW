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
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class FateGrailLootSerializer {

    public static final ResourceKey<Registry<LootSerializerType<?>>> SERIALIZER_KEY = ResourceKey.createRegistryKey(Fate.modRes("grail_loot_serialzer"));
    public static final LoaderRegistryAccess.CustomLoaderRegistry<LootSerializerType<?>> SERIALIZER = LoaderRegistryAccess.INSTANCE.newRegistry(SERIALIZER_KEY, Fate.modRes("default_serializer"), true, true);
    public static final LoaderRegister<LootItemFunctionType<?>> LOOT_FUNCTION = LoaderRegistryAccess.INSTANCE.of(Registries.LOOT_FUNCTION_TYPE, Fate.MODID);

    public static final RegistryEntrySupplier<LootSerializerType<?>, LootSerializerType<EmptyEntry>> EMPTY = SERIALIZER.register().register("default_serializer", () -> new LootSerializerType<>(EmptyEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<?>, LootSerializerType<VanillaItemEntry>> VANILLA = SERIALIZER.register().register("vanilla_entry", () -> new LootSerializerType<>(VanillaItemEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<?>, LootSerializerType<LootTableEntry>> LOOT_TABLE = SERIALIZER.register().register("loot_table_entry", () -> new LootSerializerType<>(LootTableEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<?>, LootSerializerType<XPEntry>> XP = SERIALIZER.register().register("xp_entry", () -> new LootSerializerType<>(XPEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<?>, LootSerializerType<AttributeEntry>> ATTRIBUTE = SERIALIZER.register().register("attribute_entry", () -> new LootSerializerType<>(AttributeEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<?>, LootSerializerType<CommandEntry>> COMMAND = SERIALIZER.register().register("command_entry", () -> new LootSerializerType<>(CommandEntry.CODEC));
    public static final RegistryEntrySupplier<LootSerializerType<?>, LootSerializerType<ServantEntry>> SERVANT = SERIALIZER.register().register("servant_entry", () -> new LootSerializerType<>(ServantEntry.CODEC));

    public static final RegistryEntrySupplier<LootItemFunctionType<?>, LootItemFunctionType<EnchantMaxFunction>> MAX_ENCHANT = LOOT_FUNCTION.register("max_enchant", () -> new LootItemFunctionType<>(EnchantMaxFunction.CODEC));
}
