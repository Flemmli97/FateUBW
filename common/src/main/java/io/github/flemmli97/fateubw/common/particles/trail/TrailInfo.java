package io.github.flemmli97.fateubw.common.particles.trail;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.particles.trail.provider.TrailData;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector4f;

import java.util.Optional;
import java.util.function.Function;

public record TrailInfo(float r, float g, float b, float a, float width, float r2, float g2, float b2, float a2,
                        float width2, int interpolation, TrailInfo.Visual visual,
                        int textureIndex, TrailData data) {

    private static final Function<String, Codec<Vector4f>> COLOR = suffix -> RecordCodecBuilder.create((builder) -> builder.group(
            Codec.FLOAT.fieldOf("r" + suffix).forGetter(Vector4f::x),
            Codec.FLOAT.fieldOf("g" + suffix).forGetter(Vector4f::y),
            Codec.FLOAT.fieldOf("b" + suffix).forGetter(Vector4f::z),
            Codec.FLOAT.fieldOf("a" + suffix).forGetter(Vector4f::w)
    ).apply(builder, Vector4f::new));

    public static final Codec<TrailInfo> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            COLOR.apply("").fieldOf("color").forGetter(d -> new Vector4f(d.r, d.g, d.b, d.a)),
            Codec.FLOAT.fieldOf("scale").forGetter(d -> d.width),
            COLOR.apply("_2").fieldOf("color_2").forGetter(d -> new Vector4f(d.r2, d.g2, d.b2, d.a2)),
            Codec.FLOAT.fieldOf("scale_2").forGetter(d -> d.width2),
            Codec.INT.fieldOf("interpolation").forGetter(d -> d.interpolation),
            CodecUtils.stringEnumCodec(Visual.class, Visual.SOLID).fieldOf("type").forGetter(d -> d.visual),
            Codec.INT.optionalFieldOf("texture_index").forGetter(d -> d.visual == Visual.SOLID || d.textureIndex == 0 ? Optional.empty() : Optional.of(d.textureIndex)),
            TrailProviderRegistry.CODEC.fieldOf("provider").forGetter(d -> d.data)
    ).apply(builder, (color, scale, color_2, scale_2, interpolation, visual, text, provider) ->
            new TrailInfo(color.x(), color.y(), color.z(), color.w(), scale,
                    color_2.x(), color_2.y(), color_2.z(), color_2.w(), scale_2, interpolation, visual, text.orElse(0), provider)
    ));

    public static final StreamCodec<RegistryFriendlyByteBuf, TrailInfo> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public TrailInfo decode(RegistryFriendlyByteBuf buf) {
            return new TrailInfo(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readEnum(Visual.class), buf.readInt(), TrailProviderRegistry.fromBuffer(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, TrailInfo data) {
            buf.writeFloat(data.r);
            buf.writeFloat(data.g);
            buf.writeFloat(data.b);
            buf.writeFloat(data.a);
            buf.writeFloat(data.width);
            buf.writeFloat(data.r2);
            buf.writeFloat(data.g2);
            buf.writeFloat(data.b2);
            buf.writeFloat(data.a2);
            buf.writeFloat(data.width2);
            buf.writeFloat(data.interpolation);
            buf.writeEnum(data.visual);
            buf.writeInt(data.textureIndex);
            TrailProviderRegistry.toBuffer(data.data, buf);
        }
    };

    public static TrailInfo.Builder builder(TrailData provider) {
        return new TrailInfo.Builder(provider);
    }

    public static class Builder {

        private final TrailData provider;

        private float r = 1, g = 1, b = 1, a = 0.5f;
        private float r2 = 1, g2 = 1, b2 = 1, a2 = 0.5f;

        private float width = 1, width2;

        private int interpolation = 4;

        private Visual visual = Visual.SOLID;
        private int textureIndex;

        public Builder(TrailData provider) {
            this.provider = provider;
        }

        public Builder setColor(float r, float g, float b, float a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            return this;
        }

        public Builder setColor2(float r, float g, float b, float a) {
            this.r2 = r;
            this.g2 = g;
            this.b2 = b;
            this.a2 = a;
            return this;
        }

        public Builder setWidth(float width) {
            this.width = width;
            return this;
        }

        public Builder setWidth2(float scale) {
            this.width2 = scale;
            return this;
        }

        public Builder setType(Visual visual, int index) {
            this.visual = visual;
            this.textureIndex = index;
            return this;
        }

        public Builder setInterpolation(int interpolation) {
            this.interpolation = Math.max(1, interpolation);
            return this;
        }

        public TrailInfo build() {
            return new TrailInfo(this.r, this.g, this.b, this.a, this.width, this.r2, this.g2, this.b2, this.a2, this.width2, this.interpolation, this.visual, this.textureIndex, this.provider);
        }
    }

    public enum Visual {
        SOLID,
        TEXTURE
    }
}
