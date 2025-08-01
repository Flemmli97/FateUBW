package io.github.flemmli97.fateubw.common.entity.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.client.ShakeHandler;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.registry.ModSounds;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Excalibur extends BaseBeam {

    public static final float RADIUS = 1.35f;
    public static final float RANGE = 16;

    private Vec3 dir, up, side;

    public Excalibur(EntityType<? extends Excalibur> type, Level level) {
        super(type, level);
    }

    public Excalibur(Level level, LivingEntity shooter) {
        super(ModEntities.EXCALIBUR.get(), level, shooter);
        Vec3 off = new Vec3(shooter.getLookAngle().x, 0, shooter.getLookAngle().z).normalize().scale(shooter.getBbWidth() * 0.5);
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
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.livingTicks <= this.livingTickMax() - 15)
                for (int i = 0; i < 2; i++)
                    this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 245 / 255F, 245 / 255F, 5 / 255F, 1, 2), this.hitVec.x(), this.hitVec.y() - 0.15, this.hitVec.z(), this.random.nextGaussian() * 0.007, this.random.nextGaussian() * 0.007 + 0.003, this.random.nextGaussian() * 0.007);
            Vec3 pos = this.position();
            for (int i = 0; i < 4; i++) {
                double upScale = this.random.nextDouble() * 2 - 1 + 0.3;
                double sideScale = this.random.nextDouble() * 2.2 - 1.1;
                double lenScale = this.random.nextDouble();
                Vec3 ppos = pos.add(this.up.scale(upScale)).add(this.side.scale(sideScale)).add(this.dir.scale(lenScale));
                this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 245 / 255F, 245 / 255F, 5 / 255F, 1, 0.15f), ppos.x(), ppos.y(), ppos.z(), this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01);
            }
            if (this.tickCount % 3 == 1) {
                ShakeHandler.shakeScreen(this.position(), this.getRange() + 4, 3, 1.5f);
            }
        } else if (this.livingTicks == 1) {
            this.playSound(ModSounds.ENTITY_EXCALIBUR_SHOOT.get(), 0.8f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.4f);
        }
    }

    @Override
    public HitResult getHitRay() {
        HitResult res = super.getHitRay();
        this.up = this.getUpVector(1).normalize().scale(this.radius());
        this.dir = res.getLocation().subtract(this.position());
        this.side = new Vec3(RayTraceUtils.rotatedAround(this.dir, new Vector3f(this.up), 90))
                .normalize().scale(this.radius());
        return res;
    }

    @Override
    public void onImpact(EntityHitResult result) {
        result.getEntity().hurt(CustomDamageSource.excalibur(this, this.getOwner()), Utils.magicDamage(this.getOwner()) + CommonConfig.excaliburDamage);
    }
}
