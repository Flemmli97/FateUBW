package io.github.flemmli97.fateubw.common.particles.trail.provider.entity;

import org.joml.Vector4f;

public interface EntityWeaponTrailHolderProvider {

    EntityWeaponTrailHolder<?> getTrailHolder();

    default WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector4f(0, 0, 0, 1), new Vector4f(0, 0, -0.5f, 1));
    }

    record WeaponTrail(Vector4f start, Vector4f end) {
    }
}
