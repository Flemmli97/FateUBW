package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.particles.ParticleTypeContainer;
import io.github.flemmli97.fateubw.common.particles.RingParticleData;
import io.github.flemmli97.fateubw.common.particles.SimpleParticleTypeExp;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class FateParticles {

    public static final LoaderRegister<ParticleType<?>> PARTICLES = LoaderRegistryAccess.INSTANCE.of(Registries.PARTICLE_TYPE, Fate.MODID);

    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> LIGHT = PARTICLES.register("light", () -> SimpleParticleTypeExp.of(false));
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<TrailParticleData>> TRAIL = PARTICLES.register("trail", () -> new ParticleTypeContainer<>(false, TrailParticleData::codec, TrailParticleData::streamCodec));
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<RingParticleData>> RING = PARTICLES.register("ring", () -> new ParticleTypeContainer<>(false, RingParticleData.CODEC, RingParticleData.STREAM_CODEC));
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> FLASH = PARTICLES.register("flash", () -> SimpleParticleTypeExp.of(false));
}
