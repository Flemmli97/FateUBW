package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityEmiya extends BaseServant {

    private static final AnimatedAction MELEE_1 = new AnimatedAction(0.68, 0.36, "dual_slash_1");
    private static final AnimatedAction MELEE_2 = new AnimatedAction(0.64, 0.32, "dual_slash_2");
    private static final AnimatedAction MELEE_3 = new AnimatedAction(1., 0.4, "dual_slash_3");
    private static final AnimatedAction MELEE_4 = new AnimatedAction(0.84, 0.48, "dual_slash_4");

    private static final AnimatedAction BOW = new AnimatedAction(1, 0.76, "bow");
    private static final AnimatedAction JUMP_SHOT = new AnimatedAction(1.28, 0.92, "jump_shot");
    private static final AnimatedAction CALADBOLG = new AnimatedAction(1.28, 1.04, "caladbolg");
    private static final AnimatedAction[] ANIMS = {MELEE_1, MELEE_2, MELEE_3, MELEE_4, BOW, JUMP_SHOT, CALADBOLG};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityEmiya>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.MELEE_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 5 * 5)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 8),
            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.MELEE_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 5 * 5)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 8),
            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.MELEE_3)
                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 5 * 5)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 8),
            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.MELEE_4)
                    .cooldown(e -> e.getRandom().nextInt(15) + 7)
                    .withCondition((goal, target, previous) -> goal.distanceToTargetSq < 5 * 5)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(5, 6, 1.1))), 8),
            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.BOW)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.JUMP_SHOT)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 10, 1.1))), 7),
            WeightedEntry.wrap(new GoalAttackAction<EntityEmiya>(EntityEmiya.CALADBOLG)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(Utils.npCheck())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(8, 14, 1.2))), 15)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityEmiya>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(6, 1.1, 2)), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 3)), 4)
    );

    public final AnimatedAttackGoal<EntityEmiya> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityEmiya> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> {
                if (anim != null) {
                    if (anim.is(BOW, JUMP_SHOT, CALADBOLG) && !this.hasBow()) {
                        this.switchableWeapon.switchItems(false);
                    }
                } else {
                    if (this.getAnimationHandler().isCurrent(BOW, JUMP_SHOT, CALADBOLG)) {
                        this.switchableWeapon.switchItems(true);
                    }
                }
            });

    public final SwitchableWeapon<EntityEmiya> switchableWeapon = new SwitchableWeapon<>(this, ItemStack.EMPTY, new ItemStack(ModItems.ARCHBOW.get()));

    public EntityEmiya(EntityType<? extends EntityEmiya> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.KANSHOU.get()));
        //this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.bakuya.get()));
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
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attack);
        else
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(CALADBOLG)) {
            LivingEntity target = this.getTarget();
            if (anim.isAtTick(0.24))
                this.startUsingItem(this.bowHand());
            if (anim.canAttack()) {
                if (target != null && this.getSensing().hasLineOfSight(target))
                    this.attackWithNP(target);
                this.stopUsingItem();
            }
        } else if (anim.is(BOW)) {
            LivingEntity target = this.getTarget();
            if (anim.isAtTick(0.2))
                this.startUsingItem(this.bowHand());
            if (anim.canAttack()) {
                if (target != null && this.getSensing().hasLineOfSight(target))
                    this.attackWithRangedAttack(target);
                this.stopUsingItem();
            }
        } else if (anim.is(JUMP_SHOT)) {
            LivingEntity target = this.getTarget();
            if (anim.isAtTick(0.12)) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(-1).add(0, 0.9, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.isAtTick(0.36))
                this.startUsingItem(this.bowHand());
            if (anim.canAttack()) {
                if (target != null && this.getSensing().hasLineOfSight(target))
                    this.attackWithRangedAttackBarrage(target);
                this.stopUsingItem();
            }
            this.fallDistance = 0;
        } else if (anim.is(MELEE_3)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null) {
                this.lookAtNow(this.getTarget(), 360, 90);
                this.targetPosition = this.getTarget().position();
            }
            boolean first = anim.canAttack();
            if (first || anim.isAtTick(0.76)) {
                this.mobAttack(anim, this.getTarget(), e -> {
                    this.doHurtTarget(e);
                    if (first)
                        e.invulnerableTime = 10;
                });
                this.targetPosition = null;
            }
        } else if (anim.is(MELEE_4)) {
            this.getNavigation().stop();
            LivingEntity target = this.getTarget();
            if (anim.isAtTick(0.12)) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().add(0, 0.24, 0);
                this.setDeltaMovement(dir);
            }
            super.handleAttack(anim);
        } else
            super.handleAttack(anim);
    }

    public void attackWithRangedAttack(LivingEntity target) {
        ArcherArrow arrow = new ArcherArrow(this.level, this);
        if (!this.level.isClientSide) {
            double dX = target.getX() - this.getX();
            double dY = target.getY(0.3333333333333333) - arrow.getY();
            double dZ = target.getZ() - this.getZ();
            double l = Math.sqrt(dX * dX + dZ * dZ);
            arrow.shoot(dX, dY + l * 0.13, dZ, 2.2F, 2);
            arrow.setBaseDamage(arrow.getBaseDamage() + 5.0);
            arrow.setKnockback(0);
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(arrow);
        }
    }

    public void attackWithRangedAttackBarrage(LivingEntity target) {
        for (int i = 0; i < 6; i++) {
            ArcherArrow arrow = new ArcherArrow(this.level, this);
            if (!this.level.isClientSide) {
                double dX = target.getX() - this.getX();
                double dY = target.getY(0.3333333333333333) - arrow.getY();
                double dZ = target.getZ() - this.getZ();
                double l = Math.sqrt(dX * dX + dZ * dZ);
                arrow.shoot(dX, dY + l * 0.13, dZ, 2.2F, 7);
                arrow.setBaseDamage(arrow.getBaseDamage() + 5.0);
                arrow.setKnockback(0);
                this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level.addFreshEntity(arrow);
            }
        }
    }

    public void attackWithNP(LivingEntity target) {
        if (target != null) {
            CaladBolg bolg = new CaladBolg(this.level, this);
            bolg.shootAtEntity(target, 2F, 0);
            this.level.addFreshEntity(bolg);
            this.revealServant();
            this.switchableWeapon.switchItems(true);
        }
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

    protected boolean hasBow() {
        return this.getMainHandItem().getItem() instanceof BowItem || this.getOffhandItem().getItem() instanceof BowItem;
    }

    protected InteractionHand bowHand() {
        return this.getMainHandItem().getItem() instanceof BowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    @Override
    public boolean flipAnimation() {
        return this.getAnimationHandler().isCurrent(BOW, JUMP_SHOT, CALADBOLG)
                && this.getMainHandItem().getItem() instanceof BowItem;
    }
}