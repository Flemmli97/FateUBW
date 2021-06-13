package io.github.flemmli97.fate.common.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CachedWeaponList {

    public static final List<Item> weapons = new ArrayList<>();

    public static void init() {
        ForgeRegistries.ITEMS.forEach(item -> {
            if (item instanceof TieredItem)
                weapons.add(item);
        });
    }

    public static ItemStack getRandomWeapon(Random rand) {
        if (weapons.size() == 0)
            return ItemStack.EMPTY;
        return new ItemStack(weapons.get(rand.nextInt(weapons.size())));
    }
}
