package io.github.flemmli97.fateubw.common.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class RingParticleData extends ColoredParticleData {

    public static final MapCodec<RingParticleData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                    Codec.FLOAT.fieldOf("r").forGetter(RingParticleData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(RingParticleData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(RingParticleData::getBlue),
                    Codec.FLOAT.fieldOf("alpha").forGetter(RingParticleData::getAlpha),
                    Codec.FLOAT.fieldOf("scale").forGetter(RingParticleData::getScale),
                    Codec.FLOAT.fieldOf("y_rot").forGetter(RingParticleData::getRotY),
                    Codec.FLOAT.fieldOf("x_rot").forGetter(RingParticleData::getRotX),
                    Codec.FLOAT.fieldOf("growth").forGetter(RingParticleData::getGrowth))
            .apply(builder, RingParticleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RingParticleData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public RingParticleData decode(RegistryFriendlyByteBuf buf) {
            return new RingParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, RingParticleData data) {
            buffer.writeFloat(data.getRed());
            buffer.writeFloat(data.getGreen());
            buffer.writeFloat(data.getBlue());
            buffer.writeFloat(data.getAlpha());
            buffer.writeFloat(data.getScale());
            buffer.writeFloat(data.getRotY());
            buffer.writeFloat(data.getRotX());
            buffer.writeFloat(data.getGrowth());
        }
    };

    private final float rotY, rotX, growth;

    public RingParticleData(float red, float green, float blue, float alpha, float scale, float rotY, float rotX, float growth) {
        super(null, red, green, blue, alpha, scale);
        this.rotY = rotY;
        this.rotX = rotX;
        this.growth = growth;
    }

    public float getGrowth() {
        return this.growth;
    }

    public float getRotY() {
        return this.rotY;
    }

    public float getRotX() {
        return this.rotX;
    }

    @Override
    public ParticleType<?> getType() {
        return FateParticles.RING.get();
    }
}
