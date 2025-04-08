package io.github.flemmli97.fateubw.common.particles.trail.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.particles.trail.EntityTrailData;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.common.particles.trail.TrailProviderRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityTrailProvider<T extends Entity & EntityTrailData> implements TrailProvider {

    public static final Codec<EntityTrailProvider<?>> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                    Codec.INT.fieldOf("entity_id").forGetter(d -> d.id),
                    Codec.STRING.fieldOf("context").forGetter(d -> d.context)
            ).apply(builder, EntityTrailProvider::new)
    );

    private final String context;

    private T entity;
    private final int id;

    public EntityTrailProvider(T entity, String context) {
        this.entity = entity;
        this.id = entity.getId();
        this.context = context;
    }

    private EntityTrailProvider(int entity, String context) {
        this.id = entity;
        this.context = context;
    }

    public EntityTrailProvider(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.context = buf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        if (this.entity != null)
            buf.writeInt(this.entity.getId());
        else
            buf.writeInt(this.id);
        buf.writeUtf(this.context);
    }

    @Override
    public TrailPositions positions(Level level) {
        T entity = this.getEntity(level);
        return entity != null ? entity.getTrailData(this.context) : null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private T getEntity(Level level) {
        if (this.entity != null)
            return this.entity;
        Entity e = level.getEntity(this.id);
        if (!(e instanceof EntityTrailData)) {
            return null;
        }
        this.entity = (T) e;
        return this.entity;
    }

    @Override
    public Vec3 particleTick(Level level) {
        TrailPositions pos = this.positions(level);
        return pos != null && pos.getLast() != null ? pos.getLast().pos() : null;
    }

    @Override
    public ResourceLocation id() {
        return TrailProviderRegistry.ENTITY_TRAIL;
    }

    @Override
    public boolean removed(Level level) {
        T entity = this.getEntity(level);
        return entity == null || !entity.valid(this.context);
    }
}
