package io.github.flemmli97.fateubw.common.entity;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.api.entity.CommandType;
import io.github.flemmli97.fateubw.api.entity.ServantLike;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.SetTargetFromRider;
import io.github.flemmli97.fateubw.common.entity.utils.MoveStateTracker;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.entity.utils.ServantModelLike;
import io.github.flemmli97.fateubw.common.entity.utils.TargetableOpponent;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.network.S2CServantGui;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailHolder;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailHolderProvider;
import io.github.flemmli97.fateubw.common.registry.FateActivities;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateMemoryTypes;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.ExtendedCombatRules;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.MoveControllerPlus;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetMoveToRestriction;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedMobDataHandler;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
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
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.InteractWithDoor;
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
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BaseServant extends PathfinderMob implements AnimatedEntity, AOEAttackEntity,
        TargetableOpponent, EntityWeaponTrailHolderProvider, ServantModelLike, ServantLike<BaseServant>, SmartBrainOwner<BaseServant>,
        SyncedMobDataHandler {

    public static final int MOVE_TICK_MAX = 3;

    protected static final ResourceLocation MANA_LEECH_DEBUFF_ID = Fate.modRes("mana_leech_debuff");

    protected static final EntityDataAccessor<Boolean> SHOW_SERVANT = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> STATIONARY = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.BYTE);

    public static final TypedResource<TargetPosition> TARGET_POSITION = new TypedResource<>(Fate.modRes("target_position"));

    protected static final Vector4f SUMMON_COLOR = new Vector4f(60 / 255f, 118 / 255f, 199 / 255f, 0.8f);

    private final SyncedDataContainer<BaseServant> syncedDataContainer;

    //Mana
    private double servantMana = 100, manaRegenCounter, nobelPhantasmCooldown;
    private int manaLeechDebuffDuration;
    protected boolean commandNPUse;

    protected CommandType commandBehaviour = CommandType.NORMAL;
    protected AttackBehaviour attackBehaviour = AttackBehaviour.NORMAL;

    //PlayerUUID
    private Player owner;

    private final Component hogou;

    private final ServantProperties prop;

    public final Predicate<LivingEntity> targetPred = Utils.servantTargetPredicate(this);

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(this, MOVE_TICK_MAX, MOVE_FLAGS, this::calculateMoveType);

    private final List<ServerPlayer> tracked = new ArrayList<>();
    private boolean sendToOwnerData;
    private boolean initAnim;

    private final EntityWeaponTrailHolder<BaseServant> trailHolder = new EntityWeaponTrailHolder<>(this);

    public BaseServant(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
        SyncedDataContainer.Builder<BaseServant> builder = SyncedDataContainer.builder(this);
        this.definedAdditinoalSyncedData(builder);
        this.syncedDataContainer = builder.build();
        this.moveControl = new MoveControllerPlus(this);
        this.xpReward = 35;
        this.prop = DatapackHandler.SERVANT_PROPS.get(this.getType());
        if (!level.isClientSide) {
            this.updateAttributes();
        }
        this.hogou = Component.translatable(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()) + ".hogou");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(FateAttributes.MAGIC_ATTACK.asHolder()).add(FateAttributes.MAGIC_RESISTANCE.asHolder())
                .add(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder()).add(FateAttributes.PROJECTILE_RESISTANCE.asHolder())
                .add(FateAttributes.COMBAT_REGEN.asHolder())
                .add(FateAttributes.PASSIVE_REGEN.asHolder())
                .add(FateAttributes.MANA_REGEN.asHolder(), 1)
                .add(FateAttributes.MANA_LEECH.asHolder(), 1);
    }

    private void updateAttributes() {
        this.prop.attributes().forEach((att, val) -> {
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
        builder.define(STATIONARY, false);
        builder.define(SHOW_SERVANT, false);
        builder.define(OWNER_UUID, Optional.empty());
        builder.define(MOVE_FLAGS, (byte) 0);
    }

    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<BaseServant> builder) {
        builder.define(TARGET_POSITION, TenshilibSyncableEntityDatas.TARGET_POS.get(), null);
    }

    @Override
    public SyncedDataContainer<?> getDataContainer() {
        return this.syncedDataContainer;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        SmoothGroundNavigation nav = new SmoothGroundNavigation(this, level);
        nav.setCanOpenDoors(true);
        nav.setCanPassDoors(true);
        return nav;
    }

    @Override
    public List<? extends ExtendedSensor<? extends BaseServant>> getSensors() {
        return List.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<BaseServant>()
                        .setPredicate((target, entity) -> entity.targetPred.test(target))
                        .setScanRate(e -> 10),
                new HurtBySensor<BaseServant>().setPredicate((source, entity) -> {
                    if (source.getEntity() instanceof LivingEntity attacker)
                        return !Utils.alliedTo(entity, attacker);
                    return true;
                }));
    }

    @Override
    public BrainActivityGroup<? extends BaseServant> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<BaseServant>(),
                new SetTargetFromRider<>(),
                new InteractWithDoor<>(),
                new FollowEntity<BaseServant, Player>()
                        .following(BaseServant::getOwner)
                        .teleportToTargetAfter(17)
                        .stopFollowingWithin(6)
                        .speedMod(1.1f)
                        .startCondition(m -> m.commandBehaviour == CommandType.FOLLOW),
                this.lookBehaviour(),
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 100))
                        .whenStopping(m -> BrainUtils.clearMemory(m, MemoryModuleType.LOOK_TARGET)));
    }

    protected ExtendedBehaviour<? extends BaseServant> lookBehaviour() {
        return new AllApplicableBehaviours<BaseServant>(
                new LookAtAttackTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomLookTarget<>().lookChance(ConstantFloat.of(1)),
                        new SetPlayerLookTarget<>()
                ).startCondition(m -> m.getRandom().nextFloat() < 0.1 && !BrainUtils.hasMemory(m, MemoryModuleType.WALK_TARGET))
        ).startCondition(e -> !BrainUtils.hasMemory(e, MemoryModuleType.ATTACK_TARGET) && !e.isSleeping()
                && !e.getAnimationHandler().hasAnimation());
    }

    @Override
    public BrainActivityGroup<? extends BaseServant> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new MoveToWalkTarget<>(),
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<BaseServant>()
                                .attackablePredicate(entity -> {
                                    if (entity == BrainUtils.getMemory(this, MemoryModuleType.HURT_BY_ENTITY))
                                        return true;
                                    if (entity instanceof Enemy && this.attackBehaviour == AttackBehaviour.AGGRESSIVE)
                                        return true;
                                    return entity instanceof ServantLike<?>;
                                }),
                        new SetMoveToRestriction<BaseServant>(),
                        this.getWanderBehaviour().startCondition(m -> m.getRandom().nextInt(120) == 0)
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends BaseServant> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<BaseServant>(),
                this.getCooldownAI().startCondition(BehaviourUtils::runCooldownBehaviour)
                        .stopIf(e -> !BehaviourUtils.runCooldownBehaviour(e)),
                this.getCombatAI().startCondition(BehaviourUtils::runCombatBehaviour)
        );
    }

    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return new Idle<>();
    }

    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return new Idle<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Activity, BrainActivityGroup<? extends BaseServant>> getAdditionalTasks() {
        Map<Activity, BrainActivityGroup<? extends BaseServant>> map = new HashMap<>();
        map.put(FateActivities.STAY.get(), new BrainActivityGroup<BaseServant>(FateActivities.STAY.get()).priority(20).behaviours(new Idle<>())
                .onlyStartWithMemoryStatus(FateMemoryTypes.STAYING.get(), MemoryStatus.VALUE_PRESENT));
        return map;
    }

    @Override
    public List<Activity> getActivityPriorities() {
        return ObjectArrayList.of(FateActivities.STAY.get(), Activity.FIGHT, Activity.IDLE);
    }

    protected ExtendedBehaviour<? extends BaseServant> getWanderBehaviour() {
        return new SetRandomWalkTarget<>();
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
        if (!this.initAnim) {
            this.getAnimationHandler().withChangeListener(anim -> {
                if (!this.level().isClientSide && this.getTargetPosition() != null)
                    this.setTargetPosition((TargetPosition) null);
                if (anim != null)
                    this.setupAttack(anim);
                return false;
            });
            this.initAnim = true;
        }
        super.tick();
        Vec3 lookDir = this.directionToLookAt();
        if (lookDir != null) {
            float[] yxRot = MathsHelper.YXRotFrom(lookDir);
            float[] clamp = this.targetLookClamp();
            this.setYRot(MathsHelper.rotlerp(this.getYRot(), yxRot[0], clamp[0]));
            this.setXRot(MathsHelper.rotlerp(this.getXRot(), yxRot[1], clamp[1]));
            this.setYBodyRot(this.getYRot());
            this.setYHeadRot(this.getYRot());
        }
        this.getTrailHolder().tick();
        this.trackingTick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.getAnimationHandler().tick();
        this.moveStateTracker.tick();
        if (this.getSummonAnimation() != null && this.getAnimationHandler().isCurrent(this.getSummonAnimation())) {
            this.setDeltaMovement(Vec3.ZERO);
            this.getNavigation().stop();
        }
        if (this.level() instanceof ServerLevel) {
            --this.nobelPhantasmCooldown;
            this.regenMana();
            --this.manaLeechDebuffDuration;
            if (this.manaLeechDebuffDuration == 0) {
                AttributeInstance inst = this.getAttribute(FateAttributes.MANA_LEECH.asHolder());
                inst.removeModifier(MANA_LEECH_DEBUFF_ID);
            }
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
            if (this.getOwner() instanceof ServerPlayer serverPlayer) {
                if (!this.tracked.contains(serverPlayer) && !this.isRemoved()) {
                    if (this.sendToOwnerData) {
                        // Update meta for the player with gui open
                        S2CServantGui.sendServantGui(serverPlayer, this, false);
                    }
                }
            }
            if (this.getTarget() != null && this.getTarget().getVehicle() instanceof LivingEntity)
                this.setTarget((LivingEntity) this.getTarget().getVehicle());
        }
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.tickBrain(this);
    }

    @Override
    public float interpolatedMoveTick(float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTick(partialTicks);
    }

    @Override
    public float interpolatedMoveTickOf(MoveType moveType, float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTickOf(moveType, partialTicks);
    }

    public MoveType calculateMoveType() {
        if (this.getControllingPassenger() instanceof Player || !this.walkAnimation.isMoving()) {
            return MoveType.NONE;
        }
        if (this.isImmobile())
            return MoveType.NONE;
        double d0 = this.getMoveControl().getSpeedModifier();
        MoveType move;
        if (d0 > 1 || this.getTarget() != null) {
            move = MoveType.RUN;
        } else if (d0 <= 0.8) {
            move = MoveType.SNEAK;
        } else {
            move = MoveType.WALK;
        }
        return move;
    }

    protected Vec3 directionToLookAt() {
        return this.getAnimationHandler().hasAnimation() && this.getTargetPosition() != null ? this.getTargetPosition()
                .asVec(this.position()).subtract(this.position()) : null;
    }

    protected float[] targetLookClamp() {
        return new float[]{60, 30};
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.entityData.get(OWNER_UUID).ifPresent(uuid -> tag.putUUID("Owner", uuid));
        tag.putInt("Death", this.deathTime);
        tag.putString("Command", this.attackBehaviour.toString());
        tag.putDouble("Mana", this.servantMana);
        tag.putBoolean("Revealed", this.showServant());
        if (this.getEquipmentHandler() != null) {
            tag.put("EquipmentHandler", this.getEquipmentHandler().save(this.registryAccess()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Owner"))
            this.entityData.set(OWNER_UUID, Optional.of(tag.getUUID("Owner")));
        this.deathTime = tag.getInt("Death");
        try {
            this.onBehaviourCommand(CommandType.valueOf(tag.getString("Command")));
        } catch (IllegalArgumentException ignored) {
        }
        this.servantMana = tag.getInt("Mana");
        this.entityData.set(SHOW_SERVANT, tag.getBoolean("Revealed"));
        if (this.getEquipmentHandler() != null) {
            this.getEquipmentHandler().read(tag.getCompound("EquipmentHandler"), this.registryAccess());
        }
    }

    public TargetPosition getTargetPosition() {
        return this.getDataContainer().get(TARGET_POSITION);
    }

    public void setTargetPositionFromAttackTarget() {
        LivingEntity target = this.getTarget();
        if (target != null)
            this.setTargetPosition(target);
        else
            this.setTargetPosition(TargetPosition.of(this.position().add(this.getViewVector(1).scale(10))));
    }

    public void setTargetPosition(LivingEntity target) {
        this.setTargetPosition(target == null ? null : TargetPosition.of(target));
    }

    public void setTargetPosition(TargetPosition position) {
        this.getDataContainer().set(TARGET_POSITION, position);
    }

    @Nullable
    public Vec3 tryGetTargetPosition(LivingEntity target) {
        if (this.getTargetPosition() != null)
            return this.getTargetPosition()
                    .asVec(this.position());
        return target != null ? target.position() : null;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target) && this.isWithinRestriction(target.blockPosition());
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

    public void setupAttack(AnimationDefinition anim) {
        BrainUtils.clearMemory(this, MemoryModuleType.LOOK_TARGET);
        if (this.getTarget() != null) {
            this.setTargetPosition(this.getTarget());
        }
        this.getNavigation().stop();
    }

    public void handleAttack(AnimationState anim) {
        if (anim.is(this.getSummonAnimation()))
            return;
        if (anim.isAt("attack")) {
            this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
        }
    }

    public void mobAttack(AnimationState anim, LivingEntity target, Consumer<LivingEntity> cons) {
        OrientedBoundingBox obb = this.calculateAttackAABB(anim, this.tryGetTargetPosition(target), 0.2);
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
        float yRot = this.getYHeadRot();
        float xRot = this.getXRot();
        if (this.getControllingPassenger() instanceof Player player) {
            yRot = player.getYHeadRot();
            xRot = player.getXRot();
        } else if (target != null) {
            Vec3 dir = target.subtract(this.position()).normalize();
            float[] yXRot = MathsHelper.YXRotFrom(dir);
            yRot = yXRot[0];
            xRot = -yXRot[1];
        }
        double off = this.getBbHeight() * 0.5;
        return new OrientedBoundingBox(this.attackBB(anim)
                .inflate(grow, 0, grow)
                .move(0, -off, grow), yRot, Mth.clamp(xRot, -15, 15), this.position().add(0, off, 0));
    }

    public AABB attackBB(AnimationState anim) {
        double range = 1;
        return new AABB(-range * 0.5, -0.02, 0, range * 0.5, this.vehicleDependentHeight() + 0.02, range);
    }

    public final double vehicleDependentHeight() {
        double height = this.getBbHeight();
        Entity entity = this.getVehicle();
        if (entity != null) {
            height = this.getAttackBoundingBox().maxY - entity.getBoundingBox().minY;
        }
        return height;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, this::mobHurtTarget, 0);
    }

    protected boolean mobHurtTarget(Entity target) {
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        DamageSource damageSource = this.damageSourceAttack(target);
        if (this.level() instanceof ServerLevel serverLevel) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), target, damageSource, damage);
        }
        damage *= this.damageModifier(target);
        boolean result = target.hurt(damageSource, damage);
        if (result) {
            float knockback = this.getKnockback(target, damageSource);
            if (knockback > 0 && target instanceof LivingEntity livingEntity) {
                livingEntity.knockback(knockback * 0.5, Mth.sin(this.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(this.getYRot() * Mth.DEG_TO_RAD));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1, 0.6));
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource);
            }
            this.setLastHurtMob(target);
            this.onEntityHit(target, damage);
        }
        return result;
    }

    public float damageModifier(Entity target) {
        return 1;
    }

    public void onEntityHit(Entity target, float damage) {
        this.playAttackSound();
        this.servantMana += this.getAttributeValue(FateAttributes.MANA_LEECH.asHolder());
    }

    protected DamageSource damageSourceAttack(Entity target) {
        return this.damageSources().mobAttack(this);
    }

    protected void tryDisableShield(Player player, ItemStack stack, ItemStack playerUseItem) {
        if (!stack.isEmpty() && !playerUseItem.isEmpty() && stack.getItem() instanceof AxeItem && playerUseItem.is(Items.SHIELD)) {
            player.getCooldowns().addCooldown(Items.SHIELD, 100);
            this.level().broadcastEntityEvent(player, (byte) 30);
        }
    }

    @Override
    public Predicate<LivingEntity> validTargetPredicate() {
        return this.targetPred;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (this.getSummonAnimation() != null && this.getAnimationHandler().isCurrent(this.getSummonAnimation()))
            return true;
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(damageSource, damage);
        } else {
            if (damageSource.getEntity() == null || !damageSource.getEntity().getType().is(FateTags.EntityTypes.STRONG_MOB))
                damage *= 0.75f;
            return super.hurt(damageSource, Math.min(50, damage));
        }
    }

    @Override
    protected float getDamageAfterArmorAbsorb(DamageSource damageSource, float damageAmount) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
            this.hurtArmor(damageSource, damageAmount);
            damageAmount = ExtendedCombatRules.getDamageAfterArmor(this, damageAmount, damageSource, this.getAttributeValue(Attributes.ARMOR), this.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        }
        return damageAmount;
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        super.knockback(strength * 0.75, xRatio, zRatio);
    }

    @Override
    public ServantProperties props() {
        return this.prop;
    }

    @Override
    public Component nobelPhantasm() {
        return this.hogou;
    }

    @Override
    public void onPlayerCommand(ServerPlayer player, CommandType behaviour) {
        if (behaviour.isBehaviour)
            this.onBehaviourCommand(behaviour);
        if (behaviour == CommandType.KILL) {
            if (!player.getUUID().equals(this.getOwnerUUID()))
                return;
            this.hurt(FateDamageTypes.grail(this.registryAccess()), Float.MAX_VALUE);
        }
        if (behaviour == CommandType.NP) {
            if (!this.commandNPUse) {
                PlayerData data = Platform.INSTANCE.getPlayerData(player);
                if (player.hasInfiniteMaterials() || (data.useMana(this.props().manaCost()) && data.useCommandSeal())) {
                    player.sendSystemMessage(Component.translatable("fateubw.chat.command.npsuccess").withStyle(ChatFormatting.RED));
                    this.commandNPUse = true;
                } else {
                    player.sendSystemMessage(Component.translatable("fateubw.chat.command.npfail").withStyle(ChatFormatting.RED));
                }
            } else {
                player.sendSystemMessage(Component.translatable("fateubw.chat.command.npprep").withStyle(ChatFormatting.RED));
            }
        }
    }

    public void onBehaviourCommand(CommandType command) {
        if (!command.isBehaviour)
            return;
        this.commandBehaviour = command;
        switch (command) {
            case NORMAL, AGGRESSIVE, DEFENSIVE -> {
                this.setTarget(null);
                this.getNavigation().stop();
                this.attackBehaviour = AttackBehaviour.of(command);
            }
            case FOLLOW -> {
                this.setStaying(false);
                this.clearRestriction();
            }
            case STAY -> this.setStaying(true);
            case GUARD -> {
                this.setStaying(false);
                this.restrictTo(this.getOwner().blockPosition(), 8);
            }
        }
    }

    @Override
    public void shouldScheduleEntityDataSync(boolean sync) {
        this.sendToOwnerData = sync;
    }

    public boolean showServant() {
        return this.entityData.get(SHOW_SERVANT);
    }

    public void revealServant() {
        this.entityData.set(SHOW_SERVANT, true);
    }

    @Override
    public boolean isStaying() {
        return this.entityData.get(STATIONARY);
    }

    public void setStaying(boolean stay) {
        this.entityData.set(STATIONARY, stay);
        if (!this.level().isClientSide) {
            if (stay) {
                BrainUtils.setMemory(this, FateMemoryTypes.STAYING.get(), Unit.INSTANCE);
            } else {
                BrainUtils.clearMemory(this, FateMemoryTypes.STAYING.get());
            }
        }
    }

    public boolean useMana(float amount) {
        if (this.servantMana < amount) {
            return false;
        } else {
            this.servantMana -= amount;
            this.manaRegenCounter = 40;
            return true;
        }
    }

    protected void regenMana() {
        if (--this.manaRegenCounter <= 0) {
            this.regenMana(this.getAttributeValue(FateAttributes.MANA_REGEN.asHolder()));
            this.manaRegenCounter = 20;
        }
    }

    public void regenMana(Entity source) {
        double amount = this.getAttributeValue(FateAttributes.MANA_LEECH.asHolder());
        this.regenMana(amount);
    }

    public void regenMana(double amount) {
        this.servantMana = Mth.clamp(this.servantMana + amount, 0, 100);
    }

    public int getMana() {
        return (int) this.servantMana;
    }

    public void applyManaLeechDebuff(int duration, double amount) {
        this.manaLeechDebuffDuration = duration;
        AttributeInstance inst = this.getAttribute(FateAttributes.MANA_LEECH.asHolder());
        inst.removeModifier(MANA_LEECH_DEBUFF_ID);
        inst.addTransientModifier(new AttributeModifier(MANA_LEECH_DEBUFF_ID, 1 - amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    public boolean healthBelow(float percentage) {
        return this.getHealth() < this.getMaxHealth() * percentage;
    }

    public boolean canUseNobelPhantasm() {
        return this.nobelPhantasmCheck() || this.commandNPUse;
    }

    public boolean nobelPhantasmCheck() {
        return (this.getMana() >= this.props().manaCost() && this.nobelPhantasmCooldown <= 0);
    }

    public boolean attemptUseNobelPhantasm() {
        if (!this.commandNPUse && !this.useMana(this.props().manaCost()))
            return false;
        if (!this.commandNPUse) {
            this.nobelPhantasmCooldown = this.nobelPhantasmCooldown();
        }
        this.commandNPUse = false;
        return true;
    }

    protected int nobelPhantasmCooldown() {
        return 200 + this.getRandom().nextInt(100);
    }

    public HeldEquipmentHandler getEquipmentHandler() {
        return null;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData data) {
        super.finalizeSpawn(world, difficulty, reason, data);
        this.populateDefaultEquipmentSlots(this.getRandom(), difficulty);
        for (EquipmentSlot type : EquipmentSlot.values())
            this.setDropChance(type, 0);
        this.setLeftHanded(false);
        if (this.getSummonAnimation() != null) {
            if (reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.MOB_SUMMONED) {
                this.getAnimationHandler().setAnimation(this.getSummonAnimation());
            }
        }
        return data;
    }

    /**
     * Can return null despite having an owner if player is offline
     */
    @Override
    public Player getOwner() {
        if (this.owner != null && this.owner.isAlive())
            return this.owner;
        UUID ownerId = this.getOwnerUUID();
        if (ownerId != null) {
            Player owner;
            if (this.getServer() != null)
                owner = this.getServer().getPlayerList().getPlayer(ownerId);
            else
                owner = this.level().getPlayerByUUID(ownerId);
            if (owner != null)
                this.setOwner(owner);
        }
        return this.owner;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    @Override
    public void setOwner(Player player) {
        if (player != null) {
            this.entityData.set(OWNER_UUID, Optional.of(player.getUUID()));
        } else
            this.entityData.set(OWNER_UUID, Optional.empty());
        this.owner = player;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown() && player.getUUID().equals(this.getOwnerUUID())) {
            if (player instanceof ServerPlayer serverPlayer)
                S2CServantGui.sendServantGui(serverPlayer, this);
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || (this.getSummonAnimation() != null && this.getAnimationHandler().isCurrent(this.getSummonAnimation()));
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        this.tracked.add(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        this.tracked.remove(player);
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Override
    protected void tickDeath() {
        if (this.level().isClientSide) {
            for (int i = 0; i < ((int) ((9 / (float) this.maxDeathTick()) * this.deathTime - 1)); i++) {
                AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                        .addData(new ColorData(76 / 255f, 128 / 255f, 207 / 255f, 0.3f))
                        .addData(new ScaleData(0.15f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D))
                        .addData(new ParticleMetaData(20, false, 0))
                        .add(this.level(), this.getX(this.random.nextDouble() * 3 - 1.5),
                                this.getY(this.random.nextDouble() * 3 - 1.5),
                                this.getZ(this.random.nextDouble() * 3 - 1.5));
            }
        }
        ++this.deathTime;
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.deathTime == 1) {
                GrailWarHandler handler = GrailWarHandler.get(serverLevel.getServer());
                if (handler.isParticipant(this) || (this.getLastDamageSource() != null && this.getLastDamageSource().is(FateDamageTypes.GRAIL))) {
                    handler.broadcastParticipants(Component.translatable("fateubw.chat.servant.death").withStyle(ChatFormatting.RED));
                }
                this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 1.0F);
                this.getAnimationHandler().setAnimation(this.getDeathAnimation());
            }
            if (this.getLastDamageSource() == null || !this.getLastDamageSource().is(FateDamageTypes.GRAIL)) {
                if (this.deathTime > 15 && this.deathTime % 5 == 0 && (this.lastHurtByPlayerTime > 0 || this.isAlwaysExperienceDropper()) && this.shouldDropExperience() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    int exp = this.xpReward;
                    int splitExp;
                    while (exp > 0) {
                        splitExp = ExperienceOrb.getExperienceValue(exp);
                        exp -= splitExp;
                        this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY(), this.getZ(), splitExp));
                    }
                }
            }
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (this.deathTime >= this.maxDeathTick() && (anim == null || anim.done(0))) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    @Override
    public int maxDeathTick() {
        return 200;
    }

    @Override
    public boolean shouldDropExperience() {
        if (this.getServer() != null && GrailWarHandler.get(this.getServer()).isParticipant(this))
            return false;
        return super.shouldDropExperience();
    }

    @Override
    protected void dropAllDeathLoot(ServerLevel level, DamageSource damageSource) {
        if (damageSource.is(FateDamageTypes.GRAIL) || (this.getServer() != null && GrailWarHandler.get(this.getServer()).isParticipant(this)))
            return;
        super.dropAllDeathLoot(level, damageSource);
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return SpawnEgg.fromType(this.getType()).map(ItemStack::new).orElse(null);
    }

    protected String getSummonAnimation() {
        return null;
    }

    public double getSummonProgress(float partialTicks) {
        String summon = this.getSummonAnimation();
        if (summon != null && this.getAnimationHandler().isCurrent(summon)) {
            return this.getAnimationHandler().getAnimation().progress(partialTicks);
        }
        return -1;
    }

    public Vector4f summonColor() {
        return SUMMON_COLOR;
    }

    @Override
    public EntityWeaponTrailHolder<BaseServant> getTrailHolder() {
        return this.trailHolder;
    }

    public enum AttackBehaviour {
        NORMAL,
        AGGRESSIVE,
        DEFENSIVE;

        @Nullable
        public static AttackBehaviour of(CommandType type) {
            return switch (type) {
                case NORMAL -> NORMAL;
                case AGGRESSIVE -> AGGRESSIVE;
                case DEFENSIVE -> DEFENSIVE;
                default -> null;
            };
        }
    }
}
