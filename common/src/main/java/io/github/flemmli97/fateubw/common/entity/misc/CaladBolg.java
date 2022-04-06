package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class CaladBolg extends EntityProjectile {

    public CaladBolg(EntityType<? extends CaladBolg> type, Level worldIn) {
        super(type, worldIn);
    }

    public CaladBolg(Level world, LivingEntity shootingEntity) {
        super(ModEntities.caladbolg.get(), world, shootingEntity);
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
    protected boolean entityRayTraceHit(EntityHitResult result) {
        result.getEntity().hurt(CustomDamageSource.caladBolg(this, this.getOwner()), Config.Common.caladBolgDmg);
        this.kill();
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.kill();
    }
}