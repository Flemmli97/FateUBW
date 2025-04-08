package io.github.flemmli97.fateubw.common.particles.trail;

public interface EntityTrailData {

    TrailPositions getTrailData(String context);

    boolean valid(String context);
}
