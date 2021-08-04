package io.github.flemmli97.fate.common.entity;

import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.registry.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityDaggerHook extends EntityProjectile {

    private Entity hookedEntity;

    private boolean retracting;
    private static final DataParameter<Integer> HOOKED_ENTITY = EntityDataManager.createKey(EntityDaggerHook.class, DataSerializers.VARINT);

    public EntityDaggerHook(EntityType<? extends EntityProjectile> type, World world) {
        super(type, world);
    }

    public EntityDaggerHook(World world, double x, double y, double z) {
        super(ModEntities.daggerHook.get(), world, x, y, z);
    }

    public EntityDaggerHook(World world, LivingEntity shooter) {
        super(ModEntities.daggerHook.get(), world, shooter);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(HOOKED_ENTITY, 0);
    }

    @Override
    public boolean canHitShooter() {
        return this.retracting;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hookedEntity != null) {
            if (!this.hookedEntity.isAlive()) {
                this.hookedEntity = null;
            } else {
                this.setPosition(this.hookedEntity.getPosX(), this.hookedEntity.getPosYHeight(0.8D), this.hookedEntity.getPosZ());
            }
        }
        if (!this.world.isRemote) {
            if (this.retracting) {
                LivingEntity entity = this.getOwner();
                if (entity != null) {
                    Vector3d vector3d = new Vector3d(entity.getPosX() - this.getPosX(), entity.getPosY() - this.getPosY() + entity.getHeight() * 0.5f, entity.getPosZ() - this.getPosZ()).normalize().scale(0.8D);
                    this.setMotion(vector3d);
                }
            }
            if (this.getOwner() == null || this.getDistanceSq(this.getOwner()) > 1024)
                this.remove();
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityRayTraceResult result) {
        if (!this.retracting) {
            result.getEntity().attackEntityFrom(DamageSource.causeIndirectDamage(this, this.getOwner()), Config.Common.medeaDaggerDamage);
            this.hookedEntity = result.getEntity();
            this.dataManager.set(HOOKED_ENTITY, result.getEntity().getEntityId());
            this.setMotion(0, 0, 0);
            return true;
        } else if (result.getEntity() == this.getOwner()) {
            this.remove();
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult blockRayTraceResult) {
        this.retractHook();
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.remove("Shooter"); //Don't save owner
    }

    public void retractHook() {
        this.retracting = true;
        this.noClip = true;
        LivingEntity entity = this.getOwner();
        if (entity != null) {
            Vector3d vector3d = new Vector3d(entity.getPosX() - this.getPosX(), entity.getPosY() - this.getPosY(), entity.getPosZ() - this.getPosZ()).scale(0.1D);
            if (this.hookedEntity != null) {
                this.hookedEntity.setMotion(this.hookedEntity.getMotion().add(vector3d.x, Math.min(vector3d.y + 1, 1), vector3d.z));
            }
            this.hookedEntity = null;
        } else {
            this.remove();
        }
    }

    @Override
    protected EntityRayTraceResult getEntityHit(Vector3d from, Vector3d to) {
        if (this.retracting)
            return RayTraceUtils.projectileHit(this.world, this, this.getBoundingBox().expand(this.getMotion()).grow(1.0D), e -> e == this.getOwner(), 0.0D);
        return super.getEntityHit(from, to);
    }

    @Override
    public void remove() {
        super.remove();
        this.getOwner().getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.setThrownDagger(null));
    }
}
