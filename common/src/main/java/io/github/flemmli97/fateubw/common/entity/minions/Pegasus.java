package io.github.flemmli97.fateubw.common.entity.minions;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.ChargingHandler;
import io.github.flemmli97.fateubw.common.entity.IServantMinion;
import io.github.flemmli97.fateubw.common.entity.ai.PegasusAttackGoal;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;


public class Pegasus extends PathfinderMob implements IAnimated, IServantMinion {

    public final PegasusAttackGoal attackAI = new PegasusAttackGoal(this);

    private static final AnimatedAction charging = new AnimatedAction(20, 0, "charge");
    private static final AnimatedAction chargingFlying = AnimatedAction.builder(20, "flycing_charge").withClientID(charging.getID()).build();

    private static final AnimatedAction[] anims = {charging, chargingFlying};

    private final AnimationHandler<Pegasus> animationHandler = new AnimationHandler<>(this, anims);

    private final PathNavigation flyingNavigator;
    private boolean canFly;

    private static final Function<AnimatedAction, Boolean> chargingAnim = anim -> anim != null && (anim.getID().equals(charging.getID()) || anim.getID().equals(chargingFlying.getID()));
    private static final EntityDataAccessor<Float> lockedYaw = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.FLOAT);

    public final ChargingHandler<Pegasus> chargingHandler = new ChargingHandler<>(this, lockedYaw, chargingAnim);

    public Pegasus(EntityType<? extends Pegasus> type, Level world) {
        super(type, world);
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
        this.flyingNavigator = new FlyingPathNavigation(this, world);
        this.moveControl = new MoveHelperController(this);
    }

    @Override
    public AnimationHandler<Pegasus> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    public boolean isCharging() {
        return this.getAnimationHandler().isCurrent(charging, chargingFlying);
    }

    public AnimatedAction getChargingAnim() {
        return this.canFly ? chargingFlying : charging;
    }

    @Override
    public void tick() {
        super.tick();
        this.getAnimationHandler().tick();
        this.chargingHandler.tick();
        if (!this.level.isClientSide && this.isCharging() && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof LivingEntity) {
            List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5), EntitySelector.NO_SPECTATORS.and(e -> !this.hasPassenger(e)));
            for (LivingEntity e : list) {
                if (e != this) {
                    e.hurt(CustomDamageSource.pegasusCharge(this, (LivingEntity) this.getPassengers().get(0)), Config.Common.pegasusDamage);
                }
            }
        }
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity) {
            LivingEntity entitylivingbase = (LivingEntity) this.getControllingPassenger();
            this.setYRot(entitylivingbase.getYRot());
            this.setXRot(entitylivingbase.getXRot() * 0.5f);
            this.yRotO = this.getYRot();
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;
            float strafing = entitylivingbase.xxa * 0.5f;
            float forward = entitylivingbase.zza;
            if (forward <= 0.0f) {
                forward *= 0.25f;
            }
            this.flyingSpeed = this.getSpeed() * 0.1f;
            if (this.isControlledByLocalInstance()) {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                forward *= 0.85;
                strafing *= 0.85;
                super.travel(new Vec3(strafing, vec.y, forward));
            } else if (entitylivingbase instanceof Player) {
                this.setDeltaMovement(Vec3.ZERO);
            }
            this.calculateEntityAnimation(this, false);
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
    public PathNavigation getNavigation() {
        if (this.canFly)
            return this.flyingNavigator;
        return super.getNavigation();
    }

    public void setCanFly(boolean flag) {
        this.canFly = flag;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource != DamageSource.OUT_OF_WORLD && this.isCharging())
            damage *= 0.5f;
        return super.hurt(damageSource, damage);
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getBbHeight() * 0.6D;
    }


    class MoveHelperController extends MoveControl {

        public MoveHelperController(Pegasus vex) {
            super(vex);
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO && Pegasus.this.isCharging()) {
                Vec3 vector3d = new Vec3(this.wantedX - Pegasus.this.getX(), this.wantedY - Pegasus.this.getY(), this.wantedZ - Pegasus.this.getZ());
                double d0 = vector3d.length();
                if (d0 < Pegasus.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    Pegasus.this.setDeltaMovement(Pegasus.this.getDeltaMovement().scale(0.5D));
                } else {
                    Pegasus.this.setDeltaMovement(Pegasus.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (Pegasus.this.getTarget() == null) {
                        Vec3 vector3d1 = Pegasus.this.getDeltaMovement();
                        Pegasus.this.setYRot(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI));
                    } else {
                        double d2 = Pegasus.this.getTarget().getX() - Pegasus.this.getX();
                        double d1 = Pegasus.this.getTarget().getZ() - Pegasus.this.getZ();
                        Pegasus.this.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
                    }
                    Pegasus.this.yBodyRot = Pegasus.this.getYRot();
                }
            } else super.tick();
        }
    }
}
