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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityTrailProvider<T extends Entity & AnimatedEntity & EntityTrailHolderProvider> implements TrailProvider {

    public static final String TRAIL_START = "weapon_swing_start";
    public static final String TRAIL_END = "weapon_swing_end";

    private static final int PARTIAL_INTERVALS = 5;

    private final int size;
    private final EntityTrailData data;
    private final T entity;
    private final TrailPositions position;

    private boolean valid = true;
    private float lastUpdateTick = -1;

    private final List<TrailPositions.TrailPosition> batched = new ArrayList<>();
    private TrailPositions.TrailPosition last;
    private int invalidTicks;

    public EntityTrailProvider(EntityTrailData data, T entity) {
        this.data = data;
        this.entity = entity;
        this.size = 4;
        this.position = new TrailPositions(this.size * PARTIAL_INTERVALS);
    }

    @Override
    public TrailPositions positions() {
        return this.position;
    }

    @Override
    public Vec3 particleTick() {
        this.lastUpdateTick -= 1;
        this.addBatchedData();
        if (this.valid) {
            AnimationState anim = this.entity.getAnimationHandler().getAnimation();
            if (!this.entity.isAlive() || anim == null || anim.isPast(this.data.animationEnd)) {
                this.valid = false;
                this.last = this.position.getLast();
            }
        } else {
            ++this.invalidTicks;
            if (this.last != null) {
                for (int i = 0; i < PARTIAL_INTERVALS; i++)
                    this.position.add(this.last);
            }
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
        return 0;
    }

    public void recordData(String context, boolean left, Vec3 pos, Vec3 normal, float partialTicks) {
        if ((partialTicks - this.lastUpdateTick <= (1f / PARTIAL_INTERVALS)) || !this.valid || !context.equals(this.data.context) || left != this.data.left)
            return;
        this.batched.add(new TrailPositions.TrailPosition(pos, normal));
        this.lastUpdateTick = partialTicks;
    }

    private void addBatchedData() {
        if (!this.batched.isEmpty()) {
            int missing = PARTIAL_INTERVALS - this.batched.size();
            if (this.batched.size() == 1) {
                TrailPositions.TrailPosition last = this.batched.get(0);
                this.position.add(last);
                for (int i = 0; i < missing; i++) {
                    this.position.add(last);
                }
            } else {
                float insertLocations = this.size - 1;
                int per = (int) (missing / insertLocations);
                int rest = (int) (missing - per * insertLocations); // < insertLocations
                for (int i = 0; i < this.batched.size(); i++) {
                    TrailPositions.TrailPosition pos = this.batched.get(i);
                    this.position.add(pos);
                    if (pos != null && i + 1 < this.batched.size()) {
                        TrailPositions.TrailPosition next = this.batched.get(i + 1);
                        int amount = per + (i < (insertLocations - rest) ? 0 : 1);
                        if (next == null) { // No interpolation needed
                            for (int n = 1; n <= amount; n++) {
                                this.position.add(pos);
                            }
                        } else {
                            Vec3 dPos = next.pos().subtract(pos.pos()).scale(1. / amount);
                            Vec3 dNorm = pos.normal() == null || next.normal() == null
                                    ? null : next.normal().subtract(pos.normal()).scale(1. / amount);
                            for (int n = 1; n <= amount; n++) {
                                this.position.add(new TrailPositions.TrailPosition(pos.pos().add(dPos.scale(n)),
                                        dNorm == null ? pos.normal() : pos.normal().add(dNorm.scale(n))));
                            }
                        }
                    }
                }
            }
            this.batched.clear();
        }
    }

    public record EntityTrailData(int entityId, String context, boolean left,
                                  String animationEnd) implements TrailData {

        public static final MapCodec<EntityTrailData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                        Codec.INT.fieldOf("entity_id").forGetter(d -> d.entityId),
                        Codec.STRING.fieldOf("context").forGetter(d -> d.context),
                        Codec.BOOL.fieldOf("left").forGetter(d -> d.left),
                        Codec.STRING.fieldOf("end").forGetter(d -> d.animationEnd)
                ).apply(builder, EntityTrailData::new)
        );

        public EntityTrailData(int entityId, String context, boolean left) {
            this(entityId, context, left, EntityTrailProvider.TRAIL_END);
        }

        public EntityTrailData(FriendlyByteBuf buf) {
            this(buf.readInt(), buf.readUtf(), buf.readBoolean(), buf.readUtf());
        }

        public static <T extends Entity & EntityTrailHolderProvider> EntityTrailData create(T entity, String context, boolean left) {
            return new EntityTrailData(entity.getId(), context, left);
        }

        @Override
        public ResourceLocation id() {
            return TrailProviderRegistry.ENTITY_TRAIL;
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeInt(this.entityId);
            buf.writeUtf(this.context);
            buf.writeBoolean(this.left);
            buf.writeUtf(this.animationEnd);
        }

        @Override
        public TrailProvider createProvider(Level level) {
            Entity e = level.getEntity(this.entityId);
            if (!(e instanceof EntityTrailHolderProvider provider)) {
                return null;
            }
            return provider.getTrailHolder().createFor(this);
        }
    }
}
