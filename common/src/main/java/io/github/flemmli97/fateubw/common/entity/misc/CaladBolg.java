package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.common.utils.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CaladBolg extends BaseProjectile {

    public CaladBolg(EntityType<? extends CaladBolg> type, Level level) {
        super(type, level);
    }

    public CaladBolg(Level level, LivingEntity shootingEntity) {
        super(FateEntities.CALADBOLG.get(), level, shootingEntity);
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        this.doExplosion(result.getEntity().getX(), result.getEntity().getY(), result.getEntity().getZ(), result.getEntity());
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.doExplosion(result.getLocation().x, result.getLocation().y, result.getLocation().z, null);
    }

    private void doExplosion(double x, double y, double z, Entity hit) {
        this.doExplosion(hit);
        this.level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 1.0f, 1.0f);
        this.discard();
        S2CScreenShake.sendAround(this, 9, 8, 2);
    }

    protected void doExplosion(Entity hit) {
        float dmg = Utils.magicDamage(this.getOwner()) + CommonConfig.caladBolgDmg;
        if (hit != null)
            hit.hurt(CustomDamageSource.caladBolg(this, this.getOwner()), dmg);
        Vec3 pos = hit != null ? hit.position() : this.position();
        List<Entity> list = this.level.getEntities(this, new AABB(-6, -6, -6, 6, 6, 6).move(pos));
        for (Entity e : list) {
            double dist;
            if ((dist = e.distanceToSqr(this)) > 36 || (e != hit && !this.canHit(e)))
                continue;
            dist -= 8;
            float dmgPerc = (float) Mth.clamp(1 - (dist / 26f), 0.15f, 1);
            e.hurt(CustomDamageSource.caladBolg(this, this.getOwner()), dmg * dmgPerc);
        }
        if (this.level instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x(), pos.y(), pos.z(), 2, 1.0, 0.0, 0.0, 1);
    }
}