package io.github.flemmli97.fateubw.common.entity.servant;


import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.common.entity.DaggerHitNotifiable;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.summons.Pegasus;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityMedusa extends BaseServant implements DaggerHitNotifiable {

    protected static final EntityDataAccessor<Boolean> THROWN_DAGGER = SynchedEntityData.defineId(EntityMedusa.class, EntityDataSerializers.BOOLEAN);

    public static final AnimatedAction DUAL_REVERSE_1 = AnimatedAction.builder(0.58, "dual_reverse_1")
            .marker("attack", 0.4).build();
    public static final AnimatedAction DUAL_REVERSE_2 = AnimatedAction.copyOf(DUAL_REVERSE_1, "dual_reverse_2");
    public static final AnimatedAction DUAL_REVERSE_3 = AnimatedAction.builder(0.54, "dual_reverse_3")
            .marker("attack", 0.4).build();
    public static final AnimatedAction DUAL_REVERSE_4 = AnimatedAction.copyOf(DUAL_REVERSE_3, "dual_reverse_4");
    public static final AnimatedAction THROW = AnimatedAction.builder(0.72, "chain_throw").marker("attack", 0.48).build();
    public static final AnimatedAction RETRIEVE = AnimatedAction.builder(0.8, "chain_retrieve").marker("attack", 0.48).build();
    public static final AnimatedAction EYE = AnimatedAction.builder(1.96, "eye").marker("attack", 0.96).build();
    public static final AnimatedAction JUMP = AnimatedAction.builder(0.32, "jump").marker("jump", 0.2).infinite().build();
    public static final AnimatedAction LAND = AnimatedAction.builder(0.68, "land").marker("attack", 0.12).build();

    public static final AnimatedAction IDLE = AnimatedAction.builder(1., "idle").build();
    public static final AnimatedAction BELLEROPHON = AnimatedAction.builder(2.2, "bellerophon").marker("attack", 0.36).build();
    public static final AnimatedAction SUMMON = AnimatedAction.builder(2.04, "summon").build();
    private static final AnimatedAction[] ANIMS = {DUAL_REVERSE_1, DUAL_REVERSE_2, DUAL_REVERSE_3, DUAL_REVERSE_4, THROW, RETRIEVE, EYE, JUMP, LAND, IDLE, BELLEROPHON, SUMMON};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityMedusa>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.DUAL_REVERSE_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .withCondition(meleeCondition(EntityMedusa.DUAL_REVERSE_1))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.DUAL_REVERSE_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .withCondition((goal, target, previous) -> meleeCondition(EntityMedusa.DUAL_REVERSE_1).test(goal, target, previous) && !goal.attacker.getOffhandItem().isEmpty())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.DUAL_REVERSE_3)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .withCondition(meleeCondition(EntityMedusa.DUAL_REVERSE_1))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.DUAL_REVERSE_4)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .withCondition((goal, target, previous) -> meleeCondition(EntityMedusa.DUAL_REVERSE_1).test(goal, target, previous) && !goal.attacker.getOffhandItem().isEmpty())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.JUMP)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(((goal, target, previous) -> !goal.attacker.isPassenger() && goal.distanceToTargetSq > 9))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1.1, 7))), 13),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.JUMP)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1.1, 7))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.THROW)
                    .cooldown(e -> e.getRandom().nextInt(30) + 20)
                    .withCondition((goal, target, previous) -> goal.attacker.canThrow() && (goal.distanceToTargetSq > 25 || goal.attacker.getRandom().nextFloat() < 0.4))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1.1, 14))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.EYE) // TODO
//                    .cooldown(e -> e.getRandom().nextInt(25) + 15)
//                    .withCondition(((goal, target, previous) -> goal.attacker.eyeCooldown <= 0))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 9))), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.IDLE) // This should be a do nothing action when riding
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .withCondition((goal, target, previous) -> goal.attacker.isPassenger())
                    .prepare(() -> new WrappedRunner<>(e -> e.getRandom().nextInt(10) + 15, new DoNothingRunner<>())), 18),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.BELLEROPHON)
                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
                    .withCondition((goal, target, prev) -> !goal.attacker.isPassenger() && (goal.attacker.canUseNP() && goal.attacker.getOwner() == null && goal.attacker.getMana() >= goal.attacker.props().hogouMana()) || goal.attacker.forcedNP)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(5, 8, 1.3))), 15)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityMedusa>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<EntityMedusa>(1, 0.5))
                    .withCondition(((goal, target) -> !goal.attacker.isPassenger())), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1.1, 6)), 2),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<EntityMedusa>(8, 6))
                    .withCondition(((goal, target) -> goal.attacker.isPassenger())), 7),
            WeightedEntry.wrap(new IdleAction<>(() -> new DoNothingRunner<EntityMedusa>())
                    .withCondition(((goal, target) -> goal.attacker.isPassenger())), 3)
    );

    private static GoalAttackAction.Condition<EntityMedusa> meleeCondition(AnimatedAction anim) {
        return (goal, target, previous) -> !goal.attacker.isPassenger() ||
                goal.attacker.prepareAttackBox(anim, target, -0.2f, false).intersects(target.getBoundingBox());
    }

    public final AnimatedAttackGoal<EntityMedusa> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityMedusa> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final Vector4f summonColor = new Vector4f(175 / 255f, 88 / 255f, 142 / 255f, 0.7f);

    private ChainDagger dagger;
    private int throwCooldown, eyeCooldown;

    public EntityMedusa(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData();
        this.getEntityData().define(THROWN_DAGGER, false);
    }

    public boolean daggerThrown() {
        return this.getEntityData().get(THROWN_DAGGER);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.MEDUSA_DAGGER.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(FateItems.MEDUSA_DAGGER.get()));
    }

    public boolean canThrow() {
        return this.dagger == null && this.throwCooldown <= 0;
    }

    @Override
    public Goal getAttackAI() {
        return this.attack;
    }

    @Override
    public AnimationHandler<EntityMedusa> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.dagger != null) {
            if (!this.dagger.isAlive()) {
                this.dagger = null;
                this.getEntityData().set(THROWN_DAGGER, false);
            }
        } else
            --this.throwCooldown;
        --this.eyeCooldown;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(THROW)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAtNow(target, 60, 30);
            }
            if (anim.isAt("attack")) {
                this.throwDaggerAt(target);
            }
        } else if (anim.is(RETRIEVE)) {
            if (this.dagger != null) {
                this.dagger.retractHook();
                this.dagger = null;
                this.getEntityData().set(THROWN_DAGGER, false);
            }
        } else if (anim.is(EYE)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAtNow(target, 60, 30);
            }
            if (anim.isAt("attack")) {
                this.eyeCooldown = this.random.nextInt(150) + 250;
            }
        } else if (anim.is(JUMP)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt("jump")) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).scale(0.13);
                if (dir.lengthSqr() > 3.5 * 3.5) {
                    dir.normalize().scale(3.5);
                }
                this.setDeltaMovement(dir.add(0, 0.8, 0));
            }
            if (anim.isPast("jump")) {
                this.fallDistance = 0;
                if (anim.done(0)) {
                    if (this.isOnGround()) {
                        this.getAnimationHandler().setAnimation(LAND);
                    }
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPast(6.0) && (!this.getFeetBlockState().is(Blocks.AIR) || !this.getBlockStateOn().is(Blocks.AIR))) {
                    this.getAnimationHandler().setAnimation(LAND);
                }
            }
        } else if (anim.is(BELLEROPHON)) {
            LivingEntity target = this.getTarget();
            if (target != null && !anim.isPast(0.28)) {
                this.lookAtNow(target, 60, 30);
            }
            this.level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
                            this.getBoundingBox().inflate(12, 8, 12),
                            this.targetPred)
                    .forEach(e -> {
                        Vec3 dir = e.position().subtract(this.position());
                        boolean none = dir.x() == 0 && dir.z() == 0;
                        dir = new Vec3(none ? 1 : dir.x(), 0, dir.z()).normalize().scale(0.5);
                        e.setDeltaMovement(e.getDeltaMovement().add(dir));
                        e.hurtMarked = true;
                    });
            if (anim.isAt("attack")) {
                this.summonPegasus();
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.3);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (this.getVehicle() != null) {
            Entity vehicle = this.getVehicle();
            if (this.getVehicle() instanceof GordiusWheel gordiusWheel && gordiusWheel.getWheelEntity() != null) {
                vehicle = gordiusWheel.getWheelEntity();
            }
            double width = vehicle.getBbWidth() * 0.5 + 1.7;
            double height = (this.getY() - vehicle.getY() + this.getBbHeight()) + 0.2;
            AABB aabb = new AABB(-width * 0.5, -0.02, -width * 0.5, width * 0.5, height, width * 0.5);
            return new OrientedBoundingBox(aabb, vehicle.getYRot(), 0, vehicle.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        if (anim.is(LAND)) {
            double width = this.getBbWidth() + 2;
            return new AABB(-width * 0.5, -0.02, -width * 0.3, width * 0.5, this.getBbHeight() * 0.5, width * 0.7);
        }
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(DUAL_REVERSE_1, DUAL_REVERSE_2)) {
            width += 1;
            length += 0.6;
        }
        if (anim.is(DUAL_REVERSE_3, DUAL_REVERSE_4)) {
            width += 0.8;
            length += 0.7;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.isBypassInvul()) {
            return super.hurt(damageSource, damage);
        } else if (this.getVehicle() != null) {
            damage *= 0.5;
            this.getVehicle().hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    public void throwDaggerAt(@Nullable LivingEntity target) {
        if (!this.level().isClientSide) {
            ChainDagger dagger = new ChainDagger(this.level, this, true);
            if (target == null) {
                dagger.shoot(this, this.getXRot(), this.getYRot(), 0, 3, 0);
            } else {
                dagger.shootAtEntity(target, 3, 0);
            }
            this.level().addFreshEntity(dagger);
            this.dagger = dagger;
            this.throwCooldown = this.random.nextInt(50) + 45;
            this.getEntityData().set(THROWN_DAGGER, true);
        }
    }

    public void summonPegasus() {
        if (this.level() instanceof ServerLevel serverLevel) {
            if (!this.forcedNP && !this.useMana(this.props().hogouMana()))
                return;
            Pegasus peg = FateEntities.PEGASUS.get().create(serverLevel, null, null, null, this.blockPosition(), MobSpawnType.MOB_SUMMONED, false, false);
            peg.setPos(this.position());
            peg.setYRot(this.getYRot());
            peg.yRotO = this.getYRot();
            peg.yHeadRot = this.getYRot();
            peg.yHeadRotO = this.getYRot();
            peg.yBodyRot = this.getYRot();
            peg.yBodyRotO = this.getYRot();
            this.level().addFreshEntity(peg);
            this.startRiding(peg, true);
            this.revealServant();
        }
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }

    @Override
    public void onDaggerHit(ChainDagger dagger) {
        this.getAnimationHandler().setAnimation(RETRIEVE);
    }
}
