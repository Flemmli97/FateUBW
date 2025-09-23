package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.OneshotAnimationPlay;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.TeleportUtils;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.joml.Vector4f;

public class Diarmuid extends BaseServant {

    public static final String LEFT_TRAIL_END = "left_trail_end";
    public static final String RIGHT_TRAIL_START = "right_trail_start";

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DUAL_SPEAR_1 = BUILDER.add("dual_spear_1", AnimationsBuilder.definition(1.04)
            .marker("attack_left", 0.56).marker("attack_right", 0.92).marker("step", 0.32, 0.68)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.4).marker(LEFT_TRAIL_END, 0.68)
            .marker(RIGHT_TRAIL_START, 0.76).marker(EntityWeaponTrailProvider.TRAIL_END, 0.96));
    public static final String DUAL_SPEAR_2 = BUILDER.add("dual_spear_2", AnimationsBuilder.definition(1)
            .marker("attack_left", 0.48).marker("attack_right", 0.88).marker("step", 0.32)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.4).marker(LEFT_TRAIL_END, 0.6)
            .marker(RIGHT_TRAIL_START, 0.68).marker(EntityWeaponTrailProvider.TRAIL_END, 0.92));
    public static final String DUAL_SPEAR_3 = BUILDER.add("dual_spear_3", AnimationsBuilder.definition(0.8)
            .marker("attack_left", 0.44).marker("attack_right", 0.68));
    public static final String DUAL_SPEAR_4 = BUILDER.add("dual_spear_4", AnimationsBuilder.definition(0.68)
            .marker("attack_left", 0.56).marker("attack_right", 0.56)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.4).marker(LEFT_TRAIL_END, 0.6)
            .marker(RIGHT_TRAIL_START, 0.4).marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    public static final String BLINK = BUILDER.add("blink", AnimationsBuilder.definition(1.12)
            .marker("teleport_start", 0.28).marker("teleport", 0.5).marker("teleport_end", 0.84));
    public static final String BLINK_AWAY = BUILDER.add("blink_away", BLINK);
    public static final String UNSEAL = BUILDER.add("hogou_unseal", AnimationsBuilder.definition(5.04)
            .marker("unseal_1", 2.12).marker("unseal_2", 3.88)
            .marker("unsealed", 4.8));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    public static final TypedResource<Vec3> BLINK_TARGET = new TypedResource<>(Fate.modRes("blink_target"));

    private final AnimationHandler<Diarmuid> animationHandler = new AnimationHandler<>(this, ANIMS);
    private final Vector4f summonColor = new Vector4f(50 / 255f, 63 / 255f, 64 / 255f, 0.7f);

    private int unsealedDuration;

    private boolean leftHandAttackFlag, deargAttackFlag;

    public Diarmuid(EntityType<? extends Diarmuid> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<BaseServant> builder) {
        super.definedAdditinoalSyncedData(builder);
        builder.define(BLINK_TARGET, TenshilibSyncableEntityDatas.VEC_3.get(), null);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.GAEDEARG.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(FateItems.GAEBUIDHE.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.GAEDEARG.get()) ||
                this.getOffhandItem().is(FateItems.GAEBUIDHE.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<Diarmuid>create()
                .start(DUAL_SPEAR_1).play(BehaviourUtils.cooldownedPlay(true, 12, 27))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(DUAL_SPEAR_2).play(BehaviourUtils.cooldownedPlay(true, 12, 27))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(DUAL_SPEAR_3).play(BehaviourUtils.cooldownedPlay(true, 12, 27))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(DUAL_SPEAR_4).play(BehaviourUtils.cooldownedPlay(true, 12, 27))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(UNSEAL).play(BehaviourUtils.cooldownedPlay(false, 16, 28))
                .condition(entity -> entity.unsealedDuration < 0 && entity.canUseNobelPhantasm())
                .prepare(new SetWalkTargetWithinDist<Diarmuid>()
                        .min(8).max(14).speedMod((m, e) -> 1.25f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(30)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(12, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(5, new SetWalkTargetAwayFromTarget<>(), BehaviourUtils.moveTo())
                .add(8, BehaviourUtils.ifFurtherThan(7), new OneshotAnimationPlay<BaseServant>(BLINK)
                        .whenStarting(e -> BrainUtils.clearMemory(e, MemoryModuleType.ATTACK_COOLING_DOWN)))
                .add(3, new OneshotAnimationPlay<BaseServant>(BLINK_AWAY)
                        .whenStarting(e -> BrainUtils.clearMemory(e, MemoryModuleType.ATTACK_COOLING_DOWN))).build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            --this.unsealedDuration;
            if (this.unsealedDuration == 0) {
                this.unsealWeapon(this.getMainHandItem(), false);
                this.unsealWeapon(this.getOffhandItem(), false);
                this.playSound(SoundEvents.BEACON_DEACTIVATE, 1.0F, 1.0F);
            }
            if (this.healthBelow(0.3f)) {
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 1, false, false));
            }
        } else {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.is(UNSEAL)) {
                    if (anim.isAt("unseal_1")) {
                        this.sphereParticles();
                    }
                    if (anim.isAt("unseal_2")) {
                        this.sphereParticles();
                    }
                }
                if (anim.isAt(EntityWeaponTrailProvider.TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(new EntityWeaponTrailProvider.EntityTrailData(this.getId(), anim.getID(), true, 3, LEFT_TRAIL_END))
                                            .setColor(186 / 255f, 138 / 255f, 16 / 255f, 0.6f)
                                            .setColor2(186 / 255f, 138 / 255f, 16 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
                if (anim.isAt(RIGHT_TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityWeaponTrailProvider.EntityTrailData.create(this, anim.getID(), false))
                                            .setColor(154 / 255f, 15 / 255f, 30 / 255f, 0.6f)
                                            .setColor2(154 / 255f, 15 / 255f, 30 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
                if (anim.is(DUAL_SPEAR_3) && (anim.isAt("attack_left") || anim.isAt("attack_right"))) {
                    Vec3 offset = new Vec3(anim.isAt("attack_left") ? 0.2 : -0.2, this.getBbHeight() * 0.5, this.getBbWidth() + 1.7 * this.getScale())
                            .yRot(-this.getYRot() * Mth.DEG_TO_RAD);
                    for (int i = 0; i < 6; i++) {
                        AdvancedParticleContainer.make(ParticleTypes.CRIT)
                                .addData(new ParticleMetaData(10, false, 0))
                                .add(this.level(),
                                        this.getX() + offset.x() + this.getRandom().nextGaussian() * 0.1,
                                        this.getY() + offset.y() + this.getRandom().nextGaussian() * 0.1,
                                        this.getZ() + offset.z() + this.getRandom().nextGaussian() * 0.1);
                    }
                }
            }
        }
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(BLINK, BLINK_AWAY)) {
            Vec3 blinkTarget = this.getDataContainer().get(BLINK_TARGET);
            return blinkTarget != null ? blinkTarget.subtract(this.getEyePosition()) : null;
        }
        return super.directionToLookAt();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.unsealedDuration = tag.getInt("UnsealedDuration");
        if (this.unsealedDuration <= 0) {
            this.unsealWeapon(this.getMainHandItem(), false);
            this.unsealWeapon(this.getOffhandItem(), false);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("UnsealedDuration", this.unsealedDuration);
    }

    @Override
    protected void regenMana() {
        if (this.unsealedDuration > 0)
            return;
        super.regenMana();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(UNSEAL)) {
            if (this.getMana() >= this.props().manaCost()) {
                if (anim.isAt("unseal_1")) {
                    this.unsealWeapon(this.getMainHandItem(), true);
                }
                if (anim.isAt("unseal_2")) {
                    this.unsealWeapon(this.getOffhandItem(), true);
                }
                if (anim.isAt("unsealed")) {
                    this.useMana(this.props().manaCost());
                    this.unsealedDuration = this.getRandom().nextInt(300) + 300;
                }
            }
        } else if (anim.is(BLINK, BLINK_AWAY)) {
            if (anim.isAt("teleport_start")) {
                Vec3 target = null;
                if (this.getTarget() != null) {
                    if (anim.is(BLINK_AWAY)) {
                        for (int i = 0; i < 10; ++i) {
                            Vec3 posAway = DefaultRandomPos.getPosAway(this, 8, 7, this.getTarget().position());
                            if (posAway != null) {
                                HitResult res = this.level().clip(new ClipContext(this.position(), posAway, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                                target = res.getLocation();
                                break;
                            }
                        }
                    } else {
                        Vec3 dir = this.getTarget().position().subtract(this.position());
                        if (dir.lengthSqr() > 100) {
                            dir = dir.normalize().scale(10);
                        }
                        HitResult res = this.level().clip(new ClipContext(this.position(), this.position().add(dir), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                        target = res.getLocation();
                    }
                } else {
                    Vec3 look = Vec3.directionFromRotation(0, this.getYHeadRot()).scale(11);
                    HitResult res = this.level().clip(new ClipContext(this.position(), this.position().add(look), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    target = res.getLocation();
                }
                if (target != null) {
                    this.getDataContainer().set(BLINK_TARGET, target);
                }
            }
            Vec3 blinkTarget = this.getDataContainer().get(BLINK_TARGET);
            if (anim.isAt("teleport") && blinkTarget != null) {
                TeleportUtils.teleportTo(this, blinkTarget.x(), blinkTarget.y(), blinkTarget.z(),
                        SoundEvents.PLAYER_ATTACK_SWEEP, ParticleTypes.CLOUD);
                this.getDataContainer().set(BLINK_TARGET, null);
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.35);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            super.handleAttack(anim);
            if (anim.isAt("attack_left")) {
                this.leftHandAttackFlag = true;
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.leftHandAttackFlag = false;
            }
            if (anim.isAt("attack_right")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
            }
        }
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double height = this.getBbHeight();
        double width = this.getBbWidth();
        double length = 1 * this.getScale();
        if (anim.is(DUAL_SPEAR_1, DUAL_SPEAR_2)) {
            if (this.leftHandAttackFlag) {
                width += 1.8 * this.getScale();
                length += 1.1 * this.getScale();
                return new AABB(-width * 0.4, -0.03, 0, width * 0.6, height + 0.03, length);
            } else {
                width += 1.8 * this.getScale();
                length += 1.7 * this.getScale();
                return new AABB(-width * 0.8, -0.03, 0, width * 0.2, height + 0.03, length);
            }
        }
        if (anim.is(DUAL_SPEAR_3, DUAL_SPEAR_4)) {
            if (this.leftHandAttackFlag) {
                width += 0.8 * this.getScale();
                length += 1.4 * this.getScale();
            } else {
                width += 0.8 * this.getScale();
                length += 1.9 * this.getScale();
            }
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    public AnimationHandler<Diarmuid> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean mobHurtTarget(Entity entity) {
        boolean rightHandAttack = false;
        boolean leftHandAttack = false;
        if (this.unsealedDuration > 0) {
            rightHandAttack = !this.leftHandAttackFlag;
            leftHandAttack = this.leftHandAttackFlag;
        }
        this.deargAttackFlag = rightHandAttack;
        boolean hurt = super.mobHurtTarget(entity);
        this.deargAttackFlag = false;
        if (hurt) {
            if (leftHandAttack) {
                if (entity instanceof LivingEntity living) {
                    living.removeEffect(MobEffects.REGENERATION);
                    MobEffectInstance eff = living.getEffect(FateMobEffects.GAE_BUIDHE.asHolder());
                    int amplifier = 0;
                    if (eff != null && this.getRandom().nextFloat() < 1 - (eff.getAmplifier() * 0.2)) {
                        amplifier = Math.min(4, eff.getAmplifier() + 1);
                    }
                    living.addEffect(new MobEffectInstance(FateMobEffects.GAE_BUIDHE.asHolder(), 200 + (amplifier * 100), amplifier));
                }
            }
            if (rightHandAttack) {
                if (entity instanceof LivingEntity living) {
                    living.removeEffect(MobEffects.DAMAGE_RESISTANCE);
                }
            }
        }
        return hurt;
    }

    @Override
    public float damageModifier(Entity target) {
        if (this.unsealedDuration <= 0 && !this.leftHandAttackFlag) {
            return 0.85f;
        }
        return super.damageModifier(target);
    }

    @Override
    public void onEntityHit(Entity target, float damage) {
        if (this.unsealedDuration <= 0 && !this.leftHandAttackFlag) {
            float partial = damage * 0.15f / 0.85f;
            Utils.runWithInvulTimer(this, target, e -> e.hurt(FateDamageTypes.direct(FateDamageTypes.GAE_DEARG, this), partial), 0);
        }
        super.onEntityHit(target, damage);
    }

    @Override
    protected DamageSource damageSourceAttack(Entity target) {
        return this.deargAttackFlag ? FateDamageTypes.direct(FateDamageTypes.GAE_DEARG, this) : super.damageSourceAttack(target);
    }

    @Override
    protected void tryDisableShield(Player player, ItemStack stack, ItemStack playerUseItem) {
        if (this.deargAttackFlag) {
            if (!playerUseItem.isEmpty() && playerUseItem.getItem() instanceof ShieldItem) {
                player.getCooldowns().addCooldown(Items.SHIELD, 200);
                this.level().broadcastEntityEvent(player, (byte) 30);
            }
        } else {
            super.tryDisableShield(player, stack, playerUseItem);
        }
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        return !this.getAnimationHandler().isCurrent(UNSEAL) && super.hurt(damageSource, damage);
    }

    @Override
    public boolean isInvisible() {
        if (this.getAnimationHandler().isCurrent(BLINK, BLINK_AWAY)) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            return anim.isPast("teleport_start") && !anim.isPast("teleport_end");
        }
        return super.isInvisible();
    }

    @Override
    public boolean nobelPhantasmCheck() {
        return this.healthBelow(0.6f) && super.nobelPhantasmCheck();
    }

    private void sphereParticles() {
        double goldenAngle = Math.PI * (Math.sqrt(5) - 1);
        for (int i = 0; i < 40; i++) {
            double phi = Math.acos(1 - 2. * i / 50);
            double theta = goldenAngle * i;
            double x = Math.cos(theta) * Math.sin(phi);
            double y = Math.sin(theta) * Math.sin(phi);
            double z = Math.cos(phi);
            this.level().addParticle(ParticleTypes.WITCH, this.getX() + x, this.getY(0.5) + y, this.getZ() + z, x * 0.15, y * 0.15, z * 0.15);
        }
    }

    private void unsealWeapon(ItemStack stack, boolean unseal) {
        if (stack.getItem() == FateItems.GAEBUIDHE.get() || stack.getItem() == FateItems.GAEDEARG.get()) {
            if (unseal) {
                stack.set(FateDataComponents.UNSEALED.get(), Unit.INSTANCE);
                this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1, 1);
            } else {
                stack.remove(FateDataComponents.UNSEALED.get());
            }
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

    @Override
    public WeaponTrail weaponTrailEdge(boolean left) {
        if (left)
            return new WeaponTrail(new Vector4f(0, 0, -0.6f, 1), new Vector4f(0, 0, -1.1f, 1));
        return new WeaponTrail(new Vector4f(0, 0, -1.2f, 1), new Vector4f(0, 0, -1.7f, 1));
    }
}
