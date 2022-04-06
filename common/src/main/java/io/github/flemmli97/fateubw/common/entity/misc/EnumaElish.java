package io.github.flemmli97.fateubw.common.entity.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EnumaElish extends EntityBeam {

    public static final float radius = 1.2f;
    public static final float range = 24;

    private Vec3 dir, up, side;

    public EnumaElish(EntityType<? extends EnumaElish> type, Level worldIn) {
        super(type, worldIn);
    }

    public EnumaElish(Level world, LivingEntity shooter) {
        super(ModEntities.ea.get(), world, shooter);
    }

    @Override
    public float radius() {
        return radius;
    }

    @Override
    public float getRange() {
        return range;
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
                    this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 255 / 255F, 15 / 255F, 5 / 255F, 1, 2), this.hitVec.x(), this.hitVec.y() - 0.15, this.hitVec.z(), this.random.nextGaussian() * 0.007, this.random.nextGaussian() * 0.007 + 0.003, this.random.nextGaussian() * 0.007);
            Vec3 pos = this.position();
            for (int i = 0; i < 4; i++) {
                double upScale = this.random.nextDouble() * 2 - 1 + 0.3;
                double sideScale = this.random.nextDouble() * 2.2 - 1.1;
                double lenScale = this.random.nextDouble();
                Vec3 ppos = pos.add(this.up.scale(upScale)).add(this.side.scale(sideScale)).add(this.dir.scale(lenScale));
                this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 255 / 255F, 15 / 255F, 15 / 255F, 1, 0.15f), ppos.x(), ppos.y(), ppos.z(), this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01);
            }
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
        result.getEntity().hurt(CustomDamageSource.excalibur(this, this.getOwner()), Config.Common.eaDamage);
    }
}
