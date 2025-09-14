package io.github.flemmli97.fateubw.common.particles;

import net.minecraft.core.particles.SimpleParticleType;

public class SimpleParticleTypeExp extends SimpleParticleType {

    private SimpleParticleTypeExp(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    public static SimpleParticleType of(boolean overrideLimiter) {
        return new SimpleParticleTypeExp(overrideLimiter);
    }
}
