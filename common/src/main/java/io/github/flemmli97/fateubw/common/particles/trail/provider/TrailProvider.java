package io.github.flemmli97.fateubw.common.particles.trail.provider;

import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface TrailProvider {

    @Nullable
    TrailPositions positions();

    @Nullable
    Vec3 particleTick();

    TrailData data();

    boolean removed();

    default float adjustedPartialTicks(float partialTicks) {
        return partialTicks;
    }
}
