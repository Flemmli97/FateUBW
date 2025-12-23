package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.AestusDomusBeam;
import io.github.flemmli97.fateubw.common.particles.StaticFacingParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.CirclingData;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.tslat.smartbrainlib.util.BrainUtils;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Nero extends BaseServant {

    public static final String SWING_2_START = "swing_2";
    public static final String SWING_2_END = "swing_2_end";

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String HORIZONTAL_SLASHES = BUILDER.add("horizontal_slashes", AnimationsBuilder.definition(1.84)
            .marker("attack", 0.8, 1.48).marker("step", 0.72, 1.36)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.92)
            .marker(SWING_2_START, 1.32)
            .marker(SWING_2_END, 1.64));
    public static final String VERTICAL_SLASHES = BUILDER.add("vertical_slashes", AnimationsBuilder.definition(1.96)
            .marker("attack_1_start", 0.44).marker("attack_1_end", 0.76)
            .marker("attack_2_start", 1.32).marker("attack_2_end", 1.72)
            .marker("attack_2_sound", 1.48)
            .marker("jump", 0.44)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.4)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.76)
            .marker(SWING_2_START, 1.32)
            .marker(SWING_2_END, 1.72));
    public static final String STAB = BUILDER.add("stab", AnimationsBuilder.definition(1.08)
            .marker("crit", 0.56));
    public static final String AESTUS_DOMUS_AUREA = BUILDER.add("aestus_domus_aurea", AnimationsBuilder.definition(6.2)
            .marker("glow", 1.2).marker("circle", 3.12)
            .marker("petal_stop", 3.8)
            .marker("attack_end", 5.2)
            .marker("charge", 5.08)
            .marker("charge_motion_end", 5.28)
            .marker("charge_attack_end", 5.4)
            .marker("charge_particle_end", 5.52));
    public static final String AESTUS_DOMUS_AUREA_FULL = BUILDER.add("aestus_domus_aurea_full", AnimationsBuilder.definition(12.16)
            .marker("glow", 1.2).marker("circle", 5.56)
            .marker("petal_stop", 9.28)
            .marker("attack_end", 10.4)
            .marker("charge", 10.24)
            .marker("charge_motion_end", 10.48)
            .marker("charge_attack_end", 10.6)
            .marker("charge_particle_end", 10.72));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    public static final TypedResource<Vec3> CHARGE_DIRECTION = new TypedResource<>(Fate.modRes("charge_direction"));

    private final AnimationHandler<Nero> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (!this.level().isClientSide()) {
            if (anim == null) {
                this.getDataContainer().set(CHARGE_DIRECTION, null);
                this.hitEntity = null;
            }
        }
        return false;
    });

    private final Vector4f summonColor = new Vector4f(234 / 255f, 75 / 255f, 0 / 255f, 0.7f);

    protected List<LivingEntity> hitEntity;

    public Nero(EntityType<? extends Nero> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<BaseServant> builder) {
        super.definedAdditinoalSyncedData(builder);
        builder.define(CHARGE_DIRECTION, TenshilibSyncableEntityDatas.VEC_3.get(), null);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.AESTUS_ESTUS.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.AESTUS_ESTUS.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<BaseServant>create()
                .start(HORIZONTAL_SLASHES).play(BehaviourUtils.cooldownedPlay(true, 15, 30))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(10)
                .start(VERTICAL_SLASHES).play(BehaviourUtils.cooldownedPlay(true, 15, 30))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().closeEnoughDist(BehaviourUtils.closeEnough(4))).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(VERTICAL_SLASHES).play(BehaviourUtils.cooldownedPlay(false, 15, 30))
                .condition(entity -> {
                    Entity target = BrainUtils.getTargetOfEntity(entity);
                    if (target == null)
                        return false;
                    double horDist = entity.distanceToSqr(target.getX(), entity.getY(), target.getZ());
                    double maxDist = 12 + target.getBbWidth() * 0.5 + entity.getBbWidth();
                    if (target.getY() - entity.getY() > 4 && horDist <= maxDist * maxDist)
                        return true;
                    double dist = entity.distanceToSqr(target);
                    return dist > 25 && dist < 144;
                })
                .end(10)
                .start(STAB).play(BehaviourUtils.cooldownedPlay(true, 13, 25))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(7)
                .start(AESTUS_DOMUS_AUREA).play(BehaviourUtils.cooldownedPlay(false, 20, 35))
                .condition(BaseServant::canUseNobelPhantasm)
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>()
                        .closeEnoughDist(BehaviourUtils.closeEnough(6))
                        .speedMod((e, t) -> 1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(35)
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
        if (this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityWeaponTrailProvider.TRAIL_START) || anim.isAt(SWING_2_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(new EntityWeaponTrailProvider.EntityTrailData(this.getId(), anim.getID(), false, 4,
                                                    anim.isAt(SWING_2_START) ? SWING_2_END : EntityWeaponTrailProvider.TRAIL_END))
                                            .setColor(230 / 255f, 36 / 255f, 15 / 255f, 0.6f)
                                            .setColor2(69 / 255f, 5 / 255f, 1 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
                if (anim.is(STAB) && anim.isAt("crit")) {
                    Vec3 offset = new Vec3(0, this.getBbHeight() * 0.7, this.getBbWidth() + 1.4 * this.getScale())
                            .yRot(-this.getYHeadRot() * Mth.DEG_TO_RAD);
                    AdvancedParticleContainer.make(FateParticles.GLOWING_RING.get())
                            .addData(new ColorData(161 / 255f, 45 / 255f, 14 / 255f, 0.8f))
                            .addData(new ScaleData(0.2f, 0.8f, 2))
                            .addData(new ParticleMetaData(8, false, 0))
                            .add(this.level(),
                                    this.getX() + offset.x(),
                                    this.getY() + offset.y(),
                                    this.getZ() + offset.z());
                }
            }
        }
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(AESTUS_DOMUS_AUREA, AESTUS_DOMUS_AUREA_FULL)) {
            return this.getDataContainer().get(CHARGE_DIRECTION);
        }
        return super.directionToLookAt();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(AESTUS_DOMUS_AUREA, AESTUS_DOMUS_AUREA_FULL)) {
            this.getNavigation().stop();
            boolean full = anim.is(AESTUS_DOMUS_AUREA_FULL);
            if (this.getTarget() != null) {
                this.getLookControl().setLookAt(this.getTarget());
            }
            if (anim.isAt("glow")) {
                if (!this.attemptUseNobelPhantasm()) {
                    this.getAnimationHandler().setAnimation(null);
                    return;
                }
                Vec3 off = new Vec3(-4 / 16d, 40 / 16d, 10 / 16d)
                        .yRot(-this.getYHeadRot() * Mth.DEG_TO_RAD);
                AdvancedParticleContainer.make(FateParticles.GLOWING_RING.get())
                        .addData(new ColorData(247 / 255f, 233 / 255f, 68 / 255f, 1))
                        .addData(new ScaleData(0.1f, 1.5f, 5))
                        .addData(new ParticleMetaData(full ? 50 : 18, false, 0))
                        .add(this.level(), this.getX() + off.x(), this.getY() + off.y(), this.getZ() + off.z());
                for (int i = 0; i < 32; i++) {
                    AdvancedParticleContainer.make(FateParticles.ROSE_PETAL.get())
                            .addData(new ScaleData(this.getRandom().nextFloat() * 0.04f + 0.1f))
                            .addData(new CirclingData(0, 0.1f + this.getRandom().nextFloat() * 0.05f,
                                    this.getRandom().nextFloat() * 360, 10 + this.getRandom().nextInt(20), MathUtils.NORMAL_Y))
                            .addData(new ParticleMetaData(32, false, 0))
                            .add(this.level(), this.getX() + off.x(), this.getY() + off.y(), this.getZ() + off.z());
                }
                this.playSound(FateSounds.AESTUS_DOMUS_ROSES.get(), 2, 1);
            }
            if (anim.isAt("circle")) {
                AdvancedParticleContainer.make(new StaticFacingParticleData(FateParticles.MAGIC_CIRCLE_2.get(), 0, -90))
                        .addData(new ColorData(new Vector4f(244 / 255f, 5 / 255f, 5 / 255f, 1),
                                Optional.of(new Vector4f(244 / 255f, 198 / 255f, 37 / 255f, 1)), 18))
                        .addData(new ScaleData(0.1f, 6, 6))
                        .addData(new ParticleMetaData(full ? 100 : 50, false, 0))
                        .add(this.level(), this.getX(), this.getY() + 0.01, this.getZ());
                this.playSound(FateSounds.AESTUS_DOMUS_GROUND_STAB.get(), 2, 1);
            }
            if (!anim.isPast("petal_stop")) {
                for (int i = 0; i < 16; i++) {
                    Vec3 ppos = this.position()
                            .add((this.random.nextDouble() * 2 - 1) * 12,
                                    this.random.nextDouble() * 6 - 2,
                                    (this.random.nextDouble() * 2 - 1) * 12);
                    AdvancedParticleContainer.make(FateParticles.ROSE_PETAL.get())
                            .addData(new ScaleData(this.getRandom().nextFloat() * 0.04f + 0.05f))
                            .addData(new MotionData(this.random.nextDouble() * 0.1 - 0.05, this.random.nextDouble() * 0.1 - 0.05, this.random.nextDouble() * 0.1 - 0.05))
                            .addData(new ParticleMetaData(20 + this.getRandom().nextInt(10), false, 0))
                            .add(this.level(), ppos.x(), ppos.y(), ppos.z());
                }
            }
            if (!anim.isPast("charge")) {
                this.setTargetPositionFromAttackTarget();
            }
            if (anim.isAt("charge")) {
                Vec3 dir = this.getTargetPosition() == null ? this.getLookAngle() : this.getTargetPosition().position().subtract(this.position()).normalize();
                this.getDataContainer().set(CHARGE_DIRECTION, dir.normalize().scale(4.5));
            }
            if (anim.isPast("charge")) {
                if (!anim.isPast("charge_motion_end")) {
                    this.setDeltaMovement(this.getDataContainer().get(CHARGE_DIRECTION));
                }
                if (anim.isAt("charge_motion_end")) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.9, 0.2, 0.9));
                }
                if (!anim.isPast("charge_attack_end")) {
                    if (this.hitEntity == null)
                        this.hitEntity = new ArrayList<>();
                    this.mobAttack(anim, this.getTarget(), e -> {
                        if (!this.hitEntity.contains(e)) {
                            this.hitEntity.add(e);
                            AestusDomusBeam beam = new AestusDomusBeam(this.level(), this, e);
                            this.level().addFreshEntity(beam);
                        }
                    });
                }
                if (!anim.isPast("charge_particle_end")) {
                    for (int i = 0; i < 24; i++) {
                        double x = Mth.lerp(this.getRandom().nextDouble(), this.xo, this.getX()) + this.getBbWidth() * (1.5 * this.random.nextDouble() - 0.7);
                        double y = Mth.lerp(this.getRandom().nextDouble(), this.yo, this.getY()) + this.getBbHeight() * this.random.nextDouble();
                        double z = Mth.lerp(this.getRandom().nextDouble(), this.zo, this.getZ()) + this.getBbWidth() * (1.5 * this.random.nextDouble() - 0.7);
                        AdvancedParticleContainer.make(FateParticles.ROSE_PETAL.get())
                                .addData(new ScaleData(this.getRandom().nextFloat() * 0.05f + 0.1f))
                                .addData(new MotionData(this.getDeltaMovement().normalize().scale(-0.07 * this.getRandom().nextDouble() - 0.05)))
                                .addData(new ParticleMetaData(20 + this.getRandom().nextInt(10), false, 0))
                                .add(this.level(), x, y, z);
                    }
                }
            }
            this.fallDistance = 0;
        } else if (anim.is(VERTICAL_SLASHES)) {
            if (anim.isAt("jump")) {
                LivingEntity target = this.getTarget();
                Vec3 dir = Vec3.ZERO;
                if (target != null) {
                    dir = target.position().subtract(this.position());
                    dir = new Vec3(dir.x(), 0, dir.z());
                    if (dir.lengthSqr() > 16 * 16)
                        dir = dir.normalize().scale(16 * 0.24);
                    else
                        dir = dir.scale(0.24);
                }
                this.setDeltaMovement(dir.x(), 0.5, dir.z());
                this.applyNoGravityMove(true);
            }
            if (anim.isAt("attack_1_start")) {
                this.playSound(FateSounds.SLASH.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
            if (anim.isPast("attack_1_start") && !anim.isPast("attack_1_end")) {
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, this.getTarget(), e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            }
            if (anim.isAt("attack_1_end")) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
            }
            if (anim.isAt("attack_2_start")) {
                if (this.getTarget() != null) {
                    this.getLookControl().setLookAt(this.getTarget());
                }
                Vec3 delta = this.getDeltaMovement();
                this.setDeltaMovement(new Vec3(delta.x(), -1.2, delta.z()));
                this.applyNoGravityMove(false);
            }
            if (anim.isAt("attack_2_start")) {
                this.hitEntity = null;
            }
            if (anim.isAt("attack_2_sound")) {
                this.playSound(FateSounds.SLASH_IMPACT.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F);
            }
            if (anim.isPast("attack_2_start") && !anim.isPast("attack_2_end")) {
                this.fallDistance = 0;
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, this.getTarget(), e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(1.1);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("crit")) {
                this.playSound(FateSounds.SWOOSH.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
            }
            if (anim.isAt("attack")) {
                this.playSound(FateSounds.SLASH.get(), 2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public float damageModifier(Entity target) {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        if (anim != null && anim.isAt("crit")) {
            return 1.75f;
        }
        return super.damageModifier(target);
    }

    @Override
    public void onEntityHit(Entity target, float damage) {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        if (anim != null && anim.isAt("crit")) {
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
        if (anim.is(VERTICAL_SLASHES)) {
            height += 1.2 * this.getScale();
            width += 1.3 * this.getScale();
            length += 1.2 * this.getScale();
            AABB aabb = new AABB(-width * 0.5, -0.53, 0, width * 0.5, height + 0.03, length);
            if (anim.isPast("attack_2_start")) {
                aabb = aabb.move(0, -this.getDeltaMovement().y(), 0);
            }
            return aabb;
        }
        if (anim.is(HORIZONTAL_SLASHES)) {
            width += 3.2 * this.getScale();
            length += 1.3 * this.getScale();
            height += 0.5 * this.getScale();
        }
        if (anim.is(STAB)) {
            width += 0.2 * this.getScale();
            length += 1.7 * this.getScale();
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(VERTICAL_SLASHES)) {
            OrientedBoundingBox obb = super.calculateAttackAABB(anim, target, grow);
            return obb.rotate(obb.getYRot(), 0);
        }
        if (!anim.is(AESTUS_DOMUS_AUREA, AESTUS_DOMUS_AUREA_FULL))
            return super.calculateAttackAABB(anim, target, grow);
        double width = this.getBbWidth();
        double speed = Math.max(width, this.getDeltaMovement().length() - width);
        Vec3 look = this.directionToLookAt();
        float yRot = look != null ? MathsHelper.YRotFrom(look) : this.getYRot();
        return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                .inflate(2 + grow, 1, 2 + grow).expandTowards(0, 0, speed), yRot, 0, this.position());
    }

    @Override
    public AnimationHandler<Nero> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        return !this.getAnimationHandler().isCurrent(AESTUS_DOMUS_AUREA, AESTUS_DOMUS_AUREA_FULL) && super.hurt(damageSource, damage);
    }

    @Override
    protected boolean ignoreExternalMobInfluence() {
        return this.getAnimationHandler().isCurrent(AESTUS_DOMUS_AUREA, AESTUS_DOMUS_AUREA_FULL);
    }

    @Override
    public boolean nobelPhantasmCheck() {
        return this.healthBelow(0.5f) && super.nobelPhantasmCheck();
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
        if (anim == null)
            return false;
        return (anim.isPast(EntityWeaponTrailProvider.TRAIL_START) && !anim.isPast(EntityWeaponTrailProvider.TRAIL_END))
                || (anim.isPast(SWING_2_START) && !anim.isPast(SWING_2_END));
    }

    @Override
    public WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector4f(0, 0, -0.7f, 1), new Vector4f(0, 0, -1.4f, 1));
    }
}
