package io.github.flemmli97.fateubw.common.entity.servant.lancelot.impl;

import io.github.flemmli97.fateubw.common.entity.servant.lancelot.LancelotUseHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.phys.Vec3;

public class BowHandler implements LancelotUseHandler {

    @Override
    public void startUse(LivingEntity entity, LivingEntity target, InteractionHand hand) {
        entity.startUsingItem(hand);
    }

    @Override
    public void use(LivingEntity entity, LivingEntity target, InteractionHand hand) {
        entity.releaseUsingItem();
        ItemStack stack = entity.getItemInHand(hand);
        ItemStack ammo = entity.getProjectile(stack);
        AbstractArrow arrow = Platform.INSTANCE.customBowArrow(stack, ammo,
                ProjectileUtil.getMobArrow(entity, ammo, BowItem.getPowerForTime(entity.getTicksUsingItem()), stack));
        if (target != null) {
            double dX = target.getX() - entity.getX();
            double dY = target.getY(0.3) - arrow.getY();
            double dZ = target.getZ() - entity.getZ();
            double horLen = Math.sqrt(dX * dX + dZ * dZ);
            arrow.shoot(dX, dY + horLen * 0.13, dZ, 2.2F, 8 - entity.level().getDifficulty().getId() * 2);
        } else {
            Vec3 look = entity.getLookAngle();
            arrow.shoot(look.x(), look.y(), look.z(), 2.2F, 11);
        }
        arrow.setCritArrow(true);
        arrow.setBaseDamage(arrow.getBaseDamage() + entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.4);
        entity.playSound(SoundEvents.SKELETON_SHOOT, 1, 1 / (entity.getRandom().nextFloat() * 0.4f + 0.8f));
        entity.level().addFreshEntity(arrow);
    }

    @Override
    public boolean matches(ItemStack item) {
        return item.getItem() instanceof BowItem;
    }

    @Override
    public boolean isBetterThan(LivingEntity entity, ItemStack current, ItemStack stack) {
        int power = EnchantmentHelper.getItemEnchantmentLevel(entity.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.POWER), current);
        int power2 = EnchantmentHelper.getItemEnchantmentLevel(entity.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.POWER), stack);
        if (power == power2) {
            ItemEnchantments enchants = current.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            ItemEnchantments enchants2 = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            return enchants2.size() > enchants.size();
        }
        return power2 > power;
    }
}
