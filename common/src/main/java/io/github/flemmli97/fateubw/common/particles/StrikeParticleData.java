package io.github.flemmli97.fateubw.common.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class StrikeParticleData implements ParticleOptions {

    public static MapCodec<StrikeParticleData> codec(ParticleType<StrikeParticleData> type) {
        return RecordCodecBuilder.mapCodec((builder) -> builder.group(
                        Codec.FLOAT.fieldOf("yaw").forGetter(StrikeParticleData::yaw),
                        Codec.FLOAT.fieldOf("pitch").forGetter(StrikeParticleData::pitch),
                        Codec.FLOAT.fieldOf("width").forGetter(StrikeParticleData::width),
                        Codec.FLOAT.fieldOf("length").forGetter(StrikeParticleData::length),
                        Codec.INT.fieldOf("duration").forGetter(StrikeParticleData::duration))
                .apply(builder, (yaw, pitch, width, length, duration) -> new StrikeParticleData(type, yaw, pitch, width, length, duration)));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, StrikeParticleData> streamCodec(ParticleType<StrikeParticleData> type) {
        return new StreamCodec<>() {
            @Override
            public StrikeParticleData decode(RegistryFriendlyByteBuf buf) {
                return new StrikeParticleData(type, buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, StrikeParticleData data) {
                buf.writeFloat(data.yaw);
                buf.writeFloat(data.pitch);
                buf.writeFloat(data.width);
                buf.writeFloat(data.length);
                buf.writeInt(data.duration);
            }
        };
    }

    private final ParticleType<? extends StrikeParticleData> type;
    private final float yaw, pitch, width, length;
    private final int duration;

    public StrikeParticleData(ParticleType<? extends StrikeParticleData> type, float yaw, float pitch, float width, float length, int duration) {
        this.type = type;
        this.yaw = yaw;
        this.pitch = pitch;
        this.width = width;
        this.length = length;
        this.duration = duration;
    }

    public float yaw() {
        return this.yaw;
    }

    public float pitch() {
        return this.pitch;
    }

    public float width() {
        return this.width;
    }

    public float length() {
        return this.length;
    }

    public int duration() {
        return this.duration;
    }

    @Override
    public ParticleType<? extends StrikeParticleData> getType() {
        return this.type;
    }
}
