package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.entity.servant.ai.ArthurAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityArthur extends BaseServant {

    public static final AnimatedAction SWING_1 = new AnimatedAction(0.76, 0.52, "long_sword_1");
    public static final AnimatedAction SWING_1_VAR_1 = new AnimatedAction(0.76, 0.52, "long_sword_1_2");
    public static final AnimatedAction SWING_1_VAR_2 = new AnimatedAction(0.6, 0.24, "long_sword_1_3");
    public static final AnimatedAction SWING_2 = new AnimatedAction(0.68, 0.36, "long_sword_2");

    public static final AnimatedAction EXCALIBAA = new AnimatedAction(1.6, 0.6, "excalibur");
    public static final AnimatedAction[] ANIMS = {SWING_1, SWING_1_VAR_1, SWING_1_VAR_2, SWING_2, EXCALIBAA};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityArthur>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(SWING_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 2))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(SWING_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 2))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityArthur>(EXCALIBAA)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition(Utils.npCheck())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 8, true, true))), 8)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityArthur>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 5),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
    );

    public final AnimatedAttackGoal<EntityArthur> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    public final ArthurAttackGoal attackAI = new ArthurAttackGoal(this);

    private final AnimationHandler<EntityArthur> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeFunc(anim -> {
                if (!this.level.isClientSide()) {
                    if (anim == null) {
                        if (this.getAnimationHandler().isCurrent(EXCALIBAA)) {
                            this.switchableWeapon.switchItems(true);
                        }
                        if (this.getAnimationHandler().isCurrent(SWING_1)) {
                            if (this.getRandom().nextFloat() < 0.5) {
                                this.getAnimationHandler().setAnimation(this.getRandom().nextBoolean() ? SWING_1_VAR_2 : SWING_1_VAR_1);
                                return true;
                            }
                        }
                    } else if (anim.is(EXCALIBAA)) {
                        this.switchableWeapon.switchItems(false);
                        this.startUsingItem(InteractionHand.MAIN_HAND);
                    }
                }
                return false;
            });

    public final SwitchableWeapon<EntityArthur> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.EXCALIBUR.get()), ItemStack.EMPTY);

    public EntityArthur(EntityType<? extends EntityArthur> entityType, Level level) {
        super(entityType, level, LibEntities.ARTHUR + ".hogou");
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.INVISEXCALIBUR.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(EXCALIBAA.getID());
        return anim.getID().equals(SWING_1.getID());
    }

    @Override
    public AnimationHandler<EntityArthur> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attackAI);
        else
            this.goalSelector.addGoal(0, this.attackAI);
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
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(EXCALIBAA)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAt(target, 0, 0);
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

        } else
            super.handleAttack(anim);
    }

    public void attackWithNP(Vec3 pos) {
        Excalibur excalibur = new Excalibur(this.level, this);
        if (pos != null)
            excalibur.setRotationTo(pos.x(), pos.y(), pos.z(), 0);
        this.level.addFreshEntity(excalibur);
        this.revealServant();
        this.releaseUsingItem();
    }
}
