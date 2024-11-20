package io.github.flemmli97.fateubw.common.entity.minions;

import io.github.flemmli97.fateubw.common.entity.ChargingHandler;
import io.github.flemmli97.fateubw.common.entity.IServantMinion;
import io.github.flemmli97.fateubw.common.entity.StandingVehicle;
import io.github.flemmli97.fateubw.common.entity.ai.PegasusAttackGoal;
import io.github.flemmli97.fateubw.common.entity.ai.PegasusFlyingAttackGoal;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;


public class Pegasus extends PathfinderMob implements IAnimated, IServantMinion, StandingVehicle {

    public static float PORTAL_SIZE = 2;
    public static float PORTAL_OFFSET = 1.3f;

    private static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.BYTE);

    public static final AnimatedAction CHARGING = new AnimatedAction(1.6, 0.36, "charge");
    public static final AnimatedAction STOMP = new AnimatedAction(0.56, 0.4, "stomp");
    public static final AnimatedAction SUMMON = new AnimatedAction(2.04, 0, "summon");
    private static final AnimatedAction[] ANIMS = {CHARGING, SUMMON, STOMP};
    private static final Predicate<AnimatedAction> CHARGING_ANIM = anim -> anim != null && (anim.getID().equals(CHARGING.getID()));

    public final PegasusAttackGoal attackAI = new PegasusAttackGoal(this);
    public final PegasusFlyingAttackGoal flyingAttackAI = new PegasusFlyingAttackGoal(this);

    private final AnimationHandler<Pegasus> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final PathNavigation flyingNavigator;
    private int flyTimer;

    public final ChargingHandler<Pegasus> chargingHandler = new ChargingHandler<>(this, LOCKED_YAW, CHARGING_ANIM);

    private int moveTick;

    public static final int MOVE_TICK_MAX = 3;

    private int standInterpolation;

    public Pegasus(EntityType<? extends Pegasus> type, Level level) {
        super(type, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
        this.flyingNavigator = new FlyingPathNavigation(this, level);
        this.moveControl = new PegasusMoveController(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
        this.entityData.define(FLYING, false);
        this.entityData.define(MOVE_FLAGS, (byte) 0);
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
        return this.getAnimationHandler().isCurrent(CHARGING);
    }

    @Override
    public void tick() {
        super.tick();
        this.getAnimationHandler().tick();
        this.chargingHandler.tick();
        if (this.level.isClientSide && this.getAnimationHandler().isCurrent(SUMMON)) {
            Vec3 base = Vec3.directionFromRotation(0, this.yBodyRot).scale(-PORTAL_OFFSET);
            Vec3 base2 = MathUtils.rotate(new Vec3(0, 1, 0), base, (float) Math.toRadians(90)).normalize();
            for (int i = 0; i < 4; i++) {
                double sideScale = (this.random.nextDouble() - this.random.nextDouble()) * PORTAL_SIZE;
                double upScale = (this.random.nextDouble() - this.random.nextDouble()) * PORTAL_SIZE + PORTAL_SIZE;
                Vec3 pos = this.position().add(base).add(base2.scale(sideScale)).add(new Vec3(0, 1, 0).scale(upScale));
                this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 245 / 255F, 10 / 255F, 10 / 255F, 1, 0.5f), pos.x(), pos.y(), pos.z(), this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01);
            }
        }
        this.standInterpolation++;
        if (this.shouldStand())
            this.standInterpolation = -1;
        if (this.getMovement() != MoveType.NONE) {
            this.moveTick = Math.min(MOVE_TICK_MAX, ++this.moveTick);
        } else {
            this.moveTick = Math.max(0, --this.moveTick);
        }
        if (!this.level.isClientSide) {
            double speed = this.getDeltaMovement().lengthSqr();
            if (speed > 0.01) {
                this.setMovingFlag(this.moveControl.getSpeedModifier() > 1 ? MoveType.RUN : MoveType.WALK);
            } else {
                this.setMovingFlag(MoveType.NONE);
            }
            if (this.tickCount % 10 == 0 && this.getControllingPassenger() instanceof Mob mob) {
                this.setTarget(mob.getTarget());
            }
            if (this.getTarget() == null || !this.getTarget().isAlive()) {
                if (this.canFly())
                    this.setCanFly(false);
            } else {
                if (--this.flyTimer <= 0) {
                    if (this.canFly()) {
                        this.flyTimer = 800 + this.getRandom().nextInt(400);
                        this.setCanFly(false);
                    } else {
                        this.flyTimer = 400 + this.getRandom().nextInt(200);
                        this.setCanFly(true);
                    }
                }
            }
        }
    }

    public void setMovingFlag(MoveType type) {
        this.entityData.set(MOVE_FLAGS, (byte) type.ordinal());
    }

    public MoveType getMovement() {
        return MoveType.values()[this.entityData.get(MOVE_FLAGS)];
    }

    public float interpolatedMoveTick(float partialTicks) {
        return Mth.clamp((this.moveTick + (this.getMovement() != MoveType.NONE ? partialTicks : -partialTicks)) / (float) MOVE_TICK_MAX, 0, 1);
    }

    public float interpolatedStandingick(float partialTicks) {
        return Mth.clamp((this.standInterpolation + partialTicks) / (float) 3, 0, 1);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() | this.getAnimationHandler().isCurrent(SUMMON);
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity entitylivingbase) {
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

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        if (reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.MOB_SUMMONED) {
            this.getAnimationHandler().setAnimation(SUMMON);
        }
        return data;
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public PathNavigation getNavigation() {
        if (this.canFly())
            return this.flyingNavigator;
        return super.getNavigation();
    }

    public void setCanFly(boolean flag) {
        this.entityData.set(FLYING, flag);
        if (flag) {
            this.goalSelector.removeGoal(this.attackAI);
            this.goalSelector.addGoal(0, this.flyingAttackAI);
        } else {
            this.goalSelector.removeGoal(this.flyingAttackAI);
            this.goalSelector.addGoal(0, this.attackAI);
        }
    }

    public boolean canFly() {
        return this.entityData.get(FLYING);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.getAnimationHandler().isCurrent(SUMMON))
            return false;
        if (damageSource != DamageSource.OUT_OF_WORLD && this.isCharging())
            damage *= 0.5f;
        return super.hurt(damageSource, damage);
    }

    public AABB attackAABB(AnimatedAction anim) {
        double range = 2;
        if (anim.is(STOMP)) {
            range = this.getBbWidth() * 0.5 + 2;
            return new AABB(-range, -0.02, -range, range, this.getBbHeight() + 0.02, range).move(this.position());
        }
        return new AABB(-range, -0.02, -range, range, this.getBbHeight() + 0.02, range).move(this.position())
                .expandTowards(this.getDeltaMovement());
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.85D;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.canFly());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCanFly(compound.getBoolean("Flying"));
    }

    @Override
    public boolean shouldStand() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return anim != null && anim.is(SUMMON) && !anim.isPastTick(1.);
    }

    class PegasusMoveController extends MoveControl {

        public PegasusMoveController(Pegasus pegasus) {
            super(pegasus);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO && Pegasus.this.canFly() && Pegasus.this.getTarget() != null) {

            } else
                super.tick();
        }
    }

    public enum MoveType {
        NONE,
        WALK,
        RUN
    }
}
