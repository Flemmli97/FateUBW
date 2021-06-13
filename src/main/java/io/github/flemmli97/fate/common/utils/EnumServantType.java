package io.github.flemmli97.fate.common.utils;

public enum EnumServantType {

    SABER("saber"),
    LANCER("lancer"),
    ARCHER("archer"),
    CASTER("caster"),
    BERSERKER("berserker"),
    RIDER("rider"),
    ASSASSIN("assassin"),
    NOTASSIGNED("undef");

    private String name;

    EnumServantType(String s) {
        this.name = s;
    }

    public String getLowercase() {
        return this.name;
    }
}
