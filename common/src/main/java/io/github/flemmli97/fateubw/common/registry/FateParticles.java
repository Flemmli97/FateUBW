package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.particles.ParticleTypeContainer;
import io.github.flemmli97.fateubw.common.particles.SimpleParticleTypeExp;
import io.github.flemmli97.fateubw.common.particles.StaticFacingParticleData;
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
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<StaticFacingParticleData>> RING = PARTICLES.register("ring", () -> new ParticleTypeContainer<>(false, StaticFacingParticleData.CODEC, StaticFacingParticleData.STREAM_CODEC));
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> FLASH = PARTICLES.register("flash", () -> SimpleParticleTypeExp.of(false));
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<StaticFacingParticleData>> MAGIC_CIRCLE_1 = PARTICLES.register("magic_circle_1", () -> new ParticleTypeContainer<>(false, StaticFacingParticleData.CODEC, StaticFacingParticleData.STREAM_CODEC));
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<StaticFacingParticleData>> MAGIC_CIRCLE_2 = PARTICLES.register("magic_circle_2", () -> new ParticleTypeContainer<>(false, StaticFacingParticleData.CODEC, StaticFacingParticleData.STREAM_CODEC));
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> GLOWING_RING = PARTICLES.register("glowing_ring", () -> SimpleParticleTypeExp.of(false));
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> ROSE_PETAL = PARTICLES.register("rose_petal", () -> SimpleParticleTypeExp.of(false));
}
