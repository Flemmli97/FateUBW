package io.github.flemmli97.fateubw.common.entity.servant;


import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.common.entity.DaggerHitNotifiable;
import io.github.flemmli97.fateubw.common.entity.minions.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.minions.Pegasus;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
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

    public static final AnimatedAction MELEE_1 = new AnimatedAction(0.56, 0.4, "horizontal_slash");
    public static final AnimatedAction MELEE_1_R = AnimatedAction.copyOf(MELEE_1, "horizontal_slash_2");
    public static final AnimatedAction MELEE_2 = new AnimatedAction(0.48, 0.36, "slash_1");
    public static final AnimatedAction MELEE_2_R = AnimatedAction.copyOf(MELEE_2, "slash_2");
    public static final AnimatedAction THROW = new AnimatedAction(0.64, 0.48, "chain_throw");
    public static final AnimatedAction RETRIEVE = new AnimatedAction(0.48, 0.28, "chain_retrieve");
    public static final AnimatedAction EYE = new AnimatedAction(1.96, 0.96, "eye");
    public static final AnimatedAction JUMP = AnimatedAction.builder((int) Math.ceil(0.32 * 20), "jump").infinite().build();
    public static final AnimatedAction LAND = new AnimatedAction(0.72, 0.2, "land");

    public static final AnimatedAction IDLE = new AnimatedAction(1., 0, "idle");
    public static final AnimatedAction BELLEROPHON = new AnimatedAction(2.04, 0.36, "bellerophon");
    public static final AnimatedAction SUMMON = new AnimatedAction(2.04, 0, "summon");
    private static final AnimatedAction[] ANIMS = {MELEE_1, MELEE_1_R, MELEE_2, MELEE_2_R, THROW, RETRIEVE, EYE, JUMP, LAND, IDLE, BELLEROPHON, SUMMON};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityMedusa>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.MELEE_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(meleeCondition(EntityMedusa.MELEE_1))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.MELEE_1_R)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition((goal, target, previous) -> meleeCondition(EntityMedusa.MELEE_1).test(goal, target, previous) && !goal.attacker.getOffhandItem().isEmpty())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.MELEE_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(meleeCondition(EntityMedusa.MELEE_1))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.MELEE_2_R)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition((goal, target, previous) -> meleeCondition(EntityMedusa.MELEE_1).test(goal, target, previous) && !goal.attacker.getOffhandItem().isEmpty())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.JUMP)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(((goal, target, previous) -> !goal.attacker.isPassenger() && goal.distanceToTargetSq > 9))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 7))), 13),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedusa>(EntityMedusa.THROW)
                    .cooldown(e -> e.getRandom().nextInt(30) + 15)
                    .withCondition((goal, target, previous) -> goal.attacker.canThrow() && (goal.distanceToTargetSq > 25 || goal.attacker.getRandom().nextFloat() < 0.4))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 14))), 10),
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
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(5, 8, 1.1))), 14)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityMedusa>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<EntityMedusa>(1, 0.5))
                    .withCondition(((goal, target) -> !goal.attacker.isPassenger())), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2),
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(THROWN_DAGGER, false);
    }

    public boolean daggerThrown() {
        return this.getEntityData().get(THROWN_DAGGER);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MEDUSA_DAGGER.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.MEDUSA_DAGGER.get()));
    }

    public boolean canThrow() {
        return this.dagger == null && this.throwCooldown <= 0;
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
        if (!this.level.isClientSide && this.dagger != null) {
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
            if (anim.canAttack()) {
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
            if (anim.canAttack()) {
                this.eyeCooldown = this.random.nextInt(150) + 250;
            }
        } else if (anim.is(JUMP)) {
            LivingEntity target = this.getTarget();
            if (anim.isAtTick(0.12)) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).scale(0.13).add(0, 0.8, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.isPastTick(0.12)) {
                this.fallDistance = 0;
                if (anim.isPastTick(anim.getLength())) {
                    if (this.isOnGround()) {
                        this.getAnimationHandler().setAnimation(LAND);
                    }
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPastTick(6.0) && (!this.getFeetBlockState().is(Blocks.AIR) || !this.getBlockStateOn().is(Blocks.AIR))) {
                    this.getAnimationHandler().setAnimation(LAND);
                }
            }
        } else if (anim.is(BELLEROPHON)) {
            LivingEntity target = this.getTarget();
            if (target != null && !anim.isPastTick(0.28)) {
                this.lookAtNow(target, 60, 30);
            }
            this.level.getEntities(EntityTypeTest.forClass(LivingEntity.class),
                            this.getBoundingBox().inflate(12, 8, 12),
                            this.targetPred)
                    .forEach(e -> {
                        Vec3 dir = e.position().subtract(this.position());
                        boolean none = dir.x() == 0 && dir.z() == 0;
                        dir = new Vec3(none ? 1 : dir.x(), 0, dir.z()).normalize().scale(0.5);
                        e.setDeltaMovement(e.getDeltaMovement().add(dir));
                        e.hurtMarked = true;
                    });
            if (anim.canAttack()) {
                if (!this.forcedNP)
                    this.useMana(this.props().hogouMana());
                this.summonPegasus();
                this.forcedNP = false;
            }
        } else {
            boolean step = anim.is(MELEE_1, MELEE_1_R) && anim.isAtTick(0.28) ||
                    anim.is(MELEE_2, MELEE_2_R) && anim.isAtTick(0.24);
            if (step) {
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
        double width = this.getBbWidth() + 0.4;
        double length = 1;
        if (anim.is(MELEE_1, MELEE_1_R)) {
            width += 1.3;
            length += 0.7;
        }
        if (anim.is(MELEE_2, MELEE_2_R)) {
            width += 0.9;
            length += 0.7;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attack);
        else
            this.goalSelector.addGoal(0, this.attack);
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
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return super.hurt(damageSource, damage);
        } else if (this.getVehicle() != null) {
            damage *= 0.5;
            this.getVehicle().hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    public void throwDaggerAt(@Nullable LivingEntity target) {
        if (!this.level.isClientSide) {
            ChainDagger dagger = new ChainDagger(this.level, this, true);
            if (target == null) {
                dagger.shoot(this, this.getXRot(), this.getYRot(), 0, 3, 0);
            } else {
                dagger.shootAtEntity(target, 3, 0);
            }
            this.level.addFreshEntity(dagger);
            this.dagger = dagger;
            this.throwCooldown = this.random.nextInt(50) + 45;
            this.getEntityData().set(THROWN_DAGGER, true);
        }
    }

    public void summonPegasus() {
        if (this.level instanceof ServerLevel serverLevel) {
            Pegasus peg = ModEntities.PEGASUS.get().create(serverLevel, null, null, null, this.blockPosition(), MobSpawnType.MOB_SUMMONED, false, false);
            peg.setPos(this.position());
            peg.setYRot(this.getYRot());
            peg.yRotO = this.getYRot();
            peg.yHeadRot = this.getYRot();
            peg.yHeadRotO = this.getYRot();
            peg.yBodyRot = this.getYRot();
            peg.yBodyRotO = this.getYRot();
            this.level.addFreshEntity(peg);
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
