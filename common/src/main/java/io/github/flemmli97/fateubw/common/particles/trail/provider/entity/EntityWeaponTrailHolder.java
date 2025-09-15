package io.github.flemmli97.fateubw.common.particles.trail.provider.entity;

import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class EntityWeaponTrailHolder<T extends Entity & AnimatedEntity & EntityWeaponTrailHolderProvider> {

    private final Set<EntityWeaponTrailProvider<T>> trailPositionTracker = new HashSet<>();

    private final T entity;

    public EntityWeaponTrailHolder(T entity) {
        this.entity = entity;
    }

    public void tick() {
        this.trailPositionTracker.removeIf(EntityWeaponTrailProvider::removed);
    }

    public EntityWeaponTrailProvider<T> createFor(EntityWeaponTrailProvider.EntityTrailData data) {
        EntityWeaponTrailProvider<T> provider = new EntityWeaponTrailProvider<>(data, this.entity);
        this.trailPositionTracker.add(provider);
        return provider;
    }

    public void recordData(String context, boolean left, Vec3 pos, Vec3 normal) {
        this.trailPositionTracker.forEach(p -> p.recordData(context, left, pos, normal));
    }
}
