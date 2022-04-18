package io.github.flemmli97.fateubw.fabric.mixin;

import io.github.flemmli97.fateubw.common.attachment.ItemStackData;
import io.github.flemmli97.fateubw.fabric.common.data.ItemStackDataGet;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackDataGet {

    @Unique
    private final ItemStackData fateData = new ItemStackData();

    @Override
    public ItemStackData getData() {
        return this.fateData;
    }
}
