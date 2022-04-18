package io.github.flemmli97.fateubw.fabric.mixin;

import io.github.flemmli97.fateubw.common.items.weapons.ClassSpear;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void enchantTest(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        EnchantmentCategory cat = ((Enchantment) (Object) this).category;
        if ((cat == EnchantmentCategory.WEAPON || cat == EnchantmentCategory.TRIDENT) && stack.getItem() instanceof ClassSpear) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}
