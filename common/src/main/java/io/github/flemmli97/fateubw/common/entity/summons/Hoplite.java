package io.github.flemmli97.fateubw.common.entity.summons;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.SpearProjectile;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateDimensions;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import org.jetbrains.annotations.Nullable;

public class Hoplite extends SummonedEntity {

    public static final ResourceLocation IRON_MODIFIER = Fate.modRes("iron_modifier");

    protected static final EntityDataAccessor<Boolean> HAS_SHIELD = SynchedEntityData.defineId(Hoplite.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> HAS_SPEAR = SynchedEntityData.defineId(Hoplite.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> BLOCKING = SynchedEntityData.defineId(Hoplite.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IRON = SynchedEntityData.defineId(Hoplite.class, EntityDataSerializers.BOOLEAN);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String STAB = BUILDER.add("stab", AnimationsBuilder.definition(1).marker("attack", 0.56));
    public static final String THROW = BUILDER.add("throw", AnimationsBuilder.definition(1.76).marker("throw", 0.8));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Hoplite> animationHandler = new AnimationHandler<>(this, ANIMS);

    private float shieldHealth;
    private int shieldCooldown, stopBlockingTick;
    private int spearRegen;

    private int wrongDimensionTicker = 300;

    public Hoplite(EntityType<? extends Hoplite> type, Level level) {
        super(type, level);
        this.shieldHealth = this.getMaxHealth() * 0.4f;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_SHIELD, true);
        builder.define(HAS_SPEAR, true);
        builder.define(BLOCKING, false);
        builder.define(IRON, false);
    }

    public boolean hasShield() {
        return this.entityData.get(HAS_SHIELD);
    }

    public void restoreShield() {
        this.entityData.set(HAS_SHIELD, true);
        this.playSound(FateSounds.HOPLITE_REPAIR.get(), 1, 1);
        this.shieldHealth = this.getMaxHealth() * 0.4f;
    }

    public boolean isBlockingShield() {
        return this.hasShield() && this.entityData.get(BLOCKING);
    }

    public void startBlockWithShield() {
        if (this.hasShield() && !this.isBlockingShield()) {
            this.entityData.set(BLOCKING, true);
        }
        this.stopBlockingTick = 100;
    }

    public boolean hasSpear() {
        return this.entityData.get(HAS_SPEAR);
    }

    public boolean hasIronArmor() {
        return this.entityData.get(IRON);
    }

    public void setIronArmor(boolean armor) {
        this.entityData.set(IRON, armor);
        if (!this.level().isClientSide) {
            this.getAttribute(Attributes.MAX_HEALTH)
                    .addTransientModifier(new AttributeModifier(IRON_MODIFIER, 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            this.getAttribute(Attributes.ARMOR)
                    .addTransientModifier(new AttributeModifier(IRON_MODIFIER, 1.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }

    @Override
    protected Pair<Integer, Integer> followRange() {
        return Pair.of(10, 48);
    }

    @Override
    public BrainActivityGroup<? extends SummonedEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                this.getCooldownAI().startCondition(owner -> !owner.hasSpear() || BehaviourUtils.runCooldownBehaviour(owner))
                        .stopIf(owner -> owner.hasSpear() && !BehaviourUtils.runCooldownBehaviour(owner)),
                this.getCombatAI().startCondition(owner -> owner.hasSpear() && BehaviourUtils.runCombatBehaviour(owner))
        );
    }

    @Override
    public ExtendedBehaviour<? extends Hoplite> getCombatAI() {
        return AttackBehaviourBuilder.<Hoplite>create()
                .start(STAB).play(BehaviourUtils.cooldownedPlay(false, 10, 25))
                .prepare(new SetWalkTargetToAttackTarget<Hoplite>().closeEnoughDist(BehaviourUtils.closeEnough(2))).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(1)
                .start(THROW).play(BehaviourUtils.cooldownedPlay(false, 10, 25))
                .condition(owner -> BehaviourUtils.ifFurtherThan(5).test(owner))
                .prepare(new SetWalkTargetToAttackTarget<Hoplite>()
                        .closeEnoughDist(BehaviourUtils.closeEnough(15))).prepareOptional(BehaviourUtils.moveTo())
                .end(1)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends Hoplite> getCooldownAI() {
        return SelectableBehaviourBuilder.<Hoplite>builder()
                .add(1, owner -> owner.hasShield() || owner.hasSpear(), new SetWalkTargetToAttackTarget<Hoplite>().closeEnoughDist(BehaviourUtils.closeEnough(2)), BehaviourUtils.moveTo())
                .add(1, owner -> !owner.hasShield() && !owner.hasSpear(), new SetWalkTargetAwayFromTarget<Hoplite>()
                        .minDist(7).speedMod(1.5f), BehaviourUtils.moveTo())
                .build();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.shieldCooldown = Math.max(-1, --this.shieldCooldown);
            if (this.shieldCooldown == 0) {
                this.restoreShield();
            }
            if (this.getTarget() == null || !this.getTarget().isAlive()) {
                this.stopBlockingTick = Math.max(-1, --this.stopBlockingTick);
            }
            if (this.stopBlockingTick == 0) {
                this.entityData.set(BLOCKING, false);
            }
            this.spearRegen = Math.max(-1, --this.spearRegen);
            if (this.spearRegen == 0) {
                this.entityData.set(HAS_SPEAR, true);
                this.playSound(FateSounds.HOPLITE_REPAIR.get(), 1, 1);
            }
            if (this.level().dimension().equals(FateDimensions.SAND_DUNES.dimension())) {
                this.wrongDimensionTicker = 300;
            } else {
                --this.wrongDimensionTicker;
                if (this.wrongDimensionTicker <= 0) {
                    this.wrongDimensionTicker = 20;
                    this.hurt(FateDamageTypes.create(FateDamageTypes.GRAIL, this.registryAccess()), this.getMaxHealth() * 0.1f);
                }
            }
        }
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(THROW)) {
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.lookAt(this.getTarget(), 60, 30);
            }
            if (anim.isAt("throw")) {
                this.throwSpear();
            }
        } else if (anim.is(STAB)) {
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.lookAt(this.getTarget(), 60, 30);
            }
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.playSound(FateSounds.SWOOSH_2.get(), 1, 1.2f);
            }
        }
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, double grow) {
        double width = this.getBbWidth() * 1.25;
        double length = this.getBbWidth() * 5.5;
        AABB aabb = new AABB(-width * 0.7, -0.02, 0, width * 0.3, this.getBbHeight() + 0.02, length)
                .inflate(grow);
        return new OrientedBoundingBox(aabb, this.getYHeadRot(), 0, this.position());
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        this.startBlockWithShield();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide && !source.is(DamageTypeTags.BYPASSES_SHIELD)) {
            this.startBlockWithShield();
            if (this.isBlockingShield()) {
                amount *= 0.5f;
                this.shieldHealth -= Math.clamp(amount, 0.0f, 5.0f);
                if (this.shieldHealth <= 0.0f) {
                    this.playSound(FateSounds.HOPLITE_SHIELD_BREAK.get(), 1, 1);
                    this.entityData.set(HAS_SHIELD, false);
                    this.entityData.set(BLOCKING, false);
                    this.shieldCooldown = 100;
                } else {
                    this.playSound(SoundEvents.SHIELD_BLOCK, 1, 1);
                }
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(HAS_SHIELD, tag.getBoolean("HasShield"));
        this.entityData.set(HAS_SPEAR, tag.getBoolean("HasSpear"));
        this.setIronArmor(tag.getBoolean("HasIronArmor"));
        this.shieldHealth = tag.getInt("ShieldHealth");
        this.shieldCooldown = tag.getInt("ShieldCooldown");
        this.stopBlockingTick = tag.getInt("StopBlockingTick");
        this.spearRegen = tag.getInt("SpearRegen");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasShield", this.hasShield());
        tag.putBoolean("HasSpear", this.hasSpear());
        tag.putBoolean("HasIronArmor", this.hasIronArmor());
        tag.putFloat("ShieldHealth", this.shieldHealth);
        tag.putInt("ShieldCooldown", this.shieldCooldown);
        tag.putInt("StopBlockingTick", this.stopBlockingTick);
        tag.putInt("SpearRegen", this.spearRegen);
    }

    @Override
    public AnimationHandler<Hoplite> getAnimationHandler() {
        return this.animationHandler;
    }

    public void throwSpear() {
        if (!this.hasSpear())
            return;
        this.entityData.set(HAS_SPEAR, false);
        this.spearRegen = 100;
        SpearProjectile projectile = new SpearProjectile(this.level(), this);
        Vec3 side = this.calculateViewVector(0, this.getViewYRot(1) + 90).scale(0.4);
        projectile.setPos(this.getX() + side.x(), this.getY() + this.getEyeHeight() - 0.1, this.getZ() + side.z());
        if (this.getTarget() != null) {
            Vec3 pos = this.getTarget().position();
            projectile.shootAtPosition(pos.x(), this.getTarget().getY(1), pos.z(), 1.2f, 0);
        } else {
            projectile.shootFromRotation(this, this.getViewXRot(1) - 15, this.getViewYRot(1), 0.0F, 1.2f, 0);
        }
        this.playSound(FateSounds.HOPLITE_SPEAR.get(), 1, 1);
        this.level().addFreshEntity(projectile);
    }
}
