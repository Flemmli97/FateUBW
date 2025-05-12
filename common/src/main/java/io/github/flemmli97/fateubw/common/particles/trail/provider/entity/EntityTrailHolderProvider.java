package io.github.flemmli97.fateubw.common.particles.trail.provider.entity;

import com.mojang.math.Vector4f;

public interface EntityTrailHolderProvider {

    EntityTrailHolder<?> getTrailHolder();

    default Vector4f[] weaponTrailEdge(boolean left) {
        return new Vector4f[]{
                new Vector4f(0, 0, 0, 1),
                new Vector4f(0, -0.2f, -1, 1)
        };
    }
}
