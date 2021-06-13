package io.github.flemmli97.fate.common.items.weapons;

import com.flemmli97.tenshilib.api.item.IExtendedWeapon;
import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;

public class ItemDagger extends SwordItem implements IExtendedWeapon {

    public ItemDagger(IItemTier tier, int baseDmg, float speed, Properties props) {
        super(tier, baseDmg, speed, props);
    }

    @Override
    public float getRange() {
        return 3;
    }
}
