package io.github.flemmli97.fateubw.common.entity.servant;


import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityGilgamesh extends BaseServant {

    public static final AnimatedAction ONE_HAND_1 = AnimatedAction.builder(0.62, "one_hand_1")
            .marker("attack", 0.52).marker("step", 0.52).build();
    public static final AnimatedAction ONE_HAND_2 = AnimatedAction.builder(0.62, "one_hand_2")
            .marker("attack", 0.48).marker("step", 0.52).build();
    public static final AnimatedAction ONE_HAND_3 = AnimatedAction.builder(0.58, "one_hand_3")
            .marker("attack", 0.44).marker("step", 0.48).build();
    public static final AnimatedAction ONE_HAND_4 = AnimatedAction.builder(0.58, "one_hand_4")
            .marker("attack", 0.48).marker("step", 0.48).build();
    public static final AnimatedAction STAB_1 = AnimatedAction.builder(0.86, "stab_1")
            .marker("attack", 0.6).build();

    public static final AnimatedAction BABYLON_1 = AnimatedAction.builder(0.96, "babylon_1").marker("attack", 0.24).build();
    public static final AnimatedAction BABYLON_2 = AnimatedAction.builder(0.96, "babylon_2").marker("attack", 0.24).build();
    public static final AnimatedAction BABYLON_3 = AnimatedAction.builder(0.96, "babylon_3").marker("attack", 0.28).build();
    public static final AnimatedAction EA = AnimatedAction.builder(1.68, "ea").marker("attack", 0.76).build();
    public static final AnimatedAction SUMMON = AnimatedAction.builder(2., "summon").build();

    private static final AnimatedAction[] ANIMS = {ONE_HAND_1, ONE_HAND_2, ONE_HAND_3, ONE_HAND_4, STAB_1, BABYLON_1, BABYLON_2, BABYLON_3, EA, SUMMON};

    private final Vector4f summonColor = new Vector4f(1.0f, 0.85f, 0.3f, 0.7f);

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGilgamesh>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.ONE_HAND_1)
                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
                    .chain(GoalAttackAction.<EntityGilgamesh>chainBuilder(EntityGilgamesh.ONE_HAND_2, 2, 0.2f, 1)
                            .or(EntityGilgamesh.ONE_HAND_3, 2, 0.2f, 1)
                            .withChance(0.4f))
                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.ONE_HAND_2)
                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
                    .chain(GoalAttackAction.<EntityGilgamesh>chainBuilder(EntityGilgamesh.ONE_HAND_1, 2, 0.2f, 1)
                            .withChance(0.4f))
                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.ONE_HAND_3)
                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
                    .chain(GoalAttackAction.<EntityGilgamesh>chainBuilder(EntityGilgamesh.ONE_HAND_4, 2, 0.2f, 1)
                            .withChance(0.4f))
                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.ONE_HAND_4)
                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 12, 1.2))), 15),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(e -> 40, new MoveToTargetRunner<>(1, 18))), 13),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_2)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 12, 1.2))), 15),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_2)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(e -> 40, new MoveToTargetRunner<>(1, 18))), 13),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_3)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 12, 1.2))), 15),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_3)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(e -> 40, new MoveToTargetRunner<>(1, 18))), 13),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.EA)
                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
                    .withCondition(Utils.npCheck())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 8, 1.1))), 18)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityGilgamesh>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<EntityGilgamesh>(DoNothingRunner::new)
                    .withCondition((goal, target) -> goal.attacker.useRanged() || goal.attacker.getRandom().nextFloat() < 0.7f), 2),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<EntityGilgamesh>(6, 1.1, 2))
                    .withCondition((goal, target) -> goal.attacker.useRanged() || goal.attacker.getRandom().nextFloat() < 0.7f), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<EntityGilgamesh>(14, 6, 1, 0.3f))
                    .withCondition((goal, target) -> goal.attacker.useRanged() || goal.attacker.getRandom().nextFloat() < 0.7f), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<EntityGilgamesh>(1, 1))
                    .withCondition((goal, target) -> !goal.attacker.useRanged()), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<EntityGilgamesh>(1, 1, 6))
                    .withCondition((goal, target) -> !goal.attacker.useRanged()), 6)
    );

    public final AnimatedAttackGoal<EntityGilgamesh> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityGilgamesh> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level.isClientSide()) {
                    if (anim == null) {
                        if (this.getAnimationHandler().isCurrent(EA)) {
                            this.switchableWeapon.switchItems(true);
                        }
                    } else if (anim.is(EA)) {
                        this.switchableWeapon.switchItems(false);
                        this.startUsingItem(InteractionHand.MAIN_HAND);
                        Platform.INSTANCE.getItemStackData(this.getMainHandItem()).ifPresent(data -> data.setInUse(this, true, this.getUsedItemHand() == InteractionHand.MAIN_HAND));
                    }
                }
                return false;
            });

    public final SwitchableWeapon<EntityGilgamesh> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.ENUMAELISH.get()), ItemStack.EMPTY);

    public EntityGilgamesh(EntityType<? extends EntityGilgamesh> entityType, Level level) {
        super(entityType, level);
        this.revealServant();
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    public boolean showServant() {
        return true;
    }

    @Override
    public AnimationHandler<EntityGilgamesh> getAnimationHandler() {
        return this.animationHandler;
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
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(EA)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAtNow(target, 360, 90);
            }
            if (anim.isAt(0.4)) {
                this.targetPosition = target != null ? EntityUtil.getStraightProjectileTarget(this.position()
                        .add(0, this.getEyeHeight() - 0.1, 0), target) :
                        this.position().add(this.getLookAngle().scale(8));
            }
            if (anim.isAt("attack")) {
                if (!this.forcedNP)
                    this.useMana(this.props().hogouMana());
                this.attackWithNP(this.targetPosition);
                this.forcedNP = false;
            }

        } else if (anim.is(BABYLON_1, BABYLON_2, BABYLON_3)) {
            LivingEntity target = this.getTarget();
            if (!anim.isPast("attack") && target != null) {
                this.lookAtNow(target, 60.0F, 30.0F);
            }
            if (anim.isAt("attack")) {
                if (target != null) {
                    this.attackWithRangedAttack(target);
                }
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
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(ONE_HAND_1)) {
            width += 0.4;
            length += 0.6;
        }
        if (anim.is(ONE_HAND_2)) {
            width += 1;
            length += 0.6;
        }
        if (anim.is(ONE_HAND_3)) {
            width += 1.1;
            length += 0.6;
        }
        if (anim.is(ONE_HAND_4)) {
            width += 0.2;
            length += 0.6;
        }
        if (anim.is(STAB_1)) {
            width += 0.1;
            length += 0.7;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ENUMAELISH.get()));
        }
    }

    public void attackWithNP(Vec3 pos) {
        EnumaElish ea = new EnumaElish(this.level, this);
        if (pos != null)
            ea.setRotationTo(pos.x(), pos.y(), pos.z(), 0);
        this.level.addFreshEntity(ea);
        this.revealServant();
        this.stopUsingItem();
        Platform.INSTANCE.getItemStackData(this.getMainHandItem()).ifPresent(data -> data.setInUse(this, false, true));
        this.switchableWeapon.switchItems(true);
    }

    public void attackWithRangedAttack(LivingEntity target) {
        double perc = Mth.clamp(1 - this.getHealth() / this.getMaxHealth(), 0.1, 1);
        int randAmount = (int) (20 * perc);
        int base = 6 + (int) (5 * perc);
        int weaponAmount = this.getRandom().nextInt(Math.max(1, randAmount)) + base;
        if (this.getAnimationHandler().getAnimation() == null)
            this.spawnBehind(target, weaponAmount);
        else if (this.getAnimationHandler().isCurrent(BABYLON_1, BABYLON_2, BABYLON_3)) {
            if (this.getRandom().nextInt(3) == 0)
                this.spawnAroundTarget(target, weaponAmount);
            else
                this.spawnBehind(target, weaponAmount);
        }
    }

    private void spawnBehind(LivingEntity target, int amount) {
        BabylonWeapon.spawnWeapons(this, target, amount, 7);
    }

    private void spawnAroundTarget(LivingEntity target, int amount) {
        BabylonWeapon.spawnWeaponsAround(this, target, amount, 6);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.switchableWeapon.save(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.switchableWeapon.read(tag);
    }

    protected boolean useRanged() {
        return this.getMainHandItem().getItem() != ModItems.ENUMAELISH.get();
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }
}