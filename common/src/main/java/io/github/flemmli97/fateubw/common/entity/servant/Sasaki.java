package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.ParticlePositionProvider;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
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
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.SinMotionData;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.joml.Vector4f;

public class Sasaki extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String TWO_HAND_1 = BUILDER.add("two_hand_1", AnimationsBuilder.definition(1)
            .marker("attack", 0.88).marker("step", 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.68)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.92));
    public static final String TWO_HAND_2 = BUILDER.add("two_hand_2", AnimationsBuilder.definition(0.96)
            .marker("attack", 0.84).marker("step", 0.56)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.88));
    public static final String TWO_HAND_3 = BUILDER.add("two_hand_3", AnimationsBuilder.definition(0.92)
            .marker("attack", 0.8).marker("step", 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.84));
    public static final String TWO_HAND_4 = BUILDER.add("two_hand_4", AnimationsBuilder.definition(0.96)
            .marker("attack", 0.84).marker("step", 0.56)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.88));
    public static final String TWO_HAND_5 = BUILDER.add("two_hand_5", AnimationsBuilder.definition(0.92)
            .marker("attack", 0.8).marker("step", 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.84));
    public static final String TWO_HAND_6 = BUILDER.add("two_hand_6", AnimationsBuilder.definition(0.92)
            .marker("attack", 0.8).marker("step", 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.84));
    public static final String TWO_HAND_7 = BUILDER.add("two_hand_7", AnimationsBuilder.definition(0.92)
            .marker("attack", 0.8).marker("step", 0.52)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.84));
    public static final String ONE_HAND_1 = BUILDER.add("one_hand_1", AnimationsBuilder.definition(0.88)
            .marker("attack", 0.76)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.6)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.8));
    public static final String KATANA_1 = BUILDER.add("katana_1", AnimationsBuilder.definition(0.84)
            .marker("attack", 0.72).marker("step", 0.64));
    private static final String TSUBAME_GAESHI = BUILDER.add("tsubame_gaeshi", AnimationsBuilder.definition(2.16)
            .marker("attack_prepare", 1.24).marker("attack", 1.36)
            .marker("particle", 1.36));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(4.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Sasaki> animationHandler = new AnimationHandler<>(this, ANIMS);

    private Vec3 hikenPos;
    private boolean hiken;

    public Sasaki(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.MONOHOSHI_ZAO.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.MONOHOSHI_ZAO.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<BaseServant>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_1)
                        .start(TWO_HAND_2, 2, 0.52f, 3)
                        .start(TWO_HAND_2, 2, 0.52f, 1).chain(Sasaki.TWO_HAND_6, 2, 0.52f)
                        .start(TWO_HAND_3, 2, 0.52f, 1).chain(Sasaki.TWO_HAND_4, 2, 0.52f)
                        .start(TWO_HAND_3, 2, 0.52f, 1).chain(Sasaki.ONE_HAND_1, 2, 0.52f)
                        .start(TWO_HAND_5, 2, 0.52f, 3)
                        .start(TWO_HAND_5, 2, 0.52f, 1).chain(Sasaki.TWO_HAND_7, 2, 0.52f)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 18, 35))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((e, t) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(10)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_2)
                        .start(TWO_HAND_1, 2, 0.52f, 3)
                        .start(TWO_HAND_1, 2, 0.52f, 1).chain(Sasaki.TWO_HAND_3, 2, 0.52f)
                        .start(TWO_HAND_1, 2, 0.52f, 1).chain(Sasaki.TWO_HAND_5, 2, 0.52f)
                        .start(TWO_HAND_4, 2, 0.52f, 3)
                        .start(TWO_HAND_4, 2, 0.52f, 1).chain(Sasaki.TWO_HAND_5, 2, 0.52f)
                        .start(TWO_HAND_7, 2, 0.52f, 3)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 18, 35))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((e, t) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(10)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_3)
                        .start(TWO_HAND_6, 2, 0.52f, 5)
                        .start(TWO_HAND_6, 2, 0.52f, 3).chain(Sasaki.TWO_HAND_2, 2, 0.52f)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 18, 35))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((e, t) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(10)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_4)
                        .start(TWO_HAND_5, 2, 0.52f, 5)
                        .start(TWO_HAND_5, 2, 0.52f, 3).chain(Sasaki.TWO_HAND_1, 2, 0.52f)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 18, 35))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((e, t) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(10)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(KATANA_1)
                        .start(TWO_HAND_5, 2, 0.52f, 2)
                        .start(ONE_HAND_1, 2, 0.52f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 18, 35))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((e, t) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(10)
                .start(TSUBAME_GAESHI).play(BehaviourUtils.cooldownedPlay(false, 18, 35))
                .condition(BaseServant::canUseNP)
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(5).max(10).speedMod((e, t) -> 1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(50)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(5, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(3, new SetWalkTargetAwayFromTarget<BaseServant>()
                        .radius(5), BehaviourUtils.moveTo())
                .add(2, BehaviourUtils.ifCloserThan(8), new LeapInDirection<BaseServant>()
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()).scale(1.25f))
                        .whenStarting(e -> BrainUtils.clearMemory(e, MemoryModuleType.ATTACK_COOLING_DOWN))).build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityWeaponTrailProvider.TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityWeaponTrailProvider.EntityTrailData.create(this, anim.getID(), false))
                                            .setColor(37 / 255f, 37 / 255f, 88 / 255f, 0.4f)
                                            .setColor2(181 / 255f, 189 / 255f, 206 / 255f, 0.1f)
                                            .setWidth(1)
                                            .setWidth2(1)
                                            .setType(TrailInfo.Visual.TEXTURE, 3)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(TSUBAME_GAESHI)) {
            if (!anim.isPast("attack_prepare")) {
                if (this.getTarget() != null) {
                    this.lookAt(this.getTarget(), 60, 90);
                }
            }
            if (anim.isAt("attack_prepare")) {
                Vec3 dir;
                if (this.getTarget() != null) {
                    dir = this.getTarget().position().subtract(this.position());
                    dir = dir.add(dir.normalize().scale(3));
                    if (dir.lengthSqr() > 144)
                        dir = dir.normalize().scale(12);
                } else {
                    Vec3 look = Vec3.directionFromRotation(0, this.getYHeadRot()).scale(11);
                    HitResult res = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(look), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    dir = res.getLocation().subtract(this.getEyePosition());
                }
                this.hikenPos = this.position().add(dir);
            }
            if (anim.isAt("attack")) {
                this.tsubameSlash();
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.4);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            super.handleAttack(anim);
        }
    }

    public void tsubameSlash() {
        if (this.hikenPos == null)
            return;
        Vec3 dir = this.hikenPos.subtract(this.position());
        float yRot = MathsHelper.YRotFrom(dir);
        OrientedBoundingBox obb = new OrientedBoundingBox(new AABB(this.getBbWidth() * 0.5 - 1.5, -0.3, 0, this.getBbWidth() * 0.5 + 1.5, this.getBbHeight() + 0.3, dir.length() + 2),
                yRot, 0, this.position());
        S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
        boolean damage = false;
        this.hiken = true;
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()))) {
            if (this.doHurtTarget(entity) && !damage)
                damage = true;
            if (this.doHurtTarget(entity) && !damage)
                damage = true;
            if (this.doHurtTarget(entity) && !damage)
                damage = true;
        }
        this.hiken = false;
        if (dir.lengthSqr() < 64)
            dir = dir.normalize().scale(5);
        else
            dir = dir.scale(0.7);
        this.tsubameParticles(this.position().add(0, this.getEyeHeight(), 0).add(dir));
        if (damage) {
            this.level().playSound(null, this, SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 0.7f, 0.9f);
        }
        this.teleportTo(this.hikenPos.x(), this.hikenPos.y(), this.hikenPos.z());
    }

    private void tsubameParticles(Vec3 at) {
        int duration = 6;
        Vec3 basePos = new Vec3(3, 0, -1);
        Vec3 baseDir = new Vec3(-3.5, 0, 2).scale(1f / duration);
        Vec3 baseSweer = new Vec3(0, 0, 0.4);
        Vec3 baseNormal = baseDir.add(0, 1, 0).normalize().scale(0.4).yRot(90);
        float yRot = -this.getYRot() * Mth.DEG_TO_RAD;
        float[] angles = new float[]{-45, 45, -135};
        for (int i = 0; i < 3; i++) {
            float angle = angles[i] * Mth.DEG_TO_RAD;
            Vec3 pos = basePos.zRot(angle).yRot(yRot);
            Vec3 dir = baseDir.zRot(angle).yRot(yRot);
            Vec3 sweer = baseSweer.zRot(angle).yRot(yRot);
            Vec3 normal = baseNormal.zRot(angle).yRot(yRot);
            AdvancedParticleContainer.make(new TrailParticleData(FateParticles.TRAIL.get(),
                            TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(normal, duration))
                                    .setColor(72 / 255f, 13 / 255f, 161 / 255f, 0.7f)
                                    .setColor2(146 / 255f, 105 / 255f, 207 / 255f, 0.4f)
                                    .setWidth(0.4f)
                                    .setWidth2(0.2f)
                                    .setType(TrailInfo.Visual.TEXTURE, 0)
                                    .build()))
                    .addData(new MotionData(dir, true, false))
                    .addData(new SinMotionData(sweer, duration * 0.85f, duration, true))
                    .addData(new ParticleMetaData(duration, false, 0))
                    .build()
                    .add(this.level(), at.x() + pos.x(), at.y() + pos.y(), at.z() + pos.z());
        }
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double height = this.getBbHeight();
        double width = this.getBbWidth();
        double length = 1 * this.getScale();
        if (anim.is(TWO_HAND_1)) {
            width += 2.8 * this.getScale();
            length += 1.4 * this.getScale();
            return new AABB(-width * 0.8, -0.03, 0, width * 0.2, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_2)) {
            width += 2.8 * this.getScale();
            length += 1.4 * this.getScale();
            return new AABB(-width * 0.2, -0.03, 0, width * 0.8, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_3)) {
            width += 2.8 * this.getScale();
            length += 1.4 * this.getScale();
            return new AABB(-width * 0.4, -0.03, 0, width * 0.6, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_4)) {
            width += 2.4 * this.getScale();
            length += 1.4 * this.getScale();
            return new AABB(-width * 0.4, -0.03, 0, width * 0.6, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_5)) {
            width += 2.4 * this.getScale();
            length += 1.4 * this.getScale();
            return new AABB(-width * 0.3, -0.03, 0, width * 0.7, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_6)) {
            width += 2.8 * this.getScale();
            length += 1.4 * this.getScale();
            return new AABB(-width * 0.8, -0.03, 0, width * 0.2, height + 0.03, length);
        }
        if (anim.is(TWO_HAND_7)) {
            width += 0.5 * this.getScale();
            height += 2.1 * this.getScale();
            length += 1.6 * this.getScale();
        }
        if (anim.is(ONE_HAND_1)) {
            width += 3.4 * this.getScale();
            length += 1.7 * this.getScale();
        }
        if (anim.is(KATANA_1)) {
            width += 0.5 * this.getScale();
            length += 1.7 * this.getScale();
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    protected DamageSource damageSourceAttack(Entity target) {
        return this.hiken ? FateDamageTypes.direct(FateDamageTypes.TSUBAME, this) : super.damageSourceAttack(target);
    }

    @Override
    public AnimationHandler<Sasaki> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        return !this.getAnimationHandler().isCurrent(TSUBAME_GAESHI) && super.hurt(damageSource, damage);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public double getSummonProgress(float partialTicks) {
        double prog = super.getSummonProgress(partialTicks);
        return prog >= 0 ? Mth.clamp(prog * 2, 0, 1) : prog;
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector4f(0, 0, -1.2f, 1), new Vector4f(0, 0, -1.8f, 1));
    }
}
