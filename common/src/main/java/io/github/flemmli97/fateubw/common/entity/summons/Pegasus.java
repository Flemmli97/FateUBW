package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.ChargingHandler;
import io.github.flemmli97.fateubw.common.entity.StandingVehicle;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.MotionTrailProvider;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.AoeAttackEntity;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.ActionRun;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.ActionStart;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class Pegasus extends PathfinderMob implements IAnimated, StandingVehicle, AoeAttackEntity {

    public static float PORTAL_SIZE = 2;
    public static float PORTAL_OFFSET = 1.3f;

    private static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.BYTE);

    public static final AnimatedAction CHARGING = AnimatedAction.builder(1.4, "charge").marker("attack", 0.36).build();
    public static final AnimatedAction STOMP = AnimatedAction.builder(0.56, "stomp").marker("attack", 0.4).build();
    public static final AnimatedAction SUMMON = AnimatedAction.builder(2.04, "summon").build();
    private static final AnimatedAction[] ANIMS = {CHARGING, SUMMON, STOMP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<Pegasus>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<Pegasus>(Pegasus.STOMP)
                    .cooldown(e -> e.getRandom().nextInt(20) + 25)
                    .withCondition(((goal, target, previous) -> !goal.attacker.canFly()))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 6),
            WeightedEntry.wrap(new GoalAttackAction<Pegasus>(Pegasus.CHARGING)
                    .cooldown(e -> e.getRandom().nextInt(45) + 30)
                    .withCondition(((goal, target, previous) -> !goal.attacker.canFly() && (goal.distanceToTargetSq > 25 || goal.attacker.getRandom().nextFloat() < 0.5)))
                    .prepare(ChargeTo::new), 5),
            WeightedEntry.wrap(new GoalAttackAction<Pegasus>(Pegasus.CHARGING)
                    .cooldown(e -> e.getRandom().nextInt(100) + 100)
                    .withCondition(((goal, target, previous) -> goal.attacker.canFly()))
                    .prepare(ChargeTo::new), 5)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<Pegasus>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<Pegasus>(1, 0.5))
                    .withCondition(((goal, target) -> !goal.attacker.canFly())), 3),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<Pegasus>(12, 5))
                    .withCondition(((goal, target) -> !goal.attacker.canFly())), 5),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<Pegasus>(1, 1, 5))
                    .withCondition(((goal, target) -> !goal.attacker.canFly())), 4),
            WeightedEntry.wrap(new IdleAction<>(() -> new PegasusFlyRunner(1))
                    .withCondition(((goal, target) -> goal.attacker.canFly())), 5)
    );

    public final Predicate<LivingEntity> targetPred = target -> {
        if (target == this)
            return false;
        if (this.getTarget() == target)
            return true;
        if (this.getFirstPassenger() instanceof Mob mob && target == mob.getTarget())
            return true;
        if (this.getFirstPassenger() instanceof BaseServant servant) {
            return servant.targetPred.test(target);
        }
        return this.canAttack(target) && !this.hasPassenger(target);
    };

    public final AnimatedAttackGoal<Pegasus> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<Pegasus> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level.isClientSide && CHARGING.is(anim)) {
                    this.hitEntities = new ArrayList<>();
                }
                return false;
            });

    private final PathNavigation main;
    private final PathNavigation flyingNavigator;
    private int flyTimer;

    public final ChargingHandler<Pegasus> chargingHandler = new ChargingHandler<>(this, LOCKED_YAW, a -> this.isCharging());
    private List<Entity> hitEntities = new ArrayList<>();
    private Vec3 chargeMotion;

    private int moveTick;

    public static final int MOVE_TICK_MAX = 3;

    private int standInterpolation;

    public Pegasus(EntityType<? extends Pegasus> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.goalSelector.addGoal(0, this.attack);
            this.updateAttributes();
        }
        this.main = this.navigation;
        this.flyingNavigator = this.createFlyNavigator(level);
        this.moveControl = new PegasusMoveController(this);
    }

    protected PathNavigation createFlyNavigator(Level level) {
        FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return true;
            }
        };
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanFloat(false);
        flyingpathnavigator.setCanPassDoors(false);
        return flyingpathnavigator;
    }

    private void updateAttributes() {
        ResourceLocation id = Registry.ENTITY_TYPE.getKey(this.getType());
        AttributeHolderProperties props = DatapackHandler.SERVANT_PROPS.getGeneric(id);
        props.attributes().forEach((att, val) -> {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                inst.setBaseValue(val);
                if (att == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth());
            }
        });
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
        this.entityData.define(FLYING, false);
        this.entityData.define(MOVE_FLAGS, (byte) 0);
    }

    @Override
    public float getYRot() {
        return this.isCharging() ? this.entityData.get(LOCKED_YAW) : super.getYRot();
    }

    @Override
    public AnimationHandler<Pegasus> getAnimationHandler() {
        return this.animationHandler;
    }

    public boolean isCharging() {
        if (this.getAnimationHandler() == null)
            return false;
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return anim != null && CHARGING.is(anim) && anim.isPast("attack");
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.goalSelector.tick();
        this.getAnimationHandler().tick();
        this.chargingHandler.tick();
        if (this.level.isClientSide) {
            if (this.getAnimationHandler().isCurrent(SUMMON)) {
                Vec3 base = Vec3.directionFromRotation(0, this.yBodyRot).scale(-PORTAL_OFFSET);
                Vec3 base2 = MathUtils.rotate(new Vec3(0, 1, 0), base, (float) Math.toRadians(90)).normalize();
                for (int i = 0; i < 4; i++) {
                    double sideScale = (this.random.nextDouble() - this.random.nextDouble()) * PORTAL_SIZE;
                    double upScale = (this.random.nextDouble() - this.random.nextDouble()) * PORTAL_SIZE + PORTAL_SIZE;
                    Vec3 pos = this.position().add(base).add(base2.scale(sideScale)).add(new Vec3(0, 1, 0).scale(upScale));
                    this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 245 / 255F, 10 / 255F, 10 / 255F, 1, 0.5f), pos.x(), pos.y(), pos.z(), this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01);
                }
            }
            if (this.getAnimationHandler().isCurrent(CHARGING) && this.getAnimationHandler().getAnimation().isPast(0.48)) {
                Vec3 base = MathUtils.rotate(new Vec3(0, 1, 0), Vec3.directionFromRotation(0, this.yBodyRot), (float) Math.toRadians(90)).normalize();
                Vec3 dir = this.getDeltaMovement().scale(-0.2);
                for (int i = 0; i < 8; i++) {
                    double sideScale = ((this.random.nextDouble() * 2) - 1) * 3;
                    double upScale = (this.random.nextDouble() * 2) - 1;
                    Vec3 pos = this.position().add(base.scale(sideScale)).add(new Vec3(0, 1, 0).scale(upScale));
                    float r = (235 + this.getRandom().nextInt(10)) / 255F;
                    float g = (235 + this.getRandom().nextInt(10)) / 255F;
                    float b = 245 / 255F;
                    float scale = (float) (0.05 + this.getRandom().nextDouble() * 0.1);
                    this.level.addParticle(new TrailParticleData(ModParticles.TRAIL.get(),
                                    TrailInfo.builder(new MotionTrailProvider.MotionTrailData(dir, 6, 10))
                                            .setColor(r, g, b, 0.6f)
                                            .setColor2(r, g, b, 0.6f)
                                            .setWidth(scale)
                                            .setWidth2(scale)
                                            .build()),
                            pos.x(), pos.y(), pos.z(), 0, 0, 0);
                }
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
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
            if (this.getTarget() == null || !this.getTarget().isAlive()) {
                if (this.canFly())
                    this.setCanFly(false);
            } else {
                if (--this.flyTimer <= 0) {
                    if (this.canFly()) {
                        this.flyTimer = 400 + this.getRandom().nextInt(300);
                        this.setCanFly(false);
                    } else {
                        this.flyTimer = 250 + this.getRandom().nextInt(350);
                        this.setCanFly(true);
                    }
                }
            }
            if (this.getTarget() == null) {
                if (this.getFirstPassenger() instanceof Mob mob) {
                    if (mob.getTarget() != this.getTarget())
                        this.setTarget(mob.getTarget());
                }
            }
        }
    }

    public void handleAttack(AnimatedAction anim) {
        if (anim.is(CHARGING)) {
            if (anim.isPast("attack")) {
                this.setDeltaMovement(this.chargeMotion);
                OrientedBoundingBox obb = this.prepareAttackBox(anim, null, 0.2, false);
                List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                        entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()));
                boolean hit = this.canFly() && this.verticalCollision;
                if (hit) {
                    this.chargeMotion = new Vec3(this.chargeMotion.x() * 0.4, Math.abs(this.chargeMotion.y()) * 0.7, this.chargeMotion.z() * 0.4);
                }
                for (LivingEntity e : list) {
                    if (this.hitEntities.contains(e))
                        continue;
                    if (e.hurt(CustomDamageSource.pegasusCharge(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
                        if (this.hitEntities.isEmpty()) {
                            hit = true;
                        }
                        this.hitEntities.add(e);
                    }
                }
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
                if (hit)
                    S2CScreenShake.sendAround(this, 14, 8, 2);
            }
        } else if (!anim.is(SUMMON)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                S2CScreenShake.sendAround(this, 6, 8, 2);
            }
        }
    }

    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        OrientedBoundingBox obb = this.prepareAttackBox(anim, target, 0.2, false);
        this.level.getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox())).forEach(cons);
        if (!this.level.isClientSide)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, super::doHurtTarget, 0);
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
        return this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof Player player) ? null : player;
    }

    @Override
    public OrientedBoundingBox prepareAttackBox(AnimatedAction anim, LivingEntity target, double grow, boolean withDebug) {
        if (anim.is(CHARGING)) {
            AABB aabb = OrientedBoundingBox.originAABB(this).inflate(grow).expandTowards(this.getDeltaMovement());
            return new OrientedBoundingBox(aabb, this.getYRot(), 0, this.position());
        }
        double width = this.getBbWidth() * 0.5 + 1.5;
        AABB aabb = new AABB(-width * 0.8, -0.02, -width * 0.5, width * 0.8, this.getBbHeight() * 0.5, width * 1.2)
                .inflate(grow);
        return new OrientedBoundingBox(aabb, this.getYRot(), 0, this.position());
    }

    @Override
    public PathNavigation getNavigation() {
        if (this.canFly())
            return this.flyingNavigator;
        return super.getNavigation();
    }

    public void setCanFly(boolean flag) {
        this.entityData.set(FLYING, flag);
        this.setNoGravity(flag);
        if (flag) {
            this.navigation = this.flyingNavigator;
        } else {
            this.navigation = this.main;
        }
        this.main.stop();
        this.flyingNavigator.stop();
        this.setDeltaMovement(this.getDeltaMovement().scale(0.3));
    }

    public boolean canFly() {
        return this.entityData.get(FLYING);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.isBypassInvul())
            return super.hurt(damageSource, damage);
        if (this.getAnimationHandler().isCurrent(SUMMON))
            return false;
        if (this.isCharging())
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
        return anim != null && anim.is(SUMMON) && !anim.isPast(1.);
    }

    public void setChargeTo(Vec3 pos) {
        Vec3 dir = pos.subtract(this.position());
        if (!this.canFly()) {
            dir = new Vec3(dir.x(), 0, dir.z());
            if (dir.lengthSqr() > 1.3 * 1.3) {
                dir = dir.normalize().scale(1.3);
            }
        } else {
            dir = dir.scale(0.3);
            if (dir.lengthSqr() > 2.5 * 2.5) {
                dir = dir.normalize().scale(2.5);
            }
        }
        this.chargeMotion = dir;
        float[] xYRot = MathsHelper.XYRotFrom(dir);
        float targetYRot = xYRot[0];
        float targetXRot = xYRot[1];
        this.setYRot(targetYRot);
        this.setXRot(targetXRot);
        this.yHeadRot = this.getYRot();
        this.yBodyRot = this.getYRot();
        this.chargingHandler.lockYaw(this.getYRot());
    }

    class PegasusMoveController extends MoveControl {

        public PegasusMoveController(Pegasus pegasus) {
            super(pegasus);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO && Pegasus.this.canFly()) {
                BlockPos targetPos = this.mob.getNavigation().getTargetPos();
                if (targetPos == null)
                    return;
                float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
                Vec3 target = Vec3.atCenterOf(targetPos);
                double dX = target.x() - this.mob.getX();
                double dY = target.y() - this.mob.getY();
                double dZ = target.z() - this.mob.getZ();
                double horDist = Math.sqrt(dX * dX + dZ * dZ);
                if (Math.abs(horDist) > 0.5) {
                    double h = 1.0 - Math.abs(dY * (double) 0.7f) / horDist;
                    horDist = Math.sqrt((dX *= h) * dX + (dZ *= h) * dZ);
                    double dist = Math.sqrt(dX * dX + dZ * dZ + dY * dY);
                    float targetYRot = (float) Mth.wrapDegrees((Mth.atan2(dZ, dX) * Mth.RAD_TO_DEG)) - 90;
                    float targetXRot = (float) Mth.wrapDegrees((Mth.atan2(dY, horDist) * Mth.RAD_TO_DEG));
                    this.mob.setYRot(targetYRot);
                    this.mob.yBodyRot = this.mob.getYRot();
                    this.mob.setXRot(targetXRot);
                    float yRot = this.mob.getYRot() + 90.0f;
                    double x = (double) (speed * Mth.cos(yRot * ((float) Math.PI / 180))) * Math.abs(dX / dist);
                    double y = (double) (speed * Mth.sin(targetXRot * ((float) Math.PI / 180))) * Math.abs(dY / dist);
                    double z = (double) (speed * Mth.sin(yRot * ((float) Math.PI / 180))) * Math.abs(dZ / dist);
                    Vec3 vec3 = this.mob.getDeltaMovement();
                    this.mob.setDeltaMovement(vec3.add(new Vec3(x, y, z).subtract(vec3).scale(0.1)));
                }
            } else
                super.tick();
            if (Pegasus.this.canFly()) {
                LivingEntity target = this.mob.getTarget();
                if (target != null && target.distanceToSqr(this.mob) < 24 * 24) {
                    this.mob.lookAt(target, 60.0F, 30.0F);
                }
            }
        }
    }

    public enum MoveType {
        NONE,
        WALK,
        RUN
    }

    public static class ChargeTo implements ActionStart<Pegasus> {

        private boolean moving;
        private Vec3 targetPos;

        @Override
        public GoalAttackAction.IntProvider<Pegasus> timeout() {
            return e -> 20;
        }

        @Override
        public boolean start(AnimatedAttackGoal<Pegasus> goal, LivingEntity target) {
            if (goal.current == null)
                return false;
            if (goal.distanceToTargetSq < 4 * 4) {
                // Move away if too close
                for (int i = 0; i < 10; i++) {
                    Vec3 posAway = DefaultRandomPos.getPosAway(goal.attacker, 6, 5, target.position());
                    if (posAway != null) {
                        goal.moveToTargetPosition(posAway.x(), posAway.y(), posAway.z(), 1.1);
                        break;
                    }
                }
                this.moving = true;
            }
            if (this.moving && !goal.attacker.getNavigation().isDone()) {
                return false;
            }
            if (this.targetPos == null) {
                this.targetPos = target.getEyePosition();
            }
            double dY = this.targetPos.y() - goal.attacker.getEyeY();
            double dX = this.targetPos.x() - goal.attacker.getX();
            double dZ = this.targetPos.z() - goal.attacker.getZ();
            float[] xYRot = MathsHelper.XYRotFrom(dX, dY, dZ);
            float yRot = xYRot[0];
            float xRot = xYRot[1];

            float diffY = Mth.degreesDifference(goal.attacker.getYRot(), yRot);
            goal.attacker.setXRot(xRot);
            if (Math.abs(diffY) < 8) {
                goal.attacker.setChargeTo(this.targetPos);
                return true;
            }
            goal.attacker.setYRot(goal.attacker.getYRot() + Mth.clamp(diffY, -10, 10));
            goal.attacker.yBodyRot = goal.attacker.getYRot();
            goal.attacker.yHeadRot = goal.attacker.getYRot();
            goal.attacker.hasImpulse = true;
            return false;
        }
    }

    public static class PegasusFlyRunner implements ActionRun<Pegasus> {

        private final double speed;

        private boolean start, towards;
        private int cooldown;

        public PegasusFlyRunner(double speed) {
            this.speed = speed;
        }

        @Override
        public boolean run(AnimatedAttackGoal<Pegasus> goal, LivingEntity target, AnimatedAction anim) {
            if (!this.start) {
                this.start = true;
                if (goal.distanceToTargetSq > 24 * 24) {
                    goal.moveToTarget(this.speed);
                    this.towards = true;
                } else {
                    for (int i = 0; i < 10; i++) {
                        double x = target.getX() + (goal.attacker.getRandom().nextDouble() * 10) - 5;
                        double y = target.getEyeY() + 3 + (goal.attacker.getRandom().nextDouble() * 4);
                        double z = target.getZ() + (goal.attacker.getRandom().nextDouble() * 10) - 5;
                        BlockPos blockPos = new BlockPos(x, y, z);
                        if (GoalUtils.isOutsideLimits(blockPos, goal.attacker) || GoalUtils.isRestricted(true, goal.attacker, blockPos) || GoalUtils.isNotStable(goal.attacker.getNavigation(), blockPos) || GoalUtils.hasMalus(goal.attacker, blockPos)) {
                            continue;
                        }
                        goal.moveToTargetPosition(x, y, z, this.speed);
                        this.cooldown = goal.attacker.getRandom().nextInt(7) + 5;
                        break;
                    }
                }
            }
            if (this.towards && goal.distanceToTargetSq < 20 * 20) {
                goal.attacker.getNavigation().stop();
            }
            goal.attacker.lookControl.setLookAt(target, 100, 10);
            boolean done = goal.attacker.getNavigation().isDone();
            if (done) {
                if (--this.cooldown <= 0)
                    this.start = false;
            }
            return false;
        }
    }

}
