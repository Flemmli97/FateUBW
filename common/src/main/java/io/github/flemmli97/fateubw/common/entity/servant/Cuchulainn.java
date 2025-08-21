package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.LeapInDirection;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

public class Cuchulainn extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SPEAR_1 = BUILDER.add("spear_1", AnimationsBuilder.definition(0.62)
            .marker("attack", 0.48).marker("step", 0.52));
    public static final String SPEAR_2 = BUILDER.add("spear_2", AnimationsBuilder.definition(0.62)
            .marker("attack", 0.44).marker("step", 0.52));
    public static final String SPEAR_3 = BUILDER.add("spear_3", AnimationsBuilder.definition(0.62)
            .marker("attack", 0.52));
    public static final String SPEAR_4 = BUILDER.add("spear_4", AnimationsBuilder.definition(0.86)
            .marker("attack", 0.44));
    public static final String SPEAR_5 = BUILDER.add("spear_5", AnimationsBuilder.definition(0.86)
            .marker("attack", 0.44));
    private static final String GAE_BOLG = BUILDER.add("gae_bolg", AnimationsBuilder.definition(1)
            .marker("attack", 0.72));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private int gaeBolgThrowTick;

    private final AnimationHandler<Cuchulainn> animationHandler = new AnimationHandler<>(this, ANIMS);

    public Cuchulainn(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.GAEBOLG.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.GAEBOLG.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<BaseServant>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(SPEAR_1)
                        .start(SPEAR_2, 2, 0.24f, 1)
                        .start(SPEAR_3, 2, 0.24f, 1)
                        .chainChance(0.6f).build())).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(SPEAR_2)
                        .start(SPEAR_1, 2, 0.24f, 2)
                        .chain(Cuchulainn.SPEAR_3, 2, 0.24f)
                        .start(SPEAR_1, 2, 0.24f, 1)
                        .chainChance(0.6f).build())).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(SPEAR_4).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(SPEAR_5).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(GAE_BOLG).play(BehaviourUtils.cooldownedPlay(false, 20, 27))
                .condition(BaseServant::canUseNP)
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(4).max(8).speedMod(1.3f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(30)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(7, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(2, BehaviourUtils.ifCloserThan(7), new LeapInDirection<BaseServant>()
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()).scale(1.5f))
                        .whenStarting(e -> BrainUtils.clearMemory(e, MemoryModuleType.ATTACK_COOLING_DOWN))).build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            this.gaeBolgThrowTick = Math.max(0, --this.gaeBolgThrowTick);
            if (this.gaeBolgThrowTick == 1 && this.getMainHandItem().getItem() != FateItems.GAEBOLG.get())
                this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(FateItems.GAEBOLG.get()));
            if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
                if (!this.critHealth) {
                    this.level().getServer().getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.servant.cuchulainn").withStyle(ChatFormatting.GOLD), true);
                    this.critHealth = true;
                }
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 2, false, false));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GaeBolgTick", this.gaeBolgThrowTick);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.gaeBolgThrowTick = tag.getInt("GaeBolgTick");
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(GAE_BOLG)) {
            if (anim.isAt(0.16)) {
                LivingEntity target = this.getTarget();
                if (target != null)
                    this.setTargetPosition(target);
                else
                    this.setTargetPosition(TargetPosition.of(this.position().add(this.getLookAngle().scale(10))));
                Vec3 dir = this.getTarget() != null ? this.getTarget().position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(-1).add(0, 1, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.isAt("attack")) {
                this.gaeBolg(this.getTargetPosition().asVec(this.position()));
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.35);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(SPEAR_1, SPEAR_2)) {
            width += 1.4;
            length += 1.3;
        }
        if (anim.is(SPEAR_3)) {
            width += 0.1;
            length += 1.7;
        }
        if (anim.is(SPEAR_4, SPEAR_5)) {
            width += 0.1;
            length += 1.8;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public AnimationHandler<Cuchulainn> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void gaeBolg(Vec3 pos) {
        if (!this.attemptUseNobelPhantasm())
            return;
        GaeBolg gaeBolg = new GaeBolg(this.level(), this);
        gaeBolg.shootAtPosition(pos.x(), pos.y(), pos.z(), 1.5F, 0);
        this.level().addFreshEntity(gaeBolg);
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        this.gaeBolgThrowTick = 100;
        this.revealServant();
    }

    public void retrieveGaeBolg() {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(FateItems.GAEBOLG.get()));
        this.gaeBolgThrowTick = 0;
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }
}
