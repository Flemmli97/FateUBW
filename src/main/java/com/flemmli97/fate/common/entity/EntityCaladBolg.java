package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.utils.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class EntityCaladBolg extends EntityProjectile {

    public EntityCaladBolg(EntityType<? extends EntityCaladBolg> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityCaladBolg(World world, LivingEntity shootingEntity) {
        super(ModEntities.caladbolg.get(), world, shootingEntity);
    }

    public EntityCaladBolg(World worldIn, double x, double y, double z) {
        super(ModEntities.caladbolg.get(), worldIn, x, y, z);
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.001F;
    }

    @Override
    protected boolean onEntityHit(EntityRayTraceResult result) {
        result.getEntity().attackEntityFrom(CustomDamageSource.caladBolg(this, this.getShooter()), Config.Common.caladBolgDmg);
        this.remove();
        return true;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult blockRayTraceResult) {
        this.remove();
    }
}