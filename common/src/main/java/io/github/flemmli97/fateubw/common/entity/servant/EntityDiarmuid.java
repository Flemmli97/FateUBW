package io.github.flemmli97.fateubw.common.entity.servant;

import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.common.entity.ai.AnimationRunner;
import io.github.flemmli97.fateubw.common.items.weapons.SpearItem;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.common.utils.TeleportUtils;
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
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.ai.goal.Goal;
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

import java.util.List;

public class EntityDiarmuid extends BaseServant {

    public static final AnimatedAction DUAL_SPEAR_1 = AnimatedAction.builder(0.86, "dual_spear_1")
            .marker("attack_right", 0.4).marker("attack_left", 0.72).marker("step", 0.44, 0.72).build();
    public static final AnimatedAction DUAL_SPEAR_2 = AnimatedAction.builder(0.9, "dual_spear_2")
            .marker("attack_right", 0.4).marker("attack_left", 0.8).marker("step", 0.48).build();
    public static final AnimatedAction DUAL_SPEAR_3 = AnimatedAction.builder(0.78, "dual_spear_3")
            .marker("attack_right", 0.32).marker("attack_left", 0.56).build();
    public static final AnimatedAction DUAL_SPEAR_4 = AnimatedAction.builder(0.58, "dual_spear_4")
            .marker("attack", 0.48).build();
    public static final AnimatedAction BLINK = AnimatedAction.builder(1.12, "blink")
            .marker("teleport_start", 0.28).marker("teleport", 0.5).marker("teleport_end", 0.84).build();
    public static final AnimatedAction BLINK_AWAY = AnimatedAction.copyOf(BLINK, "blink_away");

    public static final AnimatedAction UNSEAL = AnimatedAction.builder(5.04, "hogou_unseal")
            .marker("unseal_1", 2.12).marker("unseal_2", 3.88)
            .marker("unsealed", 4.8).build();
    public static final AnimatedAction SUMMON = new AnimatedAction(2., "summon");
    private static final AnimatedAction[] ANIMS = {DUAL_SPEAR_1, DUAL_SPEAR_2, DUAL_SPEAR_3, DUAL_SPEAR_4, BLINK, BLINK_AWAY, UNSEAL, SUMMON};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityDiarmuid>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityDiarmuid>(EntityDiarmuid.DUAL_SPEAR_1)
                    .cooldown(e -> e.getRandom().nextInt(18) + 10)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityDiarmuid>(EntityDiarmuid.DUAL_SPEAR_2)
                    .cooldown(e -> e.getRandom().nextInt(18) + 10)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityDiarmuid>(EntityDiarmuid.DUAL_SPEAR_3)
                    .cooldown(e -> e.getRandom().nextInt(18) + 10)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityDiarmuid>(EntityDiarmuid.DUAL_SPEAR_4)
                    .cooldown(e -> e.getRandom().nextInt(18) + 10)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityDiarmuid>(EntityDiarmuid.UNSEAL)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .withCondition((goal, target, prev) -> goal.attacker.unsealedDuration < 0
                            && ((goal.attacker.canUseNP() && goal.attacker.getOwner() == null && goal.attacker.getMana() >= goal.attacker.props().hogouMana()) || goal.attacker.forcedNP))
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(10, 18, 1.3))), 30)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityDiarmuid>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 12),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1.2, 6)), 7),
            WeightedEntry.wrap(new IdleAction<EntityDiarmuid>(() -> new AnimationRunner<>(BLINK))
                    .duration(e -> Mth.ceil(BLINK.getLength()) + 1)
                    .withCondition(((goal, target) -> goal.distanceToTargetSq > 49)), 8),
            WeightedEntry.wrap(new IdleAction<EntityDiarmuid>(() -> new AnimationRunner<>(BLINK_AWAY))
                    .duration(e -> Mth.ceil(BLINK_AWAY.getLength()) + 1), 3)
    );

    public final AnimatedAttackGoal<EntityDiarmuid> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityDiarmuid> animationHandler = new AnimationHandler<>(this, ANIMS);
    private final Vector4f summonColor = new Vector4f(50 / 255f, 63 / 255f, 64 / 255f, 0.7f);

    private int unsealedDuration;

    private boolean leftHandAttackFlag, deargAttackFlag;

    private Vec3 blinkTarget;

    public EntityDiarmuid(EntityType<? extends EntityDiarmuid> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.GAEDEARG.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(FateItems.GAEBUIDHE.get()));
    }

    @Override
    public Goal getAttackAI() {
        return this.attack;
    }

    @Override
    public AnimationHandler<EntityDiarmuid> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            --this.unsealedDuration;
            if (this.unsealedDuration == 0) {
                this.unsealWeapon(this.getMainHandItem(), false);
                this.unsealWeapon(this.getOffhandItem(), false);
                this.playSound(SoundEvents.BEACON_DEACTIVATE, 1.0F, 1.0F);
            }
            if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
                if (!this.critHealth) {
                    this.critHealth = true;
                }
            }
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 1, false, false));
        } else {
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
            if (UNSEAL.is(anim)) {
                if (anim.isAt("unseal_1")) {
                    this.sphereParticles();
                }
                if (anim.isAt("unseal_2")) {
                    this.sphereParticles();
                }
            }
        }
    }

    @Override
    protected void regenMana() {
        if (this.unsealedDuration > 0)
            return;
        super.regenMana();
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(UNSEAL)) {
            if (this.getMana() >= this.props().hogouMana()) {
                if (anim.isAt("unseal_1")) {
                    this.unsealWeapon(this.getMainHandItem(), true);
                }
                if (anim.isAt("unseal_2")) {
                    this.unsealWeapon(this.getOffhandItem(), true);
                }
                if (anim.isAt("unsealed")) {
                    this.useMana(this.props().hogouMana());
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
                                HitResult res = this.level.clip(new ClipContext(this.position(), posAway, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                                target = res.getLocation();
                                break;
                            }
                        }
                    } else {
                        Vec3 dir = this.getTarget().position().subtract(this.position());
                        if (dir.lengthSqr() > 100) {
                            dir = dir.normalize().scale(10);
                        }
                        HitResult res = this.level.clip(new ClipContext(this.position(), this.position().add(dir), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                        target = res.getLocation();
                    }
                } else {
                    Vec3 look = Vec3.directionFromRotation(0, this.getYHeadRot()).scale(11);
                    HitResult res = this.level.clip(new ClipContext(this.position(), this.position().add(look), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    target = res.getLocation();
                }
                if (target != null) {
                    this.blinkTarget = target;
                    if (anim.is(BLINK) || this.getTarget() == null) {
                        this.lookAt(EntityAnchorArgument.Anchor.EYES, this.blinkTarget);
                    } else {
                        this.lookAt(EntityAnchorArgument.Anchor.EYES, this.getTarget().getEyePosition());
                    }
                }
            }
            if (anim.isAt("teleport") && this.blinkTarget != null) {
                TeleportUtils.teleportTo(this, this.blinkTarget.x(), this.blinkTarget.y(), this.blinkTarget.z(),
                        SoundEvents.PLAYER_ATTACK_SWEEP, ParticleTypes.CLOUD);
                this.blinkTarget = null;
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.3);
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
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(DUAL_SPEAR_1, DUAL_SPEAR_2)) {
            if (this.leftHandAttackFlag) {
                width += 0.8;
                length += 1.2;
            } else {
                width += 1.8;
                length += 1.5;
            }
        }
        if (anim.is(DUAL_SPEAR_3)) {
            if (this.leftHandAttackFlag) {
                width += 0.6;
                length += 1.3;
            } else {
                width += 0.6;
                length += 1.8;
            }
        }
        if (anim.is(DUAL_SPEAR_4)) {
            width += 0.6;
            length += 1.8;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public boolean mobHurtTarget(Entity entity) {
        boolean rightHandAttack = false;
        boolean leftHandAttack = false;
        if (this.unsealedDuration > 0) {
            rightHandAttack = !this.leftHandAttackFlag || this.getAnimationHandler().isCurrent(DUAL_SPEAR_4);
            leftHandAttack = this.leftHandAttackFlag || this.getAnimationHandler().isCurrent(DUAL_SPEAR_4);
        }
        this.deargAttackFlag = rightHandAttack;
        boolean hurt = super.mobHurtTarget(entity);
        this.deargAttackFlag = false;
        if (hurt) {
            if (leftHandAttack) {
                if (entity instanceof LivingEntity living) {
                    living.removeEffect(MobEffects.REGENERATION);
                    MobEffectInstance eff = living.getEffect(FateMobEffects.GAE_BUIDHE.get());
                    int amplifier = 0;
                    if (eff != null && this.getRandom().nextFloat() < 1 - (eff.getAmplifier() * 0.2)) {
                        amplifier = Math.min(4, eff.getAmplifier() + 1);
                    }
                    living.addEffect(new MobEffectInstance(FateMobEffects.GAE_BUIDHE.get(), 200 + (amplifier * 100), amplifier));
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
    protected DamageSource damageSourceAttack(Entity target) {
        return this.deargAttackFlag ? CustomDamageSource.gaeDearg(this) : DamageSource.mobAttack(this);
    }

    @Override
    protected void tryDisableShield(Player player, ItemStack stack, ItemStack playerUseItem) {
        if (this.deargAttackFlag) {
            if (!playerUseItem.isEmpty() && playerUseItem.getItem() instanceof ShieldItem) {
                player.getCooldowns().addCooldown(Items.SHIELD, 200);
                this.level.broadcastEntityEvent(player, (byte) 30);
            }
        } else {
            super.tryDisableShield(player, stack, playerUseItem);
        }
    }

    @Override
    public boolean isInvisible() {
        if (this.getAnimationHandler().isCurrent(BLINK, BLINK_AWAY)) {
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
            return anim.isPast("teleport_start") && !anim.isPast("teleport_end");
        }
        return super.isInvisible();
    }

    private void sphereParticles() {
        double goldenAngle = Math.PI * (Math.sqrt(5) - 1);
        for (int i = 0; i < 40; i++) {
            double phi = Math.acos(1 - 2. * i / 50);
            double theta = goldenAngle * i;
            double x = Math.cos(theta) * Math.sin(phi);
            double y = Math.sin(theta) * Math.sin(phi);
            double z = Math.cos(phi);
            this.level.addParticle(ParticleTypes.WITCH, this.getX() + x, this.getY(0.5) + y, this.getZ() + z, x * 0.15, y * 0.15, z * 0.15);
        }
    }

    private void unsealWeapon(ItemStack stack, boolean unseal) {
        if (stack.getItem() == FateItems.GAEBUIDHE.get() || stack.getItem() == FateItems.GAEDEARG.get()) {
            SpearItem.applyFoil(stack, !unseal);
            if (unseal) {
                this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1, 1);
            }
        }
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
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }
}
