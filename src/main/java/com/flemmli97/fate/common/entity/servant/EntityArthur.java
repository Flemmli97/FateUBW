package com.flemmli97.fate.common.entity.servant;

import com.flemmli97.fate.common.utils.EnumServantType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityArthur extends EntityServant{

    public EntityArthur(EntityType<? extends EntityArthur> entityType, World world) {
        super(entityType, world, EnumServantType.SABER, "arthur.hogou", drops);
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        return 0;
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        return false;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return new AnimatedAction[0];
    }
}
