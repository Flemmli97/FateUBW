package io.github.flemmli97.fate.common.entity;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.utils.CustomDamageSource;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.World;

import java.util.List;

public class EntityPegasus extends CreatureEntity implements IAnimated, IServantMinion {

    private static final AnimatedAction charging = new AnimatedAction(20, 0, "charge");
    private static final AnimatedAction[] anims = {charging};

    private final AnimationHandler<EntityPegasus> animationHandler = new AnimationHandler<>(this, anims);

    public EntityPegasus(EntityType<? extends EntityPegasus> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public AnimationHandler<EntityPegasus> getAnimationHandler() {
        return this.animationHandler;
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
                    e.attackEntityFrom(CustomDamageSource.pegasusCharge(this, (LivingEntity) this.getPassengers().get(0)), Config.Common.pegasusDamage);
                }
            }
        }
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
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (damageSource != DamageSource.OUT_OF_WORLD && this.isCharging())
            damage *= 0.5f;
        return super.attackEntityFrom(damageSource, damage);
    }

    @Override
    public double getMountedYOffset() {
        return (double) this.getHeight() * 0.6D;
    }
}
