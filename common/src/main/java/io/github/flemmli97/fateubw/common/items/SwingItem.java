package io.github.flemmli97.fateubw.common.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface SwingItem {

    boolean onEntitySwing(ItemStack stack, LivingEntity entity);
}
