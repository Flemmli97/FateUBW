package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.effects.PetrificationEffect;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.SetWalkToFront;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.summons.Pegasus;
import io.github.flemmli97.fateubw.common.entity.utils.OnProjectileHit;
import io.github.flemmli97.fateubw.common.particles.RingParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Medusa extends BaseServant implements OnProjectileHit {

    protected static final EntityDataAccessor<Boolean> THROWN_DAGGER = SynchedEntityData.defineId(Medusa.class, EntityDataSerializers.BOOLEAN);

    public static final double VIEW_ANGLE = 30 * Mth.DEG_TO_RAD;

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DUAL_REVERSE_1 = BUILDER.add("dual_reverse_1", AnimationsBuilder.definition(0.64)
            .marker("attack", 0.48).marker("step", 0.24)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.32)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.52));
    public static final String DUAL_REVERSE_2 = BUILDER.add("dual_reverse_2", AnimationsBuilder.definition(0.64)
            .marker("attack", 0.48).marker("step", 0.24)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.32)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.52));
    public static final String DUAL_REVERSE_3 = BUILDER.add("dual_reverse_3", AnimationsBuilder.definition(0.64)
            .marker("attack", 0.48).marker("step", 0.24)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.32)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.52));
    public static final String DUAL_REVERSE_4 = BUILDER.add("dual_reverse_4", AnimationsBuilder.definition(0.64)
            .marker("attack", 0.48).marker("step", 0.24)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.32)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.52));
    public static final String CHAIN_THROW = BUILDER.add("chain_throw", AnimationsBuilder.definition(0.84).marker("attack", 0.6));
    public static final String RETRIEVE = BUILDER.add("chain_retrieve", AnimationsBuilder.definition(0.88).marker("retrieve", 0.56));
    public static final String EYE = BUILDER.add("eye", AnimationsBuilder.definition(1.88)
            .marker("open", 0.64).marker("close", 1.64));
    public static final String JUMP = BUILDER.add("jump", AnimationsBuilder.definition(0.48).marker("jump", 0.24).infinite());
    public static final String LAND = BUILDER.add("land", AnimationsBuilder.definition(0.64).marker("attack", 0.16));
    public static final String BELLEROPHON = BUILDER.add("bellerophon", AnimationsBuilder.definition(2.28).marker("attack", 0.2));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static Predicate<Medusa> meleeCondition(String anim) {
        return medusa -> {
            Entity vehicle = medusa.getVehicle();
            if (!(vehicle instanceof Pegasus pegasus) || !pegasus.canFly())
                return true;
            LivingEntity target = BrainUtils.getTargetOfEntity(medusa);
            return target == null || medusa.prepareAttackBox(anim, target, -0.2f, false).intersects(target.getBoundingBox());
        };
    }

    private final AnimationHandler<Medusa> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                this.eyeAffected = null;
                return false;
            });

    private final Vector4f summonColor = new Vector4f(175 / 255f, 88 / 255f, 142 / 255f, 0.7f);
    private List<LivingEntity> eyeAffected;
    private ChainDagger dagger;
    private int throwCooldown, summonCooldown, eyeCooldown;

    public Medusa(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(THROWN_DAGGER, false);
    }

    public boolean daggerThrown() {
        return this.getEntityData().get(THROWN_DAGGER);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.MEDUSA_DAGGER.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(FateItems.MEDUSA_DAGGER.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.MEDUSA_DAGGER.get()) ||
                this.getOffhandItem().is(FateItems.MEDUSA_DAGGER.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<Medusa>create()
                .start(DUAL_REVERSE_1).play(BehaviourUtils.cooldownedPlay(true, 12, 20))
                .condition(meleeCondition(DUAL_REVERSE_1))
                .prepare(new SetWalkTargetToAttackTarget<Medusa>().speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(DUAL_REVERSE_2).play(BehaviourUtils.cooldownedPlay(true, 12, 20))
                .condition(entity -> meleeCondition(DUAL_REVERSE_2).test(entity) && !entity.getOffhandItem().isEmpty())
                .prepare(new SetWalkTargetToAttackTarget<Medusa>().speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(DUAL_REVERSE_3).play(BehaviourUtils.cooldownedPlay(true, 12, 20))
                .condition(meleeCondition(DUAL_REVERSE_3))
                .prepare(new SetWalkTargetToAttackTarget<Medusa>().speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(DUAL_REVERSE_4).play(BehaviourUtils.cooldownedPlay(true, 12, 20))
                .condition(entity -> meleeCondition(DUAL_REVERSE_4).test(entity) && !entity.getOffhandItem().isEmpty())
                .prepare(new SetWalkTargetToAttackTarget<Medusa>().speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(JUMP).play(BehaviourUtils.cooldownedPlay(false, 15, 25))
                .condition(entity -> !entity.isPassenger())
                .prepare(new SetWalkTargetToAttackTarget<Medusa>().speedMod((m, e) -> 1.1f)
                        .closeEnoughDist(BehaviourUtils.closeEnough(9))).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(2)
                .start(JUMP).play(BehaviourUtils.cooldownedPlay(false, 15, 25))
                .condition(entity -> !entity.isPassenger() && BehaviourUtils.ifFurtherThan(8).test(entity))
                .prepare(new SetWalkTargetToAttackTarget<Medusa>().speedMod((m, e) -> 1.1f)
                        .closeEnoughDist(BehaviourUtils.closeEnough(9))).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(13)
                .start(CHAIN_THROW).play(BehaviourUtils.cooldownedPlay(false, 15, 25))
                .condition(Medusa::canThrow)
                .prepare(new SetWalkTargetWithinDist<Medusa>()
                        .min(5).max(16).speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.moveTo())
                .end(6)
                .start(CHAIN_THROW).play(BehaviourUtils.cooldownedPlay(false, 15, 25))
                .condition(entity -> entity.canThrow() && BehaviourUtils.ifFurtherThan(6).test(entity))
                .prepare(new SetWalkTargetWithinDist<Medusa>()
                        .min(5).max(16).speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.moveTo())
                .end(7)
                .start(EYE).play(BehaviourUtils.cooldownedPlay(false, 30, 50))
                .condition(entity -> entity.eyeCooldown <= 0 && entity.healthBelow(0.75f))
                .prepare(new SetWalkToFront<Medusa>().distance(7).speedMod((m, e) -> 1.1f)
                        .startCondition(medusa -> medusa.getTarget() != null && !Utils.isInView(medusa.getTarget(), medusa, VIEW_ANGLE)))
                .prepareOptional(BehaviourUtils.timedMoveAttack(20, 25))
                .end(7)
                .start(EYE).play(BehaviourUtils.cooldownedPlay(false, 30, 50))
                .condition(entity -> entity.eyeCooldown <= 0 && entity.healthBelow(0.5f))
                .prepare(new SetWalkToFront<Medusa>().distance(7).speedMod((m, e) -> 1.1f)
                        .startCondition(medusa -> medusa.getTarget() != null && !Utils.isInView(medusa.getTarget(), medusa, VIEW_ANGLE)))
                .prepareOptional(BehaviourUtils.timedMoveAttack(20, 25))
                .end(5)
                .start(EYE).play(BehaviourUtils.cooldownedPlay(false, 30, 50))
                .condition(entity -> entity.eyeCooldown <= 0 && entity.healthBelow(0.75f) && !entity.isPassenger())
                .prepare(new SetWalkToFront<Medusa>().distance(7).speedMod((m, e) -> 1.1f)
                        .startCondition(medusa -> medusa.getTarget() != null && !Utils.isInView(medusa.getTarget(), medusa, VIEW_ANGLE)))
                .prepareOptional(BehaviourUtils.timedMoveAttack(20, 25))
                .end(5)
                .start(BELLEROPHON).play(BehaviourUtils.cooldownedPlay(false, 20, 40))
                .condition(Medusa::canSummonPegasus)
                .prepare(new SetWalkTargetWithinDist<Medusa>()
                        .min(6).max(12).speedMod((m, e) -> 1.3f)).prepareOptional(BehaviourUtils.moveTo())
                .end(40)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(6, entity -> !entity.isPassenger(), new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(3, new SetWalkTargetAwayFromTarget<BaseServant>().speedMod(1.1f), BehaviourUtils.moveTo())
                .add(6, Entity::isPassenger, new SetRandomWalkTarget<>(), BehaviourUtils.moveTo())
                .add(3, Entity::isPassenger, new Idle<>()).build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            if (this.dagger != null) {
                if (!this.dagger.isAlive()) {
                    this.dagger = null;
                    this.getEntityData().set(THROWN_DAGGER, false);
                }
            } else
                --this.throwCooldown;
            if (!this.isPassenger())
                --this.summonCooldown;
            --this.eyeCooldown;
        } else {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityWeaponTrailProvider.TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityWeaponTrailProvider.EntityTrailData.create(this, anim.getID(), false))
                                            .setColor(80 / 255f, 80 / 255f, 80 / 255f, 0.6f)
                                            .setColor2(80 / 255f, 80 / 255f, 80 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(CHAIN_THROW)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAt(target, 60, 30);
            }
            if (anim.isAt("attack")) {
                this.throwDaggerAt(target);
            }
        } else if (anim.is(RETRIEVE)) {
            if (anim.isAt("retrieve") && this.dagger != null) {
                this.dagger.retractHook();
                this.dagger = null;
                this.getEntityData().set(THROWN_DAGGER, false);
                BrainUtils.clearMemory(this, MemoryModuleType.ATTACK_COOLING_DOWN);
            }
        } else if (anim.is(EYE)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAt(target, 60, 30);
            }
            if (anim.isAt("open")) {
                this.eyeAffected = new ArrayList<>();
                this.eyeCooldown = this.random.nextInt(150) + 150;
            }
            if (anim.isPast("open") && !anim.isPast("close")) {
                this.gorgonsEyes();
            }
        } else if (anim.is(JUMP)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt("jump")) {
                Vec3 dir;
                if (target != null) {
                    dir = target.position().subtract(this.position());
                    dir = new Vec3(dir.x(), 0, dir.z());
                    if (dir.lengthSqr() > 32 * 32)
                        dir = dir.normalize().scale(32 * 0.14);
                    else
                        dir = dir.scale(0.14);
                } else {
                    dir = this.getLookAngle().scale(0.75);
                }
                this.setDeltaMovement(dir.x(), 0.8, dir.z());
            }
            if (anim.isPast("jump")) {
                this.fallDistance = 0;
                if (anim.done(0)) {
                    if (this.onGround()) {
                        this.getAnimationHandler().setAnimation(this.getAnimationHandler().get(LAND),
                                0, AnimationHandler.FALLBACK_TRANSIT_TIME, 0);
                    }
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPast(6.0) && (!this.getInBlockState().is(Blocks.AIR) || !this.getBlockStateOn().is(Blocks.AIR))) {
                    this.getAnimationHandler().setAnimation(this.getAnimationHandler().get(LAND),
                            0, AnimationHandler.FALLBACK_TRANSIT_TIME, 0);
                }
            }
        } else if (anim.is(BELLEROPHON)) {
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
                this.summonPegasus();
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
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(LAND)) {
            return new OrientedBoundingBox(this.attackBB(anim), this.getYRot(), 0, this.position());
        }
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
        if (anim.is(LAND)) {
            double width = this.getBbWidth() + 3 * this.getScale();
            return new AABB(-width * 0.5, -0.02, -width * 0.3, width * 0.5, this.getBbHeight() * 0.5, width * 0.7);
        }
        double height = this.getBbHeight();
        double width = this.getBbWidth();
        double length = 1 * this.getScale();
        if (anim.is(DUAL_REVERSE_1)) {
            width += 0.9 * this.getScale();
            length += 0.6 * this.getScale();
            return new AABB(-width * 0.7, -0.03, 0, width * 0.3, height + 0.03, length);
        }
        if (anim.is(DUAL_REVERSE_2)) {
            width += 0.9 * this.getScale();
            length += 0.6 * this.getScale();
            return new AABB(-width * 0.3, -0.03, 0, width * 0.7, height + 0.03, length);
        }
        if (anim.is(DUAL_REVERSE_3, DUAL_REVERSE_4)) {
            width += 0.8 * this.getScale();
            length += 0.7 * this.getScale();
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    public AnimationHandler<Medusa> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.getAnimationHandler().isCurrent(BELLEROPHON)) {
            return false;
        }
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(damageSource, damage);
        }
        if (this.getVehicle() != null) {
            damage *= 0.5f;
            this.getVehicle().hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    @Override
    public boolean nobelPhantasmCheck() {
        return this.healthBelow(0.5f) && super.nobelPhantasmCheck();
    }

    public void throwDaggerAt(@Nullable LivingEntity target) {
        if (!this.level().isClientSide) {
            ChainDagger dagger = new ChainDagger(this.level(), this, true);
            if (target == null) {
                dagger.shoot(this, this.getXRot(), this.getYRot(), 0, 3, 0);
            } else {
                dagger.shootAtEntity(target, 3, 0);
            }
            this.level().addFreshEntity(dagger);
            this.dagger = dagger;
            this.throwCooldown = this.random.nextInt(50) + 45;
            this.getEntityData().set(THROWN_DAGGER, true);
        }
    }

    public void gorgonsEyes() {
        if (this.eyeAffected == null) {
            this.eyeAffected = new ArrayList<>();
        }
        List<LivingEntity> entities = this.level().getEntities(EntityTypeTest.forClass(LivingEntity.class), this.getBoundingBox().inflate(32),
                e -> e == this.getTarget() || this.targetPred.test(e));
        boolean success = false;
        for (LivingEntity entity : entities) {
            if (this.eyeAffected.contains(entity))
                continue;
            if (Utils.isInView(entity, this, VIEW_ANGLE)) {
                this.eyeAffected.add(entity);
                MobEffectInstance eff = entity.getEffect(FateMobEffects.PETRIFICATION.asHolder());
                int amplifier = 0;
                if (eff != null) {
                    if (eff.getAmplifier() >= PetrificationEffect.MAX_PROGRESS)
                        continue;
                    amplifier = eff.getAmplifier() + 1;
                }
                entity.addEffect(new MobEffectInstance(FateMobEffects.PETRIFICATION.asHolder(), CommonConfig.petrificationDuration, amplifier));
                success = true;
            }
        }
        if (success) {
            AdvancedParticleContainer.make(new RingParticleData(this.getYHeadRot(), 0))
                    .addData(new ScaleData(1, 4, 8))
                    .addData(new ColorData(new Vector4f(125 / 255f, 12 / 255f, 127 / 255f, 1),
                            Optional.of(new Vector4f(125 / 255f, 12 / 255f, 127 / 255f, 0.2f)), 10))
                    .addData(new ParticleMetaData(10, false, 0))
                    .add(this.level(), this.getX(), this.getEyeY(), this.getZ());
            this.playSound(SoundEvents.TOTEM_USE, 1, this.getRandom().nextFloat() * 0.2f + 1.1f);
        }
    }

    protected boolean canSummonPegasus() {
        return !this.isPassenger() && this.canUseNobelPhantasm() && this.summonCooldown <= 0;
    }

    public void summonPegasus() {
        if (this.level() instanceof ServerLevel serverLevel) {
            if (!this.attemptUseNobelPhantasm())
                return;
            Pegasus peg = FateEntities.PEGASUS.get().create(serverLevel, null, this.blockPosition(), MobSpawnType.MOB_SUMMONED, false, false);
            peg.setPos(this.position());
            peg.setYRot(this.getYRot());
            peg.yRotO = this.getYRot();
            peg.yHeadRot = this.getYRot();
            peg.yHeadRotO = this.getYRot();
            peg.yBodyRot = this.getYRot();
            peg.yBodyRotO = this.getYRot();
            this.level().addFreshEntity(peg);
            this.startRiding(peg, true);
            this.revealServant();
            this.summonCooldown = 150 + this.getRandom().nextInt(100);
        }
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }

    public boolean canThrow() {
        return this.dagger == null && this.throwCooldown <= 0;
    }

    @Override
    public void onProjectileHit(Entity entity) {
        if (entity instanceof ChainDagger)
            this.getAnimationHandler().setAnimation(RETRIEVE);
    }

    @Override
    public WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector4f(0, 0, -0.5f, 1), new Vector4f(0, 0, -0.8f, 1));
    }
}
