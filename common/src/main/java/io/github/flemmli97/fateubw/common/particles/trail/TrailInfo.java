package io.github.flemmli97.fateubw.common.particles.trail;

import com.mojang.math.Vector4f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.particles.trail.provider.TrailProvider;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;


public class TrailInfo {

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
            CodecUtils.stringEnumCodec(Visual.class, Visual.SOLID).fieldOf("type").forGetter(d -> d.visual),
            TrailProviderRegistry.CODEC.fieldOf("provider").forGetter(d -> d.provider)
    ).apply(builder, (color, scale, color_2, scale_2, visual, provider) ->
            new TrailInfo(color.x(), color.y(), color.z(), color.w(), scale,
                    color_2.x(), color_2.y(), color_2.z(), color_2.w(), scale_2, visual, provider)
    ));

    public final float r, g, b, a, width;
    public final float r2, g2, b2, a2, width2;
    public final Visual visual;

    public final TrailProvider provider;

    public TrailInfo(float r, float g, float b, float a, float width, float r2, float g2, float b2, float a2, float width2, Visual visual, TrailProvider provider) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.width = width;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.a2 = a2;
        this.width2 = width2;
        this.visual = visual;
        this.provider = provider;
    }

    public TrailInfo(FriendlyByteBuf buf) {
        this(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readEnum(Visual.class), TrailProviderRegistry.fromBuffer(buf));
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeFloat(this.r);
        buf.writeFloat(this.g);
        buf.writeFloat(this.b);
        buf.writeFloat(this.a);
        buf.writeFloat(this.width);
        buf.writeFloat(this.r2);
        buf.writeFloat(this.g2);
        buf.writeFloat(this.b2);
        buf.writeFloat(this.a2);
        buf.writeFloat(this.width2);
        TrailProviderRegistry.toBuffer(this.provider, buf);
    }

    public static TrailInfo.Builder builder(TrailProvider provider) {
        return new TrailInfo.Builder(provider);
    }

    public static class Builder {

        private final TrailProvider provider;

        private float r = 1, g = 1, b = 1, a = 0.5f;
        private float r2 = 1, g2 = 1, b2 = 1, a2 = 0.5f;

        private float width = 1, width2;

        private Visual visual = Visual.SOLID;

        public Builder(TrailProvider provider) {
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

        public Builder setType(Visual visual) {
            this.visual = visual;
            return this;
        }

        public TrailInfo build() {
            return new TrailInfo(this.r, this.g, this.b, this.a, this.width, this.r2, this.g2, this.b2, this.a2, this.width2, this.visual, this.provider);
        }
    }

    public enum Visual {
        SOLID,
        TEXTURE
    }
}
