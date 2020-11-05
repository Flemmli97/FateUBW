package com.flemmli97.fate.common.utils;

public enum EnumServantType {

    SABER("saber"),
    ARCHER("archer"),
    LANCER("lancer"),
    RIDER("rider"),
    BERSERKER("berserker"),
    CASTER("caster"),
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
