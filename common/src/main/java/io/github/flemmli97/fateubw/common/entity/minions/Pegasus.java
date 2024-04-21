package io.github.flemmli97.fateubw.common.entity.minions;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.ChargingHandler;
import io.github.flemmli97.fateubw.common.entity.IServantMinion;
import io.github.flemmli97.fateubw.common.entity.ai.PegasusAttackGoal;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
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
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;


public class Pegasus extends PathfinderMob implements IAnimated, IServantMinion {

    public static float PORTAL_SIZE = 2;
    public static float PORTAL_OFFSET = 1.3f;

    private static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.FLOAT);

    public static final AnimatedAction CHARGING = new AnimatedAction(20, 0, "charge");
    public static final AnimatedAction SUMMON = new AnimatedAction(1.84, 0, "summon");
    public static final AnimatedAction CHARGING_FLYING = AnimatedAction.builder(20, "flying_charge").withClientID(CHARGING.getID()).build();
    private static final AnimatedAction[] ANIMS = {CHARGING, SUMMON, CHARGING_FLYING};
    private static final Predicate<AnimatedAction> CHARGING_ANIM = anim -> anim != null && (anim.getID().equals(CHARGING.getID()) || anim.getID().equals(CHARGING_FLYING.getID()));

    public final PegasusAttackGoal attackAI = new PegasusAttackGoal(this);

    private final AnimationHandler<Pegasus> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final PathNavigation flyingNavigator;
    private boolean canFly;

    public final ChargingHandler<Pegasus> chargingHandler = new ChargingHandler<>(this, LOCKED_YAW, CHARGING_ANIM);

    public Pegasus(EntityType<? extends Pegasus> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
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
        return this.getAnimationHandler().isCurrent(CHARGING, CHARGING_FLYING);
    }

    public AnimatedAction getChargingAnim() {
        return this.canFly ? CHARGING_FLYING : CHARGING;
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
