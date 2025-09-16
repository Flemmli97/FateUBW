package io.github.flemmli97.fateubw.common.particles.trail.provider.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.common.particles.trail.TrailProviderRegistry;
import io.github.flemmli97.fateubw.common.particles.trail.provider.TrailData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.TrailProvider;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class EntityWeaponTrailProvider<T extends Entity & AnimatedEntity & EntityWeaponTrailHolderProvider> implements TrailProvider {

    public static final String TRAIL_START = "weapon_swing_start";
    public static final String TRAIL_END = "weapon_swing_end";

    private final EntityTrailData data;
    private final T entity;
    private final TrailPositions position;

    private boolean valid = true;
    private boolean sameTick;

    private int invalidTicks;

    public EntityWeaponTrailProvider(EntityTrailData data, T entity) {
        this.data = data;
        this.entity = entity;
        this.position = new TrailPositions(data.frames());
    }

    @Override
    public TrailPositions positions() {
        return this.position;
    }

    @Override
    public Vec3 particleTick() {
        this.sameTick = false;
        if (this.valid) {
            AnimationState anim = this.entity.getAnimationHandler().getAnimation();
            if (!this.entity.isAlive() || anim == null || !anim.is(this.data.context()) || anim.isPast(this.data.animationEnd)) {
                this.valid = false;
            }
        } else {
            ++this.invalidTicks;
            this.position.add(this.position.getLast());
        }
        return this.position.getLast() != null ? this.position.getLast().pos() : null;
    }

    @Override
    public TrailData data() {
        return this.data;
    }

    @Override
    public boolean removed() {
        return this.invalidTicks > this.position.getLength();
    }

    @Override
    public float adjustedPartialTicks(float partialTicks) {
        return 1;
    }

    public void recordData(String context, boolean left, Vec3 pos, Vec3 normal) {
        if (!this.valid || !context.equals(this.data.context) || left != this.data.left)
            return;
        TrailPositions.TrailPosition trail = new TrailPositions.TrailPosition(this.entity.position().add(pos), normal);
        if (!this.sameTick) {
            this.position.add(trail);
        } else {
            this.position.replaceLast(trail);
        }
        this.sameTick = true;
    }

    public record EntityTrailData(int entityId, String context, boolean left,
                                  int frames,
                                  String animationEnd) implements TrailData {

        public static final MapCodec<EntityTrailData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                        Codec.INT.fieldOf("entity_id").forGetter(d -> d.entityId),
                        Codec.STRING.fieldOf("context").forGetter(d -> d.context),
                        Codec.BOOL.fieldOf("left").forGetter(d -> d.left),
                        Codec.INT.fieldOf("frames").forGetter(d -> d.frames),
                        Codec.STRING.fieldOf("end").forGetter(d -> d.animationEnd)
                ).apply(builder, EntityTrailData::new)
        );
        public static final StreamCodec<ByteBuf, EntityTrailData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, EntityTrailData::entityId,
                ByteBufCodecs.STRING_UTF8, EntityTrailData::context,
                ByteBufCodecs.BOOL, EntityTrailData::left,
                ByteBufCodecs.INT, EntityTrailData::frames,
                ByteBufCodecs.STRING_UTF8, EntityTrailData::animationEnd, EntityTrailData::new);

        public EntityTrailData(int entityId, String context, int size, boolean left) {
            this(entityId, context, left, size, EntityWeaponTrailProvider.TRAIL_END);
        }

        public static <T extends Entity & EntityWeaponTrailHolderProvider> EntityTrailData create(T entity, String context, boolean left) {
            return new EntityTrailData(entity.getId(), context, 4, left);
        }

        public static <T extends Entity & EntityWeaponTrailHolderProvider> EntityTrailData create(T entity, String context, int size, boolean left) {
            return new EntityTrailData(entity.getId(), context, size, left);
        }

        @Override
        public TrailProviderRegistry.TrailType<?> type() {
            return TrailProviderRegistry.ENTITY_TRAIL;
        }

        @Override
        public TrailProvider createProvider(Level level, Supplier<Vec3> position, IntSupplier lifetime) {
            Entity e = level.getEntity(this.entityId);
            if (!(e instanceof EntityWeaponTrailHolderProvider provider)) {
                return null;
            }
            return provider.getTrailHolder().createFor(this);
        }
    }
}
