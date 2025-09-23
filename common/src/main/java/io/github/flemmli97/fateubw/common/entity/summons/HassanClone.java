package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.MoveBehindBehaviour;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.fateubw.common.entity.servant.Hassan;
import io.github.flemmli97.fateubw.common.entity.utils.MoveStateTracker;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.entity.utils.ServantModelLike;
import io.github.flemmli97.fateubw.common.entity.utils.TargetableOpponent;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailHolder;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailHolderProvider;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetMoveToRestriction;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedMobDataHandler;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.InteractWithDoor;
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
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HassanClone extends PathfinderMob implements AnimatedEntity, OwnableEntity, AOEAttackEntity, TargetableOpponent,
        ServantModelLike, SmartBrainOwner<HassanClone>, SyncedMobDataHandler, EntityWeaponTrailHolderProvider {

    public static final ResourceLocation BACKSTAB_MODIFIER = Fate.modRes("hassan_backstab");

    protected static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(HassanClone.class, EntityDataSerializers.BYTE);

    public static final AnimationDefinitionContainer ANIMS = Hassan.BUILDER.build();

    private UUID ownerUUID;
    private Hassan owner;

    public final Predicate<LivingEntity> targetPred = Utils.servantTargetPredicate(this);

    private final AnimationHandler<HassanClone> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (anim != null && this.getTarget() != null)
            this.targetPosition = this.getTarget().position();
        if (anim == null || !anim.is(Hassan.SUMMON)) {
            if (!this.offHandCache.isEmpty()) {
                this.setItemInHand(InteractionHand.OFF_HAND, this.offHandCache);
                this.offHandCache = ItemStack.EMPTY;
            }
            if (!this.mainHandCache.isEmpty()) {
                this.setItemInHand(InteractionHand.MAIN_HAND, this.mainHandCache);
                this.mainHandCache = ItemStack.EMPTY;
            }
        }
        return false;
    });

    private final SyncedDataContainer<HassanClone> syncedDataContainer;
    private final MoveStateTracker moveStateTracker = new MoveStateTracker(BaseServant.MOVE_TICK_MAX, this::getMoveFlag);

    protected Vec3 targetPosition;

    private ItemStack mainHandCache = ItemStack.EMPTY;
    private ItemStack offHandCache = ItemStack.EMPTY;

    private final EntityWeaponTrailHolder<HassanClone> trailHolder = new EntityWeaponTrailHolder<>(this);

    public HassanClone(EntityType<? extends HassanClone> type, Level level) {
        super(type, level);
        SyncedDataContainer.Builder<HassanClone> builder = SyncedDataContainer.builder(this);
        this.definedAdditinoalSyncedData(builder);
        this.syncedDataContainer = builder.build();
        if (!level.isClientSide) {
            this.updateAttributes();
        }
    }

    public HassanClone(Level level, Hassan entityHassan) {
        this(FateEntities.HASSAN_COPY.get(), level);
        this.setOriginal(entityHassan);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOVE_FLAGS, (byte) 0);
    }

    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<HassanClone> builder) {
        builder.define(BaseServant.TARGET_POSITION, TenshilibSyncableEntityDatas.TARGET_POS.get(), null);
    }

    @Override
    public SyncedDataContainer<?> getDataContainer() {
        return this.syncedDataContainer;
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
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.ASSASSIN_DAGGER.get()));
    }

    @Override
    public List<? extends ExtendedSensor<? extends HassanClone>> getSensors() {
        return List.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<HassanClone>()
                        .setPredicate((target, entity) -> entity.getOwner() != null && entity.getOwner().getTarget() == target)
                        .setScanRate(e -> 10),
                new HurtBySensor<HassanClone>().setPredicate((source, entity) -> {
                    if (source.getEntity() instanceof LivingEntity attacker)
                        return !Utils.alliedTo(entity, attacker);
                    return true;
                }));
    }

    @Override
    public BrainActivityGroup<? extends HassanClone> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<HassanClone>(),
                new InteractWithDoor<>(),
                new FollowEntity<HassanClone, Hassan>()
                        .following(HassanClone::getOwner)
                        .teleportToTargetAfter(17)
                        .stopFollowingWithin(6)
                        .speedMod(1.1f),
                this.lookBehaviour(),
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 100))
                        .whenStopping(m -> BrainUtils.clearMemory(m, MemoryModuleType.LOOK_TARGET)));
    }

    protected ExtendedBehaviour<? extends HassanClone> lookBehaviour() {
        return new AllApplicableBehaviours<HassanClone>(
                new LookAtAttackTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomLookTarget<>().lookChance(ConstantFloat.of(1)),
                        new SetPlayerLookTarget<>()
                ).startCondition(m -> m.getRandom().nextFloat() < 0.1 && !BrainUtils.hasMemory(m, MemoryModuleType.WALK_TARGET))
        ).startCondition(e -> !BrainUtils.hasMemory(e, MemoryModuleType.ATTACK_TARGET) && !e.isSleeping()
                && !e.getAnimationHandler().hasAnimation());
    }

    @Override
    public BrainActivityGroup<? extends HassanClone> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new MoveToWalkTarget<>(),
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<HassanClone>(),
                        new SetMoveToRestriction<HassanClone>(),
                        new SetRandomWalkTarget<>().startCondition(m -> m.getRandom().nextInt(120) == 0)
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<? extends HassanClone> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<HassanClone>(),
                new FirstApplicableBehaviour<>(
                        (ExtendedBehaviour<HassanClone>) this.getCooldownAI()
                                .startCondition(HassanClone::runCooldownBehaviour)
                                .stopIf(e -> !e.runCooldownBehaviour()),
                        (ExtendedBehaviour<HassanClone>) this.getCombatAI()
                ).startCondition(m -> m.getTarget() != null && m.isWithinRestriction(m.getTarget().blockPosition()))
        );
    }

    public ExtendedBehaviour<? extends HassanClone> getCombatAI() {
        return AttackBehaviourBuilder.<HassanClone>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<HassanClone>builder(Hassan.DAGGER_1)
                        .start(Hassan.DAGGER_3, 2, 0.2f, 1)
                        .start(Hassan.DAGGER_4, 2, 0.16f, 1)
                        .chainChance(0.6f).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BehaviourUtils.of(AnimationPlayHolder.<HassanClone>builder(Hassan.DAGGER_1)
                        .start(Hassan.DAGGER_3, 2, 0.2f, 1)
                        .start(Hassan.DAGGER_4, 2, 0.16f, 1)
                        .chainChance(0.6f).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new MoveBehindBehaviour<>())
                .end(7)
                .start(BehaviourUtils.of(AnimationPlayHolder.<HassanClone>builder(Hassan.DAGGER_3)
                        .start(Hassan.DAGGER_1, 2, 0.2f, 1)
                        .chain(Hassan.DAGGER_2, 2, 0.2f)
                        .start(Hassan.DAGGER_1, 2, 0.2f, 1)
                        .chain(Hassan.DAGGER_4, 2, 0.16f)
                        .chainChance(0.6f).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(Hassan.TOP_STAB).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new SetWalkTargetToAttackTarget<HassanClone>().speedMod((e, t) -> 1.2f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(9)
                .start(Hassan.TOP_STAB).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new MoveBehindBehaviour<HassanClone>().speedMod(1.2f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(11)
                .start(Hassan.THROW).play(BehaviourUtils.cooldownedPlay(false, 15, 26))
                .prepare(new SetWalkTargetWithinDist<HassanClone>()
                        .min(7).max(14).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(9)
                .start(Hassan.THROW).play(BehaviourUtils.cooldownedPlay(false, 15, 26))
                .condition(entity -> {
                    if (BehaviourUtils.ifFurtherThan(8).test(entity))
                        return true;
                    LivingEntity target = BrainUtils.getTargetOfEntity(entity);
                    return target != null && target.getY() - entity.getY() > 4;
                })
                .prepare(new SetWalkTargetWithinDist<HassanClone>()
                        .min(7).max(14).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(11)
                .build();
    }

    public ExtendedBehaviour<? extends HassanClone> getCooldownAI() {
        return SelectableBehaviourBuilder.<HassanClone>builder()
                .add(6, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(3, new SetWalkTargetAwayFromTarget<HassanClone>()
                        .radius(7), BehaviourUtils.moveTo()).build();
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

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            if (this.tickCount > 200 && this.isAlive() && (this.getOwner() == null || !this.getOwner().isAlive())) {
                this.hurt(this.damageSources().genericKill(), Integer.MAX_VALUE);
                return;
            }
            if (this.getTarget() == null) {
                if (this.getFirstPassenger() instanceof Mob mob) {
                    if (mob.getTarget() != this.getTarget())
                        this.setTarget(mob.getTarget());
                }
            }
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
        }
        super.tick();
        this.getAnimationHandler().tick();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityWeaponTrailProvider.TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityWeaponTrailProvider.EntityTrailData.create(this, anim.getID(), false))
                                            .setColor(68 / 255f, 68 / 255f, 68 / 255f, 0.6f)
                                            .setColor2(68 / 255f, 68 / 255f, 68 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.tickBrain(this);
        if (!(this.getControllingPassenger() instanceof Player) && this.isMoving() && this.isAlive()) {
            double d0 = this.getMoveControl().getSpeedModifier();
            MoveType move;
            if (d0 > 1) {
                move = MoveType.RUN;
            } else if (d0 <= 0.8) {
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

    protected boolean isMoving() {
        return this.getDeltaMovement().x != 0 || this.getDeltaMovement().z != 0;
    }

    @Override
    public float interpolatedMoveTick(float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTick(partialTicks);
    }

    @Override
    public float interpolatedMoveTickOf(MoveType moveType, float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTickOf(moveType, partialTicks);
    }

    public void setMovingFlag(MoveType type) {
        this.entityData.set(MOVE_FLAGS, (byte) type.ordinal());
    }

    public MoveType getMoveFlag() {
        return MoveType.values()[this.entityData.get(MOVE_FLAGS)];
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUUID != null)
            tag.putUUID("Owner", this.ownerUUID);
        tag.put("MainHandCache", this.mainHandCache.save(this.registryAccess(), new CompoundTag()));
        tag.put("OffHandCache", this.offHandCache.save(this.registryAccess(), new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner"))
            this.ownerUUID = tag.getUUID("Owner");
        this.mainHandCache = ItemStack.parseOptional(this.registryAccess(), tag.getCompound("MainHandCache"));
        this.offHandCache = ItemStack.parseOptional(this.registryAccess(), tag.getCompound("OffHandCache"));
    }

    public void handleAttack(AnimationState anim) {
        if (anim.is(Hassan.THROW)) {
            if (anim.isAt("attack")) {
                this.throwItem(true);
            } else if (anim.isAt(0.84)) {
                this.throwItem(false);
            }
        } else {
            if (anim.is(Hassan.SUMMON))
                return;
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.lookAt(this.getTarget(), 60, 90);
            }
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.targetPosition = null;
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, this::runHurtTarget, 0);
    }

    private boolean runHurtTarget(Entity entity) {
        if (entity instanceof Mob) {
            LivingEntity target = ((Mob) entity).getTarget();
            if (target == this.getOwner())
                ((Mob) entity).setTarget(this);
        }
        boolean behind = Hassan.behind(this, entity);
        if (behind) {
            this.getAttribute(Attributes.ATTACK_DAMAGE)
                    .addTransientModifier(new AttributeModifier(HassanClone.BACKSTAB_MODIFIER, 0.5,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        boolean hurt = super.doHurtTarget(entity);
        if (behind) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(HassanClone.BACKSTAB_MODIFIER);
            if (hurt) {
                this.level().playSound(null, this, SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 0.7f, 0.9f);
                if (this.level() instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 15; i++)
                        serverLevel.sendParticles(DustParticleOptions.REDSTONE, entity.getRandomX(1.4), entity.getRandomY(), entity.getRandomZ(1.4), 0, 0, 0, 0, 0);
                }
            }
        }
        return hurt;
    }

    public void mobAttack(AnimationState anim, LivingEntity target, Consumer<LivingEntity> cons) {
        OrientedBoundingBox obb = this.calculateAttackAABB(anim, this.targetPosition != null || target == null ? this.targetPosition : target.position(), 0.2);
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
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(Hassan.DAGGER_1)) {
            width += 0.5;
            length += 0.4;
        }
        if (anim.is(Hassan.DAGGER_2, Hassan.DAGGER_3)) {
            width += 0.7;
            length += 0.4;
        }
        if (anim.is(Hassan.DAGGER_4)) {
            width += 0.3;
            length += 0.8;
        }
        if (anim.is(Hassan.TOP_STAB)) {
            width += 0.2;
            length += 0.8;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public AnimationHandler<HassanClone> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Predicate<LivingEntity> validTargetPredicate() {
        return this.targetPred;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(damageSource, damage);
        } else {
            if (damageSource.getEntity() == null || !damageSource.getEntity().getType().is(FateTags.EntityTypes.STRONG_MOB))
                damage *= 0.75;
            if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR) && this.projectileBlockChance()) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 1, 1);
                if (damageSource.getDirectEntity() != null)
                    damageSource.getDirectEntity().remove(RemovalReason.KILLED);
                return false;
            }
            return super.hurt(damageSource, Math.min(50, damage));
        }
    }

    public boolean projectileBlockChance() {
        return this.random.nextFloat() < (float) this.getAttributeValue(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder());
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public Hassan getOwner() {
        if ((this.owner == null || !this.owner.isAlive()) && this.getOwnerUUID() != null)
            this.owner = EntityUtils.findFromUUID(Hassan.class, this.level(), this.getOwnerUUID());
        return this.owner;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData data) {
        super.finalizeSpawn(world, difficulty, reason, data);
        this.populateDefaultEquipmentSlots(this.getRandom(), difficulty);
        for (EquipmentSlot type : EquipmentSlot.values())
            this.setDropChance(type, 0);
        if (reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.MOB_SUMMONED) {
            this.getAnimationHandler().setAnimation(Hassan.SUMMON);
        }
        return data;
    }

    public void setOriginal(Hassan entityHassan) {
        this.ownerUUID = entityHassan.getUUID();
        this.owner = entityHassan;
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
                        .build().add(this.level(), this.getX(this.random.nextDouble() * 3 - 1.5),
                                this.getY(this.random.nextDouble() * 3 - 1.5),
                                this.getZ(this.random.nextDouble() * 3 - 1.5));
            }
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            ++this.deathTime;
            if (this.deathTime == 1) {
                serverLevel.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.servant.death").withStyle(ChatFormatting.RED), true);
                this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 1.0F);
            }
            if (this.deathTime == this.maxDeathTick()) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    @Override
    public int maxDeathTick() {
        return 200;
    }

    public void throwItem(boolean main) {
        ThrownItemEntity item = new ThrownItemEntity(this.level(), this);
        item.setWeapon(this.getWeaponToThrowAndReplace(main));
        if (this.getTarget() != null) {
            item.shootAtEntity(this.getTarget(), 1.2f, 7 - this.level().getDifficulty().getId() * 2);
        } else {
            item.shootFromRotation(this, this.getXRot() + 5, this.getYRot(), 0.0F, 1.2f, 1.0F);
        }
        this.playSound(SoundEvents.FISHING_BOBBER_THROW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(item);
    }

    private ItemStack getWeaponToThrowAndReplace(boolean main) {
        ItemStack weapon;
        if (!main) {
            if (!this.getOffhandItem().isEmpty()) {
                this.offHandCache = this.getOffhandItem();
                this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                weapon = this.offHandCache;
            } else
                weapon = this.getMainHandItem().isEmpty() ? this.mainHandCache.copy() : this.getMainHandItem();
        } else {
            this.mainHandCache = this.getMainHandItem();
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            weapon = this.mainHandCache;
        }
        return weapon.isEmpty() ? new ItemStack(FateItems.ASSASSIN_DAGGER.get()) : weapon.copy();
    }

    @Override
    public EntityWeaponTrailHolder<?> getTrailHolder() {
        return this.trailHolder;
    }

    @Override
    public WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector4f(0, 0, -0.2f, 1), new Vector4f(0, 0, -0.6f, 1));
    }
}