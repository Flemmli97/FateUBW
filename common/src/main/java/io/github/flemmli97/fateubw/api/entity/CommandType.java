package io.github.flemmli97.fateubw.api.entity;

public enum CommandType {
    NORMAL(true),
    AGGRESSIVE(true),
    DEFENSIVE(true),
    FOLLOW(true),
    STAY(true),
    GUARD(true),
    NP(false),
    KILL(false);

    public final boolean isBehaviour;

    CommandType(boolean isBehaviour) {
        this.isBehaviour = isBehaviour;
    }
}
