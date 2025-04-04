package io.github.flemmli97.fateubw.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class RingParticleType extends ParticleType<RingParticleData> {

    public RingParticleType() {
        super(true, RingParticleData.DESERIALIZER);
    }

    @Override
    public Codec<RingParticleData> codec() {
        return RingParticleData.CODEC;
    }
}
