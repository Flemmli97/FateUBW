package io.github.flemmli97.fateubw.common.config.value;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

import java.util.ArrayList;
import java.util.List;

public class WeaponList {

    private List<String> config;
    private List<Item> weapons;
    private boolean whiteList;

    public WeaponList(String... config) {
        this.config = List.of(config);
    }

    public WeaponList read(List<String> s) {
        this.config = List.copyOf(s);
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
        return List.copyOf(this.config);
    }

    public ItemStack getRandomWeapon(RandomSource rand) {
        if (this.weapons == null) {
            this.weapons = new ArrayList<>();
            BuiltInRegistries.ITEM.entrySet().forEach(entry -> {
                if (entry.getValue() instanceof TieredItem) {
                    boolean blackListed = this.config.contains(entry.getKey().location().getNamespace()) ||
                            this.config.contains(entry.getKey().location().toString());
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
