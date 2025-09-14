package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
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
    protected static final EntityDataAccessor<Boolean> PREPARING = SynchedEntityData.defineId(MagicBeam.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> SPAWN_ROT_Y = SynchedEntityData.defineId(MagicBeam.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> SPAWN_ROT_X = SynchedEntityData.defineId(MagicBeam.class, EntityDataSerializers.FLOAT);

    private LivingEntity target;
    private float damageMultiplier = 1;
    private boolean setSpawnRot;
    private int preparationTick;

    public MagicBeam(EntityType<? extends MagicBeam> type, Level level) {
        super(type, level);
        this.entityData.set(SHOOT_TIME, this.random.nextInt(10) + 10);
    }

    public MagicBeam(Level level, LivingEntity shooter) {
        super(FateEntities.MAGIC_BEAM.get(), level, shooter);
        this.entityData.set(SHOOT_TIME, this.random.nextInt(10) + 10);
    }

    public MagicBeam(Level level, LivingEntity shootingEntity, @Nullable LivingEntity target) {
        this(level, shootingEntity);
        this.target = target;
    }

    public void setDamageMultiplier(float multiplier) {
        this.damageMultiplier = multiplier;
    }

    @Override
    public float radius() {
        return 0.4f;
    }

    @Override
    public float getRange() {
        return 20;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHOOT_TIME, 20);
        builder.define(PREPARING, true);
        builder.define(SPAWN_ROT_Y, 0f);
        builder.define(SPAWN_ROT_X, 0f);
    }

    public boolean preparing() {
        return this.entityData.get(PREPARING);
    }

    public float getSpawnRotY() {
        return this.entityData.get(SPAWN_ROT_Y);
    }

    public float getSpawnRotX() {
        return this.entityData.get(SPAWN_ROT_X);
    }

    @Override
    public void setRotationToDir(double x, double y, double z, float inaccuracy) {
        super.setRotationToDir(x, y, z, inaccuracy);
        if (!this.setSpawnRot) {
            this.setSpawnRot = true;
            this.entityData.set(SPAWN_ROT_Y, this.getYRot());
            this.entityData.set(SPAWN_ROT_X, this.getXRot());
        }
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) {
            AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                    .addData(new ColorData(205 / 255F, 13 / 255F, 205 / 255F, 1))
                    .addData(new ScaleData(0.15f))
                    .addData(new MotionData(this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01))
                    .addData(new ParticleMetaData(20, false, 0))
                    .build().add(this.level(), this.getRandomX(2), this.getRandomY(), this.getRandomZ(2));
        } else if (!this.setSpawnRot) {
            this.setSpawnRot = true;
            this.entityData.set(SPAWN_ROT_Y, this.getYRot());
            this.entityData.set(SPAWN_ROT_X, this.getXRot());
        }
        if (this.preparing()) {
            this.preparationTick++;
            this.updatePreparation();
        } else {
            if (!this.level().isClientSide) {
                Entity thrower = this.getOwner();
                if (thrower == null || !thrower.isAlive()) {
                    this.remove(RemovalReason.KILLED);
                    return;
                }
            }
            super.tick();
        }
    }

    private void updatePreparation() {
        this.preparationTick++;
        if (this.preparing() && this.preparationTick >= this.entityData.get(SHOOT_TIME)) {
            this.entityData.set(PREPARING, false);
            if (this.target != null) {
                this.setRotationTo(this.target.getX(), this.target.getY() + this.target.getBbHeight() * 0.5, this.target.getZ(), 0.05f);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
        compound.putBoolean("Preparing", this.preparing());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageMultiplier = compound.getFloat("DamageMultieier");
        this.entityData.set(PREPARING, compound.getBoolean("Preparing"));
    }

    @Override
    public void onImpact(EntityHitResult result) {
        Utils.runWithInvulTimer(null, result.getEntity(),
                target -> target.hurt(FateDamageTypes.indirect(FateDamageTypes.MAGIC_BEAM, this, this.getOwner()), (Utils.magicDamage(this.getOwner()) + CommonConfig.magicBeam) * this.damageMultiplier), 2);
    }

    @Override
    public boolean shouldRender3d(Entity entity, int state) {
        return true;
    }
}