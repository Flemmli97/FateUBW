package io.github.flemmli97.fateubw.fabric.platform;

import io.github.flemmli97.fateubw.platform.Platform;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;

public class PlatformImpl implements Platform {

    @Override
    public boolean canSpawnEvent(Mob mob, ServerLevelAccessor level, MobSpawnType spawnReason) {
        return true;
    }

    @Override
    public CreativeModeTab.Builder tabBuilder() {
        return FabricItemGroup.builder();
    }

    @Override
    public AbstractArrow customBowArrow(ItemStack stack, ItemStack ammo, AbstractArrow arrow) {
        return arrow;
    }
}
