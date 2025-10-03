package io.github.flemmli97.fateubw.common.entity.utils;

public enum MoveType {

    NONE(false),
    WALK(true),
    RUN(true),
    SNEAK(true),
    FLY(false);

    public final boolean speedDependent;

    MoveType(boolean speedDependent) {
        this.speedDependent = speedDependent;
    }
}
