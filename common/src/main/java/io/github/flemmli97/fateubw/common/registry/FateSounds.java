package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FateSounds {

    public static final LoaderRegister<SoundEvent> SOUND_EVENTS = LoaderRegistryAccess.INSTANCE.of(Registries.SOUND_EVENT, Fate.MODID);
    public static final Map<ResourceLocation, SoundHolder> SOUND_DATA = new HashMap<>();

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_BABYLON_SPAWN = register("entity.babylon.spawn", null, SoundEvents.BEACON_ACTIVATE.getLocation());
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_BABYLON_SHOOT = register("entity.babylon.shoot", null, SoundEvents.PLAYER_ATTACK_SWEEP.getLocation(), 7);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_EXCALIBUR_SHOOT = register("entity.excalibur.shoot", null, ResourceLocation.withDefaultNamespace("random/explode"), 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_EA_SHOOT = register("entity.ea.shoot", null, ResourceLocation.withDefaultNamespace("random/explode"), 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> HERACLES_ROAR = register("entity.heracles.roar");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> AESTUS_DOMUS_ROSES = register("nero.aestus_domus_roses", "Rose-Petals Scatter");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> AESTUS_DOMUS_GROUND_STAB = register("nero.aestus_domus_ground_stab", "Aestus Domus Aurea Prepare", ResourceLocation.withDefaultNamespace("random/anvil_land"), 1, 0.4f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> AESTUS_DOMUS_IMPACT = register("nero.aestus_domus_impact", "Aestus Domus Aurea Impact", ResourceLocation.withDefaultNamespace("random/explode"), 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SLASH = register("generic.slash");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SLASH_IMPACT = register("generic.slash_impact");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SWOOSH = register("generic.swoosh");

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name) {
        return register(name, null, 1);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation) {
        return register(name, translation, 1);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, int variations) {
        return register(name, null, variations);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation, int variations) {
        RegistryEntrySupplier<SoundEvent, SoundEvent> res = SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            SOUND_DATA.put(res.getID(), new SoundHolder(res.getID(), variations, 1, translation));
        }
        return res;
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation, ResourceLocation location) {
        return register(name, translation, location, 1);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation, ResourceLocation location, int amount) {
        return register(name, translation, location, amount, 1);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation, ResourceLocation location, int amount, float pitch) {
        RegistryEntrySupplier<SoundEvent, SoundEvent> res = SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            SOUND_DATA.put(res.getID(), new SoundHolder(location, amount, pitch, translation));
        }
        return res;
    }

    public record SoundHolder(ResourceLocation location, int amount, float pitch, @Nullable String defaultTranslation) {
    }
}
