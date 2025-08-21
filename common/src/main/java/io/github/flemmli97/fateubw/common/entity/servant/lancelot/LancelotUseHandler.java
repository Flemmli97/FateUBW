package io.github.flemmli97.fateubw.common.entity.servant.lancelot;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface LancelotUseHandler {

    void startUse(LivingEntity entity, LivingEntity target, InteractionHand hand);

    void use(LivingEntity entity, LivingEntity target, InteractionHand hand);

    boolean matches(ItemStack item);

    boolean isBetterThan(LivingEntity entity, ItemStack current, ItemStack stack);
}
