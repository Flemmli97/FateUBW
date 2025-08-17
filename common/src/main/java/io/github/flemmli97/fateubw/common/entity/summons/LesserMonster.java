package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.utils.MoveStateTracker;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetMoveToRestriction;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.UUID;

public class LesserMonster extends PathfinderMob implements AnimatedEntity, OwnableEntity, SmartBrainOwner<LesserMonster> {

    protected static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(LesserMonster.class, EntityDataSerializers.BYTE);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ATTACK = BUILDER.add("attack", AnimationsBuilder.definition(0.76).marker("attack", 0.52));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private UUID ownerUUID;
    private LivingEntity owner;
    private int livingTicks;

    private final AnimationHandler<LesserMonster> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(3, this::getMoveType);

    private final int maxLivingTicks;

    public LesserMonster(EntityType<? extends LesserMonster> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.updateAttributes();
        }
        this.maxLivingTicks = DatapackHandler.SERVANT_PROPS.get(FateEntities.GILLES.get())
                .getConfig(ServantExtraData.GILLES_MONSTER_DURATION);
    }

    public LesserMonster(Level level, LivingEntity owner) {
        this(FateEntities.LESSER_MONSTER.get(), level);
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
    }

    protected void updateAttributes() {
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
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            this.livingTicks++;
            if (this.livingTicks > this.maxLivingTicks)
                this.remove(RemovalReason.KILLED);
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null && anim.is(ATTACK) && anim.isAt("attack")) {
                LivingEntity target = this.getTarget();
                if (target != null && this.getAttackBoundingBox().intersects(target.getBoundingBox())) {
                    this.doHurtTarget(target);
                }
            }
        }
        this.moveStateTracker.tick();
        this.getAnimationHandler().tick();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.tickBrain(this);
        if (this.getDeltaMovement().horizontalDistanceSqr() > 0.003 && this.isAlive() && !this.isImmobile()) {
            this.setMovingFlag(MoveType.RUN);
        } else {
            this.setMovingFlag(MoveType.NONE);
        }
    }

    @Override
    public List<? extends ExtendedSensor<? extends LesserMonster>> getSensors() {
        return List.of(new NearbyLivingEntitySensor<LesserMonster>()
                        .setPredicate((target, entity) -> !Utils.alliedTo(target, entity))
                        .setScanRate(e -> 10),
                new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<? extends LesserMonster> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<LesserMonster>(),
                this.lookBehaviour(),
                new FollowEntity<LesserMonster, LivingEntity>().following(LesserMonster::getOwner),
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 100))
                        .whenStopping(m -> BrainUtils.clearMemory(m, MemoryModuleType.LOOK_TARGET)));
    }

    protected ExtendedBehaviour<? extends LesserMonster> lookBehaviour() {
        return new AllApplicableBehaviours<LesserMonster>(
                new LookAtAttackTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomLookTarget<>().lookChance(ConstantFloat.of(1)),
                        new SetPlayerLookTarget<>()
                ).startCondition(m -> m.getRandom().nextFloat() < 0.1 && !BrainUtils.hasMemory(m, MemoryModuleType.WALK_TARGET))
        ).startCondition(e -> !BrainUtils.hasMemory(e, MemoryModuleType.ATTACK_TARGET));
    }

    @Override
    public BrainActivityGroup<? extends LesserMonster> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new MoveToWalkTarget<>(),
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<LesserMonster>(),
                        new SetMoveToRestriction<LesserMonster>(),
                        new SetRandomWalkTarget<>().startCondition(m -> m.getRandom().nextInt(120) == 0)
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends LesserMonster> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<LesserMonster>(),
                new FirstApplicableBehaviour<>(
                        SelectableBehaviourBuilder.<LesserMonster>builder()
                                .add(1, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                                .build().startCondition(LesserMonster::runCooldownBehaviour)
                                .stopIf(e -> !e.runCooldownBehaviour()),
                        AttackBehaviourBuilder.<LesserMonster>create()
                                .start(ATTACK).play(BehaviourUtils.cooldownedPlay((anim, entity) -> entity.getRandom().nextInt(30) + 10))
                                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.moveTo())
                                .end(1)
                                .build()
                ).startCondition(m -> m.getTarget() != null)
        );
    }

    protected boolean runCooldownBehaviour() {
        return !this.getAnimationHandler().hasAnimation() && BrainUtils.hasMemory(this, MemoryModuleType.ATTACK_COOLING_DOWN);
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

    public float interpolatedMoveTick(float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTick(partialTicks);
    }

    public void setMovingFlag(MoveType type) {
        this.entityData.set(MOVE_FLAGS, (byte) type.ordinal());
    }

    public MoveType getMoveType() {
        return MoveType.values()[this.entityData.get(MOVE_FLAGS)];
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

    @Override
    public AnimationHandler<LesserMonster> getAnimationHandler() {
        return this.animationHandler;
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
}