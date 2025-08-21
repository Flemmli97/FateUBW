package io.github.flemmli97.fateubw.mixin;

import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CrossbowItem.class)
public interface CrossbowAccessor {

    @Invoker("getChargingSounds")
    CrossbowItem.ChargingSounds fetchChargingSounds(ItemStack stack);
}
