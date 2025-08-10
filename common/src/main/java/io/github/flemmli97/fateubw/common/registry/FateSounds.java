package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class FateSounds {

    public static final LoaderRegister<SoundEvent> SOUND_EVENTS = LoaderRegistryAccess.INSTANCE.of(Registries.SOUND_EVENT, Fate.MODID);

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_BABYLON_SPAWN = register("entity.babylon.spawn");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_BABYLON_SHOOT = register("entity.babylon.shoot");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_EXCALIBUR_SHOOT = register("entity.excalibur.shoot");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_EA_SHOOT = register("entity.ea.shoot");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> HERACLES_ROAR = register("entity.heracles.roar");

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
    }
}
