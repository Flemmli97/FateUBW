package io.github.flemmli97.fateubw.common.entity.servant;


import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.MotionTrailProvider;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityTrailProvider;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
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
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntitySasaki extends BaseServant {

    public static final AnimatedAction TWO_HAND_1 = AnimatedAction.builder(0.78, "two_hand_1")
            .marker("attack", 0.64).marker("step", 0.68)
            .marker(EntityTrailProvider.TRAIL_START, 0.4).build();
    public static final AnimatedAction TWO_HAND_2 = AnimatedAction.builder(0.7, "two_hand_2")
            .marker("attack", 0.56).marker("step", 0.6)
            .marker(EntityTrailProvider.TRAIL_START, 0.36).build();
    public static final AnimatedAction TWO_HAND_3 = AnimatedAction.builder(0.7, "two_hand_3")
            .marker("attack", 0.48).marker("step", 0.56)
            .marker(EntityTrailProvider.TRAIL_START, 0.36).build();
    public static final AnimatedAction TWO_HAND_4 = AnimatedAction.builder(0.7, "two_hand_4")
            .marker("attack", 0.48).marker("step", 0.56)
            .marker(EntityTrailProvider.TRAIL_START, 0.36).build();
    public static final AnimatedAction TWO_HAND_5 = AnimatedAction.builder(0.7, "two_hand_5")
            .marker("attack", 0.52).marker("step", 0.56)
            .marker(EntityTrailProvider.TRAIL_START, 0.4).build();
    public static final AnimatedAction TWO_HAND_6 = AnimatedAction.builder(0.7, "two_hand_6")
            .marker("attack", 0.56).marker("step", 0.6)
            .marker(EntityTrailProvider.TRAIL_START, 0.4).build();
    public static final AnimatedAction TWO_HAND_7 = AnimatedAction.builder(0.7, "two_hand_7")
            .marker("attack", 0.6).marker("step", 0.52)
            .marker(EntityTrailProvider.TRAIL_START, 0.4).build();
    public static final AnimatedAction ONE_HAND_1 = AnimatedAction.builder(0.62, "one_hand_1")
            .marker("attack", 0.44).marker("step", 0.48)
            .marker(EntityTrailProvider.TRAIL_START, 0.32).build();
    public static final AnimatedAction KATANA_1 = AnimatedAction.builder(0.78, "katana_1")
            .marker("attack", 0.6).marker("step", 0.64).build();

    private static final AnimatedAction TSUBAME_GAESHI = AnimatedAction.builder(2, "tsubame_gaeshi")
            .marker("attack_prepare", 1.2).marker("attack", 1.28)
            .marker("particle", 1.24).build();
    public static final AnimatedAction SUMMON = new AnimatedAction(4., "summon");
    private static final AnimatedAction[] ANIMS = {TWO_HAND_1, TWO_HAND_2, TWO_HAND_3, TWO_HAND_4, TWO_HAND_5, TWO_HAND_6, TWO_HAND_7, ONE_HAND_1, KATANA_1, TSUBAME_GAESHI, SUMMON};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntitySasaki>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntitySasaki>(EntitySasaki.TWO_HAND_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 15)
                    .chain(GoalAttackAction.<EntitySasaki>chainBuilder(EntitySasaki.TWO_HAND_2, 2, 0.24f, 1)
                            .or(EntitySasaki.TWO_HAND_2, 2, 0.24f, 1).chain(EntitySasaki.TWO_HAND_1, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_2, 2, 0.24f, 1).chain(EntitySasaki.TWO_HAND_3, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_2, 2, 0.24f, 1).chain(EntitySasaki.TWO_HAND_5, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_2, 2, 0.24f, 1).chain(EntitySasaki.ONE_HAND_1, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_4, 2, 0.24f, 4)
                            .or(EntitySasaki.TWO_HAND_4, 2, 0.24f, 1).chain(EntitySasaki.TWO_HAND_5, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_6, 2, 0.24f, 4)
                            .withChance(0.5f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntitySasaki>(EntitySasaki.TWO_HAND_2)
                    .cooldown(e -> e.getRandom().nextInt(20) + 15)
                    .chain(GoalAttackAction.<EntitySasaki>chainBuilder(EntitySasaki.TWO_HAND_1, 2, 0.24f, 1)
                            .or(EntitySasaki.TWO_HAND_1, 2, 0.24f, 1).chain(EntitySasaki.TWO_HAND_2, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_1, 2, 0.24f, 1).chain(EntitySasaki.TWO_HAND_4, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_1, 2, 0.24f, 1).chain(EntitySasaki.TWO_HAND_6, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_3, 2, 0.24f, 4)
                            .or(EntitySasaki.TWO_HAND_3, 2, 0.24f, 1).chain(EntitySasaki.TWO_HAND_6, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_5, 2, 0.24f, 4)
                            .withChance(0.5f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntitySasaki>(EntitySasaki.TWO_HAND_3)
                    .cooldown(e -> e.getRandom().nextInt(20) + 15)
                    .chain(GoalAttackAction.<EntitySasaki>chainBuilder(EntitySasaki.TWO_HAND_6, 2, 0.24f, 1)
                            .chain(EntitySasaki.TWO_HAND_2, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_6, 2, 0.24f, 4)
                            .withChance(0.6f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntitySasaki>(EntitySasaki.TWO_HAND_4)
                    .cooldown(e -> e.getRandom().nextInt(20) + 15)
                    .chain(GoalAttackAction.<EntitySasaki>chainBuilder(EntitySasaki.TWO_HAND_5, 2, 0.24f, 1)
                            .chain(EntitySasaki.TWO_HAND_1, 2, 0.24f)
                            .or(EntitySasaki.TWO_HAND_5, 2, 0.24f, 4)
                            .withChance(0.6f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntitySasaki>(EntitySasaki.TWO_HAND_7)
                    .cooldown(e -> e.getRandom().nextInt(20) + 15)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntitySasaki>(EntitySasaki.KATANA_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 15)
                    .chain(GoalAttackAction.<EntitySasaki>chainBuilder(EntitySasaki.TWO_HAND_5, 2, 0.24f, 2)
                            .or(EntitySasaki.ONE_HAND_1, 2, 0.24f, 1)
                            .withChance(0.7f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntitySasaki>(EntitySasaki.TSUBAME_GAESHI)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .withCondition(Utils.npCheck())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 8, 1.2))), 25)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntitySasaki>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 5),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1.1, 6)), 3)
    );

    public final AnimatedAttackGoal<EntitySasaki> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntitySasaki> animationHandler = new AnimationHandler<>(this, ANIMS);

    private Vec3 hikenPos;
    private boolean hiken;

    public EntitySasaki(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MONOHOSHI_ZAO.get()));
    }

    @Override
    public Goal getAttackAI() {
        return this.attack;
    }

    @Override
    public AnimationHandler<EntitySasaki> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return !(TSUBAME_GAESHI.is(anim) && anim.isBetween(0.8, 1.64)) && super.hurt(damageSource, damage);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityTrailProvider.TRAIL_START)) {
                    this.level.addParticle(new TrailParticleData(ModParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityTrailProvider.EntityTrailData.create(this, anim.getID(), false))
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

    private void tsubameParticles(Vec3 at) {
        int duration = 8;
        Vec3 basePos = new Vec3(3, 0, -1);
        Vec3 baseDir = new Vec3(-2.8, 0, 2).scale(1f / duration);
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
            if (this.level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new TrailParticleData(ModParticles.TRAIL.get(),
                                TrailInfo.builder(new MotionTrailProvider.MotionTrailData(dir, sweer, normal,
                                                1, 4, duration))
                                        .setColor(72 / 255f, 13 / 255f, 161 / 255f, 0.7f)
                                        .setColor2(146 / 255f, 105 / 255f, 207 / 255f, 0.4f)
                                        .setWidth(1)
                                        .setWidth2(1)
                                        .setType(TrailInfo.Visual.TEXTURE, 0)
                                        .build()),
                        at.x() + pos.x(), at.y() + pos.y(), at.z() + pos.z(), 0, 0, 0, 0, 1);
            } else {
                this.level.addParticle(new TrailParticleData(ModParticles.TRAIL.get(),
                                TrailInfo.builder(new MotionTrailProvider.MotionTrailData(dir, sweer, normal,
                                                1, 4, duration))
                                        .setColor(72 / 255f, 13 / 255f, 161 / 255f, 0.7f)
                                        .setColor2(146 / 255f, 105 / 255f, 207 / 255f, 0.4f)
                                        .setWidth(1)
                                        .setWidth2(1)
                                        .setType(TrailInfo.Visual.TEXTURE, 0)
                                        .build()),
                        at.x() + pos.x(), at.y() + pos.y(), at.z() + pos.z(), 0, 0, 0);
            }
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(TSUBAME_GAESHI)) {
            if (!anim.isPast("attack_prepare")) {
                if (this.getTarget() != null) {
                    this.lookAtNow(this.getTarget(), 60, 90);
                }
            }
            if (anim.isAt("attack_prepare")) {
                Vec3 dir;
                if (this.getTarget() != null) {
                    dir = this.getTarget().position().subtract(this.position());
                    dir = dir.add(dir.normalize().scale(3));
                    if (dir.lengthSqr() > 121)
                        dir = dir.normalize().scale(11);
                } else {
                    Vec3 look = Vec3.directionFromRotation(0, this.getYHeadRot()).scale(11);
                    HitResult res = this.level.clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(look), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    dir = res.getLocation().subtract(this.getEyePosition());
                }
                this.hikenPos = this.position().add(dir);
            }
            if (anim.isAt("attack") && this.hikenPos != null) {
                Vec3 dir = this.hikenPos.subtract(this.position());
                float yRot = MathsHelper.YRotFrom(dir);
                OrientedBoundingBox obb = new OrientedBoundingBox(new AABB(this.getBbWidth() * 0.5 - 1.5, -0.3, 0, this.getBbWidth() * 0.5 + 1.5, this.getBbHeight() + 0.3, dir.length() + 2),
                        yRot, 0, this.position());
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
                boolean damage = false;
                this.hiken = true;
                for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                        entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()))) {
                    entity.invulnerableTime = 0;
                    if (this.doHurtTarget(entity) && !damage)
                        damage = true;
                    entity.invulnerableTime = 0;
                    if (this.doHurtTarget(entity) && !damage)
                        damage = true;
                    entity.invulnerableTime = 0;
                    if (this.doHurtTarget(entity) && !damage)
                        damage = true;
                    entity.invulnerableTime = 0;
                }
                this.hiken = false;
                this.tsubameParticles(this.position().add(0, this.getEyeHeight(), 0).add(dir.scale(0.5)));
                if (damage) {
                    this.level.playSound(null, this, SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 0.7f, 0.9f);
                }
                this.teleportTo(this.hikenPos.x(), this.hikenPos.y(), this.hikenPos.z());
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
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(TWO_HAND_1, TWO_HAND_2, TWO_HAND_3, TWO_HAND_4)) {
            width += 1.7;
            length += 1.2;
        }
        if (anim.is(TWO_HAND_5, TWO_HAND_6)) {
            width += 2.1;
            length += 1.1;
        }
        if (anim.is(TWO_HAND_7)) {
            width += 0.3;
            length += 1.3;
        }
        if (anim.is(ONE_HAND_1)) {
            width += 2.1;
            length += 1;
            return new AABB(-width * 0.7, -0.02, 0, width * 0.3, this.getBbHeight() + 0.02, length);
        }
        if (anim.is(KATANA_1)) {
            width += 0.1;
            length += 1;
            return new AABB(-width * 0.3, -0.02, 0, width * 0.7, this.getBbHeight() + 0.02, length);
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    protected DamageSource damageSourceAttack(Entity target) {
        return this.hiken ? CustomDamageSource.hiKen(this) : DamageSource.mobAttack(this);
    }

    @Override
    public float getSummonProgress(float partialTicks) {
        float prog = super.getSummonProgress(partialTicks);
        return prog >= 0 ? Mth.clamp(prog * 2, 0, 1) : prog;
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f[] weaponTrailEdge(boolean left) {
        Vector4f[] edge = super.weaponTrailEdge(left);
        edge[1].mul(new Vector3f(2, 2, 2));
        return edge;
    }
}
