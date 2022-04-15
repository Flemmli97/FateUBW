package io.github.flemmli97.fateubw.common.items.weapons;

import com.google.common.base.Suppliers;
import io.github.flemmli97.tenshilib.api.item.IDualWeapon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

import java.util.function.Supplier;

public class ItemKanshouBakuya extends SwordItem implements IDualWeapon {

    private final Supplier<ItemStack> stack;

    public ItemKanshouBakuya(Tier tier, int i, float f, Properties properties, Supplier<Item> other) {
        super(tier, i, f, properties);
        this.stack = Suppliers.memoize(() -> new ItemStack(other.get()));
    }

    @Override
    public ItemStack offHandStack(LivingEntity entity) {
        ItemStack stack = this.stack.get();
        stack.setTag(entity.getMainHandItem().getTag());
        return stack;
    }
}
