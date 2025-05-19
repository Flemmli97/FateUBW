package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static final PlatformRegistry<SoundEvent> SOUND_EVENTS = PlatformUtils.INSTANCE.of(Registry.SOUND_EVENT_REGISTRY, Fate.MODID);

    public static final RegistryEntrySupplier<SoundEvent> ENTITY_BABYLON_SPAWN = register("entity.babylon.spawn");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_BABYLON_SHOOT = register("entity.babylon.shoot");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_EXCALIBUR_SHOOT = register("entity.excalibur.shoot");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_EA_SHOOT = register("entity.ea.shoot");
    public static final RegistryEntrySupplier<SoundEvent> HERACLES_ROAR = register("entity.heracles.roar");

    private static RegistryEntrySupplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(Fate.MODID, name)));
    }
}
