package io.github.flemmli97.fateubw.common.entity.servant;


import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.particles.RingParticleData;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.registry.ModSounds;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class EntityHeracles extends BaseServant {

    public static final int MAX_DEATH = 3;
    protected static final EntityDataAccessor<Integer> DEATH_COUNT = SynchedEntityData.defineId(EntityHeracles.class, EntityDataSerializers.INT);

    private static final UUID DEATH_MOD = UUID.fromString("5f642c37-7ed0-409a-91a5-0095001eb6e3");

    private static final AnimatedAction ONE_HAND_HEAVY_1 = AnimatedAction.builder(0.7, "one_hand_heavy_1").marker("attack", 0.6).build();
    private static final AnimatedAction ONE_HAND_HEAVY_2 = AnimatedAction.builder(0.7, "one_hand_heavy_2").marker("attack", 0.52).build();
    private static final AnimatedAction ONE_HAND_HEAVY_3 = AnimatedAction.builder(0.7, "one_hand_heavy_3").marker("attack", 0.52).build();
    private static final AnimatedAction TWO_HAND_HEAVY_1 = AnimatedAction.builder(0.82, "two_hand_heavy_1").marker("attack", 0.72).build();
    private static final AnimatedAction TWO_HAND_HEAVY_2 = AnimatedAction.builder(0.78, "two_hand_heavy_2").marker("attack", 0.6).build();
    private static final AnimatedAction UPPER_CUT = AnimatedAction.builder(1.04, "upper_cut").marker("attack", 0.64).build();
    private static final AnimatedAction JUMP = AnimatedAction.builder(0.8, "jump")
            .marker("jump", 0.12).marker("attempt", 0.24).infinite().build();
    private static final AnimatedAction JUMP_HIT = AnimatedAction.builder(0.32, "jump_hit")
            .marker("attack", 0.12).infinite().build();
    private static final AnimatedAction LAND = AnimatedAction.builder(0.44, "land").build();

    private static final AnimatedAction DEATH = AnimatedAction.builder(0.68, "death").infinite().build();
    private static final AnimatedAction FAKE_DEATH = AnimatedAction.builder(5.92, "fake_death")
            .marker("roar", 5.).build();
    private static final AnimatedAction SUMMON = AnimatedAction.builder(2, "summon")
            .marker("roar", 0.84).build();
    private static final AnimatedAction[] ANIMS = {ONE_HAND_HEAVY_1, ONE_HAND_HEAVY_2, ONE_HAND_HEAVY_3, TWO_HAND_HEAVY_1, TWO_HAND_HEAVY_2, UPPER_CUT, JUMP, JUMP_HIT, LAND, DEATH, FAKE_DEATH, SUMMON};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityHeracles>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityHeracles>(EntityHeracles.ONE_HAND_HEAVY_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 15)
                    .chain(GoalAttackAction.<EntityHeracles>chainBuilder(EntityHeracles.ONE_HAND_HEAVY_2, 2, 0.24f, 5)
                            .or(EntityHeracles.ONE_HAND_HEAVY_3, 2, 0.24f, 5)
                            .withChance(0.5f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 12),
            WeightedEntry.wrap(new GoalAttackAction<EntityHeracles>(EntityHeracles.ONE_HAND_HEAVY_2)
                    .cooldown(e -> e.getRandom().nextInt(20) + 15)
                    .chain(GoalAttackAction.<EntityHeracles>chainBuilder(EntityHeracles.ONE_HAND_HEAVY_1, 2, 0.24f, 1)
                            .or(EntityHeracles.TWO_HAND_HEAVY_1, 2, 0.28f, 1)
                            .or(EntityHeracles.TWO_HAND_HEAVY_2, 2, 0.28f, 1)
                            .chain(EntityHeracles.ONE_HAND_HEAVY_3, 2, 0.24f)
                            .withChance(0.5f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 12),
            WeightedEntry.wrap(new GoalAttackAction<EntityHeracles>(EntityHeracles.TWO_HAND_HEAVY_1)
                    .cooldown(e -> e.getRandom().nextInt(20) + 15)
                    .chain(GoalAttackAction.<EntityHeracles>chainBuilder(EntityHeracles.TWO_HAND_HEAVY_2, 2, 0.28f, 5)
                            .or(EntityHeracles.TWO_HAND_HEAVY_2, 2, 0.28f, 2)
                            .chain(EntityHeracles.ONE_HAND_HEAVY_2, 2, 0.24f)
                            .withChance(0.5f))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 12),
            WeightedEntry.wrap(new GoalAttackAction<EntityHeracles>(EntityHeracles.JUMP)
                    .cooldown(e -> e.getRandom().nextInt(30) + 15)
                    .withCondition(((goal, target, previous) -> {
                        if (target.getY() - goal.attacker.getY() > 4) {
                            Vec3 pos = goal.attacker.position();
                            Vec3 targetPos = target.position();
                            Vec3 dir = targetPos.subtract(pos);
                            HitResult hit = ProjectileUtil.getEntityHitResult(goal.attacker.level, goal.attacker, pos, targetPos, goal.attacker.getBoundingBox().expandTowards(dir),
                                    e -> e == target);
                            return hit != null;
                        }
                        return false;
                    }))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityHeracles>(EntityHeracles.UPPER_CUT)
                    .cooldown(e -> e.getRandom().nextInt(30) + 20)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 5)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityHeracles>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1.1, 0.5)), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1.1, 1, 6)), 4)
    );

    public final AnimatedAttackGoal<EntityHeracles> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityHeracles> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (anim == null && this.getAnimationHandler().isCurrent(UPPER_CUT)) {
            if (this.upperCutTarget != null) {
                this.upperCutTarget = null;
                this.getAnimationHandler().setAnimation(JUMP);
                return true;
            }
        } else if (UPPER_CUT.is(anim)) {
            this.upperCutTarget = null;
        }
        return false;
    });

    private boolean voidDeath;

    private LivingEntity upperCutTarget;
    private List<LivingEntity> hits;
    private int lastHitTick;

    private final Vector4f summonColor = new Vector4f(50 / 255f, 44 / 255f, 38 / 255f, 0.8f);

    public EntityHeracles(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HERACLES_AXE.get()));
    }

    @Override
    public Goal getAttackAI() {
        return this.attack;
    }

    @Override
    public AnimationHandler<EntityHeracles> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DEATH_COUNT, 0);
    }

    public void setDeathNumber(int death) {
        this.entityData.set(DEATH_COUNT, Mth.clamp(death, 0, MAX_DEATH));
    }

    public int getDeaths() {
        return this.entityData.get(DEATH_COUNT);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
           AnimatedAction anim = this.getAnimationHandler().getAnimation();
           if (anim != null && anim.isAt("roar")) {
                this.playSound(ModSounds.HERACLES_ROAR.get(), 1, 1);
                S2CScreenShake.sendAround(this, 24, 8, 2);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource != DamageSource.OUT_OF_WORLD && (damage -= 3) < 0)
            return false;
        return super.hurt(damageSource, damage);
    }

    @Override
    protected void tickDeath() {
        if (this.getLastDamageSource() == DamageSource.OUT_OF_WORLD || this.voidDeath) {
            this.voidDeath = true;
            super.tickDeath();
        } else if (!this.level.isClientSide) {
            if (this.getDeaths() < MAX_DEATH) {
                this.deathTime++;
                if (this.deathTime == 1) {
                    this.getAnimationHandler().setAnimation(FAKE_DEATH);
                }
                AnimatedAction anim = this.getAnimationHandler().getAnimation();
                if (anim == null || !anim.getID().equals(FAKE_DEATH.getID())) {
                    this.setDeathNumber(this.getDeaths() + 1);
                    double mod = ((double) this.getDeaths() / MAX_DEATH) * 0.7;
                    this.applyDeathMod(mod);
                    this.setHealth(this.getMaxHealth());
                    this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 2, false, false));
                    this.deathTime = 0;
                    this.revealServant();
                }
            } else {
                super.tickDeath();
            }
        }
    }

    private void applyDeathMod(double mod) {
        AttributeInstance att = this.getAttribute(Attributes.MAX_HEALTH);
        att.removeModifier(DEATH_MOD);
        AttributeInstance dmg = this.getAttribute(Attributes.ATTACK_DAMAGE);
        dmg.removeModifier(DEATH_MOD);
        if (mod != 0) {
            att.addPermanentModifier(new AttributeModifier(DEATH_MOD, "fate.death.mod", -mod, AttributeModifier.Operation.MULTIPLY_TOTAL));
            dmg.addPermanentModifier(new AttributeModifier(DEATH_MOD, "fate.death.mod", mod * 0.35, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(UPPER_CUT)) {
            if (anim.isAt("attack")) {
                Vec3 dir = Vec3.directionFromRotation(0, this.getYRot()).scale(2);
                this.targetPosition = null;
                this.mobAttack(anim, this.getTarget(), e -> {
                    if (this.doHurtTarget(e)) {
                        e.setDeltaMovement(dir.x(), 2, dir.z());
                        e.hurtMarked = true;
                        if (e instanceof ServerPlayer player)
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));
                        if (this.upperCutTarget == null || e == this.getTarget())
                            this.upperCutTarget = e;
                    }
                });
            }
        } else if (anim.is(JUMP)) {
            if (anim.isAt("jump")) {
                LivingEntity target = this.upperCutTarget != null ? this.upperCutTarget : this.getTarget();
                if (target != null) {
                    Vec3 diff = target.position().add(target.getDeltaMovement().scale(20))
                            .subtract(this.position());
                    Vec3 dir = diff.add(0, diff.y() > 0 ? -diff.y() * 0.5 : 0, 0).normalize().scale(3.5);
                    this.setDeltaMovement(dir.x(), Mth.clamp(dir.y(), 1.5, 2.5), dir.z());
                } else {
                    Vec3 dir = Vec3.directionFromRotation(0, this.getYRot()).scale(2);
                    this.setDeltaMovement(dir.x(), 2, dir.z());
                }
            }
            if (anim.isPast("attempt") && !anim.done(0)) {
                OrientedBoundingBox obb = this.calculateAttackAABB(anim, null, 0);
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTEMPT, this);
                List<LivingEntity> hits = this.level.getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                        entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()));
                if (!hits.isEmpty()) {
                    S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
                    this.getAnimationHandler().setAnimation(JUMP_HIT);
                    this.setDeltaMovement(Vec3.ZERO);
                    this.hits = hits;
                    return;
                }
            }
            if (anim.done(0)) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
                this.handleAirFall(anim);
            }
        } else if (anim.is(JUMP_HIT)) {
            if (anim.isAt("attack") && this.hits != null) {
                Vec3 dir = Vec3.directionFromRotation(0, this.getYRot());
                this.hits.forEach(e -> {
                    if (this.doHurtTarget(e)) {
                        e.setDeltaMovement(dir.x(), -4, dir.z());
                        e.hurtMarked = true;
                        if (e instanceof ServerPlayer player)
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                });
                if (!this.hits.isEmpty()) {
                    ((ServerLevel) this.level)
                            .sendParticles(new RingParticleData(0.9f, 0.9f, 0.9f, 1, 1, this.getYRot(), 40, 3f), this.getX(), this.getY(), this.getZ(),
                                    0, 0, 0, 0, 1);
                }
                this.hits = null;
                this.setDeltaMovement(this.getDeltaMovement().add(0, -0.1, 0));
            }
            if (anim.isPast("attack")) {
                this.handleAirFall(anim);
            } else {
                this.setDeltaMovement(Vec3.ZERO);
                this.hits.forEach(e -> {
                    e.setDeltaMovement(Vec3.ZERO);
                    if (e instanceof ServerPlayer player)
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                });
            }
        } else {
            super.handleAttack(anim);
        }
    }

    private void handleAirFall(AnimatedAction anim) {
        this.fallDistance = 0;
        if (anim.done(0)) {
            if (this.isOnGround()) {
                this.getAnimationHandler().setAnimation(LAND);
            }
        }
        // Stuck check. Or e.g. if in water
        if (anim.isPast(6.0) && (!this.getFeetBlockState().is(Blocks.AIR) || !this.getBlockStateOn().is(Blocks.AIR))) {
            this.getAnimationHandler().setAnimation(LAND);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurt = super.doHurtTarget(entity);
        if (hurt) {
            if (this.lastHitTick != this.tickCount) {
                S2CScreenShake.sendAround(this, 12, 4, 1.5f);
                this.playSound(SoundEvents.GENERIC_EXPLODE, 1, this.getRandom().nextFloat() * 0.2f + 0.9f);
            }
            this.lastHitTick = this.tickCount;
        }
        return hurt;
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        if (anim.is(JUMP)) {
            double widthH = this.getBbWidth() * 0.5 + 1.3;
            double length = this.getBbWidth() + 2.5;
            return new AABB(-widthH, -1, -1 * 0.5, widthH, this.getBbHeight() + 1.5, length);
        }
        double width = this.getBbWidth() + 0.3;
        double length = this.getBbWidth() + 0.3;
        if (anim.is(ONE_HAND_HEAVY_1)) {
            width += 0.3;
            length += 1.4;
        }
        if (anim.is(ONE_HAND_HEAVY_2, ONE_HAND_HEAVY_3)) {
            width += 2.2;
            length += 0.9;
        }
        if (anim.is(TWO_HAND_HEAVY_1)) {
            width += 0.4;
            length += 1.6;
        }
        if (anim.is(TWO_HAND_HEAVY_2)) {
            width += 2.6;
            length += 1.2;
        }
        if (anim.is(UPPER_CUT)) {
            width += 0.4;
            length += 1.3;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public boolean transparentOnDeath() {
        return false;
    }

    @Override
    public AnimatedAction deathAnim() {
        return DEATH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Deaths", this.getDeaths());
        tag.putBoolean("DeathType", this.voidDeath);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDeathNumber(tag.getInt("Deaths"));
        this.voidDeath = tag.getBoolean("DeathType");
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public float getSummonProgress(float partialTicks) {
        float prog = super.getSummonProgress(partialTicks);
        return prog >= 0 ? Mth.clamp(prog * 2, 0, 1) : prog;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }
}
