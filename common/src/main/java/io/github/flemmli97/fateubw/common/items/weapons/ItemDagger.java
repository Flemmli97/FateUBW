package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.tenshilib.api.item.IExtendedWeapon;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class ItemDagger extends SwordItem implements IExtendedWeapon {

    public ItemDagger(Tier tier, int baseDmg, float speed, Properties props) {
        super(tier, baseDmg, speed, props);
    }

    @Override
    public float getRange() {
        return 3;
    }
}
