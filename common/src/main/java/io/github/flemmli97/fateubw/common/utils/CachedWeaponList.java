package io.github.flemmli97.fateubw.common.utils;

import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CachedWeaponList {

    private static List<Item> weapons;

    public static ItemStack getRandomWeapon(Random rand) {
        if (weapons == null) {
            weapons = new ArrayList<>();
            PlatformUtils.INSTANCE.items().getIterator().forEach(item -> {
                if (item instanceof TieredItem)
                    weapons.add(item);
            });
        }
        if (weapons.isEmpty())
            return ItemStack.EMPTY;
        return new ItemStack(weapons.get(rand.nextInt(weapons.size())));
    }
}
