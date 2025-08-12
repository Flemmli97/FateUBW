package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityArthur extends BaseServant {

    public static final AnimatedAction TWO_HAND_1 = AnimatedAction.builder(0.78, "two_hand_1")
            .marker("attack", 0.64).marker("step", 0.68)
            .marker(EntityTrailProvider.TRAIL_START, 0.36).build();
    public static final AnimatedAction TWO_HAND_2 = AnimatedAction.builder(0.7, "two_hand_2")
            .marker("attack", 0.56).marker("step", 0.6)
            .marker(EntityTrailProvider.TRAIL_START, 0.36).build();
    public static final AnimatedAction TWO_HAND_3 = AnimatedAction.builder(0.7, "two_hand_3")
            .marker("attack", 0.56).marker("step", 0.6)
            .marker(EntityTrailProvider.TRAIL_START, 0.4).build();
    public static final AnimatedAction TWO_HAND_4 = AnimatedAction.builder(0.7, "two_hand_4")
            .marker("attack", 0.56).marker("step", 0.6)
            .marker(EntityTrailProvider.TRAIL_START, 0.36).build();
    public static final AnimatedAction ONE_HAND_1 = AnimatedAction.builder(0.68, "one_hand_1")
            .marker("attack", 0.48).marker("step", 0.48)
            .marker(EntityTrailProvider.TRAIL_START, 0.32).build();
    public static final AnimatedAction STAB_1 = AnimatedAction.builder(1.02, "stab_1").marker("attack", 0.56).build();
    public static final AnimatedAction INVISIBLE_BURST = AnimatedAction.builder(0.8, "invisible_burst").marker("attack", 0.28).build();
    public static final AnimatedAction INVISIBLE_BURST_HIT = AnimatedAction.builder(0.76, "invisible_burst_hit").marker("attack", 0.44).build();

    public static final AnimatedAction EXCALIBAA = AnimatedAction.builder(1.68, "excalibur").marker("attack", 0.72).build();
    public static final AnimatedAction SUMMON = new AnimatedAction(2., "summon");
    public static final AnimatedAction[] ANIMS = {TWO_HAND_1, TWO_HAND_2, TWO_HAND_3, TWO_HAND_4, ONE_HAND_1, STAB_1, INVISIBLE_BURST, INVISIBLE_BURST_HIT, EXCALIBAA, SUMMON};

    protected static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(EntityArthur.class, EntityDataSerializers.FLOAT);

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityArthur>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.TWO_HAND_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
                    .chain(GoalAttackAction.<EntityArthur>chainBuilder(EntityArthur.TWO_HAND_2, 2, 0.24f, 1)
                            .or(EntityArthur.TWO_HAND_3, 2, 0.24f, 1)
                            .withChance(0.5f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.TWO_HAND_2)
                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
                    .chain(GoalAttackAction.<EntityArthur>chainBuilder(EntityArthur.TWO_HAND_4, 2, 0.24f, 1)
                            .or(EntityArthur.TWO_HAND_1, 2, 0.24f, 1)
                            .or(EntityArthur.TWO_HAND_1, 2, 0.24f, 1)
                            .chain(EntityArthur.STAB_1, 2, 6.4f)
                            .withChance(0.5f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.TWO_HAND_3)
                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
                    .chain(GoalAttackAction.<EntityArthur>chainBuilder(EntityArthur.TWO_HAND_1, 2, 0.24f, 1)
                            .or(EntityArthur.ONE_HAND_1, 2, 0.24f, 1)
                            .withChance(0.5f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.TWO_HAND_4)
                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
                    .chain(GoalAttackAction.<EntityArthur>chainBuilder(EntityArthur.TWO_HAND_2, 2, 0.24f, 1)
                            .or(EntityArthur.TWO_HAND_3, 2, 0.24f, 1)
                            .withChance(0.5f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.ONE_HAND_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 5)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.STAB_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.INVISIBLE_BURST)
                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
                    .withCondition(((goal, target, previous) -> goal.distanceToTargetSq > 25))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 14))), 6),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.EXCALIBAA)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .withCondition(Utils.npCheck())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(3, 8, 1.2))), 15)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityArthur>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
    );

    public final AnimatedAttackGoal<EntityArthur> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityArthur> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (!this.level().isClientSide()) {
            if (anim == null) {
                this.burstDir = null;
                if (this.getAnimationHandler().isCurrent(EXCALIBAA)) {
                    this.switchableWeapon.switchItems(true);
                }
            } else if (anim.is(EXCALIBAA)) {
                this.switchableWeapon.switchItems(false);
                this.startUsingItem(InteractionHand.MAIN_HAND);
            } else if (anim.is(INVISIBLE_BURST)) {
                this.hitEntity = null;
                this.burstDir = null;
            }
        }
        return false;
    });

    public final SwitchableWeapon<EntityArthur> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(FateItems.EXCALIBUR.get()), ItemStack.EMPTY);

    private Vec3 burstDir;
    protected List<LivingEntity> hitEntity;

    public EntityArthur(EntityType<? extends EntityArthur> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.INVISEXCALIBUR.get()));
    }

    @Override
    public Goal getAttackAI() {
        return this.attack;
    }

    @Override
    public AnimationHandler<EntityArthur> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.EXCALIBUR.get()));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
            if (!this.critHealth) {
                if (!this.level().isClientSide)
                    this.level().getServer().getPlayerList().broadcastMessage(Component.translatable("fateubw.chat.servant.avalon").withStyle(ChatFormatting.GOLD), ChatType.SYSTEM);
                this.critHealth = true;
            }
            if (!this.hasEffect(MobEffects.REGENERATION))
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 1, false, false));
        }
        if (this.level().isClientSide) {
            if (this.duringBurst()) {
                this.setXRot(0);
                float yRot = this.entityData.get(LOCKED_YAW);
                this.yBodyRotO = yRot;
                this.yBodyRot = yRot;
                this.yRotO = yRot;
                this.setYRot(yRot);
                for (int i = 0; i < 8; i++)
                    this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX(this.getRandom().nextGaussian() * 0.5), this.getY(this.getRandom().nextGaussian() * 0.5), this.getZ(this.getRandom().nextGaussian() * 0.5), 1, 1, 1);
            }
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(EXCALIBAA)) {
            LivingEntity target = this.getTarget();
            if (target != null && !anim.isPast(0.28)) {
                this.lookAtNow(target, 60, 30);
            }
            if (anim.isAt(0.4)) {
                this.targetPosition = target != null ? EntityUtil.getStraightProjectileTarget(this.position()
                        .add(0, this.getEyeHeight() - 0.1, 0), target) :
                        this.position().add(this.getLookAngle().scale(8));
            }
            if (anim.isAt(0.72)) {
                this.excalibur(this.targetPosition);
            }

        } else if (anim.is(INVISIBLE_BURST)) {
            if (anim.isAt(0.2)) {
                Vec3 dir = this.getTarget() != null ? this.getTarget().position().subtract(this.position()) : this.position().add(this.getLookAngle());
                this.burstDir = dir.normalize().scale(0.95);
                this.lookAt(EntityAnchorArgument.Anchor.EYES, this.position().add(dir));
                this.entityData.set(LOCKED_YAW, this.getYHeadRot());
            }
            if (this.duringBurst()) {
                this.setDeltaMovement(this.burstDir);
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, this.getTarget(), e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
                if (!this.hitEntity.isEmpty()) {
                    S2CScreenShake.sendAround(this, 12, 8, 2);
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.05));
                    this.getAnimationHandler().setAnimation(INVISIBLE_BURST_HIT);
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
        if (anim.is(TWO_HAND_1, TWO_HAND_2, TWO_HAND_3, TWO_HAND_4)) {
            width += 1.5;
            length += 1.1;
        }
        if (anim.is(ONE_HAND_1)) {
            width += 0.3;
            length += 1.25;
        }
        if (anim.is(STAB_1)) {
            width += 0.3;
            length += 1.4;
        }
        if (anim.is(INVISIBLE_BURST_HIT)) {
            width += 1.4;
            length += 1;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    private boolean duringBurst() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return anim != null && anim.is(INVISIBLE_BURST) && anim.isPast(0.28) && !anim.isPast(0.8);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (!anim.is(INVISIBLE_BURST))
            return super.calculateAttackAABB(anim, target, grow);
        double width = this.getBbWidth();
        double speed = Math.max(width, this.getDeltaMovement().length() - width);
        return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                .inflate(grow, 0, grow).expandTowards(0, 0, speed), this.getYRot(), this.getXRot(), this.position());
    }

    public void excalibur(Vec3 pos) {
        if (!this.forcedNP && !this.useMana(this.props().hogouMana()))
            return;
        this.forcedNP = false;
        Excalibur excalibur = new Excalibur(this.level, this);
        if (pos != null)
            excalibur.setRotationTo(pos.x(), pos.y(), pos.z(), 0);
        this.level().addFreshEntity(excalibur);
        this.revealServant();
        this.releaseUsingItem();
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }
}
