package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityCuchulainn extends BaseServant {

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

//    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityCuchulainn>>> ATTACKS = List.of(
//            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.SPEAR_1)
//                    .cooldown(e -> e.getRandom().nextInt(18) + 10)
//                    .chain(GoalAttackAction.<EntityCuchulainn>chainBuilder(EntityCuchulainn.SPEAR_2, 2, 0.24f, 1)
//                            .or(EntityCuchulainn.SPEAR_3, 2, 0.24f, 1)
//                            .withChance(0.6f))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 5),
//            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.SPEAR_2)
//                    .cooldown(e -> e.getRandom().nextInt(18) + 10)
//                    .chain(GoalAttackAction.<EntityCuchulainn>chainBuilder(EntityCuchulainn.SPEAR_1, 2, 0.24f, 2)
//                            .chain(EntityCuchulainn.SPEAR_3, 2, 0.24f)
//                            .or(EntityCuchulainn.SPEAR_1, 2, 0.24f, 1)
//                            .withChance(0.6f))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 5),
//            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.SPEAR_4)
//                    .cooldown(e -> e.getRandom().nextInt(18) + 10)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(3, 5, 1.2))), 6),
//            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.SPEAR_5)
//                    .cooldown(e -> e.getRandom().nextInt(18) + 10)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(3, 5, 1.2))), 6),
//            WeightedEntry.wrap(new GoalAttackAction<EntityCuchulainn>(EntityCuchulainn.GAE_BOLG)
//                    .cooldown(e -> e.getRandom().nextInt(18) + 10)
//                    .withCondition(Utils.npCheck())
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 7, 1.3))), 15)
//    );
//    public static final List<WeightedEntry.Wrapper<IdleAction<EntityCuchulainn>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 6)
//    );
//
//    public final AnimatedAttackGoal<EntityCuchulainn> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private int gaeBolgThrowTick;

    private final AnimationHandler<EntityCuchulainn> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityCuchulainn(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.GAEBOLG.get()));
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
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public void tick() {
        super.tick();
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
    public void handleAttack(AnimationState anim) {
        if (anim.is(GAE_BOLG)) {
            if (anim.isAt(0.16)) {
                Vec3 dir = this.getTarget() != null ? this.getTarget().position().subtract(this.position()) : this.position().add(this.getLookAngle());
                this.targetPosition = this.getTarget() != null ? EntityUtils.getStraightProjectileTarget(this.position()
                                .add(0, this.getEyeHeight() - 0.1, 0), this.getTarget())
                        .add(this.getTarget().getDeltaMovement().scale(2)) : this.position().add(this.getLookAngle().scale(10));
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(-1).add(0, 1, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.isAt("attack")) {
                this.gaeBolg(this.targetPosition);
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

    public void gaeBolg(Vec3 pos) {
        if (!this.forcedNP && !this.useMana(this.props().hogouMana()))
            return;
        this.forcedNP = false;
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
    protected String getSummonAnimation() {
        return SUMMON;
    }
}
