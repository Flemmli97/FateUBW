package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ThrownGem extends EntityProjectile {

    public ThrownGem(EntityType<? extends ThrownGem> type, Level world) {
        super(type, world);
    }

    public ThrownGem(Level world, LivingEntity shooter) {
        super(ModEntities.gem.get(), world, shooter);
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult entityRayTraceResult) {
        this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
        this.kill();
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
        this.kill();
    }
}
