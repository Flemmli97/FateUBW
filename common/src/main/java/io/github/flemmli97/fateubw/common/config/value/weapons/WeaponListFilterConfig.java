package io.github.flemmli97.fateubw.common.config.value.weapons;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;

public class WeaponListFilterConfig {

    private List<String> config;
    private boolean whiteList;

    public WeaponListFilterConfig(String... config) {
        this.config = List.of(config);
    }

    public void read(List<String> s) {
        this.config = List.copyOf(s);
        WeaponList.reload();
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

    public boolean isAllowed(Holder<Item> item) {
        ResourceLocation id = item.unwrapKey().orElseThrow().location();
        boolean blackListed = this.config.contains(id.getNamespace()) || this.config.contains(id.toString());
        return blackListed == this.whiteList;
    }
}
