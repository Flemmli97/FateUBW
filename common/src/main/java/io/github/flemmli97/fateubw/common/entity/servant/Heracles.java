package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.particles.RingParticleData;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.joml.Vector4f;

import java.util.List;

public class Heracles extends BaseServant {

    protected static final EntityDataAccessor<Integer> DEATH_COUNT = SynchedEntityData.defineId(Heracles.class, EntityDataSerializers.INT);

    private static final ResourceLocation DEATH_MOD = Fate.modRes("heracles_death_modifier");

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    private static final String ONE_HAND_HEAVY_1 = BUILDER.add("one_hand_heavy_1", AnimationsBuilder.definition(0.7).marker("attack", 0.6));
    private static final String ONE_HAND_HEAVY_2 = BUILDER.add("one_hand_heavy_2", AnimationsBuilder.definition(0.7).marker("attack", 0.52));
    private static final String ONE_HAND_HEAVY_3 = BUILDER.add("one_hand_heavy_3", AnimationsBuilder.definition(0.7).marker("attack", 0.52));
    private static final String TWO_HAND_HEAVY_1 = BUILDER.add("two_hand_heavy_1", AnimationsBuilder.definition(0.82).marker("attack", 0.72));
    private static final String TWO_HAND_HEAVY_2 = BUILDER.add("two_hand_heavy_2", AnimationsBuilder.definition(0.78).marker("attack", 0.6));
    private static final String UPPER_CUT = BUILDER.add("upper_cut", AnimationsBuilder.definition(1.04).marker("attack", 0.64));
    private static final String JUMP = BUILDER.add("jump", AnimationsBuilder.definition(0.8)
            .marker("jump", 0.12).marker("attempt", 0.24).infinite());
    private static final String JUMP_HIT = BUILDER.add("jump_hit", AnimationsBuilder.definition(0.32)
            .marker("attack", 0.12).infinite());
    private static final String LAND = BUILDER.add("land", AnimationsBuilder.definition(0.44));

    private static final String DEATH = BUILDER.add("death", AnimationsBuilder.definition(0.68).infinite());
    private static final String FAKE_DEATH = BUILDER.add("fake_death", AnimationsBuilder.definition(5.92)
            .marker("roar", 5.));
    private static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2)
            .marker("roar", 0.84));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Heracles> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (anim == null && this.getAnimationHandler().isCurrent(UPPER_CUT)) {
            if (this.upperCutTarget != null) {
                this.upperCutTarget = null;
                this.getAnimationHandler().setAnimation(JUMP);
                return true;
            }
        } else if (anim != null && anim.is(UPPER_CUT)) {
            this.upperCutTarget = null;
        }
        return false;
    });

    private boolean voidDeath;

    private LivingEntity upperCutTarget;
    private List<LivingEntity> hits;
    private int lastHitTick;

    private final Vector4f summonColor = new Vector4f(50 / 255f, 44 / 255f, 38 / 255f, 0.8f);
    private int jumpCooldown;

    public Heracles(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DEATH_COUNT, 0);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.HERACLES_AXE.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.HERACLES_AXE.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<BaseServant>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(ONE_HAND_HEAVY_1)
                        .start(ONE_HAND_HEAVY_2, 2, 0.24f, 1)
                        .start(ONE_HAND_HEAVY_3, 2, 0.24f, 1)
                        .chainChance(0.5f).build())).play(BehaviourUtils.cooldownedPlay(true, 20, 35))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((e, t) -> 1.1f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(12)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(ONE_HAND_HEAVY_2)
                        .start(ONE_HAND_HEAVY_1, 2, 0.24f, 1)
                        .start(TWO_HAND_HEAVY_1, 2, 0.28f, 1)
                        .start(TWO_HAND_HEAVY_2, 2, 0.28f, 1)
                        .chain(ONE_HAND_HEAVY_3, 2, 0.24f)
                        .chainChance(0.5f).build())).play(BehaviourUtils.cooldownedPlay(true, 20, 35))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(12)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(TWO_HAND_HEAVY_1)
                        .start(TWO_HAND_HEAVY_2, 2, 0.28f, 1)
                        .start(TWO_HAND_HEAVY_2, 2, 0.28f, 1)
                        .chain(ONE_HAND_HEAVY_2, 2, 0.24f)
                        .chainChance(0.5f).build())).play(BehaviourUtils.cooldownedPlay(true, 20, 35))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(12)
                .start(JUMP).play(BehaviourUtils.cooldownedPlay(false, 40, 60))
                .condition(entity -> {
                    LivingEntity target = BrainUtils.getTargetOfEntity(entity);
                    if (this.jumpCooldown <= 0 && target != null && target.getY() - entity.getY() > 4) {
                        Vec3 pos = entity.position();
                        Vec3 targetPos = target.position();
                        Vec3 dir = targetPos.subtract(pos);
                        HitResult hit = ProjectileUtil.getEntityHitResult(entity.level(), entity, pos, targetPos, entity.getBoundingBox().expandTowards(dir),
                                e -> e == target);
                        if (hit != null) {
                            this.jumpCooldown = 80 + entity.getRandom().nextInt(40);
                            return true;
                        }
                    }
                    return false;
                })
                .end(11)
                .start(UPPER_CUT).play(BehaviourUtils.cooldownedPlay(true, 20, 35))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(12, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(4, new SetWalkTargetAwayFromTarget<>(), BehaviourUtils.moveTo()).build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null && anim.isAt("roar")) {
                this.playSound(FateSounds.HERACLES_ROAR.get(), 1, 1);
                S2CScreenShake.sendAround(this, 24, 16, 2);
            }
        }
    }

    @Override
    protected MoveType getMoveFromSpeed(double speed) {
        if (this.getTarget() != null && speed >= 1)
            return MoveType.RUN;
        return super.getMoveFromSpeed(speed);
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

    public void setDeathNumber(int death) {
        this.entityData.set(DEATH_COUNT, Mth.clamp(death, 0, this.props().getConfig(ServantExtraData.HERACLES_DEATH_MAX)));
    }

    public int getDeaths() {
        return this.entityData.get(DEATH_COUNT);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && (damage -= 3) < 0)
            return false;
        return super.hurt(damageSource, damage);
    }

    @Override
    protected void tickDeath() {
        if (this.getLastDamageSource() != null && this.getLastDamageSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) || this.voidDeath) {
            this.voidDeath = true;
            super.tickDeath();
        } else if (!this.level().isClientSide) {
            int maxDeaths = this.props().getConfig(ServantExtraData.HERACLES_DEATH_MAX);
            if (this.getDeaths() < maxDeaths) {
                this.deathTime++;
                if (this.deathTime == 1) {
                    this.getAnimationHandler().setAnimation(FAKE_DEATH);
                }
                AnimationState anim = this.getAnimationHandler().getAnimation();
                if (anim == null || !anim.is(FAKE_DEATH)) {
                    this.setDeathNumber(this.getDeaths() + 1);
                    double mod = ((double) this.getDeaths() / maxDeaths) * 0.7;
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
            att.addPermanentModifier(new AttributeModifier(DEATH_MOD, -mod, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            dmg.addPermanentModifier(new AttributeModifier(DEATH_MOD, mod * 0.35, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(UPPER_CUT)) {
            if (anim.isAt("attack")) {
                Vec3 dir = Vec3.directionFromRotation(0, this.getYRot()).scale(2);
                this.setTargetPosition((TargetPosition) null);
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
                List<LivingEntity> hits = this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
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
                    ((ServerLevel) this.level())
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

    private void handleAirFall(AnimationState anim) {
        this.fallDistance = 0;
        if (anim.done(0)) {
            if (this.onGround()) {
                this.getAnimationHandler().setAnimation(LAND);
            }
        }
        // Stuck check. Or e.g. if in water
        if (anim.isPast(6.0) && (!this.getInBlockState().is(Blocks.AIR) || !this.getBlockStateOn().is(Blocks.AIR))) {
            this.getAnimationHandler().setAnimation(LAND);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurt = super.doHurtTarget(entity);
        if (hurt) {
            if (this.lastHitTick != this.tickCount) {
                S2CScreenShake.sendAround(this, 12, 8, 2);
                this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1, this.getRandom().nextFloat() * 0.2f + 0.9f);
            }
            this.lastHitTick = this.tickCount;
        }
        return hurt;
    }

    @Override
    public AABB attackBB(AnimationState anim) {
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
    public AnimationHandler<Heracles> getAnimationHandler() {
        return this.animationHandler;
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
    public double getSummonProgress(float partialTicks) {
        double prog = super.getSummonProgress(partialTicks);
        return prog >= 0 ? Mth.clamp(prog * 2, 0, 1) : prog;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }
}
