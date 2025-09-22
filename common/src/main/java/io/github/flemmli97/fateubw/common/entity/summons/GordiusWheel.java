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
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.MultiPartEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
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
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GordiusWheel extends PathfinderMob implements AnimatedEntity, StandingVehicle, AOEAttackEntity, SmartBrainOwner<GordiusWheel>, SyncedMobDataHandler {

    private static final EntityDataAccessor<Integer> WHEEL = SynchedEntityData.defineId(GordiusWheel.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(GordiusWheel.class, EntityDataSerializers.BYTE);
    public static final TypedResource<Vec3> CHARGE_MOTION = new TypedResource<>(Fate.modRes("charge_motion"));

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String STOMP = BUILDER.add("stomp", AnimationsBuilder.definition(0.8).marker("attack", 0.52));
    public static final String CHARGING = BUILDER.add("charge", AnimationsBuilder.definition(2.6)
            .marker("charge_start", 0.48).marker("charge_end", 2.16));
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
                if (!this.level().isClientSide && anim != null && anim.is(CHARGING)) {
                    this.setChargeMotion(null);
                }
                return false;
            });

    private final SyncedDataContainer<GordiusWheel> syncedDataContainer = SyncedDataContainer.builder(this)
            .define(CHARGE_MOTION, TenshilibSyncableEntityDatas.VEC_3.get(), null).build();

    private MultiPartEntity wheels;

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(2, this::getMoveType);

    public int wheelMoveTick;
    public float wheelPartial;

    public GordiusWheel(EntityType<? extends GordiusWheel> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.updateAttributes();
        }
        this.lookControl = new GordiusLookControl(this);
        this.moveControl = new GordiusMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BaseServant.createAttributes().add(Attributes.STEP_HEIGHT, 1.6);
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
                new FirstApplicableBehaviour<>(
                        new Idle<GordiusWheel>().startCondition(GordiusWheel::runCooldownBehaviour)
                                .stopIf(e -> !e.runCooldownBehaviour()),
                        AttackBehaviourBuilder.<GordiusWheel>create()
                                .start(STOMP).play(BehaviourUtils.cooldownedPlay(true, 10, 40))
                                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.moveAttack())
                                .end(1)
                                .start(CHARGING).play(BehaviourUtils.cooldownedPlay(false, 10, 40))
                                .prepare(new SetChargeTarget())
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
        }
        this.moveStateTracker.tick();
        if (this.getMoveType() != MoveType.NONE)
            ++this.wheelMoveTick;
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
        if (!(this.getControllingPassenger() instanceof Player) && this.getDeltaMovement().horizontalDistanceSqr() > 0.003 && this.isAlive()) {
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

    public float interpolatedMoveTick(float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTick(partialTicks);
    }

    public float interpolatedMoveTickOf(MoveType moveType, float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTickOf(moveType, partialTicks);
    }

    public void setMovingFlag(MoveType type) {
        this.entityData.set(MOVE_FLAGS, (byte) type.ordinal());
    }

    public MoveType getMoveType() {
        return MoveType.values()[this.entityData.get(MOVE_FLAGS)];
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
        Vec3 offset = new Vec3(0, 0, -1);
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
        if (anim.is(CHARGING)) {
            if (anim.isPast("charge_start") && !anim.isPast("charge_end")) {
                Vec3 dir = this.getChargeMotion();
                if (dir != null) {
                    this.setDeltaMovement(dir.x(), this.getDeltaMovement().y(), dir.z());
                    OrientedBoundingBox obb = this.prepareAttackBox(anim.getAnimation(), null, 0.2, false);
                    List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                            entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()));
                    for (LivingEntity e : list) {
                        e.hurt(FateDamageTypes.direct(FateDamageTypes.GORDIUS_TRAMPLE, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    }
                    this.playSound(SoundEvents.COW_STEP, 0.4F, 0.4F);
                    S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
                    S2CScreenShake.sendAround(this, 14, 4, 1.5f);
                }
            }
        } else {
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
            AABB aabb2 = this.getBoundingBox().minmax(this.wheels.getBoundingBox());
            double lenX = aabb2.getXsize();
            double lenZ = aabb2.getZsize();
            double expand = 0.3 + grow;
            double len = Math.sqrt(lenX * lenX + lenZ * lenZ) + 2 * expand;
            double width = Math.min(lenX, lenZ) * 0.5 + expand;
            double offset = this.wheels.getBbWidth() * 0.5 + expand;
            AABB aabb = new AABB(-width, -0.02, -offset, width, this.getBbHeight(), len - offset);
            return new OrientedBoundingBox(aabb, this.getYRot(), 0, this.wheels.position());
        }
        double width = this.getBbWidth() * 0.5 + 1.5;
        AABB aabb = new AABB(-width * 0.8, -0.02, -width * 0.5, width * 0.8, this.getBbHeight() * 0.5, width * 1.2);
        return new OrientedBoundingBox(aabb, this.getYRot(), 0, this.position());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, super::doHurtTarget, 0);
    }

    @Override
    public AnimationHandler<GordiusWheel> getAnimationHandler() {
        return this.animationHandler;
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
        return anim != null && anim.is(CHARGING) && anim.isPast("attack");
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
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
