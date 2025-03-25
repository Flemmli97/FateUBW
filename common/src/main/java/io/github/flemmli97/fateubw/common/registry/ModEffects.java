package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.effects.GaeBuidheCurse;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {

    public static final PlatformRegistry<MobEffect> EFFECTS = PlatformUtils.INSTANCE.of(Registry.MOB_EFFECT_REGISTRY, Fate.MODID);

    public static final RegistryEntrySupplier<MobEffect> GAE_BUIDHE = EFFECTS.register("cursed_wounds", GaeBuidheCurse::new);

}
