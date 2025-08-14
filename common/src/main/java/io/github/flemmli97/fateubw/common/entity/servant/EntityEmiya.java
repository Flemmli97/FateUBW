package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class EntityEmiya extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DUAL_SLASH_1 = BUILDER.add("dual_slash_1", AnimationsBuilder.definition(1.4)
            .marker("attack", 0.52, 1).marker("step", 0.52, 1));
    public static final String DUAL_SLASH_2 = BUILDER.add("dual_slash_2", AnimationsBuilder.definition(1)
            .marker("attack", 0.4, 0.76).marker("step", 0.4, 0.76));
    public static final String DUAL_SLASH_3 = BUILDER.add("dual_slash_3", AnimationsBuilder.definition(0.76).marker("attack", 0.44));
    public static final String DUAL_SLASH_4 = BUILDER.add("dual_slash_4", AnimationsBuilder.definition(0.84)
            .marker("attack", 0.4).marker("step", 0.52));
    public static final String DUAL_SLASH_5 = BUILDER.add("dual_slash_5", AnimationsBuilder.definition(0.84)
            .marker("attack", 0.4).marker("step", 0.52));
    public static final String DUAL_SLASH_6 = BUILDER.add("dual_slash_6", AnimationsBuilder.definition(0.92)
            .marker("attack", 0.48).marker("leap", 0.12));

    public static final String BOW_1 = BUILDER.add("bow_1", AnimationsBuilder.definition(1.24).marker("attack", 1));
    public static final String BOW_2 = BUILDER.add("bow_2", AnimationsBuilder.definition(1.44)
            .marker("attack", 1).marker("leap", 0.2));
    public static final String CALADBOLG = BUILDER.add("caladbolg", AnimationsBuilder.definition(2.48).marker("attack", 2.16));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

//    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityEmiya>>> ATTACKS = List.of(
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.DUAL_SLASH_1)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
//                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 40)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 8),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.DUAL_SLASH_2)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
//                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 40)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 8),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.DUAL_SLASH_3)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
//                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 40)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 8),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.DUAL_SLASH_4)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
//                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 40)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 8),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.DUAL_SLASH_5)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
//                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 40)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 8),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.DUAL_SLASH_6)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
//                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 40)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 8),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.BOW_1)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.2))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.BOW_2)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 10, 1.2))), 7),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.BOW_1)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
//                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq > 25)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 14, 1.2))), 13),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.BOW_2)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
//                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq > 25)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 10, 1.2))), 9),
//            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.CALADBOLG)
//                    .cooldown(e -> e.getRandom().nextInt(30) + 10)
//                    .withCondition(Utils.npCheck())
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(8, 14, 1.3))), 15)
//    );
//    public static final List<WeightedEntry.Wrapper<IdleAction<EntityEmiya>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(6, 1.1, 2)), 6),
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 3)), 4)
//    );
//
//    public final AnimatedAttackGoal<EntityEmiya> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityEmiya> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (anim != null) {
                    if (anim.is(BOW_1, BOW_2, CALADBOLG) && !this.hasBow()) {
                        this.switchableWeapon.switchItems(false);
                    }
                } else {
                    if (this.getAnimationHandler().isCurrent(BOW_1, BOW_2, CALADBOLG)) {
                        this.switchableWeapon.switchItems(true);
                    }
                }
                return false;
            });

    public final SwitchableWeapon<EntityEmiya> switchableWeapon = new SwitchableWeapon<>(this, ItemStack.EMPTY, new ItemStack(FateItems.ARCHBOW.get()));

    private final Vector4f summonColor = new Vector4f(213 / 255f, 0, 6 / 255f, 0.7f);

    public EntityEmiya(EntityType<? extends EntityEmiya> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.KANSHOU.get()));
    }

    @Override
    public AnimationHandler<EntityEmiya> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public void setupAttack(AnimationDefinition anim) {
        if (anim.is(DUAL_SLASH_1, DUAL_SLASH_2) && this.getTarget() != null) {
            this.lookAtNow(this.getTarget(), 360, 90);
            this.targetPosition = this.getTarget().position();
        }
        super.setupAttack(anim);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(CALADBOLG)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt(0.24))
                this.startUsingItem(this.bowHand());
            if (anim.isAt("attack")) {
                if (target != null && this.getSensing().hasLineOfSight(target))
                    this.caladBolg(target);
                this.stopUsingItem();
            }
        } else if (anim.is(BOW_1)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt(0.2))
                this.startUsingItem(this.bowHand());
            if (anim.isAt("attack")) {
                if (target != null && this.getSensing().hasLineOfSight(target))
                    this.attackWithRangedAttack(target);
                this.stopUsingItem();
            }
        } else if (anim.is(BOW_2)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt("leap")) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(-1).add(0, 0.9, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.isAt(0.36))
                this.startUsingItem(this.bowHand());
            if (anim.isAt("attack")) {
                if (target != null && this.getSensing().hasLineOfSight(target))
                    this.attackWithRangedAttackBarrage(target);
                this.stopUsingItem();
            }
            this.fallDistance = 0;
        } else if (anim.is(DUAL_SLASH_1, DUAL_SLASH_2)) {
            this.getNavigation().stop();
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.32);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.targetPosition = null;
                if (this.getTarget() != null)
                    this.targetPosition = this.getTarget().position();
            }
        } else if (anim.is(DUAL_SLASH_6)) {
            this.getNavigation().stop();
            LivingEntity target = this.getTarget();
            if (anim.isAt("leap")) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().add(0, 0.24, 0);
                this.setDeltaMovement(dir);
            }
            super.handleAttack(anim);
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.25);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(DUAL_SLASH_1, DUAL_SLASH_2)) {
            width += 0.4;
            length += 0.5;
        }
        if (anim.is(DUAL_SLASH_3)) {
            width += 0.2;
            length += 0.8;
        }
        if (anim.is(DUAL_SLASH_4)) {
            width += 0.8;
            length += 0.4;
        }
        if (anim.is(DUAL_SLASH_5, DUAL_SLASH_6)) {
            width += 1;
            length += 0.5;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    public void attackWithRangedAttack(LivingEntity target) {
        ItemStack stack = this.getItemInHand(this.bowHand());
        if (!this.level().isClientSide) {
            ArcherArrow arrow = new ArcherArrow(this.level(), this, stack);
            double dX = target.getX() - this.getX();
            double dY = target.getY(0.3333333333333333) - arrow.getY();
            double dZ = target.getZ() - this.getZ();
            double l = Math.sqrt(dX * dX + dZ * dZ);
            arrow.setCritArrow(true);
            arrow.shoot(dX, dY + l * 0.13, dZ, 2.2F, 2);
            arrow.setBaseDamage(arrow.getBaseDamage() + this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.7);
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(arrow);
        }
    }

    public void attackWithRangedAttackBarrage(LivingEntity target) {
        ItemStack stack = this.getItemInHand(this.bowHand());
        for (int i = 0; i < 6; i++) {
            ArcherArrow arrow = new ArcherArrow(this.level(), this, stack);
            if (!this.level().isClientSide) {
                double dX = target.getX() - this.getX();
                double dY = target.getY(0.33) - arrow.getY();
                double dZ = target.getZ() - this.getZ();
                double l = Math.sqrt(dX * dX + dZ * dZ);
                arrow.setCritArrow(true);
                arrow.shoot(dX, dY + l * 0.13, dZ, 2.2F, 7);
                arrow.setBaseDamage(arrow.getBaseDamage() + this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.7);
                this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(arrow);
            }
        }
    }

    public void caladBolg(LivingEntity target) {
        if (!this.forcedNP && !this.useMana(this.props().hogouMana()))
            return;
        CaladBolg bolg = new CaladBolg(this.level(), this);
        if (target != null)
            bolg.shootAtEntity(target, 2F, 0);
        else
            bolg.shoot(this, this.getXRot(), this.getYRot(), 0, 2, 0);
        this.level().addFreshEntity(bolg);
        this.revealServant();
        this.switchableWeapon.switchItems(true);
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

    protected boolean hasBow() {
        return this.getMainHandItem().getItem() instanceof BowItem || this.getOffhandItem().getItem() instanceof BowItem;
    }

    protected InteractionHand bowHand() {
        return this.getMainHandItem().getItem() instanceof BowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    @Override
    public boolean flipAnimation() {
        return this.getAnimationHandler().isCurrent(BOW_1, BOW_2, CALADBOLG)
                && this.getMainHandItem().getItem() instanceof BowItem;
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