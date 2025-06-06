package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class MagicBeam extends BaseBeam {

    protected static final EntityDataAccessor<Integer> SHOOT_TIME = SynchedEntityData.defineId(MagicBeam.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> PRE_SHOOT_TICK = SynchedEntityData.defineId(MagicBeam.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> SPAWN_ROT_Y = SynchedEntityData.defineId(MagicBeam.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> SPAWN_ROT_X = SynchedEntityData.defineId(MagicBeam.class, EntityDataSerializers.FLOAT);

    private LivingEntity target;
    private float damageMultiplier = 1;
    public boolean idle = true;
    private boolean setSpawnRot;

    public MagicBeam(EntityType<? extends MagicBeam> type, Level world) {
        super(type, world);
    }

    public MagicBeam(Level world, LivingEntity shooter) {
        super(ModEntities.MAGIC_BEAM.get(), world, shooter);
    }

    public MagicBeam(Level world, LivingEntity shootingEntity, @Nullable LivingEntity target) {
        this(world, shootingEntity);
        this.target = target;
    }

    public void setDamageMultiplier(float multiplier) {
        this.damageMultiplier = multiplier;
    }

    @Override
    public float radius() {
        return 0.3f;
    }

    @Override
    public float getRange() {
        return 16;
    }

    @Override
    public boolean piercing() {
        return super.piercing();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOT_TIME, this.random.nextInt(15) + 10);
        this.entityData.define(PRE_SHOOT_TICK, 0);
        this.entityData.define(SPAWN_ROT_Y, 0f);
        this.entityData.define(SPAWN_ROT_X, 0f);
    }

    public float getSpawnRotY() {
        return this.entityData.get(SPAWN_ROT_Y);
    }

    public float getSpawnRotX() {
        return this.entityData.get(SPAWN_ROT_X);
    }

    @Override
    public void setYRot(float yRot) {
        super.setYRot(yRot);
        if (!this.setSpawnRot) {
            this.entityData.set(SPAWN_ROT_Y, this.getYRot());
        }
    }

    @Override
    public void setXRot(float xRot) {
        super.setXRot(xRot);
        if (!this.setSpawnRot) {
            this.entityData.set(SPAWN_ROT_X, this.getXRot());
        }
    }

    @Override
    public void tick() {
        if (this.level.isClientSide) {
            this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 205 / 255F, 13 / 255F, 205 / 255F, 1, 0.15f), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01);
        } else if (!this.setSpawnRot) {
            this.setSpawnRot = true;
            this.entityData.set(SPAWN_ROT_Y, this.getYRot());
            this.entityData.set(SPAWN_ROT_X, this.getXRot());
        }
        Entity thrower = this.getOwner();
        if (this.getPreShootTick() <= this.entityData.get(SHOOT_TIME)) {
            //this.livingTicks++;
            this.updatePreShootTick();
            if (this.getPreShootTick() == 15 && this.target != null) {
                this.setRotationTo(this.target.getX(), this.target.getY() + this.target.getBbHeight() * 0.5, this.target.getZ(), 0.05f);
            }
        }
        if (this.getPreShootTick() > this.entityData.get(SHOOT_TIME)) {
            this.idle = false;
            if (!this.level.isClientSide) {
                if (thrower == null || !thrower.isAlive()) {
                    this.remove(RemovalReason.KILLED);
                    return;
                }
            }
            super.tick();
        }
    }

    @Override
    public void onImpact(EntityHitResult result) {
        result.getEntity().hurt(CustomDamageSource.magicBeam(this, this.getOwner()), (Utils.magicDamage(this.getOwner()) + CommonConfig.magicBeam) * this.damageMultiplier);
    }

    private int getPreShootTick() {
        return this.entityData.get(PRE_SHOOT_TICK);
    }

    private void updatePreShootTick() {
        this.entityData.set(PRE_SHOOT_TICK, this.getPreShootTick() + 1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("PreShoot", this.getPreShootTick());
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(PRE_SHOOT_TICK, compound.getInt("PreShoot"));
        this.damageMultiplier = compound.getFloat("DamageMultieier");
    }

    @Override
    public boolean firstPerson3d(Entity entity) {
        return true;
    }
}