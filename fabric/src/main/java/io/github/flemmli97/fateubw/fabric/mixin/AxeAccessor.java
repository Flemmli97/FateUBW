package io.github.flemmli97.fateubw.fabric.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AxeItem.class)
public interface AxeAccessor {

    @Invoker("<init>")
    static AxeItem inst(Tier tier, float f, float g, Item.Properties properties) {
        throw new IllegalStateException();
    }
}
