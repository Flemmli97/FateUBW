package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.mixinhelper.HorseExtension;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
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
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector4f;

public class Iskander extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ONE_HAND_1 = BUILDER.add("one_hand_1", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56).marker("step", 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.36)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    public static final String ONE_HAND_2 = BUILDER.add("one_hand_2", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56).marker("step", 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.36)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    public static final String ONE_HAND_3 = BUILDER.add("one_hand_3", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56).marker("step", 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.36)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    public static final String ONE_HAND_4 = BUILDER.add("one_hand_4", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56).marker("step", 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.36)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    public static final String ONE_HAND_5 = BUILDER.add("one_hand_5", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56).marker("step", 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.36)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    public static final String ONE_HAND_6 = BUILDER.add("one_hand_6", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56).marker("step", 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.36)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    public static final String ONE_HAND_7 = BUILDER.add("one_hand_7", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56).marker("step", 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.36)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    private static final String CHARIOT = BUILDER.add("chariot_summon", AnimationsBuilder.definition(1.68)
            .marker("attack", 0.76)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.8));
    private static final String SUMMON_HORSE = BUILDER.add("horse", CHARIOT);
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Iskander> animationHandler = new AnimationHandler<>(this, ANIMS);
    private final Vector4f summonColor = new Vector4f(112 / 255f, 23 / 255f, 21 / 255f, 0.7f);

    private int summonCooldown;

    public Iskander(EntityType<? extends Iskander> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.KUPRIOTS.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.KUPRIOTS.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<Iskander>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<Iskander>builder(ONE_HAND_1)
                        .start(ONE_HAND_3, 2, 0.28f, 1)
                        .start(ONE_HAND_5, 2, 0.28f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 20, 27))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BehaviourUtils.of(AnimationPlayHolder.<Iskander>builder(ONE_HAND_2)
                        .start(ONE_HAND_1, 2, 0.28f, 1)
                        .start(ONE_HAND_6, 2, 0.28f, 1)
                        .start(ONE_HAND_7, 2, 0.28f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 20, 27))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BehaviourUtils.of(AnimationPlayHolder.<Iskander>builder(ONE_HAND_3)
                        .start(ONE_HAND_6, 2, 0.28f, 1)
                        .start(ONE_HAND_7, 2, 0.28f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 20, 27))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BehaviourUtils.of(AnimationPlayHolder.<Iskander>builder(ONE_HAND_4)
                        .start(ONE_HAND_3, 2, 0.28f, 1)
                        .start(ONE_HAND_5, 2, 0.28f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 20, 27))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(SUMMON_HORSE).play(BehaviourUtils.cooldownedPlay(false, 25, 40))
                .condition(Iskander::canSummonMounts)
                .prepare(new SetWalkTargetWithinDist<Iskander>()
                        .min(5).max(10).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(5)
                .start(SUMMON_HORSE).play(BehaviourUtils.cooldownedPlay(false, 25, 40))
                .condition(entity -> entity.canSummonMounts() && !entity.canUseNobelPhantasm())
                .prepare(new SetWalkTargetWithinDist<Iskander>()
                        .min(5).max(10).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(7)
                .start(CHARIOT).play(BehaviourUtils.cooldownedPlay(false, 25, 40))
                .condition(entity -> entity.canSummonMounts() && entity.canUseNobelPhantasm())
                .prepare(new SetWalkTargetWithinDist<Iskander>()
                        .min(6).max(12).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(14)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(6, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(6, BehaviourUtils.withCondition(Entity::isPassenger), new SetRandomWalkTarget<BaseServant>()
                        .setRadius(8, 4), BehaviourUtils.moveTo())
                .add(3, new SetWalkTargetAwayFromTarget<BaseServant>()
                        .radius(6), BehaviourUtils.moveTo()).build();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && !this.isPassenger()) {
            --this.summonCooldown;
        }
        if (this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityWeaponTrailProvider.TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityWeaponTrailProvider.EntityTrailData.create(this, anim.getID(), false))
                                            .setColor(215 / 255f, 183 / 255f, 147 / 255f, 0.6f)
                                            .setColor2(215 / 255f, 183 / 255f, 147 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(CHARIOT, SUMMON_HORSE)) {
            LivingEntity target = this.getTarget();
            if (target != null && !anim.isPast(0.28)) {
                this.lookAt(target, 60, 30);
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
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.4);
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
        double height = this.getBbHeight();
        double width = this.getBbWidth();
        double length = 1 * this.getScale();
        if (anim.is(ONE_HAND_1)) {
            width += 1.1 * this.getScale();
            length += 0.7 * this.getScale();
            return new AABB(-width * 0.7, -0.03, 0, width * 0.3, height + 0.03, length);
        }
        if (anim.is(ONE_HAND_2, ONE_HAND_3, ONE_HAND_4)) {
            width += 1.3 * this.getScale();
            length += 0.6 * this.getScale();
        }
        if (anim.is(ONE_HAND_5, ONE_HAND_6)) {
            width += 1.8 * this.getScale();
            length += 0.7 * this.getScale();
        }
        if (anim.is(ONE_HAND_7)) {
            width += 0.6 * this.getScale();
            length += 0.9 * this.getScale();
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    public AnimationHandler<Iskander> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.getAnimationHandler().isCurrent(CHARIOT)) {
            return false;
        }
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(damageSource, damage);
        } else if (this.getVehicle() != null) {
            damage *= 0.5f;
            this.getVehicle().hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    @Override
    public boolean nobelPhantasmCheck() {
        return this.healthBelow(0.8f) && super.nobelPhantasmCheck();
    }

    protected boolean canSummonMounts() {
        return !this.isPassenger() && this.summonCooldown <= 0;
    }

    public void summonChariot() {
        if (this.isPassenger() || this.level().isClientSide)
            return;
        if (!this.attemptUseNobelPhantasm())
            return;
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
        this.summonCooldown = 150 + this.getRandom().nextInt(100);
        this.revealServant();
    }

    public void summonHorse() {
        if (this.isPassenger() || this.level().isClientSide)
            return;
        Horse horse = EntityType.HORSE.create(this.level());
        horse.setPos(this.getX(), this.getY(), this.getZ());
        horse.setTamed(true);
        ((HorseExtension) horse).fate$setTotalControl(true);
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
        this.summonCooldown = 150 + this.getRandom().nextInt(100);
        this.revealServant();
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
