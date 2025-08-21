package io.github.flemmli97.fateubw.common.particles.trail.provider.entity;

import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class EntityTrailHolder<T extends Entity & AnimatedEntity & EntityTrailHolderProvider> {

    private final Set<EntityTrailProvider<T>> trailPositionTracker = new HashSet<>();

    private final T entity;

    public EntityTrailHolder(T entity) {
        this.entity = entity;
    }

    public void tick() {
        this.trailPositionTracker.removeIf(EntityTrailProvider::removed);
    }

    public EntityTrailProvider<T> createFor(EntityTrailProvider.EntityTrailData data) {
        EntityTrailProvider<T> provider = new EntityTrailProvider<>(data, data.size(), this.entity);
        this.trailPositionTracker.add(provider);
        return provider;
    }

    public void recordData(String context, boolean left, Vec3 pos, Vec3 normal, float partialTicks) {
        this.trailPositionTracker.forEach(p -> p.recordData(context, left, pos, normal, partialTicks));
    }
}
