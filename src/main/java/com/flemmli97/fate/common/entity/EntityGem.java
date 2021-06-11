package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityGem extends EntityProjectile {

    public EntityGem(EntityType<? extends EntityGem> type, World world) {
        super(type, world);
    }

    public EntityGem(World world, LivingEntity shooter) {
        super(ModEntities.gem.get(), world, shooter);
    }

    @Override
    protected boolean onEntityHit(EntityRayTraceResult entityRayTraceResult) {
        this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 2.0F, Explosion.Mode.NONE);
        this.remove();
        return true;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult blockRayTraceResult) {
        this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 2.0F, Explosion.Mode.NONE);
        this.remove();
    }
}
