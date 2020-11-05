package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.utils.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityBeam;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class EntityExcalibur extends EntityBeam {

    public static final float radius = 2f;
    public static final float range = 16;

    public EntityExcalibur(EntityType<? extends EntityExcalibur> type, World world) {
        super(type, world);
    }

    public EntityExcalibur(World world, LivingEntity shooter) {
        super(ModEntities.excalibur.get(), world, shooter);
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
        result.getEntity().attackEntityFrom(CustomDamageSource.excalibur(this, this.getShooter()), Config.Common.excaliburDamage);
    }
}
