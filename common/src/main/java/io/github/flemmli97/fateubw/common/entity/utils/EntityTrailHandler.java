package io.github.flemmli97.fateubw.common.entity.utils;

import io.github.flemmli97.fateubw.client.render.EntityTrailRenderer;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityTrailHandler {

    private final Entity entity;
    private final TrailPositions positions;

    private Vec3 normal;
    private TrailInfo info;

    private boolean init = true;

    private int finishing;

    private boolean spawned;

    public EntityTrailHandler(Entity entity, int size) {
        this(entity, size, null);
    }

    public EntityTrailHandler(Entity entity, int size, Vec3 normal) {
        this.entity = entity;
        this.positions = new TrailPositions(size);
        this.normal = normal;
        if (this.entity.level().isClientSide()) {
            EntityTrailRenderer.addHandler(this);
        }
    }

    public EntityTrailHandler setInfo(TrailInfo info) {
        this.info = info;
        return this;
    }

    public EntityTrailHandler setNormal(Vec3 normal) {
        this.normal = normal;
        return this;
    }

    public boolean tick() {
        if (!this.spawned) {
            this.spawned = this.entity.level().getEntity(this.entity.getId()) == this.entity;
            if (!this.spawned) {
                this.finishing++;
            }
        } else {
            Vec3 pos = this.entity.position().add(0, this.entity.getBbHeight() * 0.5, 0);
            this.positions.add(pos, this.normal);
            if (this.init) {
                this.init = false;
                this.tick();
            }
            if (this.entity.isRemoved()) {
                this.finishing++;
                this.entity.xo = this.entity.getX();
                this.entity.yo = this.entity.getY();
                this.entity.zo = this.entity.getZ();
            }
        }
        return this.finishing > this.positions.size();
    }

    public boolean valid() {
        return this.spawned;
    }

    @Nullable
    public TrailInfo getInfo() {
        return this.info;
    }

    public TrailPositions getPositions() {
        return this.positions;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
