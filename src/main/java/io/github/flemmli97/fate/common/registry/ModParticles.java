package io.github.flemmli97.fate.common.registry;

import com.flemmli97.tenshilib.common.particle.ColoredParticleData;
import com.flemmli97.tenshilib.common.particle.ColoredParticleType;
import io.github.flemmli97.fate.Fate;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Fate.MODID);

    public static final RegistryObject<ParticleType<ColoredParticleData>> light = PARTICLES.register("light", () -> new ColoredParticleType(false));

}
