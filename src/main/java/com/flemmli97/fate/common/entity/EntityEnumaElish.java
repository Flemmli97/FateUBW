package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.utils.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityBeam;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class EntityEnumaElish extends EntityBeam {

    public static final float radius = 1.2f;
    public static final float range = 24;

    public EntityEnumaElish(EntityType<? extends EntityEnumaElish> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityEnumaElish(World world, LivingEntity shooter) {
        super(ModEntities.ea.get(), world, shooter);
    }

    @Override
    public float radius() {
        return radius;
    }

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public void onImpact(EntityRayTraceResult result) {
        result.getEntity().attackEntityFrom(CustomDamageSource.excalibur(this, this.getOwner()), Config.Common.eaDamage);
    }
}
