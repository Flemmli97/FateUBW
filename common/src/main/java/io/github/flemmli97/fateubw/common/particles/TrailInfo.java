package io.github.flemmli97.fateubw.common.particles;

import com.mojang.math.Vector4f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;


public class TrailInfo {

    public static final TrailInfo DEFAULT = TrailInfo.builder(new Vec3(0, 0, -1), new Vec3(0, 0, 1)).build();

    public static final Codec<Vec3> VEC_CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            Codec.DOUBLE.fieldOf("x").forGetter(Vec3::x),
            Codec.DOUBLE.fieldOf("y").forGetter(Vec3::y),
            Codec.DOUBLE.fieldOf("z").forGetter(Vec3::z)
    ).apply(builder, Vec3::new));

    private static final Function<String, Codec<Vector4f>> COLOR = suffix -> RecordCodecBuilder.create((builder) -> builder.group(
            Codec.FLOAT.fieldOf("r" + suffix).forGetter(Vector4f::x),
            Codec.FLOAT.fieldOf("g" + suffix).forGetter(Vector4f::y),
            Codec.FLOAT.fieldOf("b" + suffix).forGetter(Vector4f::z),
            Codec.FLOAT.fieldOf("a" + suffix).forGetter(Vector4f::w)
    ).apply(builder, Vector4f::new));

    public static final Codec<TrailInfo> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            VEC_CODEC.fieldOf("start").forGetter(d -> d.start),
            VEC_CODEC.fieldOf("end").forGetter(d -> d.end),
            VEC_CODEC.fieldOf("control").forGetter(d -> d.controlPoint),
            Codec.FLOAT.fieldOf("y_rot").forGetter(d -> d.yRot),
            Codec.FLOAT.fieldOf("x_rot").forGetter(d -> d.xRot),
            Codec.FLOAT.fieldOf("z_rot").forGetter(d -> d.zRot),
            Codec.INT.fieldOf("duration").forGetter(d -> d.duration),
            Codec.INT.fieldOf("fade").forGetter(d -> d.fadeTime),
            COLOR.apply("").fieldOf("color").forGetter(d -> new Vector4f(d.r, d.g, d.b, d.a)),
            Codec.FLOAT.fieldOf("scale").forGetter(d -> d.scale),
            COLOR.apply("_2").fieldOf("color_2").forGetter(d -> new Vector4f(d.r2, d.g2, d.b2, d.a2)),
            Codec.FLOAT.fieldOf("scale_2").forGetter(d -> d.scale2)
    ).apply(builder, (start, end, control, yRot, xRot, zRot, duration, fade, color, scale, color_2, scale_2) ->
            new TrailInfo(start, end, control, yRot, xRot, zRot, duration, fade, color.x(), color.y(), color.z(), color.w(), scale,
                    color_2.x(), color_2.y(), color_2.z(), color_2.w(), scale_2)
    ));

    public final Vec3 start, end, controlPoint, normalY;

    public final float yRot, xRot, zRot;

    public final boolean direct;
    public final int duration;
    public final int fadeTime;

    public final float r, g, b, a, scale;
    public final float r2, g2, b2, a2, scale2;

    public TrailInfo(Vec3 start, Vec3 end, Vec3 controlPoint, float yRot, float xRot, float zRot, int duration, int fadeTime, float r, float g, float b, float a, float scale, float r2, float g2, float b2, float a2, float scale2) {
        this.start = start;
        this.end = end;
        this.controlPoint = controlPoint;
        this.yRot = yRot;
        this.xRot = xRot;
        this.zRot = zRot;
        this.duration = duration;
        this.fadeTime = Math.min(duration, fadeTime);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.scale = scale;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.a2 = a2;
        this.scale2 = scale2;
        Vec3 normal = this.end.subtract(this.start).cross(this.controlPoint.subtract(this.start)).normalize();
        this.direct = normal.equals(Vec3.ZERO);
        if (this.direct) {
            normal = new Vec3(0, 1, 0);
        } else {
            normal = MathUtils.rotate(normal, this.end.subtract(this.start), Mth.HALF_PI).normalize();
        }
        this.normalY = normal;
    }

    public TrailInfo(FriendlyByteBuf buf) {
        this(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                buf.readFloat(), buf.readFloat(), buf.readFloat(),
                buf.readInt(), buf.readInt(),
                buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeDouble(this.start.x());
        buf.writeDouble(this.start.y());
        buf.writeDouble(this.start.z());
        buf.writeDouble(this.end.x());
        buf.writeDouble(this.end.y());
        buf.writeDouble(this.end.z());
        buf.writeDouble(this.controlPoint.x());
        buf.writeDouble(this.controlPoint.y());
        buf.writeDouble(this.controlPoint.z());
        buf.writeFloat(this.xRot);
        buf.writeFloat(this.yRot);
        buf.writeFloat(this.zRot);
        buf.writeInt(this.duration);
        buf.writeInt(this.fadeTime);
        buf.writeFloat(this.r);
        buf.writeFloat(this.g);
        buf.writeFloat(this.b);
        buf.writeFloat(this.a);
        buf.writeFloat(this.scale);
        buf.writeFloat(this.r2);
        buf.writeFloat(this.g2);
        buf.writeFloat(this.b2);
        buf.writeFloat(this.a2);
        buf.writeFloat(this.scale2);
    }

    public static Builder builder(Vec3 start, Vec3 end) {
        return new Builder(start, end);
    }

    public static class Builder {

        private final Vec3 start, end;
        private Vec3 controlPoint;

        private float yRot, xRot, zRot;

        private int duration = 10;
        private int fadeTime = 5;

        private float r = 1, g = 1, b = 1, a = 0.5f;
        private float r2 = 1, g2 = 1, b2 = 1, a2 = 0.5f;

        private float scale = 1, scale2;

        private Builder(Vec3 start, Vec3 end) {
            this.start = start;
            this.controlPoint = start.add(end.subtract(start).scale(0.5));
            this.end = end;
        }

        public Builder setControlPoint(Vec3 controlPoint) {
            this.controlPoint = controlPoint;
            return this;
        }

        public Builder rotateBy(float yaw, float pitch, float roll) {
            this.yRot = yaw;
            this.xRot = pitch;
            this.zRot = roll;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setFadeTime(int fadeTime) {
            this.fadeTime = fadeTime;
            return this;
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

        public Builder setScale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder setScale2(float scale) {
            this.scale2 = scale;
            return this;
        }

        public TrailInfo build() {
            return new TrailInfo(this.start, this.end, this.controlPoint, this.yRot, this.xRot, this.zRot, this.duration, this.fadeTime,
                    this.r, this.g, this.b, this.a, this.scale, this.r2, this.g2, this.b2, this.a2, this.scale2);
        }
    }
}
