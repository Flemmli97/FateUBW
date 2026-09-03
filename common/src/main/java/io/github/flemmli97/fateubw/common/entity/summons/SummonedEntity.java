package io.github.flemmli97.fateubw.common.entity.summons;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.SetTargetFromRider;
import io.github.flemmli97.fateubw.common.entity.utils.MoveStateTracker;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.entity.utils.TargetableOpponent;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetMoveToRestriction;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class SummonedEntity extends PathfinderMob implements AnimatedEntity, OwnableEntity, AOEAttackEntity,
        SmartBrainOwner<SummonedEntity>, TargetableOpponent {

    protected static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(SummonedEntity.class, EntityDataSerializers.BYTE);

    public final Predicate<LivingEntity> targetPred = this.createTargetPredicate();

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(this, 2, MOVE_FLAGS, this::calculateMoveType);

    private UUID ownerUUID;
    private LivingEntity owner;

    public SummonedEntity(EntityType<? extends SummonedEntity> type, Level level) {
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

    protected Predicate<LivingEntity> createTargetPredicate() {
        return Utils.summonTargetPredicate(this);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOVE_FLAGS, (byte) 0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    public List<? extends ExtendedSensor<? extends SummonedEntity>> getSensors() {
        return List.of(new NearbyLivingEntitySensor<SummonedEntity>()
                        .setPredicate((target, entity) -> entity.targetPred.test(target))
                        .setScanRate(e -> 10),
                new HurtBySensor<SummonedEntity>().setPredicate((source, entity) -> {
                    if (source.getEntity() instanceof LivingEntity attacker)
                        return !Utils.alliedTo(entity, attacker);
                    return true;
                }));
    }

    @Override
    public BrainActivityGroup<? extends SummonedEntity> getCoreTasks() {
        Pair<Integer, Integer> follow = this.followRange();
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<>(),
                this.lookBehaviour(),
                new FollowEntity<SummonedEntity, LivingEntity>()
                        .following(SummonedEntity::getOwner)
                        .stopFollowingWithin(follow.getFirst())
                        .teleportToTargetAfter(follow.getSecond())
                        .speedMod(1.2f),
                new SetTargetFromRider<>());
    }

    protected Pair<Integer, Integer> followRange() {
        return Pair.of(10, 20);
    }

    protected ExtendedBehaviour<? extends SummonedEntity> lookBehaviour() {
        return new AllApplicableBehaviours<SummonedEntity>(
                new LookAtAttackTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomLookTarget<>().lookChance(ConstantFloat.of(1)),
                        new SetPlayerLookTarget<>()
                ).startCondition(m -> m.getRandom().nextFloat() < 0.1 && !BrainUtils.hasMemory(m, MemoryModuleType.WALK_TARGET))
        ).startCondition(e -> !BrainUtils.hasMemory(e, MemoryModuleType.ATTACK_TARGET) && !e.isSleeping()
                && !e.getAnimationHandler().hasAnimation());
    }

    @Override
    public BrainActivityGroup<? extends SummonedEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new MoveToWalkTarget<>(),
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<>(),
                        new SetMoveToRestriction<>(),
                        new SetRandomWalkTarget<>().startCondition(m -> m.getRandom().nextInt(150) == 0)
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends SummonedEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<Bucephalos>(),
                this.getCooldownAI().startCondition(BehaviourUtils::runCooldownBehaviour)
                        .stopIf(e -> !BehaviourUtils.runCooldownBehaviour(e)),
                this.getCombatAI().startCondition(BehaviourUtils::runCombatBehaviour)
        );
    }

    public abstract ExtendedBehaviour<? extends SummonedEntity> getCombatAI();

    public abstract ExtendedBehaviour<? extends SummonedEntity> getCooldownAI();

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
    public boolean removeWhenFarAway(double dist) {
        return false;
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
        if (this.getControllingPassenger() instanceof Player) {
            return MoveType.RUN;
        }
        if (!this.walkAnimation.isMoving() || this.isImmobile()) {
            return MoveType.NONE;
        }
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

    @Override
    public LivingEntity getTarget() {
        return BrainUtils.getTargetOfEntity(this);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        // In case setTarget is called without BrainUtils
        // Sync to memory
        // If BrainUtils is used it will override the brain target anyway
        if (super.getTarget() == null) {
            BrainUtils.clearMemory(this, MemoryModuleType.ATTACK_TARGET);
        } else {
            BrainUtils.setMemory(this, MemoryModuleType.ATTACK_TARGET, super.getTarget());
        }
    }

    public abstract void handleAttack(AnimationState anim);

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

    public abstract OrientedBoundingBox calculateAttackAABB(AnimationState anim, double grow);

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        if (this.ignoreExternalMobInfluence())
            return;
        super.knockback(strength, xRatio, zRatio);
    }

    @Override
    public void push(Entity entity) {
        if (this.ignoreExternalMobInfluence())
            return;
        super.push(entity);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        if (this.ignoreExternalMobInfluence())
            return false;
        return super.canCollideWith(entity);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.ignoreExternalMobInfluence())
            return;
        super.push(x, y, z);
    }

    protected boolean ignoreExternalMobInfluence() {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Owner"))
            this.ownerUUID = tag.getUUID("Owner");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUUID != null)
            tag.putUUID("Owner", this.ownerUUID);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
    }

    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null) {
            this.owner = EntityUtils.findFromUUID(LivingEntity.class, this.level(), this.ownerUUID);
        }
        return this.owner;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public Predicate<LivingEntity> validTargetPredicate() {
        return this.targetPred;
    }
}
