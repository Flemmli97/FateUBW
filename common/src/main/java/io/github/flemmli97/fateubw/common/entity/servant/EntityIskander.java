package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.utils.StandingVehicle;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
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
import org.joml.Vector4f;

public class EntityIskander extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ONE_HAND_1 = BUILDER.add("one_hand_1", AnimationsBuilder.definition(0.62)
            .marker("attack", 0.48).marker("step", 0.44));
    public static final String ONE_HAND_2 = BUILDER.add("one_hand_2", AnimationsBuilder.definition(0.62)
            .marker("attack", 0.4).marker("step", 0.44));
    public static final String ONE_HAND_3 = BUILDER.add("one_hand_3", AnimationsBuilder.definition(0.58)
            .marker("attack", 0.44).marker("step", 0.44));
    public static final String ONE_HAND_4 = BUILDER.add("one_hand_4", AnimationsBuilder.definition(0.54)
            .marker("attack", 0.44).marker("step", 0.4));
    public static final String ONE_HAND_5 = BUILDER.add("one_hand_5", AnimationsBuilder.definition(0.58)
            .marker("attack", 0.4).marker("step", 0.4));
    public static final String ONE_HAND_6 = BUILDER.add("one_hand_6", AnimationsBuilder.definition(0.58)
            .marker("attack", 0.48).marker("step", 0.44));
    public static final String ONE_HAND_7 = BUILDER.add("one_hand_7", AnimationsBuilder.definition(0.58)
            .marker("attack", 0.48).marker("step", 0.4));

    private static final String CHARIOT = BUILDER.add("chariot_summon", AnimationsBuilder.definition(1.64).marker("attack", 0.68));
    private static final String SUMMON_HORSE = BUILDER.add("horse", CHARIOT);
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

//    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityIskander>>> ATTACKS = List.of(
//            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.ONE_HAND_1)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.ONE_HAND_2)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.ONE_HAND_3)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.ONE_HAND_4)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.ONE_HAND_5)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .withCondition((goal, target, previous) -> !goal.attacker.isPassenger())
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.ONE_HAND_6)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .withCondition((goal, target, previous) -> !goal.attacker.isPassenger())
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.ONE_HAND_7)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.SUMMON_HORSE)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .withCondition((goal, target, prev) -> !goal.attacker.isPassenger())
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(5, 8, 1.2))), 6),
//            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.CHARIOT)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
//                    .withCondition((goal, target, prev) -> !goal.attacker.isPassenger() && (goal.attacker.canUseNP() && goal.attacker.getOwner() == null && goal.attacker.getMana() >= goal.attacker.props().hogouMana()) || goal.attacker.forcedNP)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(5, 8, 1.2))), 14)
//    );
//    public static final List<WeightedEntry.Wrapper<IdleAction<EntityIskander>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 6),
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<EntityIskander>(8, 6))
//                    .withCondition(((goal, target) -> goal.attacker.isPassenger())), 7),
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
//    );
//
//    public final AnimatedAttackGoal<EntityIskander> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityIskander> animationHandler = new AnimationHandler<>(this, ANIMS);
    private final Vector4f summonColor = new Vector4f(112 / 255f, 23 / 255f, 21 / 255f, 0.7f);

    public EntityIskander(EntityType<? extends EntityIskander> entityType, Level level) {
        super(entityType, level);
        this.canUseNP = true;
    }

    protected boolean useStandingAnim() {
        return this.getVehicle() != null && !StandingVehicle.shouldSit(this.getVehicle());
    }

    protected boolean useSittingAnim() {
        return StandingVehicle.shouldSit(this);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.KUPRIOTS.get()));
    }

    @Override
    public AnimationHandler<EntityIskander> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(damageSource, damage);
        } else if (this.getVehicle() != null) {
            damage *= 0.5;
            this.getVehicle().hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(CHARIOT, SUMMON_HORSE)) {
            LivingEntity target = this.getTarget();
            if (target != null && !anim.isPast(0.28)) {
                this.lookAtNow(target, 60, 30);
            }
            this.level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
                            this.getBoundingBox().inflate(12, 8, 12),
                            this.targetPred)
                    .forEach(e -> {
                        Vec3 dir = e.position().subtract(this.position());
                        boolean none = dir.x() == 0 && dir.z() == 0;
                        dir = new Vec3(none ? 1 : dir.x(), 0, dir.z()).normalize().scale(0.5);
                        e.setDeltaMovement(e.getDeltaMovement().add(dir));
                        e.hurtMarked = true;
                    });
            if (anim.isAt("attack")) {
                if (anim.is(SUMMON_HORSE)) {
                    this.summonHorse();
                } else {
                    this.summonChariot();
                }
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(anim.is(ONE_HAND_4) ? 0.25 : 0.3);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
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

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(ONE_HAND_1)) {
            width += 0.7;
            length += 0.7;
        }
        if (anim.is(ONE_HAND_2, ONE_HAND_3, ONE_HAND_4)) {
            width += 1.3;
            length += 0.6;
        }
        if (anim.is(ONE_HAND_5, ONE_HAND_6)) {
            width += 1.5;
            length += 0.7;
        }
        if (anim.is(ONE_HAND_7)) {
            width += 0.6;
            length += 0.9;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    public boolean summonChariot() {
        if (!this.forcedNP && !this.useMana(this.props().hogouMana()))
            return false;
        if (this.isPassenger() || this.level().isClientSide)
            return false;
        GordiusWheel wheel = FateEntities.GORDIUS_WHEEL.get().create(this.level());
        wheel.setPos(this.getX(), this.getY(), this.getZ());
        this.level().addFreshEntity(wheel);
        this.boardingCooldown = 0;
        this.startRiding(wheel);
        for (int i = 0; i < 5; i++) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level());
            lightningboltentity.moveTo(this.getX() + this.random.nextGaussian() * 2, this.getY(), this.getZ() + this.random.nextGaussian() * 2);
            lightningboltentity.setVisualOnly(true);
            this.level().addFreshEntity(lightningboltentity);
        }
        this.revealServant();
        return true;
    }

    public boolean summonHorse() {
        if (this.isPassenger() || this.level().isClientSide)
            return false;
        Horse horse = EntityType.HORSE.create(this.level());
        horse.setPos(this.getX(), this.getY(), this.getZ());
        horse.setTamed(true);
        this.level().addFreshEntity(horse);
        horse.getAttribute(Attributes.MAX_HEALTH).setBaseValue(horse.getAttributeBaseValue(Attributes.MAX_HEALTH) + 30);
        horse.getAttribute(Attributes.ARMOR).setBaseValue(2);
        horse.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(horse.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) + 0.15);
        this.boardingCooldown = 0;
        this.startRiding(horse);
        for (int i = 0; i < 5; i++) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level());
            lightningboltentity.moveTo(this.getX() + this.random.nextGaussian() * 2, this.getY(), this.getZ() + this.random.nextGaussian() * 2);
            lightningboltentity.setVisualOnly(true);
            this.level().addFreshEntity(lightningboltentity);
        }
        this.revealServant();
        return true;
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
