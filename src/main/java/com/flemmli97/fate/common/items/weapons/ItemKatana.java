package com.flemmli97.fate.common.items.weapons;

import com.flemmli97.tenshilib.api.item.IExtendedWeapon;
import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;

public class ItemKatana extends SwordItem implements IExtendedWeapon {

    public ItemKatana(IItemTier p_i48460_1_, int p_i48460_2_, float p_i48460_3_, Properties p_i48460_4_) {
        super(p_i48460_1_, p_i48460_2_, p_i48460_3_, p_i48460_4_);
    }

    @Override
    public float getRange() {
        return 5;
    }
}
