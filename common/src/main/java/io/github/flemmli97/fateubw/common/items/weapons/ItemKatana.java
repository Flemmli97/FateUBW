package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.tenshilib.api.item.IExtendedWeapon;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class ItemKatana extends SwordItem implements IExtendedWeapon {

    public ItemKatana(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public float getRange() {
        return 5;
    }
}
