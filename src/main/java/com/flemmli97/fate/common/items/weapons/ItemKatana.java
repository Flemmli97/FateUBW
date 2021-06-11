package com.flemmli97.fate.common.items.weapons;

import com.flemmli97.tenshilib.api.item.IExtendedWeapon;
import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;

public class ItemKatana extends SwordItem implements IExtendedWeapon {

    public ItemKatana(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public float getRange() {
        return 5;
    }
}
