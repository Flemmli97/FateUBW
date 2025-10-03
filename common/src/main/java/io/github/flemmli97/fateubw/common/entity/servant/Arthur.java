package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.HeldEquipmentHandler;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
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
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Arthur extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String TWO_HAND_1 = BUILDER.add("two_hand_1", AnimationsBuilder.definition(1)
            .marker("attack", 0.88).marker("step", 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.92));
    public static final String TWO_HAND_2 = BUILDER.add("two_hand_2", AnimationsBuilder.definition(0.96)
            .marker("attack", 0.84).marker("step", 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.88));
    public static final String TWO_HAND_3 = BUILDER.add("two_hand_3", AnimationsBuilder.definition(0.92)
            .marker("attack", 0.8).marker("step", 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.84));
    public static final String TWO_HAND_4 = BUILDER.add("two_hand_4", AnimationsBuilder.definition(0.96)
            .marker("attack", 0.84).marker("step", 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.88));
    public static final String ONE_HAND_1 = BUILDER.add("one_hand_1", AnimationsBuilder.definition(0.88)
            .marker("attack", 0.76)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.8));
    public static final String STAB_1 = BUILDER.add("stab_1", AnimationsBuilder.definition(1.4)
            .marker("attack", 0.72, 0.96).marker("attack_final", 1.16));
    public static final String INVISIBLE_BURST = BUILDER.add("invisible_burst", AnimationsBuilder.definition(1).marker("start", 0.44));
    public static final String INVISIBLE_BURST_HIT = BUILDER.add("invisible_burst_hit", AnimationsBuilder.definition(0.6)
            .marker("attack", 0.48)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.52));
    public static final String EXCALIBAA = BUILDER.add("excalibur", AnimationsBuilder.definition(2.84)
            .marker("start_attack", 0.1).marker("attack", 1.4));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    public static final TypedResource<Vec3> BURST_DIRECTION = new TypedResource<>(Fate.modRes("burst_direction"));

    private final AnimationHandler<Arthur> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (!this.level().isClientSide()) {
            if (anim == null) {
                this.getDataContainer().set(BURST_DIRECTION, null);
            } else if (anim.is(INVISIBLE_BURST)) {
                this.hitEntity = null;
                this.getDataContainer().set(BURST_DIRECTION, null);
            }
        }
        return false;
    });

    public final HeldEquipmentHandler heldEquipmentHandler = new HeldEquipmentHandler(this, new ItemStack(FateItems.EXCALIBUR.get()), null);

    protected List<LivingEntity> hitEntity;

    public Arthur(EntityType<? extends Arthur> entityType, Level level) {
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
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.INVISEXCALIBUR.get()) ||
                this.getMainHandItem().is(FateItems.EXCALIBUR.get());
    }

    @Override
    public HeldEquipmentHandler getEquipmentHandler() {
        return this.heldEquipmentHandler;
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<BaseServant>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_1)
                        .start(TWO_HAND_2, 2, 0.52f, 3)
                        .start(TWO_HAND_3, 2, 0.52f, 2)
                        .start(TWO_HAND_3, 2, 0.52f, 1)
                        .chain(ONE_HAND_1, 2, 0.52f)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 15, 30))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_2)
                        .start(TWO_HAND_1, 2, 0.52f, 3)
                        .start(TWO_HAND_4, 2, 0.52f, 2)
                        .start(TWO_HAND_4, 2, 0.52f, 1)
                        .chain(ONE_HAND_1, 2, 0.52f)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 15, 30))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_3)
                        .start(TWO_HAND_1, 2, 0.52f, 1)
                        .start(TWO_HAND_4, 2, 0.52f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 15, 30))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_4)
                        .start(TWO_HAND_2, 2, 0.52f, 1)
                        .start(TWO_HAND_3, 2, 0.52f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 15, 30))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(ONE_HAND_1).play(BehaviourUtils.cooldownedPlay(true, 13, 25))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(STAB_1).play(BehaviourUtils.cooldownedPlay(true, 13, 25))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(4)
                .start(INVISIBLE_BURST).play(BehaviourUtils.cooldownedPlay(false, 10, 27))
                .condition(BehaviourUtils.ifFurtherThan(5))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().closeEnoughDist(BehaviourUtils.closeEnough(16)))
                .prepareOptional(BehaviourUtils.moveAttack())
                .end(15)
                .start(EXCALIBAA).play(BehaviourUtils.cooldownedPlay(false, 20, 35))
                .condition(BaseServant::canUseNobelPhantasm)
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(3).max(8).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(30)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(6, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(2, new SetWalkTargetAwayFromTarget<BaseServant>()
                        .radius(7), BehaviourUtils.moveTo()).build();
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
                if (anim.is(STAB_1) && (anim.isAt("attack") || anim.isAt("attack_final"))) {
                    Vec3 offset = new Vec3(0, this.getBbHeight() * 0.5, this.getBbWidth() + 1.4 * this.getScale())
                            .yRot(-this.getYRot() * Mth.DEG_TO_RAD);
                    for (int i = 0; i < 6; i++) {
                        AdvancedParticleContainer.make(FateParticles.FLASH.get())
                                .addData(new ColorData(248 / 255f, 248 / 255f, 100 / 255f, 0.5f))
                                .addData(new ScaleData(0.4f))
                                .addData(new ParticleMetaData(10, false, 0))
                                .add(this.level(),
                                        this.getX() + offset.x() + this.getRandom().nextGaussian() * 0.3,
                                        this.getY() + offset.y() + this.getRandom().nextGaussian() * 0.15,
                                        this.getZ() + offset.z() + this.getRandom().nextGaussian() * 0.3);
                    }
                }
            }
        } else {
            this.heldEquipmentHandler.setInUse(this.getAnimationHandler().isCurrent(EXCALIBAA) || this.healthBelow(0.5f));
        }
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
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.35);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("attack_final")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public float damageModifier(Entity target) {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        if (anim != null && anim.isAt("attack_final")) {
            return 1.5f;
        }
        return super.damageModifier(target);
    }

    @Override
    public void onEntityHit(Entity target, float damage) {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        if (anim != null && anim.isAt("attack_final")) {
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
        if (anim.is(TWO_HAND_1)) {
            width += 2.5 * this.getScale();
            length += 1.1 * this.getScale();
            return new AABB(-width * 0.6, -0.03, 0, width * 0.4, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_2)) {
            width += 2.5 * this.getScale();
            length += 1.1 * this.getScale();
            return new AABB(-width * 0.4, -0.03, 0, width * 0.6, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_3, TWO_HAND_4)) {
            width += 2 * this.getScale();
            length += 1.1 * this.getScale();
        }
        if (anim.is(ONE_HAND_1)) {
            width += 0.5 * this.getScale();
            height += 1.5 * this.getScale();
            length += 1 * this.getScale();
        }
        if (anim.is(STAB_1)) {
            width += 0.2 * this.getScale();
            length += 1.6 * this.getScale();
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
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (!anim.is(INVISIBLE_BURST))
            return super.calculateAttackAABB(anim, target, grow);
        double width = this.getBbWidth();
        double speed = Math.max(width, this.getDeltaMovement().length() - width);
        return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                .inflate(grow, 0, grow).expandTowards(0, 0, speed), this.getYRot(), this.getXRot(), this.position());
    }

    @Override
    public AnimationHandler<Arthur> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        return !this.getAnimationHandler().isCurrent(EXCALIBAA) && super.hurt(damageSource, damage);
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
    public WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector4f(0, 0, -0.7f, 1), new Vector4f(0, 0, -1.4f, 1));
    }
}
