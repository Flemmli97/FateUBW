package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class EntityGilgamesh extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ONE_HAND_1 = BUILDER.add("one_hand_1", AnimationsBuilder.definition(0.62)
            .marker("attack", 0.52).marker("step", 0.52));
    public static final String ONE_HAND_2 = BUILDER.add("one_hand_2", AnimationsBuilder.definition(0.62)
            .marker("attack", 0.48).marker("step", 0.52));
    public static final String ONE_HAND_3 = BUILDER.add("one_hand_3", AnimationsBuilder.definition(0.58)
            .marker("attack", 0.44).marker("step", 0.48));
    public static final String ONE_HAND_4 = BUILDER.add("one_hand_4", AnimationsBuilder.definition(0.58)
            .marker("attack", 0.48).marker("step", 0.48));
    public static final String STAB_1 = BUILDER.add("stab_1", AnimationsBuilder.definition(0.86)
            .marker("attack", 0.6));

    public static final String BABYLON_1 = BUILDER.add("babylon_1", AnimationsBuilder.definition(0.96).marker("attack", 0.24));
    public static final String BABYLON_2 = BUILDER.add("babylon_2", AnimationsBuilder.definition(0.96).marker("attack", 0.24));
    public static final String BABYLON_3 = BUILDER.add("babylon_3", AnimationsBuilder.definition(0.96).marker("attack", 0.28));
    public static final String EA = BUILDER.add("ea", AnimationsBuilder.definition(1.68).marker("attack", 0.76));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));

    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final Vector4f summonColor = new Vector4f(1.0f, 0.85f, 0.3f, 0.7f);

//    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGilgamesh>>> ATTACKS = List.of(
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.ONE_HAND_1)
//                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
//                    .chain(GoalAttackAction.<EntityGilgamesh>chainBuilder(EntityGilgamesh.ONE_HAND_2, 2, 0.2f, 1)
//                            .or(EntityGilgamesh.ONE_HAND_3, 2, 0.2f, 1)
//                            .withChance(0.4f))
//                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.ONE_HAND_2)
//                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
//                    .chain(GoalAttackAction.<EntityGilgamesh>chainBuilder(EntityGilgamesh.ONE_HAND_1, 2, 0.2f, 1)
//                            .withChance(0.4f))
//                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.ONE_HAND_3)
//                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
//                    .chain(GoalAttackAction.<EntityGilgamesh>chainBuilder(EntityGilgamesh.ONE_HAND_4, 2, 0.2f, 1)
//                            .withChance(0.4f))
//                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.ONE_HAND_4)
//                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
//                    .withCondition((goal, target, previous) -> !goal.attacker.useRanged())
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_1)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 12, 1.2))), 15),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_1)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
//                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
//                    .prepare(() -> new WrappedRunner<>(e -> 40, new MoveToTargetRunner<>(1, 18))), 13),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_2)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 12, 1.2))), 15),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_2)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
//                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
//                    .prepare(() -> new WrappedRunner<>(e -> 40, new MoveToTargetRunner<>(1, 18))), 13),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_3)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 12, 1.2))), 15),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.BABYLON_3)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 30)
//                    .withCondition((goal, target, previous) -> goal.attacker.useRanged())
//                    .prepare(() -> new WrappedRunner<>(e -> 40, new MoveToTargetRunner<>(1, 18))), 13),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilgamesh>(EntityGilgamesh.EA)
//                    .cooldown(e -> e.getRandom().nextInt(23) + 10)
//                    .withCondition(Utils.npCheck())
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 8, 1.1))), 18)
//    );
//    public static final List<WeightedEntry.Wrapper<IdleAction<EntityGilgamesh>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<EntityGilgamesh>(DoNothingRunner::new)
//                    .withCondition((goal, target) -> goal.attacker.useRanged() || goal.attacker.getRandom().nextFloat() < 0.7f), 2),
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<EntityGilgamesh>(6, 1.1, 2))
//                    .withCondition((goal, target) -> goal.attacker.useRanged() || goal.attacker.getRandom().nextFloat() < 0.7f), 6),
//            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<EntityGilgamesh>(14, 6, 1, 0.3f))
//                    .withCondition((goal, target) -> goal.attacker.useRanged() || goal.attacker.getRandom().nextFloat() < 0.7f), 6),
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<EntityGilgamesh>(1, 1))
//                    .withCondition((goal, target) -> !goal.attacker.useRanged()), 6),
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<EntityGilgamesh>(1, 1, 6))
//                    .withCondition((goal, target) -> !goal.attacker.useRanged()), 6)
//    );
//
//    public final AnimatedAttackGoal<EntityGilgamesh> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityGilgamesh> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide()) {
                    if (anim == null) {
                        if (this.getAnimationHandler().isCurrent(EA)) {
                            this.switchableWeapon.switchItems(true);
                        }
                    } else if (anim.is(EA)) {
                        this.switchableWeapon.switchItems(false);
                        this.startUsingItem(InteractionHand.MAIN_HAND);
                        this.getMainHandItem().set(FateDataComponents.GLOWING_ITEM.get(), Unit.INSTANCE);
                    }
                }
                return false;
            });

    public final SwitchableWeapon<EntityGilgamesh> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(FateItems.ENUMAELISH.get()), ItemStack.EMPTY);

    public EntityGilgamesh(EntityType<? extends EntityGilgamesh> entityType, Level level) {
        super(entityType, level);
        this.revealServant();
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
    public void handleAttack(AnimationState anim) {
        if (anim.is(EA)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAtNow(target, 360, 90);
            }
            if (anim.isAt(0.4)) {
                this.targetPosition = target != null ? EntityUtils.getStraightProjectileTarget(this.position()
                        .add(0, this.getEyeHeight() - 0.1, 0), target) :
                        this.position().add(this.getLookAngle().scale(8));
            }
            if (anim.isAt("attack")) {
                this.ea(this.targetPosition);
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
    public AABB attackBB(AnimationState anim) {
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
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.ENUMAELISH.get()));
        }
    }

    public void ea(Vec3 pos) {
        if (!this.forcedNP && !this.useMana(this.props().hogouMana()))
            return;
        EnumaElish ea = new EnumaElish(this.level(), this);
        if (pos != null)
            ea.setRotationTo(pos.x(), pos.y(), pos.z(), 0);
        this.level().addFreshEntity(ea);
        this.revealServant();
        this.stopUsingItem();
        this.getMainHandItem().remove(FateDataComponents.GLOWING_ITEM.get());
        this.switchableWeapon.switchItems(true);
        this.forcedNP = false;
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
        this.switchableWeapon.save(tag, this.registryAccess());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.switchableWeapon.read(tag, this.registryAccess());
    }

    protected boolean useRanged() {
        return this.getMainHandItem().getItem() != FateItems.ENUMAELISH.get();
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }
}