package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityCuchulainn extends BaseServant {

    public static final AnimatedAction SPEAR_1 = new AnimatedAction(0.76, 0.52, "spear_1");
    public static final AnimatedAction SPEAR_2 = new AnimatedAction(0.64, 0.28, "spear_2");
    public static final AnimatedAction SPEAR_3 = new AnimatedAction(0.6, 0.36, "spear_3");
    public static final AnimatedAction SPEAR_STAB = new AnimatedAction(0.64, 0.32, "spear_stab");
    public static final AnimatedAction SPEAR_STAB_2 = new AnimatedAction(0.68, 0.32, "spear_stab_2");

    private static final AnimatedAction GAE_BOLG = new AnimatedAction(0.92, 0.6, "gae_bolg");
    private static final AnimatedAction[] ANIMS = {SPEAR_1, SPEAR_2, SPEAR_3, SPEAR_STAB, SPEAR_STAB_2, GAE_BOLG};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityCuchulainn>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.SPEAR_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .chain(GoalAttackAction.<EntityCuchulainn>chainBuilder(EntityCuchulainn.SPEAR_2)
                            .withPredicate(e -> e.getRandom().nextFloat() < 0.5))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.SPEAR_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .chain(GoalAttackAction.<EntityCuchulainn>chainBuilder(
                                    List.of(new GoalAttackAction.ChainedAction<>(EntityCuchulainn.SPEAR_1, e -> 0),
                                            new GoalAttackAction.ChainedAction<>(EntityCuchulainn.SPEAR_3, e -> 0)))
                            .withPredicate(e -> e.getRandom().nextFloat() < 0.5))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.SPEAR_3)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.SPEAR_STAB)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.SPEAR_STAB_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.GAE_BOLG)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(Utils.npCheck())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 7, 1.1))), 8)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityCuchulainn>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 6)
    );

    public final AnimatedAttackGoal<EntityCuchulainn> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private int gaeBolgThrowTick;

    private final AnimationHandler<EntityCuchulainn> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityCuchulainn(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GAEBOLG.get()));
    }

    @Override
    public AnimationHandler<EntityCuchulainn> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
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
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.gaeBolgThrowTick = Math.max(0, --this.gaeBolgThrowTick);
            if (this.gaeBolgThrowTick == 1 && this.getMainHandItem().getItem() != ModItems.GAEBOLG.get())
                this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.GAEBOLG.get()));
            if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
                if (!this.critHealth) {
                    this.level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.servant.cuchulainn").withStyle(ChatFormatting.GOLD), ChatType.SYSTEM, Util.NIL_UUID);
                    this.critHealth = true;
                }
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 2, false, false));
            }
        }
    }


    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(GAE_BOLG)) {
            if (anim.isAtTick(0.16)) {
                Vec3 dir = this.getTarget() != null ? this.getTarget().position().subtract(this.position()) : this.position().add(this.getLookAngle());
                this.targetPosition = this.getTarget() != null ? EntityUtil.getStraightProjectileTarget(this.position()
                        .add(0, this.getEyeHeight() - 0.1, 0), this.getTarget()) : this.position().add(this.getLookAngle().scale(10));
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(-1).add(0, 1, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.canAttack()) {
                if (!this.forcedNP)
                    this.useMana(this.props().hogouMana());
                this.attackWithNP(this.targetPosition);
                this.forcedNP = false;
            }

        } else
            super.handleAttack(anim);
    }

    public void attackWithNP(Vec3 pos) {
        GaeBolg gaeBolg = new GaeBolg(this.level, this);
        gaeBolg.shootAtPosition(pos.x(), pos.y(), pos.z(), 1.5F, 0);
        this.level.addFreshEntity(gaeBolg);
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        this.gaeBolgThrowTick = 100;
        this.revealServant();
    }

    public void retrieveGaeBolg() {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.GAEBOLG.get()));
        this.gaeBolgThrowTick = 0;
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
}
