package io.github.flemmli97.fateubw.common.particles.trail.provider.entity;

import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import org.joml.Vector3f;

public interface EntityWeaponTrailHolderProvider {

    EntityWeaponTrailHolder<?> getTrailHolder();

    default boolean shouldRecordData() {
        if (this instanceof AnimatedEntity animated) {
            AnimationState anim = animated.getAnimationHandler().getAnimation();
            return anim != null && anim.isPast(EntityWeaponTrailProvider.TRAIL_START) && !anim.isPast(EntityWeaponTrailProvider.TRAIL_END);
        }
        return false;
    }

    default WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector3f(0, 0, 0), new Vector3f(0, 0, -1f));
    }

    record WeaponTrail(Vector3f start, Vector3f end) {
    }
}
