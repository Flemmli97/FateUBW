package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
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
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntityArthur extends BaseServant {

    public static final AnimatedAction SWING_1 = new AnimatedAction(0.64, 0.52, "long_sword_1");
    public static final AnimatedAction SWING_1_VAR_1 = new AnimatedAction(0.64, 0.52, "long_sword_1_2");
    public static final AnimatedAction SWING_1_VAR_2 = new AnimatedAction(0.6, 0.24, "long_sword_1_3");
    public static final AnimatedAction SWING_2 = new AnimatedAction(0.44, 0.36, "vertical_slash");
    public static final AnimatedAction INVISIBLE_BURST = new AnimatedAction(0.8, 0.28, "invisible_burst");
    public static final AnimatedAction INVISIBLE_BURST_HIT = new AnimatedAction(0.64, 0.36, "invisible_burst_hit");

    public static final AnimatedAction EXCALIBAA = new AnimatedAction(1.6, 0.6, "excalibur");
    public static final AnimatedAction SUMMON = new AnimatedAction(2., 0, "summon");
    public static final AnimatedAction[] ANIMS = {SWING_1, SWING_1_VAR_1, SWING_1_VAR_2, SWING_2, INVISIBLE_BURST, INVISIBLE_BURST_HIT, EXCALIBAA, SUMMON};

    protected static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(EntityArthur.class, EntityDataSerializers.FLOAT);

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityArthur>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.SWING_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .chain(GoalAttackAction.<EntityArthur>chainBuilder(EntityArthur.SWING_1_VAR_1)
                            .chain(EntityArthur.SWING_1_VAR_2).withPredicate(e -> e.getRandom().nextFloat() < 0.5))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.SWING_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.INVISIBLE_BURST)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(((goal, target, previous) -> goal.distanceToTargetSq > 25))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 14))), 6),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EntityArthur.EXCALIBAA)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(Utils.npCheck())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(3, 8, 1.1))), 8)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityArthur>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
    );

    public final AnimatedAttackGoal<EntityArthur> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityArthur> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeFunc(anim -> {
                if (!this.level.isClientSide()) {
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

    public final SwitchableWeapon<EntityArthur> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.EXCALIBUR.get()), ItemStack.EMPTY);

    private Vec3 burstDir;
    protected List<LivingEntity> hitEntity;

    public EntityArthur(EntityType<? extends EntityArthur> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.INVISEXCALIBUR.get()));
    }

    @Override
    public AnimationHandler<EntityArthur> getAnimationHandler() {
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
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.EXCALIBUR.get()));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
            if (!this.critHealth) {
                if (!this.level.isClientSide)
                    this.level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.servant.avalon").withStyle(ChatFormatting.GOLD), ChatType.SYSTEM, Util.NIL_UUID);
                this.critHealth = true;
            }
            if (!this.hasEffect(MobEffects.REGENERATION))
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 1, false, false));
        }
        if (this.level.isClientSide && this.duringBurst()) {
            this.setXRot(0);
            float yRot = this.entityData.get(LOCKED_YAW);
            this.yBodyRotO = yRot;
            this.yBodyRot = yRot;
            this.yRotO = yRot;
            this.setYRot(yRot);
            for (int i = 0; i < 8; i++)
                this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX(this.getRandom().nextGaussian() * 0.5), this.getY(this.getRandom().nextGaussian() * 0.5), this.getZ(this.getRandom().nextGaussian() * 0.5), 1, 1, 1);
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(EXCALIBAA)) {
            LivingEntity target = this.getTarget();
            if (target != null && !anim.isPastTick(0.28)) {
                this.lookAtNow(target, 60, 30);
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

        } else if (anim.is(INVISIBLE_BURST)) {
            if (anim.isAtTick(0.2)) {
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
                    NetworkCalls.INSTANCE.sendToTracking(new S2CScreenShake(6, 1), this);
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.05));
                    this.getAnimationHandler().setAnimation(INVISIBLE_BURST_HIT);
                }
            }
        } else
            super.handleAttack(anim);
    }

    private boolean duringBurst() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return anim != null && anim.is(INVISIBLE_BURST) && anim.isPastTick(0.28) && !anim.isPastTick(0.8);
    }

    @Override
    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        if (anim.is(INVISIBLE_BURST_HIT)) {
            super.mobAttack(anim, target, e -> {
                e.invulnerableTime -= 10;
                cons.accept(e);
            });
        }
        super.mobAttack(anim, target, cons);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (!anim.is(INVISIBLE_BURST))
            return super.calculateAttackAABB(anim, target, grow);
        Vec3 dir = this.getDeltaMovement().scale(0.5);
        return this.getBoundingBox().move(dir.x, dir.y, dir.z);
    }

    public void attackWithNP(Vec3 pos) {
        Excalibur excalibur = new Excalibur(this.level, this);
        if (pos != null)
            excalibur.setRotationTo(pos.x(), pos.y(), pos.z(), 0);
        this.level.addFreshEntity(excalibur);
        this.revealServant();
        this.releaseUsingItem();
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }
}
