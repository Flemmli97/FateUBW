package io.github.flemmli97.fate.common.entity;

import com.flemmli97.tenshilib.common.entity.EntityBeam;
import com.flemmli97.tenshilib.common.particle.ColoredParticleData;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.registry.ModEntities;
import io.github.flemmli97.fate.common.registry.ModParticles;
import io.github.flemmli97.fate.common.utils.CustomDamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class EntityEnumaElish extends EntityBeam {

    public static final float radius = 1.2f;
    public static final float range = 24;

    private Vector3d dir, up, side;

    public EntityEnumaElish(EntityType<? extends EntityEnumaElish> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityEnumaElish(World world, LivingEntity shooter) {
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
        if(this.world.isRemote) {
            for(int i = 0; i < 2; i++)
                this.world.addParticle(new ColoredParticleData(ModParticles.light.get(), 255 / 255F, 15 / 255F, 5 / 255F, 1, 2), this.hitVec.getX(), this.hitVec.getY(), this.hitVec.getZ(), this.rand.nextGaussian() * 0.007, this.rand.nextGaussian() * 0.007 + 0.003, this.rand.nextGaussian() * 0.007);
            Vector3d pos = this.getPositionVec();
            for (int i = 0; i < 4; i++) {
                double upScale = this.rand.nextDouble() * 2 - 1 + 0.3;
                double sideScale = this.rand.nextDouble() * 2 - 1 + 0.3;
                double lenScale = this.rand.nextDouble();
                Vector3d ppos = pos.add(this.up.scale(upScale)).add(this.side.scale(sideScale)).add(this.dir.scale(lenScale));
                this.world.addParticle(new ColoredParticleData(ModParticles.light.get(), 255 / 255F, 15 / 255F, 15 / 255F, 1, 0.15f), ppos.getX(), ppos.getY(), ppos.getZ(), this.rand.nextGaussian() * 0.01, this.rand.nextGaussian() * 0.01, this.rand.nextGaussian() * 0.01);
            }
        }
    }

    @Override
    public RayTraceResult getHitRay() {
        RayTraceResult res = super.getHitRay();
        this.up = this.getUpVector(1).normalize().scale(this.radius());
        this.side = new Vector3d(RayTraceUtils.rotatedAround(res.getHitVec(), new Vector3f(this.up), 90))
                .normalize().scale(this.radius());
        this.dir = res.getHitVec().subtract(this.getPositionVec());
        return res;
    }

    @Override
    public void onImpact(EntityRayTraceResult result) {
        result.getEntity().attackEntityFrom(CustomDamageSource.excalibur(this, this.getOwner()), Config.Common.eaDamage);
    }
}
