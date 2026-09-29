package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.summons.Bucephalos;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.summons.Hoplite;
import io.github.flemmli97.fateubw.common.entity.utils.CooldownHolder;
import io.github.flemmli97.fateubw.common.entity.utils.ServantModelLike;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.particles.StaticFacingParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateDimensions;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.common.world.realitymarble.RealityMarbleHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.CirclingData;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector4f;

import java.util.List;

public class Iskander extends BaseServant {

    private static final float ATTACK_MOVE_SPEED = 1.5f;

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ONE_HAND_1_1 = BUILDER.add("one_hand_1_1", AnimationsBuilder.definition(0.8)
            .marker("attack", 0.72).marker("step", 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.72));
    public static final String ONE_HAND_1_2 = BUILDER.add("one_hand_1_2", AnimationsBuilder.definition(0.84)
            .marker("attack", 0.72).marker("step", 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.72));
    public static final String LIGHTNING_1 = BUILDER.add("lightning_1", AnimationsBuilder.definition(1.4)
            .marker("prepare", 0.72).marker("sparks", 0.92)
            .marker("attack", 1.04));

    public static final String ONE_HAND_2_1 = BUILDER.add("one_hand_2_1", AnimationsBuilder.definition(0.84)
            .marker("attack", 0.72).marker("step", 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.72));
    public static final String ONE_HAND_2_2 = BUILDER.add("one_hand_2_2", AnimationsBuilder.definition(0.84)
            .marker("attack", 0.72).marker("step", 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.72));
    public static final String STAB_1 = BUILDER.add("stab_1", AnimationsBuilder.definition(0.76)
            .marker("attack", 0.68).marker("step", 0.6));

    public static final String SUMMON_ARMY = BUILDER.add("summon_army", AnimationsBuilder.definition(1.6)
            .marker("summon", 0.88));
    public static final String SUMMON_BUCEPHALOS = BUILDER.add("summon_bucephalos", SUMMON_ARMY);
    public static final String SUMMON_CHARIOT = BUILDER.add("summon_chariot", AnimationsBuilder.definition(1.6)
            .marker("summon", 1.12)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.8)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 1.04));

    public static final String IONIOI_HETAIROI = BUILDER.add("ionioi_hetairoi", AnimationsBuilder.definition(7)
            .marker("start", 0.28).marker("cast", 3.88).marker("teleport", 6));
    private static final String DEATH = BUILDER.add("death", AnimationsBuilder.definition(3.68).infinite());
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));

    public static final String IDLE_BREAK_1 = BUILDER.add("idle_break_1", AnimationsBuilder.definition(1.8));
    public static final String IDLE_BREAK_2 = BUILDER.add("idle_break_2", AnimationsBuilder.definition(1.8));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Iskander> animationHandler = new AnimationHandler<>(this, ANIMS);
    private final Vector4f summonColor = new Vector4f(112 / 255f, 23 / 255f, 21 / 255f, 0.7f);

    private final CooldownHolder summonCooldown;
    private final CooldownHolder armySummonCooldown;
    private final CooldownHolder realityMarbleDuration;

    public Iskander(EntityType<? extends Iskander> entityType, Level level) {
        super(entityType, level);
        this.summonCooldown = this.createCooldown("summon", this.props().getConfig(ServantExtraData.MOUNT_SUMMON_COOLDOWN), () -> !this.isPassenger());
        this.armySummonCooldown = this.createCooldown("army_summon", this.props().getConfig(ServantExtraData.ARMY_SUMMON_COOLDOWN));
        this.realityMarbleDuration = this.createCooldown("reality_marble_duration", this.props().getConfig(ServantExtraData.REALITY_MARBLE_DURATION)).persist();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.KUPRIOTS.get()));
    }

    @Override
    public ItemStack getRenderHandStack(InteractionHand hand) {
        return switch (hand) {
            case MAIN_HAND -> {
                ItemStack stack = this.getMainHandItem();
                if (stack.is(FateItems.KUPRIOTS.get())) {
                    yield stack;
                }
                yield ServantModelLike.getStack(FateItems.KUPRIOTS.get());
            }
            case OFF_HAND -> this.getOffhandItem();
        };
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<Iskander>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<Iskander>builder(ONE_HAND_1_1)
                        .start(ONE_HAND_1_2, 2, 0.36f, 1)
                        .start(ONE_HAND_1_2, 2, 0.36f, 3, owner -> owner.healthBelow(0.66f) && !owner.isPassenger())
                        .chain(LIGHTNING_1, 1, 0.36f)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 20, 27))
                .prepare(new SetWalkTargetToAttackTarget<Iskander>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(12)
                .start(BehaviourUtils.of(AnimationPlayHolder.<Iskander>builder(ONE_HAND_2_1)
                        .start(ONE_HAND_2_2, 2, 0.36f, 1)
                        .start(ONE_HAND_2_2, 2, 0.36f, 3, owner -> owner.healthBelow(0.66f))
                        .chain(STAB_1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 20, 27))
                .prepare(new SetWalkTargetToAttackTarget<Iskander>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(12)
                .start(STAB_1).play(BehaviourUtils.cooldownedPlay(true, 20, 27))
                .condition(Entity::isPassenger)
                .prepare(new SetWalkTargetToAttackTarget<Iskander>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(14)
                .start(LIGHTNING_1).play(BehaviourUtils.cooldownedPlay(true, 20, 27))
                .condition(owner -> !owner.isPassenger() && owner.healthBelow(0.66f))
                .prepare(new SetWalkTargetToAttackTarget<Iskander>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(9)

                .start(SUMMON_BUCEPHALOS).play(BehaviourUtils.cooldownedPlay(false, 25, 40))
                .condition(Iskander::canSummonMounts)
                .prepare(new SetWalkTargetWithinDist<Iskander>()
                        .min(5).max(10).speedMod(ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.moveAttack())
                .end(5)
                .start(SUMMON_BUCEPHALOS).play(BehaviourUtils.cooldownedPlay(false, 25, 40))
                .condition(owner -> owner.canSummonMounts() && !owner.healthBelow(0.66f))
                .prepare(new SetWalkTargetWithinDist<Iskander>()
                        .min(5).max(10).speedMod(ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.moveAttack())
                .end(7)
                .start(SUMMON_CHARIOT).play(BehaviourUtils.cooldownedPlay(false, 25, 40))
                .condition(entity -> entity.canSummonMounts() && entity.healthBelow(0.66f))
                .prepare(new SetWalkTargetWithinDist<Iskander>()
                        .min(6).max(12).speedMod(ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.moveAttack())
                .end(15)
                .start(IONIOI_HETAIROI)
                .condition(owner -> owner.canUseNobelPhantasm() && owner.canOverrideRealityMarble())
                .prepare(new SetWalkTargetToAttackTarget<Iskander>().closeEnoughDist(BehaviourUtils.closeEnough(16))).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(30)
                .start(SUMMON_ARMY)
                .condition(owner -> owner.isInRealityMarble() && owner.canSummonHoplites())
                .prepare(new SetWalkTargetToAttackTarget<Iskander>().closeEnoughDist(BehaviourUtils.closeEnough(24))).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(18)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(6, new SetWalkTargetToAttackTarget<BaseServant>().speedMod((owner, target) -> ATTACK_MOVE_SPEED), BehaviourUtils.moveTo())
                .add(6, BehaviourUtils.withCondition(Entity::isPassenger), new SetRandomWalkTarget<BaseServant>()
                        .setRadius(8, 4).speedModifier(ATTACK_MOVE_SPEED), BehaviourUtils.moveTo())
                .add(3, new SetWalkTargetAwayFromTarget<BaseServant>()
                        .radius(6).speedMod(ATTACK_MOVE_SPEED), BehaviourUtils.moveTo()).build();
    }

    @Override
    protected String[] idleAnimations() {
        return new String[]{IDLE_BREAK_1, IDLE_BREAK_2};
    }

    public boolean isInRealityMarble() {
        return RealityMarbleHandler.get(this.getServer()).isManagingRealityMarble(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
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
        } else {
            if (this.realityMarbleDuration.offCooldown()) {
                RealityMarbleHandler.get(this.getServer()).deleteGroupOf(this);
            }
        }
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(IONIOI_HETAIROI)) {
            this.getNavigation().stop();
            if (anim.isPast("start")) {
                for (int i = 0; i < 4; i++) {
                    AdvancedParticleContainer.make(ParticleTypes.SMOKE)
                            .addData(new ScaleData(0.3f + this.getRandom().nextFloat() * 0.1f))
                            .addData(new ColorData((210 + this.getRandom().nextInt(10)) / 255f, (190 + this.getRandom().nextInt(20)) / 255f, (150 + this.getRandom().nextInt(15)) / 255f))
                            .add(this.level(), this.getRandomX(16), this.getY(this.getRandom().nextDouble() * 7 - 2), this.getRandomZ(16));
                }
                for (int i = 0; i < 8; i++) {
                    double x = this.getRandomX(4);
                    double y = this.getY(this.getRandom().nextDouble() * 6 - 3);
                    double z = this.getRandomZ(4);
                    AdvancedParticleContainer.make(ParticleTypes.SMOKE)
                            .addData(new ScaleData(0.25f))
                            .addData(new ColorData((210 + this.getRandom().nextInt(10)) / 255f, (190 + this.getRandom().nextInt(20)) / 255f, (150 + this.getRandom().nextInt(15)) / 255f))
                            .addData(new MotionData(new Vec3(x - this.getX(), y - this.getY(), z - this.getZ()).scale(0.15)))
                            .add(this.level(), x, y, z);
                }
                for (int i = 0; i < 3; i++) {
                    AdvancedParticleContainer.make(ParticleTypes.SMOKE)
                            .addData(new ScaleData(0.25f))
                            .addData(new ColorData((210 + this.getRandom().nextInt(10)) / 255f, (190 + this.getRandom().nextInt(20)) / 255f, (150 + this.getRandom().nextInt(15)) / 255f))
                            .addData(new CirclingData(this.getBbWidth() * 1.5f, 0,
                                    this.getRandom().nextFloat() * 360, 20, MathUtils.NORMAL_Y))
                            .add(this.level(), this.getX(), this.getY(this.getRandom().nextDouble()), this.getZ());
                }
            }
            if (anim.isAt("cast")) {
                if (!this.canOverrideRealityMarble() || !this.attemptUseNobelPhantasm()) {
                    this.getAnimationHandler().setAnimation(null);
                    return;
                }
                AdvancedParticleContainer.make(FateParticles.SPHERE_CLOUD.get())
                        .addData(new ScaleData(0, 48, 40))
                        .addData(new ColorData(1, 1, 1, 0.5f))
                        .addData(new ParticleMetaData(60, false, 0))
                        .add(this.level(), null, this.getX(), this.getY(0.5), this.getZ(), true);
                RealityMarbleHandler.prepareChunks(this, FateDimensions.SAND_DUNES.dimension(), 48);
                this.playSound(FateSounds.REALITY_MARBLE.get(), 4, 1);
            }
            if (anim.isPast("cast")) {
                S2CScreenShake.sendAround(this, 64, 2, 1);
            }
            if (anim.isAt("teleport")) {
                List<Entity> entities = this.level().getEntities(EntityTypeTest.forClass(Entity.class), this.getBoundingBox().inflate(48), e -> true);
                this.heal(this.getMaxHealth() * 0.2f);
                this.realityMarbleDuration.use();
                RealityMarbleHandler.get(this.getServer())
                        .createAndTransportTo(this, entities, FateDimensions.SAND_DUNES.dimension());
            }
        } else if (anim.is(SUMMON_CHARIOT, SUMMON_BUCEPHALOS)) {
            LivingEntity target = this.getTarget();
            if (target != null && !anim.isPast(0.28)) {
                this.setTargetPositionFromAttackTarget();
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
            if (anim.isAt("summon")) {
                if (anim.is(SUMMON_BUCEPHALOS)) {
                    this.summonBucephalos();
                } else {
                    this.summonChariot();
                }
            }
        } else if (anim.is(SUMMON_ARMY)) {
            LivingEntity target = this.getTarget();
            if (target != null && !anim.isPast(0.28)) {
                this.setTargetPositionFromAttackTarget();
            }
            if (anim.isAt("summon")) {
                this.summonArmy();
            }
        } else if (anim.is(LIGHTNING_1)) {
            if (anim.isAt("prepare")) {
                S2CScreenShake.sendAround(this, 32, 4, 1);
                AdvancedParticleContainer.make(new StaticFacingParticleData(FateParticles.RING.get(), 0, 90))
                        .addData(new ColorData(0.9f, 0.9f, 0.9f))
                        .addData(new ScaleData(1, 3, 4))
                        .addData(new ParticleMetaData(8, false, 0))
                        .add(this.level(), this.getX(), Math.ceil(this.getY()) + 0.01, this.getZ());
                AdvancedParticleContainer.make(new StaticFacingParticleData(FateParticles.RING.get(), 0, 90))
                        .addData(new ColorData(0.9f, 0.9f, 0.9f))
                        .addData(new ScaleData(1, 5, 4))
                        .addData(new ParticleMetaData(8, false, 0))
                        .add(this.level(), this.getX(), Math.ceil(this.getY()) + 0.01, this.getZ());
                this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.8f);
            }
            if (anim.isAt("sparks")) {
                Vec3 dir = new Vec3(0, 0, 3);
                for (int i = 0; i < 8; i++) {
                    Vec3 off = dir.yRot(i * 45 * Mth.DEG_TO_RAD);
                    AdvancedParticleContainer.make(FateParticles.LIGHTNING.get())
                            .addData(new ScaleData(1))
                            .addData(new ColorData(42 / 255f, 151 / 255f, 255 / 255f, 1))
                            .addData(new ParticleMetaData(15, false, 0))
                            .add(this.level(), this.getX() + off.x(), this.getY() + 0.01, this.getZ() + off.z());
                }
                this.playSound(FateSounds.ZAP.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0f);
            }
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.4);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("attack")) {
                this.playSound(FateSounds.SWOOSH_2.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.9f);
            }
            super.handleAttack(anim);
        }
    }

    @Override
    protected DamageSource damageSourceAttack(Entity target) {
        if (this.animationHandler.isCurrent(LIGHTNING_1)) {
            return FateDamageTypes.direct(FateDamageTypes.LIGHTNING_STRIKE, this);
        }
        return super.damageSourceAttack(target);
    }

    @Override
    public float damageModifier(Entity target) {
        if (this.animationHandler.isCurrent(LIGHTNING_1)) {
            return 1.5f;
        }
        return super.damageModifier(target);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (this.getVehicle() != null) {
            Entity vehicle = this.getVehicle();
            if (this.getVehicle() instanceof GordiusWheel gordiusWheel && gordiusWheel.getWheelEntity() != null) {
                vehicle = gordiusWheel.getWheelEntity();
            }
            double height = (this.getY() - vehicle.getY() + this.getBbHeight()) + 0.2;
            AABB aabb = this.attackBB(anim);
            aabb = new AABB(aabb.minX, -0.02, aabb.minZ, aabb.maxX, height, aabb.maxZ);
            return new OrientedBoundingBox(aabb, vehicle.getViewYRot(1), 0, vehicle.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double height = this.getBbHeight();
        double width = this.getBbWidth();
        double length = 1 * this.getScale();
        if (anim.is(ONE_HAND_1_1, ONE_HAND_2_2)) {
            width += 1.3 * this.getScale();
            length += 0.8 * this.getScale();
            return new AABB(-width * 0.8, -0.03, 0, width * 0.2, height + 0.03, length);
        }
        if (anim.is(ONE_HAND_1_2, ONE_HAND_2_1)) {
            width += 1.5 * this.getScale();
            length += 0.7 * this.getScale();
        }
        if (anim.is(STAB_1)) {
            width += 0.7 * this.getScale();
            length += 1.3 * this.getScale();
        }
        if (anim.is(LIGHTNING_1)) {
            width *= 5;
            return new AABB(-width, -0.2, -width, width, height + 0.03, width);
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    public AnimationHandler<Iskander> getAnimationHandler() {
        return this.animationHandler;
    }


    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (this.getAnimationHandler().isCurrent(SUMMON_CHARIOT, IONIOI_HETAIROI) && this.isNoblePhantasmImmune(source)) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(damageSource, damage);
        } else if (!this.isInvulnerableTo(damageSource) && this.getVehicle() != null) {
            damage *= 0.5f;
            this.getVehicle().hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    @Override
    public boolean nobelPhantasmCheck() {
        return this.healthBelow(0.5f) && super.nobelPhantasmCheck();
    }

    protected boolean canSummonMounts() {
        return !this.isPassenger() && this.summonCooldown.offCooldown();
    }

    public void summonChariot() {
        if (this.isPassenger() || !(this.level() instanceof ServerLevel level))
            return;
        GordiusWheel wheel = FateEntities.GORDIUS_WHEEL.get().spawn(level, e -> {
                    e.setPos(this.getX(), this.getY(), this.getZ());
                    e.setOwner(this);
                },
                this.blockPosition(), MobSpawnType.MOB_SUMMONED, false, false);
        this.boardingCooldown = 0;
        this.startRiding(wheel);
        for (int i = 0; i < 5; i++) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level());
            lightningboltentity.moveTo(this.getX() + this.random.nextGaussian() * 2, this.getY(), this.getZ() + this.random.nextGaussian() * 2);
            lightningboltentity.setVisualOnly(true);
            this.level().addFreshEntity(lightningboltentity);
        }
        this.summonCooldown.use();
        this.revealServant();
    }

    public void summonBucephalos() {
        if (this.isPassenger() || !(this.level() instanceof ServerLevel level))
            return;
        Bucephalos bucephalos = FateEntities.BUCEPHALOS.get().spawn(level, e -> {
                    e.setPos(this.getX(), this.getY(), this.getZ());
                    e.setOwner(this);
                },
                this.blockPosition(), MobSpawnType.MOB_SUMMONED, false, false);
        this.boardingCooldown = 0;
        this.startRiding(bucephalos);
        for (int i = 0; i < 5; i++) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level());
            lightningboltentity.moveTo(this.getX() + this.random.nextGaussian() * 2, this.getY(), this.getZ() + this.random.nextGaussian() * 2);
            lightningboltentity.setVisualOnly(true);
            this.level().addFreshEntity(lightningboltentity);
        }
        this.summonCooldown.use();
        this.revealServant();
    }

    public boolean canSummonHoplites() {
        return this.armySummonCooldown.offCooldown() && this.level().getEntitiesOfClass(Hoplite.class, this.getBoundingBox().inflate(32), e -> this.getUUID().equals(e.getOwnerUUID())).size() < this.props().getConfig(ServantExtraData.MAX_NEARBY_ARMY);
    }

    public void summonArmy() {
        if (!(this.level() instanceof ServerLevel level))
            return;
        int amount = this.getRandom().nextIntBetweenInclusive(3, 6);
        Hoplite last = null;
        for (int i = 0; i < amount; i++) {
            Hoplite hoplite = FateEntities.HOPLITE.get().create(level, b -> b.setOwner(this), this.blockPosition(), MobSpawnType.MOB_SUMMONED, false, false);
            for (int tries = 0; tries < 4; tries++) {
                double x = this.getX() + this.random.nextInt(10) - 5.0;
                double y = this.getY() + this.random.nextInt(3) - 1.0;
                double z = this.getZ() + this.random.nextInt(10) - 5.0;
                BlockPos pos = this.firstNonSolidBelow(x, y, z);
                if (pos == null)
                    continue;
                hoplite.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, this.getRandom().nextFloat() * 360.0f, 0.0f);
                if (level.noCollision(hoplite)) {
                    if (this.getRandom().nextFloat() < this.props().getConfig(ServantExtraData.STRONG_HOPLITE_CHANCE)) {
                        hoplite.setIronArmor(true);
                    }
                    level.addFreshEntity(hoplite);
                    last = hoplite;
                    break;
                }
            }
        }
        if (last != null) {
            for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(32))) {
                if (mob.getTarget() == this)
                    mob.setTarget(last);
            }
        }
        this.armySummonCooldown.use();
    }

    @SuppressWarnings("deprecation")
    private BlockPos firstNonSolidBelow(double x, double y, double z) {
        BlockPos.MutableBlockPos blockpos = BlockPos.containing(x, y, z).mutable();
        while (blockpos.getY() > this.level().getMinBuildHeight()) {
            BlockState blockstate = this.level().getBlockState(blockpos.below());
            if (blockstate.blocksMotion()) {
                return blockpos.immutable();
            }
            blockpos.move(Direction.DOWN);
        }
        return null;
    }

    @Override
    public String getDeathAnimation() {
        return DEATH;
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
