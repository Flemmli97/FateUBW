package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.client.ShakeHandler;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EnumaElish extends BaseBeam {

    public static final float RADIUS = 1.5f;
    public static final float RANGE = 28;

    private Vec3 dir, up, side;

    public EnumaElish(EntityType<? extends EnumaElish> type, Level level) {
        super(type, level);
    }

    public EnumaElish(Level level, LivingEntity shooter) {
        super(FateEntities.EA.get(), level, shooter);
        Vec3 dir = shooter.getLookAngle();
        Vec3 off = new Vec3(dir.x, 0, dir.z).normalize().scale(shooter.getBbWidth() * 0.5);
        this.setPos(this.getX() + off.x, this.getY(), this.getZ() + off.z);
    }

    @Override
    public float radius() {
        return RADIUS;
    }

    @Override
    public float getRange() {
        return RANGE;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 28;
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks > 2 && this.livingTicks + 2 < this.livingTickMax();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.livingTicks <= this.livingTickMax() - 15)
                for (int i = 0; i < 2; i++) {
                    AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                            .addData(new ColorData(162 / 255F, 12 / 255F, 12 / 255F, 0.6f))
                            .addData(new ScaleData(2))
                            .addData(new MotionData(this.random.nextGaussian() * 0.007, this.random.nextGaussian() * 0.007 + 0.003, this.random.nextGaussian() * 0.007))
                            .addData(new ParticleMetaData(20, false, 0))
                            .add(this.level(), this.hitVec.x(), this.hitVec.y() - 0.15, this.hitVec.z());
                }
            Vec3 pos = this.position();
            for (int i = 0; i < 4; i++) {
                double upScale = this.random.nextDouble() * 2 - 1 + 0.3;
                double sideScale = this.random.nextDouble() * 2.2 - 1.1;
                double lenScale = this.random.nextDouble();
                Vec3 ppos = pos.add(this.up.scale(upScale)).add(this.side.scale(sideScale)).add(this.dir.scale(lenScale));
                AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                        .addData(new ColorData(162 / 255F, 12 / 255F, 12 / 255F, 1))
                        .addData(new ScaleData(0.15f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01))
                        .addData(new ParticleMetaData(20, false, 0))
                        .add(this.level(), ppos.x(), ppos.y(), ppos.z());
            }
            if (this.tickCount % 3 == 1) {
                ShakeHandler.shakeScreen(this.position(), this.getRange() + 4, 3, 1.5f);
            }
        } else if (this.livingTicks == 1) {
            this.playSound(FateSounds.ENTITY_EA_SHOOT.get(), 0.8f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.5f);
        }
    }

    @Override
    public HitResult getHitRay() {
        HitResult res = super.getHitRay();
        this.dir = res.getLocation().subtract(this.position());
        this.up = this.calculateViewVector(this.getXRot() - 90, this.getYRot()).scale(this.radius());
        this.side = this.dir.cross(this.up).normalize().scale(this.radius());
        return res;
    }

    @Override
    public void onImpact(EntityHitResult result) {
        Utils.runWithInvulTimer(this.getOwner(), result.getEntity(),
                e -> e.hurt(FateDamageTypes.indirect(FateDamageTypes.ENUMA_ELISH, this, this.getOwner()), Utils.magicDamage(this.getOwner()) + CommonConfig.eaDamage),
                4);
    }
}
