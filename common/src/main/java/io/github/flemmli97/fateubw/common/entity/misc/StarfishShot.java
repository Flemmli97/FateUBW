package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.registry.FateEntities;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.joml.Vector3f;

public class StarfishShot extends BaseProjectile {

    public StarfishShot(EntityType<? extends StarfishShot> type, Level level) {
        super(type, level);
    }

    public StarfishShot(Level level, LivingEntity shootingEntity) {
        super(FateEntities.STARFISH_SHOT.get(), level, shootingEntity);
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    public float radius() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(new DustParticleOptions(new Vector3f(33 / 255f, 96 / 255f, 33 / 255f), 1.5f),
                        this.getX(), this.getY(0.5), this.getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1f;
    }

    @Override
    protected float motionReduction(boolean inWater) {
        return 1;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        LivingEntity owner = this.getOwner() instanceof LivingEntity living ? living : null;
        if (owner instanceof Mob mob && result.getEntity() != mob.getTarget())
            return false;
        this.discard();
        float damage = owner != null ? (float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE) : 1;
        return result.getEntity().hurt(this.damageSources().mobProjectile(this, owner), damage);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.discard();
    }
}
