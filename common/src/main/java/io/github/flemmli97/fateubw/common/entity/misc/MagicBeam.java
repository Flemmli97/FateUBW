package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
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

public class MagicBeam extends EntityBeam {

    protected static final EntityDataAccessor<Integer> shootTime = SynchedEntityData.defineId(MagicBeam.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> preShootTick = SynchedEntityData.defineId(MagicBeam.class, EntityDataSerializers.INT);

    private LivingEntity target;
    private int strengthMod;
    public boolean iddle = true;

    public MagicBeam(EntityType<? extends MagicBeam> type, Level world) {
        super(type, world);
    }

    public MagicBeam(Level world, LivingEntity shooter) {
        super(ModEntities.magicBeam.get(), world, shooter);
    }

    public MagicBeam(Level world, LivingEntity shootingEntity, LivingEntity target, int strength) {
        this(world, shootingEntity);
        this.target = target;
        this.strengthMod = strength;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(shootTime, this.random.nextInt(20) + 25);
        this.entityData.define(preShootTick, 0);
    }

    @Override
    public void tick() {
        if (this.level.isClientSide) {
            this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 205 / 255F, 13 / 255F, 205 / 255F, 1, 0.15f), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01);
        }
        Entity thrower = this.getOwner();
        if (this.getPreShootTick() <= this.entityData.get(shootTime)) {
            //this.livingTicks++;
            this.updatePreShootTick();
            if (this.getPreShootTick() == 15 && this.target != null) {
                this.setRotationTo(this.target.getX(), this.target.getY() + this.target.getBbHeight() * 0.5, this.target.getZ(), 0.05f);
            }
        }
        if (this.getPreShootTick() > this.entityData.get(shootTime)) {
            this.iddle = false;
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
        result.getEntity().hurt(CustomDamageSource.magicBeam(this, this.getOwner()), Config.Common.magicBeam);
    }

    private int getPreShootTick() {
        return this.entityData.get(preShootTick);
    }

    private void updatePreShootTick() {
        this.entityData.set(preShootTick, this.getPreShootTick() + 1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("PreShoot", this.getPreShootTick());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(preShootTick, compound.getInt("PreShoot"));
    }
}