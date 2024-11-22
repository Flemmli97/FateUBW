package io.github.flemmli97.fateubw.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class TrailParticleType extends ParticleType<TrailParticleData> {

    private final Codec<TrailParticleData> codec;

    public TrailParticleType() {
        super(false, TrailParticleData.DESERIALIZER);
        this.codec = TrailParticleData.codec(this);
    }

    @Override
    public Codec<TrailParticleData> codec() {
        return this.codec;
    }
}
