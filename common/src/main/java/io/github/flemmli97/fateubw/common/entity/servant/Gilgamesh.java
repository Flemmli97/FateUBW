package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.LeapInDirection;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.PlayAnimation;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
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
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.joml.Vector4f;

public class Gilgamesh extends BaseServant {

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

    private final AnimationHandler<Gilgamesh> animationHandler = new AnimationHandler<>(this, ANIMS)
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

    public final SwitchableWeapon<Gilgamesh> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(FateItems.ENUMAELISH.get()), ItemStack.EMPTY);

    public Gilgamesh(EntityType<? extends Gilgamesh> entityType, Level level) {
        super(entityType, level);
        this.revealServant();
    }

    @Override
    public boolean hasOwnWeapon() {
        return true;
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<Gilgamesh>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<Gilgamesh>builder(ONE_HAND_1)
                        .start(ONE_HAND_2, 2, 0.2f, 1)
                        .start(ONE_HAND_3, 2, 0.2f, 1)
                        .chainChance(0.5f).build())).play(BehaviourUtils.cooldownedPlay(true, 18, 30))
                .condition(gil -> !gil.useRanged())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BehaviourUtils.of(AnimationPlayHolder.<Gilgamesh>builder(ONE_HAND_2)
                        .start(ONE_HAND_1, 2, 0.2f, 1)
                        .chainChance(0.5f).build())).play(BehaviourUtils.cooldownedPlay(true, 18, 30))
                .condition(gil -> !gil.useRanged())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BehaviourUtils.of(AnimationPlayHolder.<Gilgamesh>builder(ONE_HAND_3)
                        .start(ONE_HAND_4, 2, 0.2f, 1)
                        .chainChance(0.5f).build())).play(BehaviourUtils.cooldownedPlay(true, 18, 30))
                .condition(gil -> !gil.useRanged())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(ONE_HAND_4).play(BehaviourUtils.cooldownedPlay(true, 18, 30))
                .condition(gil -> !gil.useRanged())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BABYLON_1, BABYLON_2, BABYLON_3).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(18), 30, 50))
                .prepare(new SetWalkTargetWithinDist<Gilgamesh>()
                        .min(6).max(16).speedMod(1.2f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(35)
                .start(BABYLON_1, BABYLON_2, BABYLON_3).play((PlayAnimation<Gilgamesh>) BehaviourUtils.<Gilgamesh>cooldownedPlay(BehaviourUtils.ifCloserThan(18), 30, 50)
                        .startCondition(BehaviourUtils.ifCloserThan(16)))
                .condition(gil -> !gil.useRanged())
                .prepare(new SetWalkTargetToAttackTarget<Gilgamesh>().closeEnoughDist(BehaviourUtils.closeEnough(16)))
                .prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(35)
                .start(EA).play(BehaviourUtils.cooldownedPlay(false, 20, 35))
                .condition(BaseServant::canUseNP)
                .prepare(new SetWalkTargetWithinDist<Gilgamesh>()
                        .min(4).max(8).speedMod(1.1f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(40)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<Gilgamesh>builder()
                .add(3, Gilgamesh::useRanged, new Idle<>())
                .add(10, Gilgamesh::useRanged, new StrafeTarget<Gilgamesh>()
                        .strafeDistance(14))
                .add(7, Gilgamesh::useRanged,
                        new SetWalkTargetAwayFromTarget<Gilgamesh>()
                                .radius(7).speedMod(1.1f), BehaviourUtils.moveTo())
                .add(8, gil -> !gil.useRanged(), new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(4, gil -> !gil.useRanged(),
                        new SetWalkTargetAwayFromTarget<Gilgamesh>()
                                .radius(7).speedMod(1.1f), BehaviourUtils.moveTo())
                .add(3, gil -> BehaviourUtils.ifCloserThan(7).test(gil), new LeapInDirection<Gilgamesh>()
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()))
                        .whenStarting(e -> BrainUtils.clearMemory(e, MemoryModuleType.ATTACK_COOLING_DOWN))).build();
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
    public void setupAttack(AnimationDefinition anim) {
        if (this.getAnimationHandler().isCurrent(BABYLON_1, BABYLON_2, BABYLON_3)) {
            BrainUtils.clearMemory(this, MemoryModuleType.LOOK_TARGET);
            this.getNavigation().stop();
            return;
        }
        super.setupAttack(anim);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(EA)) {
            if (anim.isAt(0.4)) {
                LivingEntity target = this.getTarget();
                if (target != null)
                    this.setTargetPosition(target);
                else
                    this.setTargetPosition(TargetPosition.of(this.position().add(this.getLookAngle().scale(8))));
            }
            if (anim.isAt("attack")) {
                this.ea(this.getTargetPosition().asVec(this.getEyePosition()));
            }
        } else if (anim.is(BABYLON_1, BABYLON_2, BABYLON_3)) {
            LivingEntity target = this.getTarget();
            if (!anim.isPast("attack") && target != null) {
                this.getLookControl().setLookAt(target, 60.0F, 30.0F);
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
    public AnimationHandler<Gilgamesh> getAnimationHandler() {
        return this.animationHandler;
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
        if (!this.attemptUseNobelPhantasm())
            return;
        EnumaElish ea = new EnumaElish(this.level(), this);
        if (pos != null)
            ea.setRotationTo(pos.x(), pos.y(), pos.z(), 0);
        this.level().addFreshEntity(ea);
        this.revealServant();
        this.stopUsingItem();
        this.getMainHandItem().remove(FateDataComponents.GLOWING_ITEM.get());
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