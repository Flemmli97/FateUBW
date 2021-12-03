package io.github.flemmli97.fate.common.entity.minions;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.ChargingHandler;
import io.github.flemmli97.fate.common.entity.IServantMinion;
import io.github.flemmli97.fate.common.entity.ai.PegasusAttackGoal;
import io.github.flemmli97.fate.common.utils.CustomDamageSource;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class EntityPegasus extends CreatureEntity implements IAnimated, IServantMinion {

    public final PegasusAttackGoal attackAI = new PegasusAttackGoal(this);

    private static final AnimatedAction charging = new AnimatedAction(20, 0, "charge");
    private static final AnimatedAction chargingFlying = new AnimatedAction(20, 0, "flying_charge", charging.getID());

    private static final AnimatedAction[] anims = {charging, chargingFlying};

    private final AnimationHandler<EntityPegasus> animationHandler = new AnimationHandler<>(this, anims);

    private final PathNavigator flyingNavigator;
    private boolean canFly;

    private static final Function<AnimatedAction, Boolean> chargingAnim = anim -> anim != null && (anim.getID().equals(charging.getID()) || anim.getID().equals(chargingFlying.getID()));
    private static final DataParameter<Float> lockedYaw = EntityDataManager.createKey(EntityPegasus.class, DataSerializers.FLOAT);

    public final ChargingHandler<EntityPegasus> chargingHandler = new ChargingHandler<>(this, lockedYaw, chargingAnim);

    public EntityPegasus(EntityType<? extends EntityPegasus> type, World world) {
        super(type, world);
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
        this.flyingNavigator = new FlyingPathNavigator(this, world);
        this.moveController = new MoveHelperController(this);
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
        return this.getAnimationHandler().isCurrentAnim(charging.getID(), chargingFlying.getID());
    }

    public AnimatedAction getChargingAnim() {
        return this.canFly ? chargingFlying : charging;
    }

    @Override
    public void livingTick() {
        super.livingTick();
        this.getAnimationHandler().tick();
        this.chargingHandler.tick();
        if (!this.world.isRemote && this.isCharging() && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof LivingEntity) {
            List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(0.5), EntityPredicates.NOT_SPECTATING.and(e -> !this.isPassenger(e)));
            for (LivingEntity e : list) {
                if (e != this) {
                    e.attackEntityFrom(CustomDamageSource.pegasusCharge(this, (LivingEntity) this.getPassengers().get(0)), Config.Common.pegasusDamage);
                }
            }
        }
    }

    @Override
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
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public PathNavigator getNavigator() {
        if (this.canFly)
            return this.flyingNavigator;
        return super.getNavigator();
    }

    public void setCanFly(boolean flag) {
        this.canFly = flag;
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


    class MoveHelperController extends MovementController {

        public MoveHelperController(EntityPegasus vex) {
            super(vex);
        }

        @Override
        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO && EntityPegasus.this.isCharging()) {
                Vector3d vector3d = new Vector3d(this.posX - EntityPegasus.this.getPosX(), this.posY - EntityPegasus.this.getPosY(), this.posZ - EntityPegasus.this.getPosZ());
                double d0 = vector3d.length();
                if (d0 < EntityPegasus.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = MovementController.Action.WAIT;
                    EntityPegasus.this.setMotion(EntityPegasus.this.getMotion().scale(0.5D));
                } else {
                    EntityPegasus.this.setMotion(EntityPegasus.this.getMotion().add(vector3d.scale(this.speed * 0.05D / d0)));
                    if (EntityPegasus.this.getAttackTarget() == null) {
                        Vector3d vector3d1 = EntityPegasus.this.getMotion();
                        EntityPegasus.this.rotationYaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                        EntityPegasus.this.renderYawOffset = EntityPegasus.this.rotationYaw;
                    } else {
                        double d2 = EntityPegasus.this.getAttackTarget().getPosX() - EntityPegasus.this.getPosX();
                        double d1 = EntityPegasus.this.getAttackTarget().getPosZ() - EntityPegasus.this.getPosZ();
                        EntityPegasus.this.rotationYaw = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                        EntityPegasus.this.renderYawOffset = EntityPegasus.this.rotationYaw;
                    }
                }
            } else super.tick();
        }
    }
}
