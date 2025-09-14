package io.github.flemmli97.fateubw.common.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record RingParticleData(float rotY, float rotX) implements ParticleOptions {

    public static final MapCodec<RingParticleData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                    Codec.FLOAT.fieldOf("y_rot").forGetter(RingParticleData::rotY),
                    Codec.FLOAT.fieldOf("x_rot").forGetter(RingParticleData::rotX))
            .apply(builder, RingParticleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RingParticleData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public RingParticleData decode(RegistryFriendlyByteBuf buf) {
            return new RingParticleData(buf.readFloat(), buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, RingParticleData data) {
            buffer.writeFloat(data.rotY());
            buffer.writeFloat(data.rotX());
        }
    };

    @Override
    public ParticleType<?> getType() {
        return FateParticles.RING.get();
    }
}
