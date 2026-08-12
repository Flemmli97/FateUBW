package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.HeldEquipmentHandler;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.fateubw.common.entity.misc.WeaponProjectile;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.entity.misc.ItemInGroundEntity;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.fateubw.common.registry.FateDimensions;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.common.world.RealityMarbleHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.LeapInDirection;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector4f;

import java.util.List;

public class Emiya extends BaseServant {

    public static final String LEFT_TRAIL_START = "left_trail_start";
    public static final String LEFT_TRAIL_END = "left_trail_end";
    public static final String RIGHT_TRAIL_START = "right_trail_start";
    public static final String RIGHT_TRAIL_END = "right_trail_end";

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DUAL_SLASH_1_1 = BUILDER.add("dual_blade_1_1", AnimationsBuilder.definition(0.8)
            .marker("attack_right", 0.64).marker("step", 0.6)
            .marker(RIGHT_TRAIL_START, 0.48).marker(RIGHT_TRAIL_END, 0.68));
    public static final String DUAL_SLASH_1_2 = BUILDER.add("dual_blade_1_2", AnimationsBuilder.definition(1)
            .marker("attack_left", 0.72).marker("step", 0.64)
            .marker(LEFT_TRAIL_START, 0.48).marker(LEFT_TRAIL_END, 0.88));
    public static final String DUAL_BLADE_1_3 = BUILDER.add("dual_blade_1_3", AnimationsBuilder.definition(1.04)
            .marker("attack_left", 0.6).marker("attack_right", 0.6).marker("leap", 0.4)
            .marker(LEFT_TRAIL_START, 0.48).marker(LEFT_TRAIL_END, 0.68)
            .marker(RIGHT_TRAIL_START, 0.48).marker(RIGHT_TRAIL_END, 0.68));

    public static final String DUAL_BLADE_2_1 = BUILDER.add("dual_blade_2_1", AnimationsBuilder.definition(0.8)
            .marker("attack_left", 0.68).marker("attack_right", 0.68)
            .marker(LEFT_TRAIL_START, 0.48).marker(LEFT_TRAIL_END, 0.68)
            .marker(RIGHT_TRAIL_START, 0.48).marker(RIGHT_TRAIL_END, 0.68));
    public static final String DUAL_BLADE_2_2 = BUILDER.add("dual_blade_2_2", AnimationsBuilder.definition(0.8)
            .marker("attack_left", 0.68).marker("attack_right", 0.68));

    public static final String DUAL_BLADE_THROW = BUILDER.add("dual_blade_throw", AnimationsBuilder.definition(0.76)
            .marker("throw", 0.56));

    public static final String BOW_1 = BUILDER.add("bow_1", AnimationsBuilder.definition(1.24)
            .marker("use_start", 0.48).marker("use_end", 1).marker("shoot", 1));
    public static final String BOW_2 = BUILDER.add("bow_2", AnimationsBuilder.definition(1.72)
            .marker("use_start", 0.48).marker("use_end", 1.48).marker("shoot", 1, 1.24, 1.48));
    public static final String BOW_AIR = BUILDER.add("bow_air", AnimationsBuilder.definition(1.92)
            .marker("use_start", 0.6).marker("use_end", 1.12).marker("shoot", 1.12)
            .marker("float_start", 0.6).marker("float_end", 1.24)
            .marker("jump", 0.24));

    public static final String CALADBOLG = BUILDER.add("caladbolg", AnimationsBuilder.definition(5)
            .marker("use_start", 0.68).marker("use_end", 3.2)
            .marker("shoot", 3.2));

    public static final String UNLIMITED_BLADE_WORKS = BUILDER.add("unlimited_blade_works", AnimationsBuilder.definition(7)
            .marker("start", 0.36).marker("cast", 4.2).marker("teleport", 6.8));
    public static final String UBW_ATTACK_1 = BUILDER.add("ubw_attack_1", AnimationsBuilder.definition(1.08)
            .marker("shoot", 0.48));
    public static final String UBW_ATTACK_2 = BUILDER.add("ubw_attack_2", AnimationsBuilder.definition(1.08)
            .marker("shoot", 0.48));

    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Emiya> animationHandler = new AnimationHandler<>(this, ANIMS);

    public final HeldEquipmentHandler heldEquipmentHandler = new HeldEquipmentHandler(this, ItemStack.EMPTY, new ItemStack(FateItems.EMIYAS_BOW.get()));

    private final Vector4f summonColor = new Vector4f(213 / 255f, 0, 6 / 255f, 0.7f);

    private boolean leftHandAttackFlag;

    private int weaponSpawnCooldown;

    public Emiya(EntityType<? extends Emiya> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.KANSHOU.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.KANSHOU.get());
    }

    @Override
    public HeldEquipmentHandler getEquipmentHandler() {
        return this.heldEquipmentHandler;
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        float moveSpeed = 1.5f;
        return AttackBehaviourBuilder.<Emiya>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<Emiya>builder(DUAL_SLASH_1_1)
                        .start(DUAL_SLASH_1_2, 2, 0.28f, 1)
                        .start(DUAL_SLASH_1_2, 2, 0.28f, 2, owner -> owner.healthBelow(0.5f))
                                .chain(DUAL_BLADE_1_3)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 16, 28))
                .condition(BehaviourUtils.ifCloserThan(7))
                .prepare(new SetWalkTargetToAttackTarget<Emiya>().speedMod((owner, target) -> moveSpeed)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)

                .start(BehaviourUtils.of(AnimationPlayHolder.<Emiya>builder(DUAL_BLADE_2_1)
                        .start(DUAL_BLADE_2_2, 2, 0.28f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 16, 28))
                .condition(BehaviourUtils.ifCloserThan(7))
                .prepare(new SetWalkTargetToAttackTarget<Emiya>().speedMod((owner, target) -> moveSpeed)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)

//                .start(DUAL_BLADE_THROW).play(BehaviourUtils.cooldownedPlay(true, 16, 28))
//                .condition(BehaviourUtils.ifCloserThan(7))
//                .prepare(new SetWalkTargetToAttackTarget<Emiya>().speedMod((owner, target) -> 1.5f)).prepareOptional(BehaviourUtils.timedMoveAttack())
//                .end(6)

                .start(BOW_1).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .prepare(new SetWalkTargetWithinDist<Emiya>()
                        .min(5).max(14).speedMod(moveSpeed)).prepareOptional(BehaviourUtils.moveAttack())
                .end(9)
                .start(BOW_1).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .prepare(new LeapInDirection<Emiya>().shouldLeap((owner, target) -> owner.distanceToSqr(target) < 49)
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()).scale(1.3f)))
                .end(7)
                .start(BOW_1).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .condition(BehaviourUtils.ifFurtherThan(11))
                .prepare(new SetWalkTargetWithinDist<Emiya>()
                        .min(5).max(14).speedMod(moveSpeed)).prepareOptional(BehaviourUtils.moveAttack())
                .end(6)
                .start(BOW_2).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .prepare(new SetWalkTargetWithinDist<Emiya>()
                        .min(5).max(14).speedMod(moveSpeed)).prepareOptional(BehaviourUtils.moveAttack())
                .end(7)
                .start(BOW_2).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .prepare(new LeapInDirection<Emiya>().shouldLeap((owner, target) -> owner.distanceToSqr(target) < 49)
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()).scale(1.3f)))
                .end(7)
                .start(BOW_2).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .condition(BehaviourUtils.ifFurtherThan(11))
                .prepare(new SetWalkTargetWithinDist<Emiya>()
                        .min(5).max(14).speedMod(moveSpeed)).prepareOptional(BehaviourUtils.moveAttack())
                .end(5)
                .start(BOW_AIR).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .prepare(new SetWalkTargetWithinDist<Emiya>()
                        .min(4).max(10).speedMod(moveSpeed)).prepareOptional(BehaviourUtils.moveAttack())
                .end(7)
                .start(BOW_AIR).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(12), 16, 28))
                .condition(BehaviourUtils.ifFurtherThan(11))
                .prepare(new SetWalkTargetWithinDist<Emiya>()
                        .min(4).max(10).speedMod(moveSpeed)).prepareOptional(BehaviourUtils.moveAttack())
                .end(9)
                .start(UBW_ATTACK_1).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(18), 16, 28))
                .condition(owner -> this.isInRealityMarble() && BehaviourUtils.ifFurtherThan(11).test(owner))
                .prepare(new SetWalkTargetWithinDist<Emiya>()
                        .min(5).max(14).speedMod(moveSpeed)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(19)
                .start(UBW_ATTACK_2).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(18), 16, 28))
                .condition(owner -> this.isInRealityMarble() && BehaviourUtils.ifFurtherThan(11).test(owner))
                .prepare(new SetWalkTargetWithinDist<Emiya>()
                        .min(5).max(14).speedMod(moveSpeed)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(19)
                .start(CALADBOLG).play(BehaviourUtils.cooldownedPlay(false, 20, 30))
                .condition(BaseServant::canUseNobelPhantasm)
                .prepare(new SetWalkTargetWithinDist<Emiya>()
                        .min(8).max(16).speedMod(moveSpeed + 0.1f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(3)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        float moveSpeed = 1.5f;
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(4, new SetWalkTargetToAttackTarget<BaseServant>().speedMod((owner, target) -> moveSpeed), BehaviourUtils.moveTo())
                .add(6, new SetWalkTargetAwayFromTarget<BaseServant>().speedMod((owner, target) -> moveSpeed), BehaviourUtils.moveTo()).build();
    }

    public boolean isInRealityMarble() {
//        RealityMarbleHandler.get(this.getServer())
//                        .isInRealityMarble(this)
        return this.level().dimension().equals(FateDimensions.UNLIMITED_BLADEWORKS.dimension());
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(LEFT_TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(new EntityWeaponTrailProvider.EntityTrailData(this.getId(), anim.getID(), true, 3, LEFT_TRAIL_END))
                                            .setColor(25 / 255f, 25 / 255f, 75 / 255f, 0.6f)
                                            .setColor2(25 / 255f, 25 / 255f, 75 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
                if (anim.isAt(RIGHT_TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(new EntityWeaponTrailProvider.EntityTrailData(this.getId(), anim.getID(), false, 3, RIGHT_TRAIL_END))
                                            .setColor(25 / 255f, 25 / 255f, 75 / 255f, 0.6f)
                                            .setColor2(25 / 255f, 25 / 255f, 75 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
            }
        } else {
            this.heldEquipmentHandler.setInUse(this.getAnimationHandler().isCurrent(BOW_1, BOW_2, BOW_AIR, CALADBOLG));
            --this.weaponSpawnCooldown;
            if (this.level().dimension().equals(FateDimensions.UNLIMITED_BLADEWORKS.dimension())) {
                if (this.weaponSpawnCooldown <= 0 && this.getTarget() != null) {
                    this.spawnWeaponsAround();
                }
            }

        }
    }

    private void spawnWeaponsAround() {
        int amount = this.getRandom().nextInt(7) + 4;
        for (int i = 0; i < amount; i++) {
            ItemInGroundEntity item = FateEntities.ITEM_IN_GROUND_ENTITY.get().create(this.level());
            item.setItem(CommonConfig.babylonWeapons.getRandomWeapon(this.getRandom()));
            double x = this.getX() + (this.getRandom().nextDouble() - 0.5) * 16;
            double y = this.getY() + 12;
            double z = this.getZ() + (this.getRandom().nextDouble() - 0.5) * 16;
            BlockPos.MutableBlockPos pos = BlockPos.containing(x, y - 1, z).mutable();
            while (!(this.level().getBlockState(pos)).entityCanStandOnFace(this.level(), pos, item, Direction.UP) && pos.distToCenterSqr(x, y, z) < 16 * 16) {
                pos.move(Direction.DOWN);
            }
            item.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            if (this.level().noCollision(item)) {
                this.level().addFreshEntity(item);
            }
        }
        this.weaponSpawnCooldown = this.getRandom().nextInt(300) + 600;
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(CALADBOLG)) {
            if (!this.getAnimationHandler().getAnimation().isPast("shoot")) {
                LivingEntity target = this.getTarget();
                if (target != null)
                    return target.getEyePosition().subtract(this.getEyePosition());
            }
            return null;
        }
        return super.directionToLookAt();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(UNLIMITED_BLADE_WORKS)) {
            this.getNavigation().stop();
            if (anim.isPast("start")) {
                for (int i = 0; i < 40; i++) {
                    AdvancedParticleContainer.make(FateParticles.UBW_CHANT_SMOKE.get())
                            .addData(new CirclingData(1 + this.getRandom().nextFloat() * 0.5f, 0.19f,
                                    this.getRandom().nextFloat() * 360, 2 + this.getRandom().nextFloat() * 1, MathUtils.NORMAL_Y))
                            .addData(new MotionData(0, 0.04, 0))
                            .addData(new ScaleData(0.6f))
                            .addData(new ParticleMetaData(8 + this.getRandom().nextInt(4), false, 0))
                            .add(this.level(), this.getX(), this.getY(), this.getZ());
                }
                for (int i = 0; i < 4; i++) {
                    AdvancedParticleContainer.make(FateParticles.LIGHTNING.get())
                            .addData(new ScaleData(0.3f))
                            .addData(new ParticleMetaData(5, false, 0))
                            .add(this.level(), this.getRandomX(16), this.getY(this.getRandom().nextDouble() * 7 - 2), this.getRandomZ(16));
                }
            }
            if (anim.isAt("cast")) {
                if (RealityMarbleHandler.get(this.getServer())
                        .isInRealityMarble(this) || !this.attemptUseNobelPhantasm()) {
                    this.getAnimationHandler().setAnimation(null);
                    return;
                }
                AdvancedParticleContainer.make(FateParticles.SPHERE.get())
                        .addData(new ScaleData(0, 48, 40))
                        .addData(new ColorData(1, 1, 1, 0.5f))
                        .addData(new ParticleMetaData(60, false, 0))
                        .add(this.level(), null, this.getX(), this.getY(0.5), this.getZ(), false);
                RealityMarbleHandler.prepareChunks(this, FateDimensions.UNLIMITED_BLADEWORKS.dimension(), 48);
                for (int i = 0; i < 20; i++) {
                    AdvancedParticleContainer.make(ParticleTypes.SMOKE)
                            .addData(new CirclingData(0.1f, 48 / 40f,
                                    this.getRandom().nextFloat() * 360, 10 + this.getRandom().nextFloat() * 8, MathUtils.NORMAL_Y))
                            .addData(new ScaleData(0.5f))
                            .addData(new ColorData(1, 1, 1, 0.7f))
                            .addData(new ParticleMetaData(40, false, 0))
                            .add(this.level(), this.getX(), this.getY(0.5), this.getZ());
                    AdvancedParticleContainer.make(ParticleTypes.SMOKE)
                            .addData(new CirclingData(0.1f, 40 / 40f,
                                    this.getRandom().nextFloat() * 360, 10 + this.getRandom().nextFloat() * 8, MathUtils.NORMAL_Y))
                            .addData(new ScaleData(0.5f))
                            .addData(new ColorData(1, 1, 1, 0.7f))
                            .addData(new ParticleMetaData(40, false, 0))
                            .add(this.level(), this.getX(), this.getY(2.5), this.getZ());
                    AdvancedParticleContainer.make(ParticleTypes.SMOKE)
                            .addData(new CirclingData(0.1f, 40 / 40f,
                                    this.getRandom().nextFloat() * 360, 10 + this.getRandom().nextFloat() * 8, MathUtils.NORMAL_Y))
                            .addData(new ScaleData(0.5f))
                            .addData(new ColorData(1, 1, 1, 0.7f))
                            .addData(new ParticleMetaData(40, false, 0))
                            .add(this.level(), this.getX(), this.getY(-1.5), this.getZ());
                }
            }
            if (anim.isPast("cast")) {
                S2CScreenShake.sendAround(this, 64, 2, 1);
            }
            if (anim.isAt("teleport")) {
                List<Entity> entities = this.level().getEntities(EntityTypeTest.forClass(Entity.class), this.getBoundingBox().inflate(48), e -> true);
                RealityMarbleHandler.get(this.getServer())
                        .createAndTransportTo(this, entities, FateDimensions.UNLIMITED_BLADEWORKS.dimension());
            }
        } else if (anim.is(CALADBOLG)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt("use_start")) {
                this.startUsingItem(this.bowHand());
            }
            if (anim.isAt("shoot")) {
                if (target == null || this.getSensing().hasLineOfSight(target))
                    this.caladBolg(target);
            }
            if (anim.isAt("use_end")) {
                this.stopUsingItem();
            }
        } else if (anim.is(BOW_1, BOW_2)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt("use_start")) {
                this.startUsingItem(this.bowHand());
            }
            if (anim.isAt("shoot")) {
                if (target == null || this.getSensing().hasLineOfSight(target))
                    this.attackWithRangedAttack(target);
            }
            if (anim.isAt("use_end")) {
                this.stopUsingItem();
            }
        } else if (anim.is(BOW_AIR)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt("jump")) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.getViewVector(1);
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(-1.2).add(0, 1.1, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.isAt("use_start")) {
                this.startUsingItem(this.bowHand());
            }
            if (anim.isAt("use_end")) {
                this.stopUsingItem();
            }
            if (anim.isPast("float_start") && !anim.isPast("float_end")) {
                Vec3 delta = this.getDeltaMovement().scale(0.6);
                this.setDeltaMovement(new Vec3(delta.x(), Math.max(0, delta.y()), delta.z()));
            }
            if (anim.isAt("shoot")) {
                if (target == null || this.getSensing().hasLineOfSight(target))
                    this.attackWithRangedAttackBarrage(target);
            }
            this.fallDistance = 0;
        } else if (anim.is(UBW_ATTACK_1, UBW_ATTACK_2)) {
            LivingEntity target = this.getTarget();
            if (!anim.isPast("shoot") && target != null) {
                this.getLookControl().setLookAt(target, 60.0F, 30.0F);
            }
            if (anim.isAt("shoot")) {
                if (target != null) {
                    WeaponProjectile.spawnWeapons(this, target, 7 + this.getRandom().nextInt(10), 7, WeaponProjectile.Type.UBW);
                }
            }
        } else if (anim.is(DUAL_BLADE_THROW)) {
            LivingEntity target = this.getTarget();
            if (!anim.isPast("throw") && target != null) {
                this.getLookControl().setLookAt(target, 60.0F, 30.0F);
            }
            if (anim.isAt("throw")) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.getViewVector(1);
                Vec3 side = new Vec3(dir.x(), 0, dir.z()).normalize().yRot(90 * Mth.DEG_TO_RAD).scale(0.3);

                ThrownItemEntity item = new ThrownItemEntity(this.level(), this);
                item.setPos(item.getX() + side.x(), item.getY(), item.getZ() + side.z());
                item.setItemType(ThrownItemEntity.ItemType.KANSHOU);
                item.setWeapon(this.getMainHandItem().copy());
                item.shoot(dir.x(), dir.y(), dir.z(), 1.2f, 0);
                this.level().addFreshEntity(item);

                item = new ThrownItemEntity(this.level(), this);
                item.setPos(item.getX() - side.x(), item.getY(), item.getZ() - side.z());
                item.setWeapon(new ItemStack(FateItems.BAKUYA.get()));
                item.setItemType(ThrownItemEntity.ItemType.BAKUYA);
                item.shoot(dir.x(), dir.y(), dir.z(), 1.2f, 0);
                this.playSound(FateSounds.DAGGER_THROW.get(), 1.0F, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F);
                this.level().addFreshEntity(item);
            }
        } else {
            if (anim.isAt("leap")) {
                LivingEntity target = this.getTarget();
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.getViewVector(1);
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().add(0, 0.24, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.35);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("attack_left")) {
                this.leftHandAttackFlag = true;
                this.playSound(FateSounds.SWOOSH_2.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7f);
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.setTargetPosition((TargetPosition) null);
                this.leftHandAttackFlag = false;
            }
            if (anim.isAt("attack_right")) {
                this.playSound(FateSounds.SWOOSH_2.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7f);
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.setTargetPosition((TargetPosition) null);
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double height = this.getBbHeight();
        double width = this.getBbWidth();
        double length = 1 * this.getScale();
        if (anim.is(DUAL_SLASH_1_1)) {
            width += 0.7 * this.getScale();
            length += 0.5 * this.getScale();
            return new AABB(-width * (this.leftHandAttackFlag ? 0.3 : 0.7), -0.03, 0, width * (this.leftHandAttackFlag ? 0.7 : 0.3), height + 0.03, length);
        }
        if (anim.is(DUAL_SLASH_1_2)) {
            width += 1 * this.getScale();
            length += 1 * this.getScale();
            return new AABB(-width * 1.5, -0.03, -length, width * 0.5, height + 0.03, length);
        }
        if (anim.is(DUAL_BLADE_1_3)) {
            width += 0.8 * this.getScale();
            length += 1.5 * this.getScale();
        }
        if (anim.is(DUAL_BLADE_2_1) || anim.is(DUAL_BLADE_2_2)) {
            width += 1 * this.getScale();
            length += 1.5 * this.getScale();
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    public AnimationHandler<Emiya> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        return !this.getAnimationHandler().isCurrent(CALADBOLG) && super.hurt(damageSource, damage);
    }

    @Override
    public void regenMana(Entity source) {
        if (source != this) {
            double amount = this.getAttributeValue(FateAttributes.MANA_LEECH.asHolder());
            this.regenMana(amount * 0.5);
            return;
        }
        super.regenMana(source);
    }

    @Override
    public boolean nobelPhantasmCheck() {
        return this.healthBelow(0.75f) && super.nobelPhantasmCheck();
    }

    public void attackWithRangedAttack(LivingEntity target) {
        ItemStack stack = this.getItemInHand(this.bowHand());
        if (!this.level().isClientSide) {
            ArcherArrow arrow = new ArcherArrow(this.level(), this, stack.isEmpty() ? null : stack);
            if (target != null) {
                double dX = target.getX() - this.getX();
                double dY = target.getY(0.3333333333333333) - arrow.getY();
                double dZ = target.getZ() - this.getZ();
                double l = Math.sqrt(dX * dX + dZ * dZ);
                arrow.shoot(dX, dY + l * 0.13, dZ, 2.2F, 2);
            } else {
                Vec3 look = this.getViewVector(1);
                arrow.shoot(look.x(), look.y(), look.z(), 2.2F, 2);
            }
            arrow.setCritArrow(true);
            double mod = this.getAnimationHandler().isCurrent(BOW_2) ? 0.5 : 0.6;
            arrow.setBaseDamage(arrow.getBaseDamage() + this.getAttributeValue(Attributes.ATTACK_DAMAGE) * mod);
            this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(arrow);
        }
    }

    public void attackWithRangedAttackBarrage(LivingEntity target) {
        if (this.level().isClientSide)
            return;
        ItemStack stack = this.getItemInHand(this.bowHand());
        for (int i = 0; i < 8; i++) {
            ArcherArrow arrow = new ArcherArrow(this.level(), this, stack);
            if (target != null) {
                double dX = target.getX() - this.getX();
                double dY = target.getY(0.33) - arrow.getY();
                double dZ = target.getZ() - this.getZ();
                double l = Math.sqrt(dX * dX + dZ * dZ);
                arrow.shoot(dX, dY + l * 0.13, dZ, 2.2F, 11);
            } else {
                Vec3 look = this.getViewVector(1);
                arrow.shoot(look.x(), look.y(), look.z(), 2.2F, 11);
            }
            arrow.setCritArrow(true);
            arrow.setBaseDamage(arrow.getBaseDamage() + this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.33);
            this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(arrow);
        }
        this.applyManaLeechDebuff(40, 0.3);
    }

    public void caladBolg(LivingEntity target) {
        CaladBolg bolg = new CaladBolg(this.level(), this);
        if (target != null)
            bolg.shootAtEntity(target, 2F, 0);
        else
            bolg.shoot(this, this.getXRot(), this.getYRot(), 0, 2, 0);
        this.level().addFreshEntity(bolg);
        this.revealServant();
    }

    protected InteractionHand bowHand() {
        return this.getMainHandItem().getItem() instanceof BowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    @Override
    public boolean flipAnimation() {
        return this.getAnimationHandler().isCurrent(BOW_1, BOW_AIR, CALADBOLG)
                && this.getMainHandItem().getItem() instanceof BowItem;
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }

    @Override
    public boolean shouldRecordData() {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && ((anim.isPast(LEFT_TRAIL_START) && !anim.isPast(LEFT_TRAIL_END))
                || (anim.isPast(RIGHT_TRAIL_START) && !anim.isPast(RIGHT_TRAIL_END)));
    }

    @Override
    public WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector4f(0, 0, -0.2f, 1), new Vector4f(0, 0, -0.6f, 1));
    }
}