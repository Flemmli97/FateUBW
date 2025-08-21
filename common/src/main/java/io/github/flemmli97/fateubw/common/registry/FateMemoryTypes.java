package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public class FateMemoryTypes {

    public static final LoaderRegister<MemoryModuleType<?>> MEMORIES = LoaderRegistryAccess.INSTANCE.of(Registries.MEMORY_MODULE_TYPE, Fate.MODID);

    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<Unit>> STAYING = MEMORIES.register("staying", () -> new MemoryModuleType<>(Optional.empty()));
}
