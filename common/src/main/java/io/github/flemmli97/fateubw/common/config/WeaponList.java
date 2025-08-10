package io.github.flemmli97.fateubw.common.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeaponList {

    private List<Item> weapons;
    private List<String> blacklist;
    private boolean whiteList;

    public WeaponList(String... blacklist) {
        this.blacklist = List.of(blacklist);
    }

    public WeaponList read(List<String> s) {
        this.blacklist = List.copyOf(s);
        this.weapons = null;
        return this;
    }

    public void setWhiteList(boolean whiteList) {
        this.whiteList = whiteList;
    }

    public boolean isWhiteList() {
        return this.whiteList;
    }

    public List<String> write() {
        return List.copyOf(this.blacklist);
    }

    public ItemStack getRandomWeapon(Random rand) {
        if (this.weapons == null) {
            this.weapons = new ArrayList<>();
            BuiltInRegistries.ITEM.entrySet().forEach(entry -> {
                if (entry.getValue() instanceof TieredItem) {
                    boolean blackListed = this.blacklist.contains(entry.getKey().location().getNamespace()) ||
                            this.blacklist.contains(entry.getKey().location().toString());
                    if (blackListed == this.whiteList)
                        this.weapons.add(entry.getValue());
                }
            });
        }
        if (this.weapons.isEmpty())
            return ItemStack.EMPTY;
        return new ItemStack(this.weapons.get(rand.nextInt(this.weapons.size())));
    }
}
