package io.github.flemmli97.fateubw.common.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class RingParticleData extends ColoredParticleData {

    public static Codec<RingParticleData> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                    Codec.FLOAT.fieldOf("r").forGetter(RingParticleData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(RingParticleData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(RingParticleData::getBlue),
                    Codec.FLOAT.fieldOf("alpha").forGetter(RingParticleData::getAlpha),
                    Codec.FLOAT.fieldOf("scale").forGetter(RingParticleData::getScale),
                    Codec.FLOAT.fieldOf("y_rot").forGetter(RingParticleData::getRotY),
                    Codec.FLOAT.fieldOf("x_rot").forGetter(RingParticleData::getRotX),
                    Codec.FLOAT.fieldOf("growth").forGetter(RingParticleData::getGrowth))
            .apply(builder, RingParticleData::new));

    @SuppressWarnings("deprecation")
    public static final Deserializer<RingParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public RingParticleData fromCommand(ParticleType<RingParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float a = reader.readFloat();
            reader.expect(' ');
            float scale = reader.readFloat();
            reader.expect(' ');
            float rotY = reader.readFloat();
            reader.expect(' ');
            float rotX = reader.readFloat();
            reader.expect(' ');
            float growth = reader.readFloat();
            return new RingParticleData(r, g, b, a, scale, rotY, rotX, growth);
        }

        @Override
        public RingParticleData fromNetwork(ParticleType<RingParticleData> type, FriendlyByteBuf buffer) {
            return new RingParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                    buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
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
        return ModParticles.RING.get();
    }
}
