package io.github.flemmli97.fateubw.common.entity.summons;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.SetTargetFromRider;
import io.github.flemmli97.fateubw.common.entity.utils.MoveStateTracker;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.entity.utils.StandingVehicle;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.ParticlePositionProvider;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetMoveToRestriction;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedMobDataHandler;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
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
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.SequentialBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomFlyingTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.navigation.SmoothFlyingPathNavigation;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Pegasus extends PathfinderMob implements AnimatedEntity, StandingVehicle, AOEAttackEntity, SyncedMobDataHandler, SmartBrainOwner<Pegasus> {

    public static float PORTAL_SIZE = 2;
    public static float PORTAL_OFFSET = 1.3f;

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(Pegasus.class, EntityDataSerializers.BYTE);
    public static final TypedResource<Vec3> CHARGE_MOTION = new TypedResource<>(Fate.modRes("charge_motion"));

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String CHARGING = BUILDER.add("charge", AnimationsBuilder.definition(1.2)
            .marker("charge_start", 0.36).marker("charge_end", 1));
    public static final String STOMP = BUILDER.add("stomp", AnimationsBuilder.definition(0.56).marker("attack", 0.4));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.04)
            .marker("seated", 1.04));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

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

    private final AnimationHandler<Pegasus> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide && anim != null && anim.is(CHARGING)) {
                    this.hitEntities = new ArrayList<>();
                    this.setChargeMotion(null);
                }
                return false;
            });

    private final PathNavigation main;
    private final PathNavigation flyingNavigator;
    private int flyTimer;

    private final SyncedDataContainer<Pegasus> syncedDataContainer = SyncedDataContainer.builder(this)
            .define(CHARGE_MOTION, TenshilibSyncableEntityDatas.VEC_3.get(), null).build();

    private List<Entity> hitEntities = new ArrayList<>();

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(2, this::getMoveType);

    public Pegasus(EntityType<? extends Pegasus> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.updateAttributes();
        }
        this.main = this.navigation;
        this.flyingNavigator = this.createFlyNavigator(level);
        this.moveControl = new PegasusMoveController(this);
    }

    private void updateAttributes() {
        AttributeHolderProperties props = DatapackHandler.SERVANT_PROPS.getGeneric(this.getType());
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLYING, false);
        builder.define(MOVE_FLAGS, (byte) 0);
    }

    @Override
    public SyncedDataContainer<?> getDataContainer() {
        return this.syncedDataContainer;
    }

    protected PathNavigation createFlyNavigator(Level level) {
        return new SmoothFlyingPathNavigation(Pegasus.this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return true;
            }
        };
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    public List<? extends ExtendedSensor<? extends Pegasus>> getSensors() {
        return List.of();
    }

    @Override
    public BrainActivityGroup<? extends Pegasus> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<GordiusWheel>(),
                new SetTargetFromRider<>());
    }

    @Override
    public BrainActivityGroup<? extends Pegasus> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new MoveToWalkTarget<>(),
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<GordiusWheel>(),
                        new SetMoveToRestriction<GordiusWheel>(),
                        new SetRandomWalkTarget<>().startCondition(m -> m.getRandom().nextInt(120) == 0)
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends Pegasus> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<Pegasus>(),
                this.getCooldownAI().startCondition(BehaviourUtils::runCooldownBehaviour)
                        .stopIf(e -> !BehaviourUtils.runCooldownBehaviour(e)),
                this.getCombatAI().startCondition(BehaviourUtils::runCombatBehaviour)
        );
    }

    public ExtendedBehaviour<? extends Pegasus> getCombatAI() {
        return AttackBehaviourBuilder.<Pegasus>create()
                .start(STOMP).play(BehaviourUtils.cooldownedPlay(true, 20, 45))
                .condition(entity -> !entity.canFly())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(CHARGING).play(BehaviourUtils.cooldownedPlay(false, 20, 50))
                .condition(entity -> !entity.canFly())
                .prepare(new SetWalkTargetWithinDist<Pegasus>().min(3)
                        .max(10).speedMod(1.2f))
                .prepareOptional(BehaviourUtils.timedMoveAttack(40, 70))
                .prepare(new ChargeBehaviour())
                .end(6)
                .start(CHARGING).play(BehaviourUtils.cooldownedPlay(false, 20, 50))
                .condition(entity -> !entity.canFly() && BehaviourUtils.ifFurtherThan(7).test(entity))
                .prepare(new SetWalkTargetWithinDist<Pegasus>().min(3)
                        .max(10).speedMod(1.2f))
                .prepareOptional(BehaviourUtils.timedMoveAttack(40, 70))
                .prepare(new ChargeBehaviour())
                .end(7)
                .start(CHARGING).play(BehaviourUtils.cooldownedPlay(false, 80, 160))
                .condition(Pegasus::canFly)
                .prepare(new SetWalkTargetWithinDist<Pegasus>().min(3)
                        .max(10).speedMod(1.2f))
                .prepareOptional(BehaviourUtils.timedMoveAttack(40, 70))
                .prepare(new ChargeBehaviour())
                .end(5)
                .build();
    }

    public ExtendedBehaviour<? extends Pegasus> getCooldownAI() {
        return SelectableBehaviourBuilder.<Pegasus>builder()
                .add(7, BehaviourUtils.withCondition(entity -> !entity.canFly()), new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(5, BehaviourUtils.withCondition(entity -> !entity.canFly() && BehaviourUtils.ifCloserThan(7).test(entity)),
                        new SetRandomWalkTarget<Pegasus>().setRadius(12, 5), BehaviourUtils.moveTo())
                .add(4, BehaviourUtils.withCondition(entity -> !entity.canFly() && BehaviourUtils.ifCloserThan(4).test(entity)),
                        new SetWalkTargetAwayFromTarget<Pegasus>()
                                .radius(6), BehaviourUtils.moveTo())
                .add(5, BehaviourUtils.withCondition(Pegasus::canFly), new AllApplicableBehaviours<>(
                        new SequentialBehaviour<>(
                                new FirstApplicableBehaviour<>(
                                        new SetWalkTargetToAttackTarget<>()
                                                .closeEnoughDist(BehaviourUtils.closeEnough(8))
                                                .startCondition(BehaviourUtils.ifFurtherThan(8)),
                                        new RandomPositionAroundTarget<>()
                                ), BehaviourUtils.moveTo()
                        ),
                        new LookAtTarget<>(),
                        new LookAtAttackTarget<>())
                )
                .build();
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.getAnimationHandler().tick();
        if (this.level().isClientSide) {
            if (this.getAnimationHandler().isCurrent(SUMMON)) {
                Vec3 base = Vec3.directionFromRotation(0, this.yBodyRot).scale(-PORTAL_OFFSET);
                Vec3 base2 = base.yRot(90 * Mth.DEG_TO_RAD).normalize();
                for (int i = 0; i < 4; i++) {
                    double sideScale = (this.random.nextDouble() - this.random.nextDouble()) * PORTAL_SIZE;
                    double upScale = (this.random.nextDouble() - this.random.nextDouble()) * PORTAL_SIZE + PORTAL_SIZE;
                    Vec3 pos = this.position().add(base).add(base2.scale(sideScale)).add(new Vec3(0, 1, 0).scale(upScale));
                    AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                            .addData(new ColorData(245 / 255F, 10 / 255F, 10 / 255F, 0.5f))
                            .addData(new ScaleData(0.5f))
                            .addData(new MotionData(this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01))
                            .addData(new ParticleMetaData(20, false, 0))
                            .add(this.level(), pos.x(), pos.y(), pos.z());
                }
            }
            if (this.getAnimationHandler().isCurrent(CHARGING) && this.getAnimationHandler().getAnimation().isPast("attack")) {
                Vec3 base = Vec3.directionFromRotation(0, this.yBodyRot).yRot(90 * Mth.DEG_TO_RAD).normalize();
                Vec3 dir = this.getDeltaMovement().scale(-0.23);
                for (int i = 0; i < 9; i++) {
                    double sideScale = ((this.random.nextDouble() * 2) - 1) * 3;
                    double upScale = (this.random.nextDouble() * 2) - 1;
                    Vec3 pos = this.position().add(base.scale(sideScale)).add(new Vec3(0, 1, 0).scale(upScale));
                    float r = (235 + this.getRandom().nextInt(10)) / 255F;
                    float g = (235 + this.getRandom().nextInt(10)) / 255F;
                    float b = 245 / 255F;
                    float scale = (float) (0.01 + this.getRandom().nextDouble() * 0.01);
                    AdvancedParticleContainer.make(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(8))
                                            .setColor(r, g, b, 0.4f)
                                            .setColor2(r, g, b, 0.4f)
                                            .setWidth(scale)
                                            .setWidth2(scale)
                                            .build()))
                            .addData(new MotionData(dir))
                            .addData(new ParticleMetaData(8 + this.getRandom().nextInt(8), false, 0))
                            .add(this.level(), pos.x(), pos.y(), pos.z());
                }
            }
        }
        if (!this.level().isClientSide) {
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
        }
        this.moveStateTracker.tick();
        Vec3 lookDir = this.directionToLookAt();
        if (lookDir != null) {
            float[] yxRot = MathsHelper.YXRotFrom(lookDir);
            this.setYRot(MathsHelper.rotlerp(this.getYRot(), yxRot[0], 30));
            this.setXRot(MathsHelper.rotlerp(this.getXRot(), yxRot[1], 30));
            this.setYBodyRot(this.getYRot());
            this.setYHeadRot(this.getYRot());
        }
    }

    private Vec3 directionToLookAt() {
        if (this.isCharging())
            return this.getChargeMotion();
        return null;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.tickBrain(this);
        if (!this.onGround())
            this.setMovingFlag(MoveType.FLY);
        else if (!(this.getControllingPassenger() instanceof Player) && this.getDeltaMovement().horizontalDistanceSqr() > 0.003 && this.isAlive()) {
            double speedMod = this.getMoveControl().getSpeedModifier();
            MoveType move;
            if (speedMod > 1 || (speedMod >= 1 && this.getTarget() != null)) {
                move = MoveType.RUN;
            } else if (speedMod <= 0.8) {
                move = MoveType.SNEAK;
            } else {
                move = MoveType.WALK;
            }
            if (this.isImmobile())
                move = MoveType.NONE;
            this.setMovingFlag(move);
        } else {
            this.setMovingFlag(MoveType.NONE);
            this.setShiftKeyDown(false);
            this.setSprinting(false);
        }
    }

    public void setMovingFlag(MoveType type) {
        this.entityData.set(MOVE_FLAGS, (byte) type.ordinal());
    }

    public MoveType getMoveType() {
        return MoveType.values()[this.entityData.get(MOVE_FLAGS)];
    }

    public float interpolatedMoveTick(float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTick(partialTicks);
    }

    public float interpolatedMoveTickOf(MoveType type, float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTickOf(type, partialTicks);
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

    public void handleAttack(AnimationState anim) {
        if (anim.is(CHARGING)) {
            if (anim.isPast("charge_start") && !anim.isPast("charge_end")) {
                Vec3 dir = this.getChargeMotion();
                if (dir == null) {
                    this.setChargeTo(this.position().add(this.getViewVector(1).scale(10)));
                    dir = this.getChargeMotion();
                }
                this.setDeltaMovement(dir);
                OrientedBoundingBox obb = this.prepareAttackBox(anim.getAnimation(), null, 0.2, false);
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                        entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()));
                boolean hit = this.canFly() && this.verticalCollision;
                if (hit) {
                    this.setChargeMotion(new Vec3(dir.x() * 0.4, Math.abs(dir.y()) * 0.7, dir.z() * 0.4));
                }
                for (LivingEntity e : list) {
                    if (this.hitEntities.contains(e))
                        continue;
                    if (e.hurt(FateDamageTypes.direct(FateDamageTypes.PEGASUS_CHARGE, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
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

    public void mobAttack(AnimationState anim, LivingEntity target, Consumer<LivingEntity> cons) {
        OrientedBoundingBox obb = this.prepareAttackBox(anim.getAnimation(), target, 0.2, false);
        this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox())).forEach(cons);
        if (!this.level().isClientSide)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
    }

    @Override
    public OrientedBoundingBox prepareAttackBox(String anim, Entity target, double grow, boolean debug) {
        OrientedBoundingBox obb = this.calculateAttackAABB(this.getAnimationHandler().createDefaulted(anim),
                target != null ? target.position() : null, grow);
        if (debug)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTEMPT, this);
        return obb;
    }

    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, @Nullable Vec3 target, double grow) {
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
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, super::doHurtTarget, 0);
    }

    @Override
    public AnimationHandler<Pegasus> getAnimationHandler() {
        return this.animationHandler;
    }

    public boolean isCharging() {
        if (this.getAnimationHandler() == null)
            return false;
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && anim.is(CHARGING) && anim.isPast("attack");
    }

    public Vec3 getChargeMotion() {
        return this.getDataContainer().get(CHARGE_MOTION);
    }

    public void setChargeTo(Vec3 pos) {
        Vec3 dir = pos.subtract(this.position());
        if (!this.canFly()) {
            dir = new Vec3(dir.x(), 0, dir.z());
            if (dir.lengthSqr() > 1.3 * 1.3) {
                dir = dir.normalize().scale(1.3);
            }
        } else {
            dir = dir.scale(0.21);
            if (dir.lengthSqr() > 1.7 * 1.7) {
                dir = dir.normalize().scale(1.7);
            }
        }
        this.setChargeMotion(dir);
    }

    public void setChargeMotion(Vec3 chargeMotion) {
        this.getDataContainer().set(CHARGE_MOTION, chargeMotion);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurt(damageSource, damage);
        if (this.getAnimationHandler().isCurrent(SUMMON))
            return false;
        if (this.isCharging())
            damage *= 0.5f;
        return super.hurt(damageSource, damage);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() | this.getAnimationHandler().isCurrent(SUMMON);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        if (reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.MOB_SUMMONED) {
            this.getAnimationHandler().setAnimation(SUMMON);
        }
        return data;
    }

    public void setCanFly(boolean flag) {
        this.entityData.set(FLYING, flag);
        this.setNoGravity(flag);
        this.navigation.stop();
        if (flag) {
            this.navigation = this.flyingNavigator;
        } else {
            this.navigation = this.main;
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(0.3));
    }

    public boolean canFly() {
        return this.entityData.get(FLYING);
    }

    @Override
    public boolean shouldStand() {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && anim.is(SUMMON) && !anim.isPast(1.);
    }

    class PegasusMoveController extends MoveControl {

        public PegasusMoveController(Pegasus pegasus) {
            super(pegasus);
        }

        @Override
        public void tick() {
            Operation op = this.operation;
            if (this.operation == Operation.MOVE_TO && Pegasus.this.canFly()) {
                this.operation = MoveControl.Operation.WAIT;
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
                if (target != null) {
                    if (target.distanceToSqr(this.mob) < 24 * 24) {
                        this.mob.lookAt(target, 60.0F, 30.0F);
                    }
                    if (op == Operation.WAIT) {
                        if (!Pegasus.this.getAnimationHandler().hasAnimation() && this.mob.getY() < target.getY() + target.getBbHeight() + 2.5) {
                            Vec3 delta = this.mob.getDeltaMovement().add(0, 0.02, 0);
                            if (delta.y() > 0.14) {
                                delta = new Vec3(delta.x(), 0.14, delta.z());
                            }
                            this.mob.setDeltaMovement(delta);
                        } else {
                            this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.98));
                        }
                    }
                }
            }
        }
    }

    public static class ChargeBehaviour extends ExtendedBehaviour<Pegasus> {

        private static final MemoryTest MEMORIES = MemoryTest.builder(1)
                .hasMemories(MemoryModuleType.ATTACK_TARGET);

        private Vec3 targetPos;

        @Override
        protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
            return MEMORIES;
        }

        @Override
        protected boolean shouldKeepRunning(Pegasus entity) {
            double dX = this.targetPos.x() - entity.getX();
            double dZ = this.targetPos.z() - entity.getZ();
            float yRot = MathsHelper.YRotFrom(dX, dZ);
            float diffY = Mth.degreesDifference(entity.getYRot(), yRot);
            if (Math.abs(diffY) < 32) {
                entity.setChargeTo(this.targetPos);
                return false;
            }
            return true;
        }

        @Override
        protected void start(Pegasus entity) {
            this.targetPos = BrainUtils.getTargetOfEntity(entity).getEyePosition();
        }

        @Override
        protected void tick(Pegasus entity) {
            super.tick(entity);
            double dY = this.targetPos.y() - entity.getEyeY();
            double dX = this.targetPos.x() - entity.getX();
            double dZ = this.targetPos.z() - entity.getZ();
            float[] yXRot = MathsHelper.YXRotFrom(dX, dY, dZ);
            entity.setYRot(MathsHelper.rotlerp(entity.getYRot(), yXRot[0], 20));
            entity.setXRot(MathsHelper.rotlerp(entity.getXRot(), yXRot[1], 30));
            entity.setYBodyRot(entity.getYRot());
            entity.setYHeadRot(entity.getYRot());
        }
    }

    public static class RandomPositionAroundTarget<E extends PathfinderMob> extends SetRandomFlyingTarget<E> {

        private static final MemoryTest MEMORIES = MemoryTest.builder(2)
                .noMemory(MemoryModuleType.WALK_TARGET)
                .hasMemories(MemoryModuleType.ATTACK_TARGET);

        @Override
        protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
            return MEMORIES;
        }

        @Override
        protected Vec3 getTargetPos(E entity) {
            LivingEntity target = BrainUtils.getTargetOfEntity(entity);
            if (target == null)
                return null;
            for (int i = 0; i < 10; i++) {
                double x = target.getX() + (entity.getRandom().nextDouble() * 10) - 5;
                double y = target.getEyeY() + 3 + (entity.getRandom().nextDouble() * 4);
                double z = target.getZ() + (entity.getRandom().nextDouble() * 10) - 5;
                BlockPos blockPos = BlockPos.containing(x, y, z);
                blockPos = RandomPos.moveUpOutOfSolid(blockPos, entity.level().getMaxBuildHeight(), (pos) -> GoalUtils.isSolid(entity, pos));
                if (GoalUtils.isOutsideLimits(blockPos, entity) || GoalUtils.isRestricted(true, entity, blockPos) || GoalUtils.hasMalus(entity, blockPos)) {
                    continue;
                }
                return new Vec3(x, y, z);
            }
            return null;
        }
    }
}
