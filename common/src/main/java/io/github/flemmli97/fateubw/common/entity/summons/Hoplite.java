package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.SpearProjectile;
import io.github.flemmli97.fateubw.common.entity.utils.MoveStateTracker;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.entity.utils.TargetableOpponent;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetMoveToRestriction;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
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
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
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

public class Hoplite extends PathfinderMob implements AnimatedEntity, AOEAttackEntity, OwnableEntity, SmartBrainOwner<Hoplite>, TargetableOpponent {

    public static final ResourceLocation IRON_MODIFIER = Fate.modRes("iron_modifier");

    protected static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(Hoplite.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Boolean> HAS_SHIELD = SynchedEntityData.defineId(Hoplite.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> HAS_SPEAR = SynchedEntityData.defineId(Hoplite.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> BLOCKING = SynchedEntityData.defineId(Hoplite.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IRON = SynchedEntityData.defineId(Hoplite.class, EntityDataSerializers.BOOLEAN);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String STAB = BUILDER.add("stab", AnimationsBuilder.definition(1).marker("attack", 0.56));
    public static final String THROW = BUILDER.add("throw", AnimationsBuilder.definition(1.76).marker("throw", 0.8));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    public final Predicate<LivingEntity> targetPred = Utils.summonTargetPredicate(this);

    private UUID ownerUUID;
    private LivingEntity owner;

    private final AnimationHandler<Hoplite> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(this, 3, MOVE_FLAGS, this::calculateMoveType);

    private float shieldHealth;
    private int shieldCooldown, stopBlockingTick;
    private int spearRegen;

    public Hoplite(EntityType<? extends Hoplite> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.updateAttributes();
        }
        this.shieldHealth = this.getMaxHealth() * 0.4f;
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
        builder.define(HAS_SHIELD, true);
        builder.define(HAS_SPEAR, true);
        builder.define(BLOCKING, false);
        builder.define(IRON, false);
    }

    public boolean hasShield() {
        return this.entityData.get(HAS_SHIELD);
    }

    public void restoreShield() {
        this.entityData.set(HAS_SHIELD, true);
        this.playSound(SoundEvents.ANVIL_USE, 1, 1);
        this.shieldHealth = this.getMaxHealth() * 0.4f;
    }

    public boolean isBlockingShield() {
        return this.hasShield() && this.entityData.get(BLOCKING);
    }

    public void startBlockWithShield() {
        if (this.hasShield() && !this.isBlockingShield()) {
            this.entityData.set(BLOCKING, true);
        }
        this.stopBlockingTick = 100;
    }

    public boolean hasSpear() {
        return this.entityData.get(HAS_SPEAR);
    }

    public boolean hasIronArmor() {
        return this.entityData.get(IRON);
    }

    public void setIronArmor(boolean armor) {
        this.entityData.set(IRON, armor);
        if (!this.level().isClientSide) {
            this.getAttribute(Attributes.MAX_HEALTH)
                    .addTransientModifier(new AttributeModifier(IRON_MODIFIER, 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            this.getAttribute(Attributes.ARMOR)
                    .addTransientModifier(new AttributeModifier(IRON_MODIFIER, 1.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    public List<? extends ExtendedSensor<? extends Hoplite>> getSensors() {
        return List.of(new NearbyLivingEntitySensor<Hoplite>()
                        .setPredicate((target, entity) -> entity.getOwner() != null
                                && (entity.getOwner() instanceof Mob mob && mob.getTarget() == target || entity.getOwner().getLastAttacker() == target))
                        .setScanRate(e -> 10),
                new HurtBySensor<Hoplite>().setPredicate((source, entity) -> {
                    if (source.getEntity() instanceof LivingEntity attacker)
                        return !Utils.alliedTo(entity, attacker);
                    return true;
                }));
    }

    @Override
    public BrainActivityGroup<? extends Hoplite> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<Hoplite>(),
                this.lookBehaviour(),
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 100))
                        .whenStopping(m -> BrainUtils.clearMemory(m, MemoryModuleType.LOOK_TARGET)));
    }

    protected ExtendedBehaviour<? extends Hoplite> lookBehaviour() {
        return new AllApplicableBehaviours<>(
                new LookAtAttackTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomLookTarget<>().lookChance(ConstantFloat.of(1)),
                        new SetPlayerLookTarget<>()
                ).startCondition(m -> m.getRandom().nextFloat() < 0.1 && !BrainUtils.hasMemory(m, MemoryModuleType.WALK_TARGET))
        );
    }

    @Override
    public BrainActivityGroup<? extends Hoplite> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new MoveToWalkTarget<>(),
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<Hoplite>(),
                        new SetMoveToRestriction<Hoplite>(),
                        new SetRandomWalkTarget<>().startCondition(m -> m.getRandom().nextInt(120) == 0)
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends Hoplite> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<Hoplite>(),
                SelectableBehaviourBuilder.<Hoplite>builder()
                        .add(1, owner -> owner.hasShield() || owner.hasSpear(), new SetWalkTargetToAttackTarget<Hoplite>().closeEnoughDist(BehaviourUtils.closeEnough(2)), BehaviourUtils.moveTo())
                        .add(1, owner -> !owner.hasShield() && !owner.hasSpear(), new SetWalkTargetAwayFromTarget<Hoplite>()
                                .minDist(7).speedMod(1.5f), BehaviourUtils.moveTo())
                        .build().startCondition(owner -> !owner.hasSpear() || BehaviourUtils.runCooldownBehaviour(owner))
                        .stopIf(owner -> owner.hasSpear() && !BehaviourUtils.runCooldownBehaviour(owner)),
                AttackBehaviourBuilder.<Hoplite>create()
                        .start(STAB).play(BehaviourUtils.cooldownedPlay(false, 10, 25))
                        .prepare(new SetWalkTargetToAttackTarget<Hoplite>().closeEnoughDist(BehaviourUtils.closeEnough(2))).prepareOptional(BehaviourUtils.timedMoveAttack())
                        .end(1)
                        .start(THROW).play(BehaviourUtils.cooldownedPlay(false, 10, 25))
                        .condition(owner -> BehaviourUtils.ifFurtherThan(5).test(owner))
                        .prepare(new SetWalkTargetToAttackTarget<Hoplite>()
                                .closeEnoughDist(BehaviourUtils.closeEnough(15))).prepareOptional(BehaviourUtils.moveTo())
                        .end(1)
                        .build()
                        .startCondition(owner -> owner.hasSpear() && BehaviourUtils.runCombatBehaviour(owner))
        );
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
    public void aiStep() {
        super.aiStep();
        this.moveStateTracker.tick();
        this.getAnimationHandler().tick();
        if (!this.level().isClientSide) {
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
            this.shieldCooldown = Math.max(-1, --this.shieldCooldown);
            if (this.shieldCooldown == 0) {
                this.restoreShield();
            }
            if (this.getTarget() == null || !this.getTarget().isAlive()) {
                this.stopBlockingTick = Math.max(-1, --this.stopBlockingTick);
            }
            if (this.stopBlockingTick == 0) {
                this.entityData.set(BLOCKING, false);
            }
            this.spearRegen = Math.max(-1, --this.spearRegen);
            if (this.spearRegen == 0) {
                this.entityData.set(HAS_SPEAR, true);
                this.playSound(SoundEvents.ANVIL_USE, 1, 1);
            }
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
        if (this.isImmobile() || !this.walkAnimation.isMoving()) {
            return MoveType.NONE;
        }
        double d0 = this.getMoveControl().getSpeedModifier();
        MoveType move;
        if (d0 > 1 && this.getTarget() == null) {
            move = MoveType.RUN;
        } else if (d0 <= 0.8) {
            move = MoveType.SNEAK;
        } else {
            move = MoveType.WALK;
        }
        return move;
    }

    public void handleAttack(AnimationState anim) {
        if (anim.is(THROW)) {
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.lookAt(this.getTarget(), 60, 30);
            }
            if (anim.isAt("throw")) {
                this.throwSpear();
            }
        } else if (anim.is(STAB)) {
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.lookAt(this.getTarget(), 60, 30);
            }
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
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
        double width = this.getBbWidth() * 1.25;
        double length = this.getBbWidth() * 5;
        AABB aabb = new AABB(-width * 0.7, -0.02, 0, width * 0.3, this.getBbHeight() + 0.02, length)
                .inflate(grow);
        return new OrientedBoundingBox(aabb, this.getYHeadRot(), 0, this.position());
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        this.startBlockWithShield();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!source.is(DamageTypeTags.BYPASSES_SHIELD) && this.isBlockingShield()) {
            amount *= 0.5f;
            this.shieldHealth -= Math.clamp(amount, 0.0f, 5.0f);
            if (this.shieldHealth <= 0.0f) {
                this.playSound(SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, 1, 1);
                this.entityData.set(HAS_SHIELD, false);
                this.entityData.set(BLOCKING, false);
                this.shieldCooldown = 100;
            } else {
                this.playSound(SoundEvents.SHIELD_BLOCK, 1, 1);
            }
        }
        this.startBlockWithShield();
        return super.hurt(source, amount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Owner"))
            this.ownerUUID = tag.getUUID("Owner");
        this.entityData.set(HAS_SHIELD, tag.getBoolean("HasShield"));
        this.entityData.set(HAS_SPEAR, tag.getBoolean("HasSpear"));
        this.setIronArmor(tag.getBoolean("HasIronArmor"));
        this.shieldHealth = tag.getInt("ShieldHealth");
        this.shieldCooldown = tag.getInt("ShieldCooldown");
        this.stopBlockingTick = tag.getInt("StopBlockingTick");
        this.spearRegen = tag.getInt("SpearRegen");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUUID != null)
            tag.putUUID("Owner", this.ownerUUID);
        tag.putBoolean("HasShield", this.hasShield());
        tag.putBoolean("HasSpear", this.hasSpear());
        tag.putBoolean("HasIronArmor", this.hasIronArmor());
        tag.putFloat("ShieldHealth", this.shieldHealth);
        tag.putInt("ShieldCooldown", this.shieldCooldown);
        tag.putInt("StopBlockingTick", this.stopBlockingTick);
        tag.putInt("SpearRegen", this.spearRegen);
    }

    @Override
    public AnimationHandler<Hoplite> getAnimationHandler() {
        return this.animationHandler;
    }

    public void throwSpear() {
        if (!this.hasSpear())
            return;
        this.entityData.set(HAS_SPEAR, false);
        this.spearRegen = 100;
        SpearProjectile projectile = new SpearProjectile(this.level(), this);
        Vec3 side = this.calculateViewVector(0, this.getViewYRot(1) + 90).scale(0.4);
        projectile.setPos(this.getX() + side.x(), this.getY() + this.getEyeHeight() - 0.1, this.getZ() + side.z());
        if (this.getTarget() != null) {
            Vec3 pos = this.getTarget().position();
            projectile.shootAtPosition(pos.x(), this.getTarget().getY(1), pos.z(), 1.3f, 0);
        } else {
            projectile.shootFromRotation(this, this.getViewXRot(1) - 15, this.getViewYRot(1), 0.0F, 1.3f, 0);
        }
        this.playSound(SoundEvents.TRIDENT_THROW.value(), 1, 1);
        this.level().addFreshEntity(projectile);
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
