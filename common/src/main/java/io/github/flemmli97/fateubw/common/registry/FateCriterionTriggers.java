package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class FateCriterionTriggers {

    public static final LoaderRegister<CriterionTrigger<?>> TRIGGERS = LoaderRegistryAccess.INSTANCE.of(Registries.TRIGGER_TYPE, Fate.MODID);

    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> JOIN_GRAIL_WAR = register("join_grail_war", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> WIN_GRAIL_WAR = register("win_grail_war", PlayerTrigger::new);

    private static <T extends CriterionTrigger<?>> RegistryEntrySupplier<CriterionTrigger<?>, T> register(String name, Supplier<T> inst) {
        return TRIGGERS.register(name, inst);
    }
}
