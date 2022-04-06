package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleType;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;

public class ModParticles {

    public static final PlatformRegistry<ParticleType<?>> PARTICLES = PlatformUtils.INSTANCE.of(Registry.PARTICLE_TYPE_REGISTRY, Fate.MODID);

    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> light = PARTICLES.register("light", () -> new ColoredParticleType(false));

}
