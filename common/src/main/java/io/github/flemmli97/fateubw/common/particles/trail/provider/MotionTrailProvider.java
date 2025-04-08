package io.github.flemmli97.fateubw.common.particles.trail.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.common.particles.trail.TrailProviderRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MotionTrailProvider implements TrailProvider {

    public static final Codec<MotionTrailProvider> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                    Codec.DOUBLE.fieldOf("x").forGetter(d -> d.motion.x()),
                    Codec.DOUBLE.fieldOf("y").forGetter(d -> d.motion.y()),
                    Codec.DOUBLE.fieldOf("z").forGetter(d -> d.motion.z()),
                    Codec.DOUBLE.fieldOf("x_sweer").forGetter(d -> d.motion.x()),
                    Codec.DOUBLE.fieldOf("y_sweer").forGetter(d -> d.motion.y()),
                    Codec.DOUBLE.fieldOf("z_sweer").forGetter(d -> d.motion.z()),
                    Codec.DOUBLE.fieldOf("period").forGetter(d -> d.motion.z()),
                    Codec.INT.fieldOf("frames").forGetter(d -> d.position.getLength()),
                    Codec.INT.fieldOf("duration").forGetter(d -> d.duration)
            ).apply(builder, MotionTrailProvider::new)
    );

    private final Vec3 motion, sweerDirection;
    private final double period;

    private final TrailPositions position;
    private final int duration;

    private int ticks;

    public MotionTrailProvider(double x, double y, double z, int frames, int duration) {
        this(new Vec3(x, y, z), Vec3.ZERO, 0, frames, duration);
    }

    public MotionTrailProvider(double x, double y, double z, double sweerX, double sweerY, double sweerZ, double period, int frames, int duration) {
        this(new Vec3(x, y, z), new Vec3(sweerX, sweerY, sweerZ), period, frames, duration);
    }

    public MotionTrailProvider(Vec3 motion, int frames, int duration) {
        this(motion, Vec3.ZERO, 0, frames, duration);
    }

    public MotionTrailProvider(Vec3 motion, Vec3 sweerDirection, double period, int frames, int duration) {
        this.motion = motion;
        this.sweerDirection = sweerDirection;
        this.period = period;
        this.position = new TrailPositions(frames);
        this.duration = duration + frames;
    }

    public MotionTrailProvider(FriendlyByteBuf buf) {
        this.motion = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.sweerDirection = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.period = buf.readDouble();
        this.position = new TrailPositions(buf.readInt());
        this.duration = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.motion.x());
        buf.writeDouble(this.motion.x());
        buf.writeDouble(this.motion.x());
        buf.writeDouble(this.sweerDirection.x());
        buf.writeDouble(this.sweerDirection.x());
        buf.writeDouble(this.sweerDirection.x());
        buf.writeDouble(this.period);
        buf.writeInt(this.position.getLength());
        buf.writeInt(this.duration);
    }

    @Override
    public TrailPositions positions(Level level) {
        return this.position;
    }

    @Override
    public Vec3 particleTick(Level level) {
        TrailPositions.TrailPosition last = this.position.getLast();
        float p = (float) this.ticks / (this.duration - this.position.getLength());
        Vec3 pos;
        if (last == null) {
            pos = this.motion;
        } else {
            if (this.ticks >= this.duration - this.position.getLength()) {
                pos = last.pos();
            } else {
                Vec3 dir = this.motion;
                if (this.period > 0 && !this.sweerDirection.equals(Vec3.ZERO)) {
                    dir = dir.add(this.sweerDirection.scale(Math.sin(p * Math.PI * 2 * this.period)));
                }
                pos = last.pos().add(dir);
            }
        }
        this.position.add(pos);
        this.ticks++;
        return pos;
    }

    @Override
    public ResourceLocation id() {
        return TrailProviderRegistry.MOTION_TRAIL;
    }

    @Override
    public boolean removed(Level level) {
        return this.ticks >= this.duration;
    }
}
