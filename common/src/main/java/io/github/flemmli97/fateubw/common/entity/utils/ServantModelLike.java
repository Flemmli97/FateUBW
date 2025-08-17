package io.github.flemmli97.fateubw.common.entity.utils;

public interface ServantModelLike {

    float interpolatedMoveTick(float partialTicks);

    float interpolatedMoveTickOf(MoveType moveType, float partialTicks);

    default boolean flipAnimation() {
        return false;
    }

    default boolean isStaying() {
        return false;
    }
}
