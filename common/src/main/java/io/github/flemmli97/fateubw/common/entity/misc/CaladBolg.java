package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
        this.level().playSound(null, x, y, z, FateSounds.CALAD_BOLG_IMPACT.get(), this.getSoundSource(), 2, 1);
        this.discard();
        S2CScreenShake.sendAround(this, 20, 30, 1.5f);
    }

    protected void doExplosion(Entity hit) {
        float dmg = Utils.magicDamage(this.getOwner()) + CommonConfig.caladBolgDmg;
        if (hit != null)
            hit.hurt(FateDamageTypes.indirect(FateDamageTypes.CALADBOLG, this, this.getOwner()), dmg);
        Vec3 pos = hit != null ? hit.position() : this.position();
        List<Entity> list = this.level().getEntities(this, new AABB(-6, -6, -6, 6, 6, 6).move(pos));
        for (Entity e : list) {
            double dist;
            if ((dist = e.distanceToSqr(this)) > 36 || (e != hit && !this.canHit(e)))
                continue;
            dist -= 8;
            float dmgPerc = (float) Mth.clamp(1 - (dist / 26f), 0.15f, 1);
            e.hurt(FateDamageTypes.indirect(FateDamageTypes.CALADBOLG, this, this.getOwner()), dmg * dmgPerc);
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x(), pos.y(), pos.z(), 2, 1.0, 0.0, 0.0, 1);
            AdvancedParticleContainer.make(FateParticles.SPHERE_CLOUD.get())
                    .addData(new ScaleData(0, 6, 8))
                    .addData(new ColorData(37 / 255f, 37 / 255f, 188 / 255f, 0.6f))
                    .addData(new ParticleMetaData(30, false, 0))
                    .add(this.level(), null, this.getX(), this.getY(0.5), this.getZ(), true);
            AdvancedParticleContainer.make(FateParticles.SPHERE_CLOUD.get())
                    .addData(new ScaleData(0, 6, 8))
                    .addData(new ColorData(245 / 255f, 101 / 255f, 116 / 255f, 0.4f))
                    .addData(new ParticleMetaData(30, false, 0))
                    .add(this.level(), null, this.getX(), this.getY(0.5), this.getZ(), true);
        }
    }
}