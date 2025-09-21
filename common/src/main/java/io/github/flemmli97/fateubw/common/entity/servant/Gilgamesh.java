package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.EnkiduChains;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
import io.github.flemmli97.fateubw.common.entity.utils.OnProjectileHit;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.ParticlePositionProvider;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.LeapInDirection;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.PlayAnimation;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.CirclingData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.joml.Vector4f;

public class Gilgamesh extends BaseServant implements OnProjectileHit {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ONE_HAND_1 = BUILDER.add("one_hand_1", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56));
    public static final String ONE_HAND_2 = BUILDER.add("one_hand_2", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56));
    public static final String STAB_1 = BUILDER.add("stab_1", AnimationsBuilder.definition(0.84)
            .marker("attack", 0.6, 0.72));
    public static final String GUARD = BUILDER.add("guard", AnimationsBuilder.definition(0.4));
    public static final String BABYLON_1 = BUILDER.add("babylon_1", AnimationsBuilder.definition(1).marker("attack", 0.32));
    public static final String BABYLON_2 = BUILDER.add("babylon_2", AnimationsBuilder.definition(1).marker("attack", 0.32));
    public static final String BABYLON_3 = BUILDER.add("babylon_3", AnimationsBuilder.definition(1).marker("attack", 0.32));
    public static final String EA = BUILDER.add("ea", AnimationsBuilder.definition(3.12).marker("attack", 1.56));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final Vector4f summonColor = new Vector4f(1.0f, 0.85f, 0.3f, 0.7f);

    private final AnimationHandler<Gilgamesh> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide()) {
                    if (anim == null) {
                        if (this.getAnimationHandler().isCurrent(EA)) {
                            this.switchableWeapon.switchItems(true);
                        }
                    } else if (anim.is(EA)) {
                        this.switchableWeapon.switchItems(false);
                        this.startUsingItem(InteractionHand.MAIN_HAND);
                        this.getMainHandItem().set(FateDataComponents.GLOWING_ITEM.get(), Unit.INSTANCE);
                    }
                }
                return false;
            });

    public final SwitchableWeapon<Gilgamesh> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(FateItems.ENUMAELISH.get()), ItemStack.EMPTY);

    private int chainCooldown = 200, leapCooldown;

    public Gilgamesh(EntityType<? extends Gilgamesh> entityType, Level level) {
        super(entityType, level);
        this.revealServant();
    }

    @Override
    public boolean hasOwnWeapon() {
        return true;
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<Gilgamesh>create()
                .start(ONE_HAND_1).play(BehaviourUtils.cooldownedPlay(true, 18, 30))
                .condition(gil -> !gil.useRanged() && BehaviourUtils.ifCloserThan(4).test(gil))
                .end(7)
                .start(ONE_HAND_2).play(BehaviourUtils.cooldownedPlay(true, 18, 30))
                .condition(gil -> !gil.useRanged() && BehaviourUtils.ifCloserThan(4).test(gil))
                .end(7)
                .start(STAB_1).play(BehaviourUtils.cooldownedPlay(true, 18, 30))
                .condition(gil -> !gil.useRanged() && BehaviourUtils.ifCloserThan(4).test(gil))
                .end(7)
                .start(BABYLON_1, BABYLON_2, BABYLON_3).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(20),
                        (s, entity) -> 30 + entity.getRandom().nextInt(20) - (entity.chainCooldown > 0 ? 15 : 0)))
                .prepare(new SetWalkTargetWithinDist<Gilgamesh>()
                        .min(8).max(18).speedMod(1.2f)).prepareOptional(BehaviourUtils.timedMoveAttack(15, 30))
                .end(11 * 3)
                .start(BABYLON_1, BABYLON_2, BABYLON_3).play((PlayAnimation<Gilgamesh>) BehaviourUtils.<Gilgamesh>cooldownedPlay(BehaviourUtils.ifCloserThan(20),
                                (s, entity) -> 30 + entity.getRandom().nextInt(20) - (entity.chainCooldown > 0 ? 15 : 0))
                        .startCondition(BehaviourUtils.ifCloserThan(16)))
                .condition(gil -> !gil.useRanged())
                .prepare(new SetWalkTargetToAttackTarget<Gilgamesh>().closeEnoughDist(BehaviourUtils.closeEnough(16)))
                .prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(9 * 3)
                .start(EA).play(BehaviourUtils.cooldownedPlay(false, 20, 35))
                .condition(BaseServant::canUseNP)
                .prepare(new SetWalkTargetWithinDist<Gilgamesh>()
                        .min(4).max(8).speedMod(1.1f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(40)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<Gilgamesh>builder()
                .add(3, Gilgamesh::useRanged, new Idle<>())
                .add(10, new StrafeTarget<Gilgamesh>().strafeDistance(14))
                .add(7, new SetWalkTargetAwayFromTarget<Gilgamesh>().radius(7).speedMod(1.1f), BehaviourUtils.moveTo())
                .add(2, gil -> gil.leapCooldown < 0 && BehaviourUtils.ifCloserThan(7).test(gil), new LeapInDirection<Gilgamesh>()
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()))
                        .whenStarting(e -> {
                            e.leapCooldown = 80 + e.getRandom().nextInt(40);
                            BrainUtils.clearMemory(e, MemoryModuleType.ATTACK_COOLING_DOWN);
                        })).build();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.switchableWeapon.save(tag, this.registryAccess());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.switchableWeapon.read(tag, this.registryAccess());
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            --this.chainCooldown;
            --this.leapCooldown;
        }
    }

    @Override
    public void setupAttack(AnimationDefinition anim) {
        if (this.getAnimationHandler().isCurrent(BABYLON_1, BABYLON_2, BABYLON_3)) {
            BrainUtils.clearMemory(this, MemoryModuleType.LOOK_TARGET);
            this.getNavigation().stop();
            return;
        }
        super.setupAttack(anim);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(EA)) {
            if (!anim.isAt("attack")) {
                this.setTargetPositionFromAttackTarget();
            }
            if (anim.isAt("attack")) {
                this.ea(this.getTargetPosition());
            }
        } else if (anim.is(BABYLON_1, BABYLON_2, BABYLON_3)) {
            LivingEntity target = this.getTarget();
            if (!anim.isPast("attack") && target != null) {
                this.getLookControl().setLookAt(target, 60.0F, 30.0F);
            }
            if (anim.isAt("attack")) {
                if (target != null) {
                    this.attackWithRangedAttack(target);
                }
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    protected float getKnockback(Entity attacker, DamageSource damageSource) {
        if (this.getAnimationHandler().isCurrent(STAB_1))
            return 0;
        return super.getKnockback(attacker, damageSource) + 3;
    }

    @Override
    public AnimationHandler<Gilgamesh> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double height = this.getBbHeight();
        double width = this.getBbWidth();
        double length = 1 * this.getScale();
        if (anim.is(ONE_HAND_1)) {
            width += 0.5 * this.getScale();
            length += 0.7 * this.getScale();
        }
        if (anim.is(ONE_HAND_2)) {
            width += 1.2 * this.getScale();
            length += 0.8 * this.getScale();
        }
        if (anim.is(STAB_1)) {
            width += 0.3 * this.getScale();
            length += 0.9 * this.getScale();
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (!this.getAnimationHandler().isCurrent(EA) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR)
                && !damageSource.is(DamageTypeTags.IS_PROJECTILE)
                && this.getMainHandItem().is(FateItems.ENUMAELISH.get()) && this.getRandom().nextFloat() < 0.15) {
            this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1, 1);
            this.getAnimationHandler().setAnimation(GUARD);
            if (damageSource.getEntity() instanceof LivingEntity entity) {
                Vec3 dir = entity.position().subtract(this.position());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(5);
                entity.setDeltaMovement(entity.getDeltaMovement().add(dir).add(0, 0.3, 0));
            }
            for (int i = 0; i < 16; i++) {
                float red = (150 + this.getRandom().nextInt(100)) / 255f;
                float col = (10 + this.getRandom().nextInt(200)) / 255f;
                if (col >= red * 0.8) {
                    col = 0.1f;
                    red = 0.1f;
                }
                AdvancedParticleContainer.make(new TrailParticleData(FateParticles.TRAIL.get(),
                                TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(MathUtils.NORMAL_Y, 6))
                                        .setColor(red, col, col, 0.7f)
                                        .setColor2(red, col, col, 0.7f)
                                        .setWidth(0.03f)
                                        .setWidth2(0.03f)
                                        .build()))
                        .addData(new CirclingData((float) ((this.getRandom().nextDouble() * 0.15 + 1) * this.getBbWidth()), 0, this.getRandom().nextInt() * 360, 80, MathUtils.NORMAL_Y))
                        .addData(new ParticleMetaData(10, false, 0))
                        .build()
                        .add(this.level(), this.getX(), this.getY(this.getRandom().nextDouble() * 1.2), this.getZ());
            }
            return false;
        }
        return !this.getAnimationHandler().isCurrent(EA) && super.hurt(damageSource, damage);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.ENUMAELISH.get()));
        }
    }

    public void ea(TargetPosition target) {
        if (!this.attemptUseNobelPhantasm())
            return;
        EnumaElish ea = new EnumaElish(this.level(), this);
        if (target != null) {
            Vec3 pos = target.asVec(ea.position());
            ea.setRotationTo(pos.x(), pos.y(), pos.z(), 0);
        }
        this.level().addFreshEntity(ea);
        this.revealServant();
        this.stopUsingItem();
        this.getMainHandItem().remove(FateDataComponents.GLOWING_ITEM.get());
        this.switchableWeapon.switchItems(true);
    }

    public void attackWithRangedAttack(LivingEntity target) {
        double perc = Mth.clamp(1 - this.getHealth() / this.getMaxHealth(), 0.2, 0.8);
        int randAmount = (int) (18 * perc);
        int base = 6 + (int) (6 * perc);
        int weaponAmount = this.getRandom().nextInt(Math.max(1, randAmount)) + base;
        if (this.getAnimationHandler().getAnimation() == null)
            this.spawnBehind(target, weaponAmount);
        else if (this.getAnimationHandler().isCurrent(BABYLON_1, BABYLON_2, BABYLON_3)) {
            int chainChance = 0;
            if (this.chainCooldown < 0) {
                if (this.getHealth() < this.getMaxHealth() * 0.3) {
                    chainChance = 3;
                } else if (this.getHealth() < this.getMaxHealth() * 0.5) {
                    chainChance = 5;
                } else if (this.getHealth() < this.getMaxHealth() * 0.8) {
                    chainChance = 7;
                }
            }
            if (chainChance > 0 && this.getRandom().nextInt(chainChance) == 0) {
                this.spawnChains(target, weaponAmount);
            } else if (this.getRandom().nextInt(3) == 0)
                this.spawnAroundTarget(target, weaponAmount);
            else
                this.spawnBehind(target, weaponAmount);
        }
    }

    private void spawnBehind(LivingEntity target, int amount) {
        BabylonWeapon.spawnWeapons(this, target, amount, 7);
        this.chainCooldown -= 50;
    }

    private void spawnAroundTarget(LivingEntity target, int amount) {
        BabylonWeapon.spawnWeaponsAround(this, target, amount, 6 + amount / 5);
        this.chainCooldown -= 50;
    }

    private void spawnChains(LivingEntity target, int amount) {
        amount = Math.max(7, Mth.ceil(amount * 0.7));
        EnkiduChains.spawnWeaponsAround(this, target, amount, 7 + amount / 7);
    }

    protected boolean useRanged() {
        return this.getMainHandItem().getItem() != FateItems.ENUMAELISH.get();
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
    public void onProjectileHit(Entity entity) {
        if (entity instanceof EnkiduChains) {
            if (this.chainCooldown < 0) {
                BehaviourUtils.modifyExpiringMemory(this, MemoryModuleType.ATTACK_COOLING_DOWN, -20);
                this.chainCooldown = 250 + this.getRandom().nextInt(100);
                LivingEntity target = BrainUtils.getTargetOfEntity(this);
                if (target != null && this.distanceToSqr(target) < 16) {
                    Vec3 dir = target.position().subtract(this.position());
                    dir = new Vec3(-dir.x(), 0, -dir.z()).normalize().scale(4.5);
                    entity.setDeltaMovement(dir.x(), 0.2, dir.z());
                }
            }
        }
    }
}