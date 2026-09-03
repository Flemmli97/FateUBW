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
import io.github.flemmli97.fateubw.common.particles.BlockStateParticleData;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.MultiPartEntity;
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
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
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
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GordiusWheel extends PathfinderMob implements AnimatedEntity, StandingVehicle, AOEAttackEntity, SmartBrainOwner<GordiusWheel>, SyncedMobDataHandler {

    private static final float ATTACK_MOVE_SPEED = 1.5f;

    public static final byte SHOCK_WAVE = 66;
    public static final int SHOCK_WAVE_PARTICLE_RANGE = 5;

    private static final EntityDataAccessor<Integer> WHEEL = SynchedEntityData.defineId(GordiusWheel.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(GordiusWheel.class, EntityDataSerializers.BYTE);
    public static final TypedResource<Vec3> CHARGE_MOTION = new TypedResource<>(Fate.modRes("charge_motion"));

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String STOMP = BUILDER.add("stomp", AnimationsBuilder.definition(0.96)
            .marker("attack", 0.52).marker("sparks", 0.72)
            .marker("lightning", 0.82));
    public static final String EXPUGNATIO = BUILDER.add("expugnatio", AnimationsBuilder.definition(3.12)
            .marker("charge_start", 0.72).marker("charge_end", 2.4));
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

    private final AnimationHandler<GordiusWheel> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide && anim != null && anim.is(EXPUGNATIO)) {
                    this.setChargeMotion(null);
                }
                return false;
            });

    private final SyncedDataContainer<GordiusWheel> syncedDataContainer = SyncedDataContainer.builder(this)
            .define(CHARGE_MOTION, TenshilibSyncableEntityDatas.VEC_3.get(), null).build();

    private MultiPartEntity wheels;

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(this, 2, MOVE_FLAGS, this::calculateMoveType);

    private boolean lightning;

    private int shockWaveTick;

    public float wheelMoveTick;
    public float wheelPartial;

    public GordiusWheel(EntityType<? extends GordiusWheel> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.updateAttributes();
        }
        this.lookControl = new GordiusLookControl(this);
        this.moveControl = new GordiusMoveControl(this);
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
        builder.define(WHEEL, -1);
        builder.define(MOVE_FLAGS, (byte) 0);
    }

    @Override
    public SyncedDataContainer<?> getDataContainer() {
        return this.syncedDataContainer;
    }

    @Override
    public List<? extends ExtendedSensor<? extends GordiusWheel>> getSensors() {
        return List.of();
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    public BrainActivityGroup<? extends GordiusWheel> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<GordiusWheel>(),
                new SetTargetFromRider<>());
    }

    @Override
    public BrainActivityGroup<? extends GordiusWheel> getIdleTasks() {
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
    public BrainActivityGroup<? extends GordiusWheel> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<GordiusWheel>(),
                SelectableBehaviourBuilder.<GordiusWheel>builder()
                        .add(1, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                        .build().startCondition(BehaviourUtils::runCooldownBehaviour)
                        .stopIf(e -> !BehaviourUtils.runCooldownBehaviour(e)),
                AttackBehaviourBuilder.<GordiusWheel>create()
                        .start(STOMP).play(BehaviourUtils.cooldownedPlay(true, 10, 40))
                        .prepare(new SetWalkTargetToAttackTarget<GordiusWheel>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.moveAttack())
                        .end(1)
                        .start(EXPUGNATIO).play(BehaviourUtils.cooldownedPlay(false, 10, 40))
                        .prepare(new SetChargeTarget())
                        .end(1)
                        .build()
                        .startCondition(BehaviourUtils::runCombatBehaviour)
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
    public void baseTick() {
        super.baseTick();
        this.getAnimationHandler().tick();
        if (!this.level().isClientSide) {
            if (this.wheels == null) {
                this.wheels = new GordiusChariot(this, 2.2f, 1.6f);
            }
            if (this.wheels.parentTick()) {
                this.entityData.set(WHEEL, this.wheels.getId());
            }
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
            if (this.getTarget() == null) {
                if (this.getFirstPassenger() instanceof Mob mob) {
                    if (mob.getTarget() != this.getTarget())
                        this.setTarget(mob.getTarget());
                }
            }
        } else {
            if (this.shockWaveTick > 0) {
                double range = SHOCK_WAVE_PARTICLE_RANGE - Math.min(this.shockWaveTick, SHOCK_WAVE_PARTICLE_RANGE);
                this.sendShockWaveParticles(range);
                this.shockWaveTick--;
            }
        }
        this.moveStateTracker.tick();
        this.wheelMoveTick += Mth.clamp(this.walkAnimation.speed(1) / 0.25f, 0, 1);
        Vec3 lookDir = this.directionToLookAt();
        if (lookDir != null) {
            float[] yxRot = MathsHelper.YXRotFrom(lookDir);
            this.setYRot(MathsHelper.rotlerp(this.getYRot(), yxRot[0], 30));
            this.setXRot(MathsHelper.rotlerp(this.getXRot(), yxRot[1], 30));
            this.setYBodyRot(this.getYRot());
            this.setYHeadRot(this.getYRot());
        }
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return false;
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
    }

    public float interpolatedMoveTick(float partialTick) {
        return this.moveStateTracker.interpolatedMoveTick(partialTick);
    }

    public float interpolatedMoveTickOf(MoveType moveType, float partialTick) {
        return this.moveStateTracker.interpolatedMoveTickOf(moveType, partialTick);
    }

    public MoveType getMoveType() {
        return MoveType.values()[this.entityData.get(MOVE_FLAGS)];
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

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            Entity wheel = this.getWheelEntity();
            if (wheel != null)
                return wheel.getPassengerRidingPosition(passenger);
        }
        return super.getPassengerRidingPosition(passenger);
    }

    @Nullable
    public MultiPartEntity getWheelEntity() {
        if (this.level().isClientSide && this.wheels == null) {
            Entity entity = this.level().getEntity(this.entityData.get(WHEEL));
            if (entity instanceof GordiusChariot part && part.getOwner() == this) {
                this.wheels = part;
            }
        }
        return this.wheels;
    }

    public Vec3 getWheelJoint() {
        Vec3 offset = new Vec3(0, 0, -0.9);
        offset.scale(this.getScale());
        return this.position().add(offset.yRot(-this.getYRot() * Mth.DEG_TO_RAD));
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

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && this.isCharging())
            damage *= 0.5f;
        return super.hurt(damageSource, damage);
    }

    public void handleAttack(AnimationState anim) {
        if (anim.is(EXPUGNATIO)) {
            if (anim.isPast("charge_start") && !anim.isPast("charge_end")) {
                Vec3 dir = this.getChargeMotion();
                if (dir == null) {
                    dir = this.calculateViewVector(0, this.getViewYRot(1));
                    this.setChargeMotion(dir);
                }
                this.setDeltaMovement(dir.x(), this.getDeltaMovement().y(), dir.z());
                OrientedBoundingBox obb = this.prepareAttackBox(anim.getAnimation(), null, 0.2, false);
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                        entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()));
                this.lightning = true;
                for (LivingEntity entity : list) {
                    this.doHurtTarget(entity);
                }
                this.lightning = false;
                this.playSound(FateSounds.GORDIUS_STEP.get(), 1, 1);
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
                S2CScreenShake.sendAround(this, 14, 4, 2f);
                for (int i = 0; i < 6; i++) {
                    AdvancedParticleContainer.make(FateParticles.LIGHTNING_SPARK.get())
                            .addData(new ScaleData(0.3f + this.getRandom().nextFloat() * 0.2f))
                            .addData(new ColorData(42 / 255f, 151 / 255f, 255 / 255f, 1))
                            .addData(new ParticleMetaData(5, false, 0))
                            .add(this.level(), this.getRandomX(1.3), this.getY(this.getRandom().nextDouble() * 1.3), this.getRandomZ(1.3));
                }
            }
        } else if (anim.is(STOMP)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                S2CScreenShake.sendAround(this, 6, 8, 2);
                this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.8f);
                this.level().broadcastEntityEvent(this, SHOCK_WAVE);
            }
            if (anim.isAt("sparks")) {
                Vec3 dir = new Vec3(0, 0, this.getBbWidth() * 1.75);
                for (int i = 0; i < 8; i++) {
                    Vec3 off = dir.yRot(i * 45 * Mth.DEG_TO_RAD);
                    AdvancedParticleContainer.make(FateParticles.LIGHTNING.get())
                            .addData(new ScaleData(1))
                            .addData(new ColorData(42 / 255f, 151 / 255f, 255 / 255f, 1))
                            .addData(new ParticleMetaData(15, false, 0))
                            .add(this.level(), this.getX() + off.x(), this.getY() + off.y(), this.getZ() + off.z());
                }
                this.playSound(FateSounds.ZAP.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0f);
            }
            if (anim.isAt("lightning")) {
                this.lightning = true;
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.lightning = false;
            }
        }
    }

    private void sendShockWaveParticles(double range) {
        int amount = Mth.ceil(1.2 * 360 * Math.ceil(range) / 90);
        Vec3 dir = new Vec3(0, 0, 1);
        HashSet<BlockPos> visited = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            float angle = 360 * ((float) i / amount) - 0.5f;
            Vec3 target = dir.scale(range).yRot(angle * Mth.DEG_TO_RAD);
            target = this.position().add(target.x, -1, target.z);
            BlockPos pos = BlockPos.containing(target);
            if (visited.contains(pos))
                continue;
            visited.add(pos);
            BlockState state = this.level().getBlockState(pos);
            this.level().addParticle(new BlockStateParticleData(FateParticles.BLOCK.get(), state, this.random.nextFloat() * 360, this.random.nextFloat() * 10, 30),
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, this.random.nextDouble() * 0.05 + 0.03, 0);
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
        if (anim.is(EXPUGNATIO)) {
            AABB aabb2 = this.getBoundingBox().minmax(this.wheels.getBoundingBox());
            double lenX = aabb2.getXsize();
            double lenZ = aabb2.getZsize();
            double expand = 0.8 + grow;
            double len = Math.sqrt(lenX * lenX + lenZ * lenZ) + 2 * expand;
            double width = Math.min(lenX, lenZ) * 0.5 + expand;
            double offset = this.wheels.getBbWidth() * 0.5 + expand;
            AABB aabb = new AABB(-width, -0.02, -offset, width, this.getBbHeight(), len - offset);
            return new OrientedBoundingBox(aabb, this.getYRot(), 0, this.wheels.position());
        }
        double width = this.getBbWidth() * (this.lightning ? 4.5 : 3.5);
        AABB aabb = new AABB(-width * 0.5, -0.02, -width * 0.5, width * 0.5, this.getBbHeight(), width * 0.5);
        return new OrientedBoundingBox(aabb, this.getYRot(), 0, this.position());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, this::mobHurtTarget, this.getAnimationHandler().isCurrent(EXPUGNATIO) ? 4 : 0);
    }

    protected boolean mobHurtTarget(Entity target) {
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        DamageSource damageSource;
        if (this.getAnimationHandler().isCurrent(EXPUGNATIO)) {
            damageSource = FateDamageTypes.direct(FateDamageTypes.GORDIUS_TRAMPLE, this);
        } else if (this.lightning) {
            damageSource = FateDamageTypes.direct(FateDamageTypes.LIGHTNING_STRIKE, this);
        } else {
            damageSource = this.damageSources().mobAttack(this);
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), target, damageSource, damage);
        }
        if (this.lightning) {
            damage *= 1.5f;
        }
        boolean result = target.hurt(damageSource, damage);
        if (result) {
            if (this.getAnimationHandler().isCurrent(EXPUGNATIO)) {
                Vec3 dir = new Vec3(this.getDeltaMovement().x(), 0, this.getDeltaMovement().z()).normalize().scale(0.5);
                target.setDeltaMovement(target.getDeltaMovement().add(dir));
            } else {
                float knockback = this.getKnockback(target, damageSource);
                if (knockback > 0 && target instanceof LivingEntity livingEntity) {
                    livingEntity.knockback(knockback * 0.5, Mth.sin(this.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(this.getYRot() * Mth.DEG_TO_RAD));
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1, 0.6));
                }
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource);
            }
            this.setLastHurtMob(target);
            this.playAttackSound();
        }
        return result;
    }

    @Override
    public AnimationHandler<GordiusWheel> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == SHOCK_WAVE) {
            this.shockWaveTick = SHOCK_WAVE_PARTICLE_RANGE;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public Vec3 getChargeMotion() {
        return this.getDataContainer().get(CHARGE_MOTION);
    }

    public void setChargeTo(Vec3 pos) {
        Vec3 dir = pos.subtract(this.position());
        dir = new Vec3(dir.x(), 0, dir.z());
        this.setChargeMotion(dir.normalize().scale(0.55));
    }

    public void setChargeMotion(Vec3 chargeMotion) {
        this.getDataContainer().set(CHARGE_MOTION, chargeMotion);
    }

    public boolean isCharging() {
        if (this.getAnimationHandler() == null)
            return false;
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && anim.is(EXPUGNATIO) && anim.isPast("attack");
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.animationHandler.isCurrent(EXPUGNATIO);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return FateSounds.GORDIUS_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return FateSounds.GORDIUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return FateSounds.GORDIUS_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        if (this.getFirstPassenger() instanceof OwnableEntity ownable) {
            return entity != ownable.getOwner();
        }
        return super.canAttack(entity);
    }

    public static class SetChargeTarget extends ExtendedBehaviour<GordiusWheel> {

        private static final MemoryTest MEMORIES = MemoryTest.builder(1)
                .hasMemories(MemoryModuleType.ATTACK_TARGET);

        private Vec3 targetPos;

        @Override
        protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
            return MEMORIES;
        }

        @Override
        protected boolean shouldKeepRunning(GordiusWheel entity) {
            double dX = this.targetPos.x() - entity.getX();
            double dZ = this.targetPos.z() - entity.getZ();
            float yRot = MathsHelper.YRotFrom(dX, dZ);
            float diffY = Mth.degreesDifference(entity.getYRot(), yRot);
            if (Math.abs(diffY) < 16) {
                entity.setChargeTo(this.targetPos);
                return false;
            }
            return true;
        }

        @Override
        protected void start(GordiusWheel entity) {
            this.targetPos = BrainUtils.getTargetOfEntity(entity).getEyePosition();
        }

        @Override
        protected void tick(GordiusWheel entity) {
            super.tick(entity);
            LivingEntity target = BrainUtils.getTargetOfEntity(entity);
            if (target != null) {
                this.targetPos = target.getEyePosition();
            }
            double dY = this.targetPos.y() - entity.getEyeY();
            double dX = this.targetPos.x() - entity.getX();
            double dZ = this.targetPos.z() - entity.getZ();
            float[] yXRot = MathsHelper.YXRotFrom(dX, dY, dZ);
            entity.setYRot(MathsHelper.rotlerp(entity.getYRot(), yXRot[0], 17));
            entity.setXRot(MathsHelper.rotlerp(entity.getXRot(), yXRot[1], 30));
            entity.setYBodyRot(entity.getYRot());
            entity.setYHeadRot(entity.getYRot());
        }
    }

    protected class GordiusMoveControl extends MoveControl {

        public GordiusMoveControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                this.operation = Operation.WAIT;
                double dX = this.wantedX - GordiusWheel.this.getX();
                double dY = this.wantedY - GordiusWheel.this.getY();
                double dZ = this.wantedZ - GordiusWheel.this.getZ();
                double len = dX * dX + dY * dY + dZ * dZ;
                if (len < 2.5E-7) {
                    this.mob.setZza(0.0f);
                    return;
                }
                float n = (float) (Mth.atan2(dZ, dX) * Mth.RAD_TO_DEG) - 90;
                float rotY = this.rotlerp(this.mob.getYRot(), n, 30.0f);
                this.mob.setYRot(rotY);
                float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                speed = Mth.degreesDifferenceAbs(rotY, this.mob.getYRot()) > 10 ? speed * 0.5f : speed;
                this.mob.setSpeed(speed);
                BlockPos blockPos = this.mob.blockPosition();
                BlockState blockState = this.mob.level().getBlockState(blockPos);
                VoxelShape voxelShape = blockState.getCollisionShape(this.mob.level(), blockPos);
                if (dY > this.mob.maxUpStep() && dX * dX + dZ * dZ < Math.max(1.0f, this.mob.getBbWidth()) || !voxelShape.isEmpty() && this.mob.getY() < voxelShape.max(Direction.Axis.Y) + blockPos.getY() && !blockState.is(BlockTags.DOORS) && !blockState.is(BlockTags.FENCES)) {
                    this.mob.getJumpControl().jump();
                    this.operation = Operation.JUMPING;
                }
            } else
                super.tick();
        }
    }

    protected static class GordiusLookControl extends LookControl {

        public GordiusLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void setLookAt(double x, double y, double z, float deltaYaw, float deltaPitch) {
            super.setLookAt(x, y, z, Math.min(deltaYaw, 10), deltaPitch);
        }
    }
}
