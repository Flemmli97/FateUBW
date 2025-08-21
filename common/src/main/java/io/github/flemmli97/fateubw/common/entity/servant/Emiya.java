package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.LeapInDirection;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
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
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector4f;

public class Emiya extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DUAL_SLASH_1 = BUILDER.add("dual_slash_1", AnimationsBuilder.definition(1.4)
            .marker("attack_left", 0.52).marker("attack_right", 1).marker("step", 0.52, 1));
    public static final String DUAL_SLASH_2 = BUILDER.add("dual_slash_2", AnimationsBuilder.definition(1)
            .marker("attack_left", 0.4).marker("attack_right", 0.76).marker("step", 0.4, 0.76));
    public static final String DUAL_SLASH_3 = BUILDER.add("dual_slash_3", AnimationsBuilder.definition(0.76)
            .marker("attack_left", 0.44).marker("attack_right", 0.44));
    public static final String DUAL_SLASH_4 = BUILDER.add("dual_slash_4", AnimationsBuilder.definition(0.84)
            .marker("attack_left", 0.4).marker("attack_right", 0.4).marker("step", 0.52));
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

    private final AnimationHandler<Emiya> animationHandler = new AnimationHandler<>(this, ANIMS)
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

    public final SwitchableWeapon<Emiya> switchableWeapon = new SwitchableWeapon<>(this, ItemStack.EMPTY, new ItemStack(FateItems.ARCHBOW.get()));

    private final Vector4f summonColor = new Vector4f(213 / 255f, 0, 6 / 255f, 0.7f);

    public Emiya(EntityType<? extends Emiya> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.KANSHOU.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.KANSHOU.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<BaseServant>create()
                .start(DUAL_SLASH_1).play(BehaviourUtils.cooldownedPlay(true, 16, 28))
                .condition(BehaviourUtils.ifCloserThan(7))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(DUAL_SLASH_2).play(BehaviourUtils.cooldownedPlay(true, 16, 28))
                .condition(BehaviourUtils.ifCloserThan(7))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(DUAL_SLASH_3).play(BehaviourUtils.cooldownedPlay(true, 16, 28))
                .condition(BehaviourUtils.ifCloserThan(7))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(DUAL_SLASH_4).play(BehaviourUtils.cooldownedPlay(true, 16, 28))
                .condition(BehaviourUtils.ifCloserThan(7))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(DUAL_SLASH_5).play(BehaviourUtils.cooldownedPlay(true, 16, 28))
                .condition(BehaviourUtils.ifCloserThan(7))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(DUAL_SLASH_6).play(BehaviourUtils.cooldownedPlay(true, 16, 28))
                .condition(BehaviourUtils.ifCloserThan(7))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BOW_1).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(5).max(14).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(9)
                .start(BOW_1).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .prepare(new LeapInDirection<BaseServant>().shouldLeap((owner, target) -> owner.distanceToSqr(target) < 49)
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()).scale(1.3f)))
                .end(7)
                .start(BOW_1).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .condition(BehaviourUtils.ifFurtherThan(11))
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(5).max(14).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(6)
                .start(BOW_2).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(4).max(10).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(7)
                .start(BOW_2).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .condition(BehaviourUtils.ifFurtherThan(11))
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(4).max(10).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(9)
                .start(CALADBOLG).play(BehaviourUtils.cooldownedPlay(false, 20, 30))
                .condition(BaseServant::canUseNP)
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(8).max(16).speedMod(1.3f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(45)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(4, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(6, new SetWalkTargetAwayFromTarget<>(), BehaviourUtils.moveTo()).build();
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(CALADBOLG)) {
            if (!this.getAnimationHandler().getAnimation().isPast("attack")) {
                LivingEntity target = this.getTarget();
                if (target != null)
                    return target.getEyePosition().subtract(this.getEyePosition());
            }
            return null;
        }
        return super.directionToLookAt();
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
        } else {
            if (anim.is(DUAL_SLASH_6) && anim.isAt("leap")) {
                LivingEntity target = this.getTarget();
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().add(0, 0.24, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(anim.is(DUAL_SLASH_1, DUAL_SLASH_2) ? 0.32 : 0.25);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("attack_left")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.setTargetPosition((TargetPosition) null);
            }
            if (anim.isAt("attack_right")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.setTargetPosition((TargetPosition) null);
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
        if (anim.is(DUAL_SLASH_5)) {
            width += 1;
            length += 0.5;
        }
        if (anim.is(DUAL_SLASH_6)) {
            width += 1;
            return new AABB(-width * 0.5, -0.02, -0.7, width * 0.5, this.getBbHeight() + 0.02, length + 1.5);
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public AnimationHandler<Emiya> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
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
        if (!this.attemptUseNobelPhantasm())
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