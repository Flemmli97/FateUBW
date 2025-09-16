package io.github.flemmli97.fateubw.common.entity.utils;

import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityTrailHandler {

    private final Entity entity;
    private final TrailPositions positions;

    public EntityTrailHandler(Entity entity, int size) {
        this.entity = entity;
        this.positions = new TrailPositions(size);
    }

    public void tick() {
        Vec3 pos = this.entity.position().add(0, this.entity.getBbHeight() * 0.5, 0);
        this.positions.add(pos, null);
    }

    public TrailPositions getPositions() {
        return this.positions;
    }
}
