package io.github.flemmli97.fate.common.entity;

import io.github.flemmli97.fate.common.registry.ModEntities;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGordiusWheel extends CreatureEntity implements IServantMinion {

    private EntityGordiusBulls bulls;
    private boolean initBulls;

    public EntityGordiusWheel(EntityType<? extends EntityGordiusWheel> type, World world) {
        super(type, world);
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    public boolean isCharging() {
        return this.bulls() != null && this.bulls().isCharging();
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if(!this.world.isRemote) {
            if(!this.initBulls) {
                EntityGordiusBulls bulls = ModEntities.gordiusBulls.get().create(this.world);
                bulls.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
                this.world.addEntity(bulls);
                this.startRiding(bulls);
                this.initBulls = true;
                this.bulls = bulls;
            }
            if(this.bulls() == null || !this.bulls.isAlive())
                super.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
        }
        if(this.bulls() != null) {
            this.rotationYaw = this.bulls().rotationYaw;
            this.rotationPitch = this.bulls().rotationPitch * 0.5f;
            this.prevRotationYaw = this.rotationYaw;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
        }
    }

    /*@Override
    public void travel(Vector3d vec) {
        if (this.isBeingRidden() && this.canBeSteered() && this.getControllingPassenger() instanceof LivingEntity) {
            LivingEntity entitylivingbase = (LivingEntity) this.getControllingPassenger();
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5f;
            this.prevRotationYaw = this.rotationYaw;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            float strafing = entitylivingbase.moveStrafing * 0.5f;
            float forward = entitylivingbase.moveForward;
            if (forward <= 0.0f) {
                forward *= 0.25f;
            }
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1f;
            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                forward *= 0.85;
                strafing *= 0.85;
                super.travel(new Vector3d(strafing, vec.y, forward));
            } else if (entitylivingbase instanceof PlayerEntity) {
                this.setMotion(Vector3d.ZERO);
            }
            this.func_233629_a_(this, false);
        } else {
            super.travel(vec);
        }
    }*/

    public EntityGordiusBulls bulls() {
        if(this.bulls == null) {
            Entity ridden = this.getLowestRidingEntity();
            if(ridden instanceof EntityGordiusBulls)
                this.bulls = (EntityGordiusBulls) ridden;
        }
        return this.bulls;
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if(this.bulls() != null)
            this.bulls().attackEntityFrom(damageSource, damage);
        return false;
    }

    @Override
    public double getMountedYOffset() {
        return this.getHeight() * 0.75D;
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        tag.putBoolean("BullsInit", this.initBulls);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        this.initBulls = tag.getBoolean("BullsInit");
    }
}