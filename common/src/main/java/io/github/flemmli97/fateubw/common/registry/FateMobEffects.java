package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.effects.GaeBuidheCurse;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

public class FateMobEffects {

    public static final LoaderRegister<MobEffect> EFFECTS = LoaderRegistryAccess.INSTANCE.of(Registries.MOB_EFFECT, Fate.MODID);

    public static final RegistryEntrySupplier<MobEffect, MobEffect> GAE_BUIDHE = EFFECTS.register("cursed_wounds", GaeBuidheCurse::new);

}
