package io.github.flemmli97.fateubw.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class SelfCodecParticleType<T extends ParticleOptions> extends ParticleType<T> {

    private final Codec<T> codec;

    @SuppressWarnings("deprecation")
    public SelfCodecParticleType(ParticleOptions.Deserializer<T> deserializer, Function<SelfCodecParticleType<T>, Codec<T>> codec) {
        super(false, deserializer);
        this.codec = codec.apply(this);
    }

    @Override
    public Codec<T> codec() {
        return this.codec;
    }
}
