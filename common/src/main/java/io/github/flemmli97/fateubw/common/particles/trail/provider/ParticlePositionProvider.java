package io.github.flemmli97.fateubw.common.particles.trail.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.common.particles.trail.TrailProviderRegistry;
import io.github.flemmli97.tenshilib.common.utils.StreamCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ParticlePositionProvider implements TrailProvider {

    private final ParticlePositionData data;
    private final Supplier<Vec3> positionSup;
    private final IntSupplier lifetime;

    private final TrailPositions position;

    private int ticks;

    public ParticlePositionProvider(ParticlePositionData data, Supplier<Vec3> position, IntSupplier lifetime) {
        this.data = data;
        this.positionSup = position;
        this.lifetime = lifetime;
        this.position = new TrailPositions(data.frames());
    }

    @Override
    public TrailPositions positions() {
        return this.position;
    }

    @Override
    public float adjustedPartialTicks(float partialTicks) {
        return this.isFading() ? 1 : partialTicks;
    }

    @Override
    public Vec3 particleTick() {
        this.ticks++;
        if (this.isFading()) {
            this.position.removeHead();
            return this.position.getLast() != null ? this.position.getLast().pos() : null;
        }
        Vec3 pos = this.positionSup.get();
        this.position.add(pos, this.data.normal);
        return pos;
    }

    private int getLifetime() {
        return this.lifetime.getAsInt() + this.data.frames;
    }

    private boolean isFading() {
        return this.ticks >= this.lifetime.getAsInt();
    }

    @Override
    public TrailData data() {
        return this.data;
    }

    @Override
    public boolean removed() {
        return this.ticks >= this.getLifetime();
    }

    public record ParticlePositionData(@Nullable Vec3 normal, int frames) implements TrailData {

        private static final Codec<Vec3> VEC_3_CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                        Codec.DOUBLE.fieldOf("x").forGetter(Vec3::x),
                        Codec.DOUBLE.fieldOf("y").forGetter(Vec3::y),
                        Codec.DOUBLE.fieldOf("z").forGetter(Vec3::z)
                ).apply(builder, Vec3::new)
        );

        public static final MapCodec<ParticlePositionData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                        VEC_3_CODEC.optionalFieldOf("normal").forGetter(d -> Optional.ofNullable(d.normal)),
                        Codec.INT.fieldOf("frames").forGetter(d -> d.frames)
                ).apply(builder, (normal, frames) -> new ParticlePositionData(normal.orElse(null), frames))
        );
        public static final StreamCodec<ByteBuf, ParticlePositionData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(StreamCodecs.VEC3), d -> Optional.ofNullable(d.normal()),
                ByteBufCodecs.INT, ParticlePositionData::frames, (n, frames) -> new ParticlePositionData(n.orElse(null), frames));

        public ParticlePositionData(int frames) {
            this(null, frames);
        }

        @Override
        public TrailProviderRegistry.TrailType<?> type() {
            return TrailProviderRegistry.MOTION_TRAIL;
        }

        @Override
        public TrailProvider createProvider(Level level, Supplier<Vec3> position, IntSupplier lifetime) {
            return new ParticlePositionProvider(this, position, lifetime);
        }
    }
}
