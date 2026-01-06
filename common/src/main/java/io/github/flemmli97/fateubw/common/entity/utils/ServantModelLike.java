package io.github.flemmli97.fateubw.common.entity.utils;

public interface ServantModelLike {

    float interpolatedMoveTick(float partialTick);

    float interpolatedMoveTickOf(MoveType moveType, float partialTick);

    default boolean flipAnimation() {
        return false;
    }

    default boolean isStaying() {
        return false;
    }

    int maxDeathTick();

    default String getDeathAnimation() {
        return null;
    }

    default boolean hasOwnWeapon() {
        return false;
    }
}
