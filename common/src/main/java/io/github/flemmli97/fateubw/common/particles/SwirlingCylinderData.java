package io.github.flemmli97.fateubw.common.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SwirlingCylinderData implements ParticleOptions {

    public static MapCodec<SwirlingCylinderData> codec(ParticleType<SwirlingCylinderData> type) {
        return RecordCodecBuilder.mapCodec((builder) -> builder.group(
                        Codec.FLOAT.fieldOf("width1").forGetter(SwirlingCylinderData::width1),
                        Codec.FLOAT.fieldOf("width2").forGetter(SwirlingCylinderData::width2),
                        Codec.FLOAT.fieldOf("height").forGetter(SwirlingCylinderData::height),
                        Codec.FLOAT.fieldOf("r1").forGetter(SwirlingCylinderData::r1),
                        Codec.FLOAT.fieldOf("g1").forGetter(SwirlingCylinderData::g1),
                        Codec.FLOAT.fieldOf("b1").forGetter(SwirlingCylinderData::b1),
                        Codec.FLOAT.fieldOf("a1").forGetter(SwirlingCylinderData::a1),
                        Codec.FLOAT.fieldOf("r2").forGetter(SwirlingCylinderData::r2),
                        Codec.FLOAT.fieldOf("b2").forGetter(SwirlingCylinderData::g2),
                        Codec.FLOAT.fieldOf("g2").forGetter(SwirlingCylinderData::b2),
                        Codec.FLOAT.fieldOf("a2").forGetter(SwirlingCylinderData::a2),
                        Codec.INT.fieldOf("duration").forGetter(SwirlingCylinderData::duration),
                        Codec.BOOL.fieldOf("base").forGetter(SwirlingCylinderData::base))
                .apply(builder, ((width1, width2, height, r1, b1, g1, a1, r2, b2, g2, a2, duration, base) ->
                        new SwirlingCylinderData(type, width1, width1, height, r1, b1, g1, a1, r2, b2, g2, a2, duration, base))));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, SwirlingCylinderData> streamCodec(ParticleType<SwirlingCylinderData> type) {
        return new StreamCodec<>() {
            @Override
            public SwirlingCylinderData decode(RegistryFriendlyByteBuf buf) {
                return new SwirlingCylinderData(type, buf.readFloat(), buf.readFloat(), buf.readFloat(),
                        buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                        buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                        buf.readInt(), buf.readBoolean());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, SwirlingCylinderData data) {
                buf.writeFloat(data.width1);
                buf.writeFloat(data.width2);
                buf.writeFloat(data.height);
                buf.writeFloat(data.r1);
                buf.writeFloat(data.g1);
                buf.writeFloat(data.b1);
                buf.writeFloat(data.a1);
                buf.writeFloat(data.r2);
                buf.writeFloat(data.g2);
                buf.writeFloat(data.b2);
                buf.writeFloat(data.a2);
                buf.writeInt(data.duration);
                buf.writeBoolean(data.base);
            }
        };
    }

    private final ParticleType<? extends SwirlingCylinderData> type;
    private final float width1, width2, height;
    private final float r1, g1, b1, a1, r2, g2, b2, a2;

    private final int duration;

    private final boolean base;

    public SwirlingCylinderData(ParticleType<? extends SwirlingCylinderData> type, float width1, float height, float r1, float g1, float b1, float a1, int duration, boolean base) {
        this(type, width1, width1, height, r1, g1, b1, a1, r1, g1, b1, a1, duration, base);
    }

    public SwirlingCylinderData(ParticleType<? extends SwirlingCylinderData> type, float width1, float width2, float height, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, int duration, boolean base) {
        this.type = type;
        this.width1 = width1;
        this.width2 = width2;
        this.height = height;
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.a1 = a1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.a2 = a2;
        this.duration = duration;
        this.base = base;
    }

    public float width1() {
        return this.width1;
    }

    public float width2() {
        return this.width2;
    }

    public float height() {
        return this.height;
    }

    public float r1() {
        return this.r1;
    }

    public float g1() {
        return this.g1;
    }

    public float b1() {
        return this.b1;
    }

    public float a1() {
        return this.a1;
    }

    public float r2() {
        return this.r2;
    }

    public float g2() {
        return this.g2;
    }

    public float b2() {
        return this.b2;
    }

    public float a2() {
        return this.a2;
    }

    public int duration() {
        return this.duration;
    }

    public boolean base() {
        return this.base;
    }

    @Override
    public ParticleType<? extends SwirlingCylinderData> getType() {
        return this.type;
    }
}
