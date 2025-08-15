package io.github.flemmli97.fateubw.common.entity.utils;

public interface ServantLike {

    float interpolatedMoveTick(float partialTicks);

    float interpolatedMoveTickOf(MoveType moveType, float partialTicks);

    boolean flipAnimation();

    boolean isStaying();
}
