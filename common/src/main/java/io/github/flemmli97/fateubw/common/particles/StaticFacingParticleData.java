package io.github.flemmli97.fateubw.common.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record StaticFacingParticleData(ParticleType<?> type, float rotY, float rotX) implements ParticleOptions {

    public static final MapCodec<StaticFacingParticleData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                    BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("type").forGetter(StaticFacingParticleData::getType),
                    Codec.FLOAT.fieldOf("y_rot").forGetter(StaticFacingParticleData::rotY),
                    Codec.FLOAT.fieldOf("x_rot").forGetter(StaticFacingParticleData::rotX))
            .apply(builder, StaticFacingParticleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StaticFacingParticleData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public StaticFacingParticleData decode(RegistryFriendlyByteBuf buf) {
            return new StaticFacingParticleData(ByteBufCodecs.registry(Registries.PARTICLE_TYPE).decode(buf), buf.readFloat(), buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, StaticFacingParticleData data) {
            ByteBufCodecs.registry(Registries.PARTICLE_TYPE).encode(buffer, data.type());
            buffer.writeFloat(data.rotY());
            buffer.writeFloat(data.rotX());
        }
    };

    @Override
    public ParticleType<?> getType() {
        return this.type();
    }
}
