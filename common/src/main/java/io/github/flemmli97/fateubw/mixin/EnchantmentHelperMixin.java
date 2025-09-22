package io.github.flemmli97.fateubw.mixin;

import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentInSlotVisitor;)V", at = @At("HEAD"), cancellable = true)
    private static void onHandleEnchants(ItemStack stack, EquipmentSlot slot, LivingEntity entity, EnchantmentHelper.EnchantmentInSlotVisitor visitor, CallbackInfo info) {
        if (entity.hasEffect(FateMobEffects.RULE_BREAKER.asHolder()))
            info.cancel();
    }
}
