package io.github.flemmli97.fateubw.common.entity.utils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface ServantModelLike {

    Map<Item, ItemStack> STACK_CACHE = new HashMap<>();

    static ItemStack getStack(Item item) {
        return STACK_CACHE.computeIfAbsent(item, i -> new ItemStack(item));
    }

    float interpolatedMoveTick(float partialTick);

    float interpolatedMoveTickOf(MoveType moveType, float partialTick);

    default boolean flipAnimation() {
        return false;
    }

    default boolean isStaying() {
        return false;
    }

    int maxDeathTick();

    default String getDeathAnimation() {
        return null;
    }

    default int blinkTick() {
        return 0;
    }

    /**
     * Animations are catered to the servants weapons.
     * So return an appropriate item to render here.
     */
    default ItemStack getRenderHandStack(InteractionHand hand) {
        LivingEntity entity = (Mob) this;
        return entity.getItemInHand(hand);
    }
}
