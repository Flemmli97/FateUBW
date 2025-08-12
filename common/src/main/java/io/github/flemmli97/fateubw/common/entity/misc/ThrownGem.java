package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.registry.FateEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ThrownGem extends BaseProjectile {

    public ThrownGem(EntityType<? extends ThrownGem> type, Level level) {
        super(type, level);
    }

    public ThrownGem(Level level, LivingEntity shooter) {
        super(FateEntities.GEM.get(), level, shooter);
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult entityRayTraceResult) {
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
        this.discard();
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
        this.discard();
    }
}
