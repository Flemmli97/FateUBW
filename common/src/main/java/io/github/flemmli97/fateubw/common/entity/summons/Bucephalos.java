package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.SetTargetFromRider;
import io.github.flemmli97.fateubw.common.entity.utils.MoveStateTracker;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetMoveToRestriction;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedMobDataHandler;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Bucephalos extends PathfinderMob implements AnimatedEntity, AOEAttackEntity, SyncedMobDataHandler, SmartBrainOwner<Bucephalos> {

    private static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(Bucephalos.class, EntityDataSerializers.BYTE);
    private static final float ATTACK_MOVE_SPEED = 1.5f;

    public static final TypedResource<Vec3> CHARGE_MOTION = new TypedResource<>(Fate.modRes("charge_motion"));

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String STOMP = BUILDER.add("stomp", AnimationsBuilder.definition(0.76).marker("attack", 0.56));
    public static final String HEADBUTT = BUILDER.add("headbutt", AnimationsBuilder.definition(0.84).marker("attack", 0.48));
    public static final String CHARGE = BUILDER.add("charge", AnimationsBuilder.definition(1.44)
            .marker("charge_start", 0.64).marker("charge_end", 1.16));

    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    public final Predicate<LivingEntity> targetPred = Utils.summonTargetPredicate(this);

    private final AnimationHandler<Bucephalos> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide && anim != null && anim.is(CHARGE)) {
                    this.getDataContainer().set(CHARGE_MOTION, null);
                }
                return false;
            });

    private final SyncedDataContainer<Bucephalos> syncedDataContainer = SyncedDataContainer.builder(this)
            .define(CHARGE_MOTION, TenshilibSyncableEntityDatas.VEC_3.get(), null).build();

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(this, 2, MOVE_FLAGS, this::calculateMoveType);

    public Bucephalos(EntityType<? extends Bucephalos> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.updateAttributes();
        }
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
        builder.define(MOVE_FLAGS, (byte) 0);
    }

    @Override
    public SyncedDataContainer<?> getDataContainer() {
        return this.syncedDataContainer;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    public List<? extends ExtendedSensor<? extends Bucephalos>> getSensors() {
        return List.of();
    }

    @Override
    public BrainActivityGroup<? extends Bucephalos> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<GordiusWheel>(),
                new SetTargetFromRider<>());
    }

    @Override
    public BrainActivityGroup<? extends Bucephalos> getIdleTasks() {
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
    public BrainActivityGroup<? extends Bucephalos> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<Bucephalos>(),
                this.getCooldownAI().startCondition(BehaviourUtils::runCooldownBehaviour)
                        .stopIf(e -> !BehaviourUtils.runCooldownBehaviour(e)),
                this.getCombatAI().startCondition(BehaviourUtils::runCombatBehaviour)
        );
    }

    public ExtendedBehaviour<? extends Bucephalos> getCombatAI() {
        return AttackBehaviourBuilder.<Bucephalos>create()
                .start(STOMP).play(BehaviourUtils.cooldownedPlay(true, 20, 45))
                .prepare(new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(HEADBUTT).play(BehaviourUtils.cooldownedPlay(true, 20, 45))
                .prepare(new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(CHARGE).play(BehaviourUtils.cooldownedPlay(false, 20, 50))
                .condition(BehaviourUtils.ifFurtherThan(5))
                .prepare(new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED).closeEnoughDist(BehaviourUtils.closeEnough(12)))
                .prepareOptional(BehaviourUtils.moveAttack())
                .end(9)
                .start(CHARGE).play(BehaviourUtils.cooldownedPlay(false, 20, 50))
                .prepare(new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED).closeEnoughDist(BehaviourUtils.closeEnough(12)))
                .prepareOptional(BehaviourUtils.moveAttack())
                .end(4)
                .build();
    }

    public ExtendedBehaviour<? extends Bucephalos> getCooldownAI() {
        return SelectableBehaviourBuilder.<Bucephalos>builder()
                .add(7, new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED), BehaviourUtils.moveTo())
                .add(5, BehaviourUtils.withCondition(BehaviourUtils.ifCloserThan(7)),
                        new SetRandomWalkTarget<Bucephalos>().speedModifier(ATTACK_MOVE_SPEED).setRadius(12, 5), BehaviourUtils.moveTo())
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
        if (this.animationHandler.isCurrent(CHARGE)) {
            Vec3 charge = this.syncedDataContainer.get(CHARGE_MOTION);
            if (charge != null) {
                float[] yxRot = MathsHelper.YXRotFrom(charge);
                this.setYRot(yxRot[0]);
                this.setXRot(yxRot[1]);
                this.setYBodyRot(this.getYRot());
                this.setYHeadRot(this.getYRot());
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.getAnimationHandler().tick();
        this.moveStateTracker.tick();
        if (!this.level().isClientSide) {
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.tickBrain(this);
    }

    public float interpolatedMoveTick(float partialTick) {
        return this.moveStateTracker.interpolatedMoveTick(partialTick);
    }

    public float interpolatedMoveTickOf(MoveType type, float partialTick) {
        return this.moveStateTracker.interpolatedMoveTickOf(type, partialTick);
    }

    public MoveType calculateMoveType() {
        if (this.getControllingPassenger() instanceof Player || !this.walkAnimation.isMoving()) {
            return MoveType.NONE;
        }
        if (this.isImmobile())
            return MoveType.NONE;
        double d0 = this.getMoveControl().getSpeedModifier();
        MoveType move;
        if (d0 > 1) {
            move = MoveType.RUN;
        } else if (d0 <= 0.8) {
            move = MoveType.SNEAK;
        } else {
            move = MoveType.WALK;
        }
        return move;
    }

    public void handleAttack(AnimationState anim) {
        if (anim.is(CHARGE)) {
            if (!anim.isPast("charge_start")) {
                if (this.getTarget() != null) {
                    this.lookAt(this.getTarget(), 60, 30);
                }
            }
            if (anim.isAt("charge_start")) {
                LivingEntity target = this.getTarget();
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.getViewVector(1);
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(3).add(0, 0.2, 0);
                this.setDeltaMovement(dir);
                this.syncedDataContainer.set(CHARGE_MOTION, dir);
            }
            if (anim.isPast("charge_start") && !anim.isPast("charge_end")) {
                OrientedBoundingBox obb = this.prepareAttackBox(anim.getAnimation(), null, 0.2, false);
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                        entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()));
                boolean hit = false;
                for (LivingEntity entity : list) {
                    if (entity.hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
                        this.setDeltaMovement(Vec3.ZERO);
                        hit = true;
                    }
                }
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
                if (hit) {
                    S2CScreenShake.sendAround(this, 14, 8, 2);
                    this.animationHandler.setAnimation(HEADBUTT);
                }
            }
        } else if (anim.is(STOMP, HEADBUTT)) {
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.lookAt(this.getTarget(), 60, 30);
            }
            if (anim.isAt("attack")) {
                Consumer<LivingEntity> consumer = this::doHurtTarget;
                if (anim.is(HEADBUTT)) {
                    consumer = entity -> {
                        if (this.doHurtTarget(entity)) {
                            entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.3, 0));
                            entity.hurtMarked = true;
                        }
                    };
                }
                this.mobAttack(anim, this.getTarget(), consumer);
                if (anim.is(STOMP)) {
                    S2CScreenShake.sendAround(this, 6, 8, 2);
                }
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
                grow);
        if (debug)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTEMPT, this);
        return obb;
    }

    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, double grow) {
        if (anim.is(CHARGE)) {
            AABB aabb = OrientedBoundingBox.originAABB(this).inflate(grow).expandTowards(this.getDeltaMovement());
            return new OrientedBoundingBox(aabb, this.getYRot(), 0, this.position());
        }
        AABB aabb;
        if (anim.is(STOMP)) {
            double width = this.getBbWidth() * 1.5;
            aabb = new AABB(-width, -0.02, -width, width, this.getBbHeight() * 0.5, width)
                    .inflate(grow);
        } else {
            double width = this.getBbWidth() * 1.5;
            double length = this.getBbWidth() * 2.2;
            aabb = new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() * 0.5, length)
                    .inflate(grow);
        }
        return new OrientedBoundingBox(aabb, this.getYHeadRot(), 0, this.position());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, super::doHurtTarget, 0);
    }

    @Override
    public AnimationHandler<Bucephalos> getAnimationHandler() {
        return this.animationHandler;
    }
}
