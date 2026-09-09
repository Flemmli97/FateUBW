package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.HeldEquipmentHandler;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.entity.utils.ServantModelLike;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.particles.BlockStateParticleData;
import io.github.flemmli97.fateubw.common.particles.StrikeParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.CombatUtils;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.PlayAnimation;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetAnimationToPlay;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.SequentialBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Artoria extends BaseServant {

    private static final float ATTACK_MOVE_SPEED = 1.5f;

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String TWO_HAND_1_1 = BUILDER.add("two_hand_1_1", AnimationsBuilder.definition(1.08)
            .marker("attack", 0.88).marker("step", 0.8)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.96));
    public static final String TWO_HAND_1_2 = BUILDER.add("two_hand_1_2", AnimationsBuilder.definition(1.08)
            .marker("attack", 0.88).marker("step", 0.8)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.96));
    public static final String TWO_HAND_1_3 = BUILDER.add("two_hand_1_3", AnimationsBuilder.definition(1.84)
            .marker("attack_start", 0.6).marker("attack_end", 1.56)
            .marker("reset", 1.2).marker("jump", 0.64, 1.08)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 1.64));
    public static final String TWO_HAND_2_1 = BUILDER.add("two_hand_2_1", AnimationsBuilder.definition(1.08)
            .marker("attack", 0.88).marker("step", 0.8)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.96));
    public static final String TWO_HAND_2_2 = BUILDER.add("two_hand_2_2", AnimationsBuilder.definition(1.08)
            .marker("attack", 0.88).marker("step", 0.8)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.96));
    public static final String TWO_HAND_OVERHEAD = BUILDER.add("two_hand_overhead", AnimationsBuilder.definition(1)
            .marker("critical", 0.84)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.72)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.88));

    public static final String STRIKE_AIR = BUILDER.add("strike_air", AnimationsBuilder.definition(1.4)
            .marker("setup", 0.72)
            .marker("attack", 0.72, 0.96).marker("critical", 1.2));
    public static final String BLOCK = BUILDER.add("block", AnimationsBuilder.definition(0.28));

    public static final String INVISIBLE_BURST = BUILDER.add("invisible_burst", AnimationsBuilder.definition(1.12).marker("start", 0.48));
    public static final String INVISIBLE_BURST_HIT = BUILDER.add("invisible_burst_hit", AnimationsBuilder.definition(0.44)
            .marker("attack", 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.08)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.32));
    public static final String EXCALIBAA = BUILDER.add("excalibur", AnimationsBuilder.definition(5)
            .marker("start_attack", 0.44).marker("attack", 3.36)
            .marker("charge_start", 0.44).marker("charge_end", 3.12));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    private static final String DEATH = BUILDER.add("death", AnimationsBuilder.definition(1.52).infinite());

    private static final String STAND = BUILDER.add("stand", AnimationsBuilder.definition(0.6).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    public static final TypedResource<Vec3> BURST_DIRECTION = new TypedResource<>(Fate.modRes("burst_direction"));

    private final AnimationHandler<Artoria> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (!this.level().isClientSide()) {
            if (anim == null) {
                this.getDataContainer().set(BURST_DIRECTION, null);
            } else if (anim.is(INVISIBLE_BURST) || anim.is(TWO_HAND_1_3)) {
                this.hitEntity = null;
                this.getDataContainer().set(BURST_DIRECTION, null);
            }
        }
        return false;
    });

    public final HeldEquipmentHandler heldEquipmentHandler = new HeldEquipmentHandler(this, new ItemStack(FateItems.EXCALIBUR.get()), null);

    protected List<LivingEntity> hitEntity;

    private float attackRotation, strikeAirYRot, strikeAirXRot;

    private int behaviourStand;

    public Artoria(EntityType<? extends Artoria> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<BaseServant> builder) {
        super.definedAdditinoalSyncedData(builder);
        builder.define(BURST_DIRECTION, TenshilibSyncableEntityDatas.VEC_3.get(), null);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.INVISEXCALIBUR.get()));
    }

    @Override
    public ItemStack getRenderHandStack(InteractionHand hand) {
        return switch (hand) {
            case MAIN_HAND -> {
                ItemStack stack = this.getMainHandItem();
                if (stack.is(FateItems.INVISEXCALIBUR.get()) || stack.is(FateItems.EXCALIBUR.get())) {
                    yield stack;
                }
                yield ServantModelLike.getStack(FateItems.INVISEXCALIBUR.get());
            }
            case OFF_HAND -> this.getOffhandItem();
        };
    }

    @Override
    public HeldEquipmentHandler getEquipmentHandler() {
        return this.heldEquipmentHandler;
    }

    @Override
    protected ExtendedBehaviour<? extends BaseServant> idleAnimationsBehaviour() {
        return new SequentialBehaviour<>(
                new SetAnimationToPlay<>(STAND),
                new PlayAnimation<Artoria>().withCallback((anim, m) -> {
                    m.idleAnimationCooldown.use();
                    m.behaviourStand = this.getRandom().nextInt(200, 300);
                })
        );
    }

    @Override
    protected String[] idleAnimations() {
        return new String[]{STAND};
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<BaseServant>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_1_1)
                        .start(TWO_HAND_1_2, 1, 0.6f, 1)
                        .start(TWO_HAND_1_2, 1, 0.6f, 3, owner -> owner.healthBelow(0.5f))
                        .chain(TWO_HAND_1_3, 1, 0.6f)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 15, 30))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(7)

                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_2_1)
                        .start(TWO_HAND_2_2, 1, 0.6f, 1)
                        .start(TWO_HAND_2_2, 1, 0.6f, 3, owner -> owner.healthBelow(0.66f))
                        .chain(TWO_HAND_OVERHEAD, 1, 0.6f)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 15, 30))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(7)

                .start(TWO_HAND_OVERHEAD).play(BehaviourUtils.cooldownedPlay(true, 13, 25))
                .condition(owner -> owner.getTarget() != null && owner.getTarget().getY() > owner.getY() + 0.5)
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(4)

                .start(STRIKE_AIR).play(BehaviourUtils.cooldownedPlay(true, 13, 25))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((owner, target) -> ATTACK_MOVE_SPEED).closeEnoughDist(BehaviourUtils.closeEnough(6))).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(INVISIBLE_BURST).play(BehaviourUtils.cooldownedPlay(false, 10, 27))
                .condition(BehaviourUtils.ifFurtherThan(5))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((owner, target) -> ATTACK_MOVE_SPEED).closeEnoughDist(BehaviourUtils.closeEnough(18)))
                .prepareOptional(BehaviourUtils.moveAttack())
                .end(15)
                .start(EXCALIBAA).play(BehaviourUtils.cooldownedPlay(false, 20, 35))
                .condition(BaseServant::canUseNobelPhantasm)
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(3).max(8).speedMod(ATTACK_MOVE_SPEED + 0.1f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(30)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(6, new SetWalkTargetToAttackTarget<BaseServant>().speedMod((owner, target) -> ATTACK_MOVE_SPEED), BehaviourUtils.moveTo())
                .add(2, new SetWalkTargetAwayFromTarget<BaseServant>()
                        .radius(7).speedMod(ATTACK_MOVE_SPEED), BehaviourUtils.moveTo()).build();
    }

    @Override
    public void setStaying(boolean stay) {
        super.setStaying(stay);
        if (stay) {
            this.getAnimationHandler().setAnimation(STAND);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.healthBelow(0.25f)) {
            if (!this.hasEffect(MobEffects.REGENERATION))
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 1, false, false));
        }
        if (this.level().isClientSide) {
            if (this.duringBurst()) {
                for (int i = 0; i < 8; i++)
                    this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1, 1, 1), this.getX(this.getRandom().nextGaussian() * 0.5), this.getY(this.getRandom().nextGaussian() * 0.5), this.getZ(this.getRandom().nextGaussian() * 0.5), 0, 0, 0);
            }
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityWeaponTrailProvider.TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityWeaponTrailProvider.EntityTrailData.create(this, anim.getID(), false))
                                            .setColor(221 / 255f, 199 / 255f, 34 / 255f, 0.6f)
                                            .setColor2(255 / 255f, 230 / 255f, 131 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
                if (anim.is(TWO_HAND_OVERHEAD) && anim.isAt("critical")) {
                    this.slamParticles();
                }
                if (anim.is(EXCALIBAA)) {
                    if (anim.isPast("charge_start") && !anim.isPast("charge_end")) {
                        for (int i = 0; i < 8; i++) {
                            AdvancedParticleContainer.make(FateParticles.FLASH.get())
                                    .addData(new ScaleData(0.15f + this.getRandom().nextFloat() * 0.1f))
                                    .addData(new ColorData(243 / 255f, 228 / 255f, 39 / 255f, 1))
                                    .addData(new ParticleMetaData(8, false, 0))
                                    .add(this.level(), this.getRandomX(8), this.getY(this.getRandom().nextDouble() * 6 - 2), this.getRandomZ(8));
                        }
                    }
                }
            }
        } else {
            --this.behaviourStand;
            if ((this.behaviourStand == 0 || this.getTarget() != null) && this.getAnimationHandler().isCurrent(STAND) && !this.isStaying()) {
                this.getAnimationHandler().setAnimation(null);
            }
            this.heldEquipmentHandler.setInUse(this.getAnimationHandler().isCurrent(EXCALIBAA) || this.healthBelow(0.5f));
        }
    }

    protected float strikeAirXRot(float xRot) {
        return Math.clamp(xRot, -40, 40);
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(INVISIBLE_BURST)) {
            return this.getDataContainer().get(BURST_DIRECTION);
        }
        return super.directionToLookAt();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(EXCALIBAA)) {
            if (anim.isAt("start_attack")) {
                this.startUsingItem(InteractionHand.MAIN_HAND);
                this.getMainHandItem().set(FateDataComponents.GLOWING_ITEM.get(), Unit.INSTANCE);
            }
            if (!anim.isAt("attack")) {
                this.setTargetPositionFromAttackTarget();
            }
            if (anim.isAt("attack")) {
                this.excalibur(this.getTargetPosition());
            }
        } else if (anim.is(INVISIBLE_BURST)) {
            if (anim.isAt("start")) {
                Vec3 dir = this.getTarget() != null ? this.getTarget().position().subtract(this.position()) : this.getViewVector(1);
                dir = new Vec3(dir.x(), 0, dir.z());
                this.getDataContainer().set(BURST_DIRECTION, dir.normalize().scale(1.3));
            }
            if (this.duringBurst()) {
                this.setDeltaMovement(this.getDataContainer().get(BURST_DIRECTION));
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, this.getTarget(), e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
                if (!this.hitEntity.isEmpty()) {
                    S2CScreenShake.sendAround(this, 12, 8, 2);
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.05));
                    this.getAnimationHandler().setAnimation(INVISIBLE_BURST_HIT);
                }
            }
        } else if (anim.is(TWO_HAND_1_3)) {
            if (this.hitEntity == null)
                this.hitEntity = new ArrayList<>();
            if (this.getTarget() != null) {
                this.setTargetPosition(this.getTarget(), false);
                this.lookAt(this.getTarget(), 60, 30);
            }
            if (anim.isAt("attack_start")) {
                this.attackRotation = this.getViewYRot(1);
                this.playSound(FateSounds.SWOOSH_1.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F);
            }
            if (anim.isAt("reset")) {
                this.hitEntity.clear();
                this.playSound(FateSounds.SWOOSH_1.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F);
            }
            float xRot = -Mth.clamp(this.getViewXRot(1), -40, 40);
            CombatUtils.spinAttack(this, anim, anim.getMarker("attack_start", 0), anim.getMarker("attack_end", 0),
                    this.attackRotation + 200, this.attackRotation - 450, this.getBbWidth() * 5.5f,
                    (yRot, prog) -> (1 - Math.abs(Mth.wrapDegrees(yRot) - this.attackRotation) / 90) * xRot, this.targetPred, target -> {
                        if (!this.hitEntity.contains(target)) {
                            this.doHurtTarget(target);
                            this.hitEntity.add(target);
                        }
                    });
            if (anim.isAt("jump")) {
                LivingEntity target = this.getTarget();
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.getViewVector(1);
                double vertical = Math.clamp(dir.y(), 0.2, 0.4);
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(0.75).add(0, vertical, 0);
                this.setDeltaMovement(dir);
            }
        } else if (anim.is(STRIKE_AIR)) {
            if (anim.isAt("critical")) {
                this.playSound(FateSounds.SWOOSH_1.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.2F);
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
            }
            if (anim.isAt("setup")) {
                float yRot = this.getViewYRot(1);
                float xRot = this.getViewXRot(1);
                Vec3 target = this.tryGetTargetPosition(this.getTarget());
                if (this.getControllingPassenger() instanceof Player player) {
                    yRot = player.getViewYRot(1);
                    xRot = player.getViewXRot(1);
                } else if (target != null) {
                    Vec3 dir = target.subtract(this.position().add(0, this.getBbHeight() * 0.5, 0)).normalize();
                    float[] yXRot = MathsHelper.YXRotFrom(dir);
                    yRot = yXRot[0];
                    xRot = -yXRot[1];
                }
                this.strikeAirXRot = this.strikeAirXRot(xRot);
                this.strikeAirYRot = yRot;
                Vec3 offset = new Vec3(0, 0, this.getBbWidth() * 1.5)
                        .xRot(this.strikeAirXRot * Mth.DEG_TO_RAD)
                        .yRot(-this.strikeAirYRot * Mth.DEG_TO_RAD);
                AdvancedParticleContainer.make(new StrikeParticleData(FateParticles.STRIKE.get(), this.strikeAirYRot, -this.strikeAirXRot,
                                this.getBbHeight() * 1.5f, this.getBbWidth() + 10 * this.getScale(), 20))
                        .addData(new ColorData(122 / 255f, 174 / 255f, 255 / 255f, 1))
                        .add(this.level(),
                                this.getX() + offset.x(),
                                this.getY() + offset.y() + this.getBbHeight() * 0.5,
                                this.getZ() + offset.z());
            }
            if (anim.isAt("attack")) {
                this.playSound(FateSounds.SWOOSH_1.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F);
            }
            super.handleAttack(anim);
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.35);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("critical")) {
                if (anim.is(TWO_HAND_OVERHEAD)) {
                    this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.1F);
                }
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
            }
            if (anim.isAt("attack")) {
                this.playSound(FateSounds.SLASH.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
            super.handleAttack(anim);
        }
    }

    private void slamParticles() {
        Vec3 dir = this.calculateViewVector(0, this.getViewYRot(1));
        Vec3 side = this.calculateViewVector(0, this.getViewYRot(1) + 90);
        HashSet<BlockPos> visited = new HashSet<>();
        for (int len = 0; len < 4; len++) {
            for (int width = 0; width < 2; width++) {
                Vec3 target = this.position().add(dir.scale(len)).add(side.scale(width));
                BlockPos pos = BlockPos.containing(target);
                if (visited.contains(pos))
                    continue;
                BlockState state = this.level().getBlockState(pos);
                if (state.isAir()) {
                    pos = pos.below();
                    state = this.level().getBlockState(pos);
                }
                visited.add(pos);
                this.level().addParticle(new BlockStateParticleData(FateParticles.BLOCK.get(), state, this.random.nextFloat() * 360, this.random.nextFloat() * 10, 30),
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, this.random.nextDouble() * 0.05 + 0.03, 0);
            }
        }
    }

    @Override
    public float damageModifier(Entity target) {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        if (anim != null && anim.isAt("critical")) {
            return 1.5f;
        }
        return super.damageModifier(target);
    }

    @Override
    public void onEntityHit(Entity target, float damage) {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        if (anim != null && anim.isAt("critical")) {
            this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1, 1);
            if (this.level() instanceof ServerLevel serverLevel)
                serverLevel.getChunkSource().broadcastAndSend(this, new ClientboundAnimatePacket(target, ClientboundAnimatePacket.CRITICAL_HIT));
        }
        super.onEntityHit(target, damage);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double height = this.getBbHeight();
        double width = this.getBbWidth();
        double length = 1 * this.getScale();
        if (anim.is(TWO_HAND_1_1, TWO_HAND_2_1)) {
            width += 2.75 * this.getScale();
            length += 1.2 * this.getScale();
            return new AABB(-width * 0.6, -0.03, 0, width * 0.4, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_1_2, TWO_HAND_2_2)) {
            width += 2.75 * this.getScale();
            length += 1.2 * this.getScale();
            return new AABB(-width * 0.4, -0.03, 0, width * 0.6, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_OVERHEAD)) {
            width += 0.5 * this.getScale();
            height += 1.5 * this.getScale();
            length += 2 * this.getScale();
        }
        if (anim.is(STRIKE_AIR)) {
            width += 1 * this.getScale();
            length += 9.5f * this.getScale();
        }
        if (anim.is(INVISIBLE_BURST_HIT)) {
            width += 2.5 * this.getScale();
            length += 1.1 * this.getScale();
            return new AABB(-width * 0.6, -0.03, 0, width * 0.4, height + 0.03, length);
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    private boolean duringBurst() {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && anim.is(INVISIBLE_BURST) && anim.isPast("start") && !anim.done(0);
    }

    @Override
    public Vec3 tryGetTargetPosition(LivingEntity target) {
        if (this.getTargetPosition() != null && this.getAnimationHandler().isCurrent(STRIKE_AIR))
            return this.getTargetPosition()
                    .asVec(this.position().add(0, this.getBbHeight() * 0.5, 0));
        return super.tryGetTargetPosition(target);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(STRIKE_AIR)) {
            double off = this.getBbHeight() * 0.5;
            return new OrientedBoundingBox(this.attackBB(anim)
                    .inflate(grow, 0, grow)
                    .move(0, -off, grow), this.strikeAirYRot, this.strikeAirXRot, this.position().add(0, off, 0));
        }
        if (!anim.is(INVISIBLE_BURST))
            return super.calculateAttackAABB(anim, target, grow);
        double width = this.getBbWidth();
        Vec3 dir = this.getDataContainer().get(BURST_DIRECTION);
        if (dir == null) {
            dir = this.calculateViewVector(0, this.getViewYRot(1));
        }
        float[] yXRot = MathsHelper.YXRotFrom(dir);
        double speed = Math.max(width, dir.length() - width);
        return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                .inflate(grow, 0, grow).expandTowards(0, 0, speed), yXRot[0], yXRot[1], this.position());
    }

    @Override
    public AnimationHandler<Artoria> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.getAnimationHandler().isCurrent(EXCALIBAA)) {
            return false;
        }
        if (!this.level().isClientSide() && !damageSource.is(DamageTypeTags.BYPASSES_SHIELD) && damageSource.getEntity() instanceof LivingEntity) {
            if (!this.getAnimationHandler().hasAnimation() && this.healthBelow(0.66f)
                    && this.getRandom().nextFloat() < this.props().getConfig(ServantExtraData.BLOCK_CHANCE)) {
                this.getAnimationHandler().setAnimation(BLOCK);
            }
            if (this.getAnimationHandler().isCurrent(BLOCK)) {
                this.playSound(FateSounds.BLOCK.get(), 1, 1.15f);
                return false;
            }
        }
        return super.hurt(damageSource, damage);
    }

    @Override
    protected boolean ignoreExternalMobInfluence() {
        return this.getAnimationHandler().isCurrent(EXCALIBAA);
    }

    @Override
    public boolean nobelPhantasmCheck() {
        return this.healthBelow(0.5f) && super.nobelPhantasmCheck();
    }

    public void excalibur(TargetPosition target) {
        this.getMainHandItem().remove(FateDataComponents.GLOWING_ITEM.get());
        if (!this.attemptUseNobelPhantasm())
            return;
        Excalibur excalibur = new Excalibur(this.level(), this);
        if (target != null) {
            Vec3 pos = target.asVec(excalibur.position());
            excalibur.setRotationTo(pos.x(), pos.y(), pos.z(), 0);
        }
        this.level().addFreshEntity(excalibur);
        this.revealServant();
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public String getDeathAnimation() {
        return DEATH;
    }

    @Override
    public WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector3f(0, 0, -0.7f), new Vector3f(0, 0, -2.1f));
    }
}
