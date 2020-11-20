package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.utils.CustomDamageSource;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityGordiusWheel extends CreatureEntity implements IServantMinion, IAnimated {

    private AnimatedAction currentAnim;

    private static final AnimatedAction charging = new AnimatedAction(20, 0, "charge");
    private static final AnimatedAction[] anims = new AnimatedAction[]{charging};

    public EntityGordiusWheel(EntityType<? extends EntityGordiusWheel> type, World world) {
        super(type, world);
    }

    @Override
    public AnimatedAction getAnimation() {
        return this.currentAnim;
    }

    @Override
    public void setAnimation(AnimatedAction anim) {
        this.currentAnim = anim == null ? null : anim.create();
        IAnimated.sentToClient(this);
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    public boolean isCharging() {
        return this.currentAnim != null && this.currentAnim.getID().equals(charging.getID());
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && this.isCharging() && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof LivingEntity) {
            List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(0.5), EntityPredicates.NOT_SPECTATING.and(e -> !this.isPassenger(e)));
            for (LivingEntity e : list) {
                if (e != this) {
                    e.attackEntityFrom(CustomDamageSource.gordiusTrample(this, (LivingEntity) this.getPassengers().get(0)), Config.Common.gordiusDmg);
                }
            }
            this.playSound(SoundEvents.ENTITY_COW_STEP, 0.4F, 0.4F);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (damageSource != DamageSource.OUT_OF_WORLD && this.isCharging())
            damage *= 0.5f;
        return super.attackEntityFrom(damageSource, damage);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isBeingRidden() && this.getPassengers().get(0) instanceof LivingEntity) {
            LivingEntity rider = (LivingEntity) this.getPassengers().get(0);
            this.rotationYaw = rider.rotationYaw;
            //if(this.rotationYaw!=this.prevRotationYaw)
            //    this.updateAABB();
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = rider.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            Vector3d offSet = this.getVectorForRotation(0, this.rotationYaw).scale(1.2);
            passenger.setPosition(this.getX() - offSet.x, this.getY() + this.getMountedYOffset() + passenger.getYOffset(), this.getZ() - offSet.z);
        }
    }

    @Override
    public double getMountedYOffset() {
        return (double) this.getHeight() * 0.6D;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COW_DEATH;
    }

    @Override
    protected float getSoundPitch() {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
    }
}