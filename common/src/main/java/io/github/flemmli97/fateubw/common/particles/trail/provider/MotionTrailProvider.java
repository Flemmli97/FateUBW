package io.github.flemmli97.fateubw.common.particles.trail.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.common.particles.trail.TrailProviderRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MotionTrailProvider implements TrailProvider {

    private final MotionTrailData data;

    private final TrailPositions position;

    private int ticks;

    public MotionTrailProvider(MotionTrailData data) {
        this.data = data;
        this.position = new TrailPositions(data.frames());
    }

    @Override
    public TrailPositions positions() {
        return this.position;
    }

    @Override
    public Vec3 particleTick() {
        TrailPositions.TrailPosition last = this.position.getLast();
        float p = (float) this.ticks / this.data.duration;
        Vec3 pos;
        if (last == null) {
            pos = this.data.motion;
        } else {
            if (this.ticks >= this.data.duration) {
                pos = last.pos();
            } else {
                Vec3 dir = this.data.motion;
                if (this.data.period > 0 && this.data.sweerDirection != null && !this.data.sweerDirection.equals(Vec3.ZERO)) {
                    dir = dir.add(this.data.sweerDirection.scale(Math.sin(p * Math.PI * 2 * this.data.period)));
                }
                pos = last.pos().add(dir);
            }
        }
        this.position.add(pos, this.data.normal);
        this.ticks++;
        return pos;
    }

    private int getLifetime() {
        return this.data.duration + this.data.frames;
    }

    @Override
    public TrailData data() {
        return this.data;
    }

    @Override
    public boolean removed() {
        return this.ticks >= this.getLifetime();
    }

    public record MotionTrailData(Vec3 motion, @Nullable Vec3 sweerDirection, @Nullable Vec3 normal, double period,
                                  int frames,
                                  int duration) implements TrailData {

        public MotionTrailData(double x, double y, double z, int frames, int duration) {
            this(new Vec3(x, y, z), null, null, 0, frames, duration);
        }

        public MotionTrailData(Vec3 motion, int frames, int duration) {
            this(motion, null, null, 0, frames, duration);
        }

        public MotionTrailData(FriendlyByteBuf buf) {
            this(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                    buf.readBoolean() ? new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()) : null,
                    buf.readBoolean() ? new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()) : null,
                    buf.readDouble(), buf.readInt(), buf.readInt());
        }

        private static final Codec<Vec3> VEC_3_CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                        Codec.DOUBLE.fieldOf("x").forGetter(Vec3::x),
                        Codec.DOUBLE.fieldOf("y").forGetter(Vec3::y),
                        Codec.DOUBLE.fieldOf("z").forGetter(Vec3::z)
                ).apply(builder, Vec3::new)
        );

        public static final MapCodec<MotionTrailData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                        VEC_3_CODEC.fieldOf("motion").forGetter(d -> d.motion),
                        VEC_3_CODEC.optionalFieldOf("sweer").forGetter(d -> Optional.ofNullable(d.sweerDirection)),
                        VEC_3_CODEC.optionalFieldOf("normal").forGetter(d -> Optional.ofNullable(d.normal)),
                        Codec.DOUBLE.fieldOf("period").forGetter(d -> d.motion.z()),
                        Codec.INT.fieldOf("frames").forGetter(d -> d.frames),
                        Codec.INT.fieldOf("duration").forGetter(d -> d.duration)
                ).apply(builder, (motion, sweer, normal, period, frames, duration) -> new MotionTrailData(motion, sweer.orElse(null),
                        normal.orElse(null), period, frames, duration))
        );

        @Override
        public ResourceLocation id() {
            return TrailProviderRegistry.MOTION_TRAIL;
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeDouble(this.motion.x());
            buf.writeDouble(this.motion.y());
            buf.writeDouble(this.motion.z());
            buf.writeBoolean(this.sweerDirection != null);
            if (this.sweerDirection != null) {
                buf.writeDouble(this.sweerDirection.x());
                buf.writeDouble(this.sweerDirection.y());
                buf.writeDouble(this.sweerDirection.z());
            }
            buf.writeBoolean(this.normal != null);
            if (this.normal != null) {
                buf.writeDouble(this.normal.x());
                buf.writeDouble(this.normal.y());
                buf.writeDouble(this.normal.z());
            }
            buf.writeDouble(this.period);
            buf.writeInt(this.frames);
            buf.writeInt(this.duration);
        }

        @Override
        public TrailProvider createProvider(Level level) {
            return new MotionTrailProvider(this);
        }
    }
}
