package io.github.flemmli97.fateubw.common.entity.servant.lancelot.impl;

import io.github.flemmli97.fateubw.common.entity.servant.lancelot.LancelotUseHandler;
import io.github.flemmli97.fateubw.mixin.CrossbowAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class CrossbowHandler implements LancelotUseHandler {

    private int getPowerLevel(LivingEntity entity) {
        return Mth.floor(entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5);
    }

    @Override
    public void startUse(LivingEntity entity, LivingEntity target, InteractionHand hand) {
        entity.startUsingItem(hand);
        ItemStack stack = entity.getUseItem();
        if (stack.getItem() instanceof CrossbowItem crossbow) {
            ((CrossbowAccessor) crossbow)
                    .fetchChargingSounds(stack)
                    .start().ifPresent(sound -> entity.playSound(sound.value(), 1, 0.7f));
        }
    }

    @Override
    public void use(LivingEntity entity, LivingEntity target, InteractionHand hand) {
        entity.releaseUsingItem();
        ItemStack stack = entity.getItemInHand(hand);
        if (stack.getItem() instanceof CrossbowItem crossbow) {
            Holder<Enchantment> power = entity.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.POWER);
            int powerLevel = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(power);
            EnchantmentHelper.updateEnchantments(stack, (mutable) -> mutable.set(power, powerLevel + this.getPowerLevel(entity)));
            ChargedProjectiles projectile = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            float vel = projectile.contains(Items.FIREWORK_ROCKET) ? 1.7F : 3.4F;
            crossbow.performShooting(entity.level(), entity, hand, stack, vel, 1, null);
            EnchantmentHelper.updateEnchantments(stack, (mutable) -> mutable.set(power, powerLevel));
        }
    }

    @Override
    public boolean matches(ItemStack item) {
        return item.getItem() instanceof CrossbowItem;
    }

    @Override
    public boolean isBetterThan(LivingEntity entity, ItemStack current, ItemStack stack) {
        ItemEnchantments enchants = current.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments enchants2 = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        return enchants2.size() > enchants.size();
    }
}
