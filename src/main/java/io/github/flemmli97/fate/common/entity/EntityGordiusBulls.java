package io.github.flemmli97.fate.common.entity;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.utils.CustomDamageSource;
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

public class EntityGordiusBulls extends CreatureEntity implements IServantMinion, IAnimated {

    private static final AnimatedAction charging = new AnimatedAction(20, 5, "charge");
    private static final AnimatedAction[] anims = {charging};

    private final AnimationHandler<EntityGordiusBulls> animationHandler = new AnimationHandler<>(this, anims);

    public EntityGordiusBulls(EntityType<? extends EntityGordiusBulls> type, World world) {
        super(type, world);
    }

    @Override
    public AnimationHandler<EntityGordiusBulls> getAnimationHandler() {
        return this.animationHandler;
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
        return this.getAnimationHandler().isCurrentAnim(charging.getID());
    }

    @Override
    public void livingTick() {
        super.livingTick();
        this.getAnimationHandler().tick();
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

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (damageSource != DamageSource.OUT_OF_WORLD && this.isCharging())
            damage *= 0.5f;
        return super.attackEntityFrom(damageSource, damage);
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if(passenger instanceof EntityGordiusWheel) {
            if (this.isPassenger(passenger)) {
                Vector3d offSet = this.getVectorForRotation(0, this.rotationYaw).scale(2.10);
                passenger.setPosition(this.getPosX() - offSet.x, this.getPosY() + passenger.getYOffset(), this.getPosZ() - offSet.z);
            }
        }
        else
            super.updatePassenger(passenger);
    }

    @Override
    public double getMountedYOffset() {
        return this.getHeight();
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