package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.ChargingHandler;
import io.github.flemmli97.fateubw.common.entity.MultiPartEntity;
import io.github.flemmli97.fateubw.common.entity.StandingVehicle;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.AoeAttackEntity;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.ActionStart;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GordiusWheel extends PathfinderMob implements IAnimated, StandingVehicle, AoeAttackEntity {

    private static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(GordiusWheel.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> WHEEL = SynchedEntityData.defineId(GordiusWheel.class, EntityDataSerializers.INT);

    public static final AnimatedAction STOMP = AnimatedAction.builder(0.52, "stomp").marker("attack", 0.28).build();
    public static final AnimatedAction CHARGING = AnimatedAction.builder(1.4, "charge").marker("attack", 0.25).build();
    public static final AnimatedAction[] ANIMS = {STOMP, CHARGING};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<GordiusWheel>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<GordiusWheel>(GordiusWheel.STOMP)
                    .cooldown(e -> e.getRandom().nextInt(30) + 10)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 4),
            WeightedEntry.wrap(new GoalAttackAction<GordiusWheel>(GordiusWheel.CHARGING)
                    .cooldown(e -> e.getRandom().nextInt(45) + 25)
                    .withCondition(((goal, target, previous) -> goal.distanceToTargetSq > 25 || goal.attacker.getRandom().nextFloat() < 0.4))
                    .prepare(ChargeTo::new), 5)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<GordiusWheel>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 3),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 5)), 5),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 5)), 5)
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

    public final AnimatedAttackGoal<GordiusWheel> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<GordiusWheel> animationHandler = new AnimationHandler<>(this, ANIMS);

    private MultiPartEntity wheels;

    public final ChargingHandler<GordiusWheel> chargingHandler = new ChargingHandler<>(this, LOCKED_YAW, a -> this.isCharging());
    private Vec3 chargeMotion;

    public GordiusWheel(EntityType<? extends GordiusWheel> type, Level level) {
        super(type, level);
        this.maxUpStep = 1;
        if (!level.isClientSide) {
            this.goalSelector.addGoal(0, this.attack);
            this.wheels = this.createWheels();
            this.updateAttributes();
        }
        this.lookControl = new GordiusLookControl(this);
        this.moveControl = new GordiusMoveControl(this);
        this.goalSelector.addGoal(0, new RandomStrollGoal(this, 1, 10));
    }

    protected MultiPartEntity createWheels() {
        MultiPartEntity entity = new MultiPartEntity(this.level, 2.2f, 1.6f,
                new MultiPartEntity.Position(new Vec3(0, 0, -1), new Vec3(0, 0, -1.6)))
                .smoothMovement()
                .gravity();
        entity.maxUpStep = this.maxUpStep;
        this.entityData.set(WHEEL, entity.getId());
        return entity;
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
        this.entityData.define(WHEEL, 0);
    }

    @Override
    public AnimationHandler<GordiusWheel> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    public boolean isCharging() {
        if (this.getAnimationHandler() == null)
            return false;
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return anim != null && CHARGING.is(anim) && anim.isPast("attack");
    }

    @Override
    public float getYRot() {
        return this.isCharging() ? this.entityData.get(LOCKED_YAW) : super.getYRot();
    }

    @Override
    public void baseTick() {
        this.getAnimationHandler().tick();
        this.chargingHandler.tick();
        if (!this.level().isClientSide) {
            if (this.wheels == null) {
                this.wheels = this.createWheels();
            }
            if (!this.wheels.isAddedToLevel()) {
                this.wheels.setParent(this);
                this.wheels.maxUpStep = 3;
                this.level().addFreshEntity(this.wheels);
            }
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
            if (this.getTarget() == null) {
                if (this.getFirstPassenger() instanceof Mob mob) {
                    if (mob.getTarget() != this.getTarget())
                        this.setTarget(mob.getTarget());
                }
            }
        }
        super.baseTick();
    }

    public void handleAttack(AnimatedAction anim) {
        if (anim.is(CHARGING)) {
            if (anim.isPast("attack")) {
                this.setDeltaMovement(this.chargeMotion.x(), this.getDeltaMovement().y(), this.chargeMotion.z());
                OrientedBoundingBox obb = this.prepareAttackBox(anim, null, 0.2, false);
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                        entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()));
                for (LivingEntity e : list) {
                    e.hurt(FateDamageTypes.direct(FateDamageTypes.GORDIUS_TRAMPLE, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                }
                this.playSound(SoundEvents.COW_STEP, 0.4F, 0.4F);
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
                S2CScreenShake.sendAround(this, 14, 4, 1.5f);
            }
        } else {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                S2CScreenShake.sendAround(this, 6, 8, 2);
            }
        }
    }

    @Override
    public void lookAt(Entity entity, float maxYRotIncrease, float maxXRotIncrease) {
        super.lookAt(entity, Math.min(maxYRotIncrease, 12), maxXRotIncrease);
    }

    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        OrientedBoundingBox obb = this.prepareAttackBox(anim, target, 0.2, false);
        this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox())).forEach(cons);
        if (!this.level().isClientSide)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
    }

    @Override
    public void moveTo(double x, double y, double z) {
        super.moveTo(x, y, z);
        this.wheels.forceUpdatePosition();
    }

    @Override
    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            Entity e = this.getWheelEntity();
            if (e != null)
                passenger.setPos(e.position().x, e.position().y() + this.getPassengersRidingOffset() - 0.3, e.position().z);
            else
                super.positionRider(passenger);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.825;
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof Player player) ? null : player;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (!damageSource.isBypassInvul() && this.isCharging())
            damage *= 0.5f;
        return super.hurt(damageSource, damage);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, super::doHurtTarget, 0);
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

    @Override
    public OrientedBoundingBox prepareAttackBox(AnimatedAction anim, LivingEntity target, double grow, boolean withDebug) {
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

    public void setChargeTo(Vec3 pos) {
        Vec3 dir = pos.subtract(this.position());
        dir = new Vec3(dir.x(), 0, dir.z());
        this.chargeMotion = dir.normalize().scale(0.55);
        float[] xYRot = MathsHelper.XYRotFrom(dir);
        float targetYRot = xYRot[0];
        float targetXRot = xYRot[1];
        this.setYRot(targetYRot);
        this.setXRot(targetXRot);
        this.yHeadRot = this.getYRot();
        this.yBodyRot = this.getYRot();
        this.chargingHandler.lockYaw(this.getYRot());
    }

    @Nullable
    public MultiPartEntity getWheelEntity() {
        if (!this.level().isClientSide) {
            if (this.wheels == null) {
                this.wheels = this.createWheels();
            }
            if (!this.wheels.isAddedToLevel()) {
                this.wheels.setParent(this);
                this.wheels.maxUpStep = 3;
                this.level().addFreshEntity(this.wheels);
            }
        } else if (this.wheels == null || !this.wheels.isAddedToLevel() || !this.wheels.isAlive()) {
            Entity entity = this.level().getEntity(this.entityData.get(WHEEL));
            if (entity instanceof MultiPartEntity part && part.getParent() == this) {
                this.wheels = part;
            }
        }
        return this.wheels;
    }

    private float rotlerpDiff(float start, float end) {
        while (start < 0) {
            start += 360;
        }
        while (end < 0) {
            end += 360;
        }
        start = start % 360;
        end = end % 360;
        float diff1 = end - start;
        float diff2 = (Math.min(start, end) + 360) - Math.max(start, end);
        if (Math.abs(diff2) > Math.abs(diff1)) {
            return diff1;
        }
        return diff2;
    }

    public static class ChargeTo implements ActionStart<GordiusWheel> {

        private Vec3 targetPos;

        @Override
        public GoalAttackAction.IntProvider<GordiusWheel> timeout() {
            return e -> 20;
        }

        @Override
        public boolean start(AnimatedAttackGoal<GordiusWheel> goal, LivingEntity target) {
            if (goal.current == null)
                return false;
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
            if (Math.abs(diffY) < 16) {
                goal.attacker.setChargeTo(this.targetPos);
                return true;
            }
            goal.attacker.setYRot(goal.attacker.getYRot() + Mth.clamp(diffY, -16, 16));
            goal.attacker.yBodyRot = goal.attacker.getYRot();
            goal.attacker.yHeadRot = goal.attacker.getYRot();
            goal.attacker.hasImpulse = true;
            return false;
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
                BlockState blockState = this.mob.level.getBlockState(blockPos);
                VoxelShape voxelShape = blockState.getCollisionShape(this.mob.level, blockPos);
                if (dY > this.mob.maxUpStep && dX * dX + dZ * dZ < Math.max(1.0f, this.mob.getBbWidth()) || !voxelShape.isEmpty() && this.mob.getY() < voxelShape.max(Direction.Axis.Y) + blockPos.getY() && !blockState.is(BlockTags.DOORS) && !blockState.is(BlockTags.FENCES)) {
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
