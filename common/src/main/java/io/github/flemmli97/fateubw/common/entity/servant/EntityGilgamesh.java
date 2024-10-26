package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.ai.DoNothingWithoutSightRun;
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
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityGilgamesh extends BaseServant {

    public static final AnimatedAction MELEE_1 = new AnimatedAction(0.52, 0.4, "vertical_slash");
    public static final AnimatedAction MELEE_2 = new AnimatedAction(0.56, 0.4, "horizontal_slash");
    public static final AnimatedAction MELEE_3 = new AnimatedAction(0.48, 0.36, "slash_1");
    public static final AnimatedAction MELEE_4 = new AnimatedAction(0.48, 0.36, "slash_2");

    public static final AnimatedAction BABYLON_1 = new AnimatedAction(0.92, 0.28, "babylon_1");
    public static final AnimatedAction BABYLON_2 = new AnimatedAction(0.8, 0.2, "babylon_2");
    public static final AnimatedAction BABYLON_3 = new AnimatedAction(0.88, 0.24, "babylon_3");
    public static final AnimatedAction EA = new AnimatedAction(1.6, 0.72, "ea");
    private static final AnimatedAction[] ANIMS = {MELEE_1, MELEE_2, MELEE_3, MELEE_4, BABYLON_1, BABYLON_2, BABYLON_3, EA};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGilgamesh>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.MELEE_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .chain(GoalAttackAction.<EntityGilgamesh>chainBuilder(EntityGilgamesh.MELEE_3).withPredicate(e -> e.getRandom().nextFloat() < 0.5))
                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.MELEE_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.MELEE_3)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .chain(GoalAttackAction.<EntityGilgamesh>chainBuilder(EntityGilgamesh.MELEE_1)
                            .chain(EntityGilgamesh.MELEE_2).withPredicate(e -> e.getRandom().nextFloat() < 0.5))
                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.MELEE_4)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .chain(GoalAttackAction.<EntityGilgamesh>chainBuilder(EntityGilgamesh.MELEE_2).withPredicate(e -> e.getRandom().nextFloat() < 0.4))
                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 12, 1.2))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(e -> 40, new DoNothingWithoutSightRun<>())), 3),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_2)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 12, 1.2))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_2)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(e -> 40, new DoNothingWithoutSightRun<>())), 3),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_3)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 12, 1.2))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_3)
                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
                    .prepare(() -> new WrappedRunner<>(e -> 40, new DoNothingWithoutSightRun<>())), 3),
            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.EA)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(Utils.npCheck())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 8, 1.1))), 10)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityGilgamesh>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<EntityGilgamesh>(DoNothingRunner::new)
                    .withCondition((goal, target) -> goal.attacker.useRanged()), 2),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<EntityGilgamesh>(6, 1.1, 2))
                    .withCondition((goal, target) -> goal.attacker.useRanged()), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<EntityGilgamesh>(14, 6, 1, 0.3f))
                    .withCondition((goal, target) -> goal.attacker.useRanged()), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<EntityGilgamesh>(1, 1))
                    .withCondition((goal, target) -> !goal.attacker.useRanged()), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<EntityGilgamesh>(1, 1, 6))
                    .withCondition((goal, target) -> !goal.attacker.useRanged()), 6)
    );

    public final AnimatedAttackGoal<EntityGilgamesh> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityGilgamesh> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeFunc(anim -> {
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
            if (anim.isAtTick(0.4)) {
                this.targetPosition = target != null ? EntityUtil.getStraightProjectileTarget(this.position()
                        .add(0, this.getEyeHeight() - 0.1, 0), target) :
                        this.position().add(this.getLookAngle().scale(8));
            }
            if (anim.canAttack()) {
                if (!this.forcedNP)
                    this.useMana(this.props().hogouMana());
                this.attackWithNP(this.targetPosition);
                this.forcedNP = false;
            }

        } else if (anim.is(BABYLON_1, BABYLON_2, BABYLON_3)) {
            LivingEntity target = this.getTarget();
            if (!anim.isPastTick(anim.getAttackTime()) && target != null) {
                this.lookAtNow(target, 60.0F, 30.0F);
            }
            if (anim.canAttack()) {
                if (target != null) {
                    this.attackWithRangedAttack(target);
                }
            }
        } else
            super.handleAttack(anim);
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
        int weaponAmount = this.getRandom().nextInt(12) + 6;
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
}