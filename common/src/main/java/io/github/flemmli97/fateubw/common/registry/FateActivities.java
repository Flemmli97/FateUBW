package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.schedule.Activity;

public class FateActivities {

    public static final LoaderRegister<Activity> ACTIVITIES = LoaderRegistryAccess.INSTANCE.of(Registries.ACTIVITY, Fate.MODID);

    public static final RegistryEntrySupplier<Activity, Activity> STAY = ACTIVITIES.register("stay", () -> new Activity("stay"));
}
