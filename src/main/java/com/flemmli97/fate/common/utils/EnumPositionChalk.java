package com.flemmli97.fate.common.utils;

import net.minecraft.util.IStringSerializable;

public enum EnumPositionChalk {

    /**
     * R = row, C = column, orientation is north starting from top left
     */
    R1C1(0, "r1c1"),
    R1C2(1, "r1c2"),
    R1C3(2, "r1c3"),
    R1C4(3, "r1c4"),
    R1C5(4, "r1c5"),
    R2C1(5, "r2c1"),
    R2C2(6, "r2c2"),
    R2C3(7, "r2c3"),
    R2C4(8, "r2c4"),
    R2C5(9, "r2c5"),
    R3C1(10, "r3c1"),
    R3C2(11, "r3c2"),
    //middle
    MIDDLE(12, "none"),

    R3C4(13, "r3c4"),
    R3C5(14, "r3c5"),
    R4C1(15, "r4c1"),
    R4C2(16, "r4c2"),
    R4C3(17, "r4c3"),
    R4C4(18, "r4c4"),
    R4C5(19, "r4c5"),
    R5C1(20, "r5c1"),
    R5C2(21, "r5c2"),
    R5C3(22, "r5c3"),
    R5C4(23, "r5c4"),
    R5C5(24, "r5c5");

    int metaData;
    String name;

    EnumPositionChalk(int meta, String name) {
        this.metaData = meta;
        this.name = name;
    }

    public int getMetadata() {
        return this.metaData;
    }

    public static EnumPositionChalk fromPos(int meta) {
        if (meta < 0 || meta >= EnumPositionChalk.values().length)
            meta = 0;
        return EnumPositionChalk.values()[meta];
    }

    public String getID() {
        return this.name;
    }
}