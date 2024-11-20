package io.github.flemmli97.fateubw.common.entity.servant;


import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.common.entity.StandingVehicle;
import io.github.flemmli97.fateubw.common.entity.minions.GordiusWheel;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
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
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityIskander extends BaseServant {

    public static final AnimatedAction MELEE_1 = new AnimatedAction(0.56, 0.4, "horizontal_slash");
    public static final AnimatedAction MELEE_2 = new AnimatedAction(0.48, 0.4, "slash_1");
    public static final AnimatedAction MELEE_3 = new AnimatedAction(0.48, 0.4, "slash_2");
    public static final AnimatedAction MELEE_4 = new AnimatedAction(0.44, 0.36, "vertical_slash");

    public static final AnimatedAction MOUNT_STAND_MELEE_1 = new AnimatedAction(0.48, 0.4, "slash_mounted_standing_1");
    public static final AnimatedAction MOUNT_STAND_MELEE_2 = new AnimatedAction(0.48, 0.4, "slash_mounted_standing_2");
    public static final AnimatedAction MOUNT_MELEE_1 = new AnimatedAction(0.48, 0.4, "slash_mounted_1");
    public static final AnimatedAction MOUNT_MELEE_2 = new AnimatedAction(0.48, 0.4, "slash_mounted_2");

    private static final AnimatedAction CHARIOT = new AnimatedAction(1.48, 0.68, "chariot_summon");
    private static final AnimatedAction SUMMON_HORSE = AnimatedAction.copyOf(CHARIOT, "horse");
    public static final AnimatedAction SUMMON = new AnimatedAction(2., 0, "summon");
    private static final AnimatedAction[] ANIMS = {MELEE_1, MELEE_2, MELEE_3, MELEE_4, MOUNT_STAND_MELEE_1, MOUNT_STAND_MELEE_2, MOUNT_MELEE_1, MOUNT_MELEE_2, CHARIOT, SUMMON_HORSE, SUMMON};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityIskander>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MELEE_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition((goal, target, previous) -> !goal.attacker.isPassenger())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MELEE_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition((goal, target, previous) -> !goal.attacker.isPassenger())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MELEE_3)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition((goal, target, previous) -> !goal.attacker.isPassenger())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MELEE_4)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition((goal, target, previous) -> !goal.attacker.isPassenger())
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MOUNT_STAND_MELEE_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
                    .withCondition((goal, target, previous) -> goal.attacker.useStandingAnim())
                    .prepare(() -> new WrappedRunner<>(e -> e.getRandom().nextInt(10) + 10, new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MOUNT_STAND_MELEE_2)
                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
                    .withCondition((goal, target, previous) -> goal.attacker.useStandingAnim())
                    .prepare(() -> new WrappedRunner<>(e -> e.getRandom().nextInt(10) + 10, new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MOUNT_MELEE_1)
                    .cooldown(e -> e.getRandom().nextInt(25) + 15)
                    .withCondition((goal, target, previous) -> goal.attacker.useSittingAnim())
                    .prepare(() -> new WrappedRunner<>(e -> e.getRandom().nextInt(10) + 10, new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MOUNT_MELEE_2)
                    .cooldown(e -> e.getRandom().nextInt(25) + 15)
                    .withCondition((goal, target, previous) -> goal.attacker.useSittingAnim())
                    .prepare(() -> new WrappedRunner<>(e -> e.getRandom().nextInt(10) + 10, new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.SUMMON_HORSE)
                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
                    .withCondition((goal, target, prev) -> !goal.attacker.isPassenger())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(5, 8, 1.1))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.CHARIOT)
                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
                    .withCondition((goal, target, prev) -> !goal.attacker.isPassenger() && (goal.attacker.canUseNP() && goal.attacker.getOwner() == null && goal.attacker.getMana() >= goal.attacker.props().hogouMana()) || goal.attacker.forcedNP)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(5, 8, 1.1))), 14)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityIskander>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<EntityIskander>(8, 6))
                    .withCondition(((goal, target) -> goal.attacker.isPassenger())), 7),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
    );

    public final AnimatedAttackGoal<EntityIskander> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityIskander> animationHandler = new AnimationHandler<>(this, ANIMS);
    private final Vector4f summonColor = new Vector4f(112 / 255f, 23 / 255f, 21 / 255f, 0.7f);

    public EntityIskander(EntityType<? extends EntityIskander> entityType, Level level) {
        super(entityType, level);
        this.canUseNP = true;
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    protected boolean useStandingAnim() {
        return StandingVehicle.stand(this.getVehicle());
    }

    protected boolean useSittingAnim() {
        return this.getVehicle() != null && !(this.getVehicle() instanceof StandingVehicle);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.KUPRIOTS.get()));
    }

    @Override
    public AnimationHandler<EntityIskander> getAnimationHandler() {
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
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return super.hurt(damageSource, damage);
        } else if (this.getVehicle() != null) {
            damage *= 0.5;
            this.getVehicle().hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(CHARIOT, SUMMON_HORSE)) {
            LivingEntity target = this.getTarget();
            if (target != null && !anim.isPastTick(0.28)) {
                this.lookAtNow(target, 60, 30);
            }
            this.level.getEntities(EntityTypeTest.forClass(LivingEntity.class),
                            this.getBoundingBox().inflate(12, 8, 12),
                            this.targetPred)
                    .forEach(e -> {
                        Vec3 dir = e.position().subtract(this.position());
                        boolean none = dir.x() == 0 && dir.z() == 0;
                        dir = new Vec3(none ? 1 : dir.x(), 0, dir.z()).normalize().scale(0.5);
                        e.setDeltaMovement(e.getDeltaMovement().add(dir));
                        e.hurtMarked = true;
                    });
            if (anim.canAttack()) {
                if (anim.is(SUMMON_HORSE)) {
                    this.summonHorse();
                } else {
                    if (!this.forcedNP)
                        this.useMana(this.props().hogouMana());
                    this.summonChariot();
                    this.forcedNP = false;
                }
            }
        } else {
            boolean step = anim.is(MELEE_1) && anim.isAtTick(0.28) ||
                    anim.is(MELEE_2) && anim.isAtTick(0.24) ||
                    anim.is(MELEE_3) && anim.isAtTick(0.24);
            if (step) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(anim.is(MELEE_3) ? 0.25 : 0.3);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (this.getVehicle() != null) {
            Entity vehicle = this.getVehicle();
            if (this.getVehicle() instanceof GordiusWheel gordiusWheel && gordiusWheel.getWheelEntity() != null) {
                vehicle = gordiusWheel.getWheelEntity();
            }
            double width = vehicle.getBbWidth() * 0.5 + 1.7;
            double height = (this.getY() - vehicle.getY() + this.getBbHeight()) + 0.2;
            AABB aabb = new AABB(-width * 0.5, -0.02, -width * 0.5, width * 0.5, height, width * 0.5);
            return new OrientedBoundingBox(aabb, vehicle.getYRot(), 0, vehicle.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    public boolean summonChariot() {
        if (this.isPassenger() || this.level.isClientSide)
            return false;
        GordiusWheel wheel = ModEntities.GORDIUS_WHEEL.get().create(this.level);
        wheel.setPos(this.getX(), this.getY(), this.getZ());
        this.level.addFreshEntity(wheel);
        this.boardingCooldown = 0;
        this.startRiding(wheel);
        for (int i = 0; i < 5; i++) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level);
            lightningboltentity.moveTo(this.getX() + this.random.nextGaussian() * 2, this.getY(), this.getZ() + this.random.nextGaussian() * 2);
            lightningboltentity.setVisualOnly(true);
            this.level.addFreshEntity(lightningboltentity);
        }
        this.revealServant();
        return true;
    }

    public boolean summonHorse() {
        if (this.isPassenger() || this.level.isClientSide)
            return false;
        Horse horse = EntityType.HORSE.create(this.level);
        horse.setPos(this.getX(), this.getY(), this.getZ());
        horse.setTamed(true);
        this.level.addFreshEntity(horse);
        horse.getAttribute(Attributes.MAX_HEALTH).setBaseValue(horse.getAttributeBaseValue(Attributes.MAX_HEALTH) + 30);
        horse.getAttribute(Attributes.ARMOR).setBaseValue(2);
        horse.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(horse.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) + 0.15);
        this.boardingCooldown = 0;
        this.startRiding(horse);
        for (int i = 0; i < 5; i++) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level);
            lightningboltentity.moveTo(this.getX() + this.random.nextGaussian() * 2, this.getY(), this.getZ() + this.random.nextGaussian() * 2);
            lightningboltentity.setVisualOnly(true);
            this.level.addFreshEntity(lightningboltentity);
        }
        this.revealServant();
        return true;
    }

    @Override
    public boolean attacksFromMount() {
        return false;
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }
}
