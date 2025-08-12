package io.github.flemmli97.fateubw.common.items.weapons;

import com.google.common.base.Suppliers;
import io.github.flemmli97.tenshilib.common.item.DualWeapon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

import java.util.function.Supplier;

public class KanshouBakuyaItem extends SwordItem implements DualWeapon {

    private final Supplier<ItemStack> stack;

    public KanshouBakuyaItem(Tier tier, Properties properties, Supplier<Supplier<? extends Item>> other) {
        super(tier, properties);
        this.stack = Suppliers.memoize(() -> other.get().get().getDefaultInstance());
    }

    @Override
    public ItemStack offHandStack(LivingEntity entity) {
        ItemStack stack = this.stack.get();
        stack.applyComponents(entity.getMainHandItem().getComponentsPatch());
        return stack;
    }
}
