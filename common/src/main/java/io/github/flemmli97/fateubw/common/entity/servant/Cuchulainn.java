package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
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
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.joml.Vector4f;

public class Cuchulainn extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SPEAR_1 = BUILDER.add("spear_1", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56).marker("step", 0.32)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.4)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    public static final String SPEAR_2 = BUILDER.add("spear_2", AnimationsBuilder.definition(0.64)
            .marker("attack", 0.52).marker("step", 0.28)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.36)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.56));
    public static final String SPEAR_3 = BUILDER.add("spear_3", AnimationsBuilder.definition(0.68)
            .marker("attack", 0.56).marker("step", 0.32)
            .marker(EntityWeaponTrailProvider.TRAIL_START, 0.4)
            .marker(EntityWeaponTrailProvider.TRAIL_END, 0.6));
    public static final String SPEAR_4 = BUILDER.add("spear_4", AnimationsBuilder.definition(0.72)
            .marker("attack", 0.6));
    public static final String SPEAR_5 = BUILDER.add("spear_5", AnimationsBuilder.definition(0.72)
            .marker("attack", 0.6));
    public static final String SPEAR_COMBO = BUILDER.add("spear_stab_combo", AnimationsBuilder.definition(2.52)
            .marker("attack", 0.6, 1.08).marker("attack_final", 1.88).marker("step_large", 1.64));
    private static final String GAE_BOLG = BUILDER.add("gae_bolg", AnimationsBuilder.definition(1.48)
            .marker("throw", 1.2).marker("jump", 0.24)
            .marker("float_start", 0.4).marker("float_end", 1.28));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private int gaeBolgThrowTick;

    private final AnimationHandler<Cuchulainn> animationHandler = new AnimationHandler<>(this, ANIMS);

    public Cuchulainn(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.GAEBOLG.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.GAEBOLG.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<BaseServant>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(SPEAR_1)
                        .start(SPEAR_2, 2, 0.28f, 1)
                        .start(SPEAR_3, 2, 0.28f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.05f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(7)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(SPEAR_2)
                        .start(SPEAR_1, 2, 0.28f, 2)
                        .chain(Cuchulainn.SPEAR_3, 2, 0.28f)
                        .start(SPEAR_1, 2, 0.28f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.05f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(7)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(SPEAR_4)
                        .start(SPEAR_3, 2, 0.28f, 1)
                        .start(SPEAR_5, 2, 0.28f, 1)
                        .build())).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.05f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(7)
                .start(SPEAR_3).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.05f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(SPEAR_5).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.05f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(SPEAR_COMBO).play(BehaviourUtils.cooldownedPlay(true, 16, 27))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((m, e) -> 1.05f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(4)
                .start(GAE_BOLG).play(BehaviourUtils.cooldownedPlay(false, 20, 27))
                .condition(BaseServant::canUseNP)
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(4).max(8).speedMod(1.25f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(30)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(7, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(4, new SetWalkTargetAwayFromTarget<BaseServant>().radius(7), BehaviourUtils.moveTo())
                .add(2, BehaviourUtils.ifCloserThan(7), new LeapInDirection<BaseServant>()
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()).scale(1.5f))
                        .whenStarting(e -> BrainUtils.clearMemory(e, MemoryModuleType.ATTACK_COOLING_DOWN))).build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            this.gaeBolgThrowTick = Math.max(0, --this.gaeBolgThrowTick);
            if (this.gaeBolgThrowTick == 1 && this.getMainHandItem().getItem() != FateItems.GAEBOLG.get())
                this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(FateItems.GAEBOLG.get()));
            if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
                if (!this.critHealth) {
                    this.level().getServer().getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.servant.cuchulainn").withStyle(ChatFormatting.GOLD), true);
                    this.critHealth = true;
                }
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 2, false, false));
            }
        } else {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityWeaponTrailProvider.TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityWeaponTrailProvider.EntityTrailData.create(this, anim.getID(), false))
                                            .setColor(102 / 255f, 3 / 255f, 3 / 255f, 0.6f)
                                            .setColor2(102 / 255f, 3 / 255f, 3 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
                if ((anim.is(SPEAR_4) || anim.is(SPEAR_5) || anim.is(SPEAR_COMBO)) && (anim.isAt("attack") || anim.isAt("attack_final"))) {
                    Vec3 offset = new Vec3(0, this.getBbHeight() * 0.5, this.getBbWidth() + (anim.isAt("attack_final") ? 2.5 : 1.9) * this.getScale())
                            .yRot(-this.getYRot() * Mth.DEG_TO_RAD);
                    for (int i = 0; i < 6; i++) {
                        AdvancedParticleContainer.make(ParticleTypes.CRIT)
                                .addData(new ParticleMetaData(10, false, 0))
                                .build().add(this.level(),
                                        this.getX() + offset.x() + this.getRandom().nextGaussian() * 0.1,
                                        this.getY() + offset.y() + this.getRandom().nextGaussian() * 0.1,
                                        this.getZ() + offset.z() + this.getRandom().nextGaussian() * 0.1);
                    }
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GaeBolgTick", this.gaeBolgThrowTick);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.gaeBolgThrowTick = tag.getInt("GaeBolgTick");
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(GAE_BOLG)) {
            if (anim.isAt("jump")) {
                this.setTargetPositionFromAttackTarget();
                Vec3 dir = this.getTarget() != null ? this.getTarget().position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(-1.6).add(0, 2.3, 0);
                this.setDeltaMovement(dir);
            }
            if (anim.isPast("float_start") && !anim.isPast("float_end")) {
                Vec3 delta = this.getDeltaMovement().scale(0.6);
                this.setDeltaMovement(new Vec3(delta.x(), Math.max(0, delta.y()), delta.z()));
            }
            if (anim.isAt("throw")) {
                this.gaeBolg(this.getTargetPosition().asVec(this.position()));
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.35);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("step_large")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.45);
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
        if (anim.is(SPEAR_1)) {
            width += 3 * this.getScale();
            length += 1.8 * this.getScale();
            return new AABB(-width * 0.6, -0.03, 0, width * 0.4, height + 0.03, length);
        }
        if (anim.is(SPEAR_2)) {
            width += 3.1 * this.getScale();
            length += 1.8 * this.getScale();
        }
        if (anim.is(SPEAR_3)) {
            width += 1.2 * this.getScale();
            length += 1.9 * this.getScale();
            return new AABB(-width * 0.3, -0.03, 0, width * 0.7, height + 0.03, length);
        }
        if (anim.is(SPEAR_4, SPEAR_5, SPEAR_COMBO)) {
            width += 0.3 * this.getScale();
            length += 2.2 * this.getScale();
            if (anim.isAt("attack_final")) {
                length += 0.5 * this.getScale();
            }
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    public AnimationHandler<Cuchulainn> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        return !this.getAnimationHandler().isCurrent(GAE_BOLG) && super.hurt(damageSource, damage);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void gaeBolg(Vec3 pos) {
        if (!this.attemptUseNobelPhantasm())
            return;
        GaeBolg gaeBolg = new GaeBolg(this.level(), this);
        if (this.getTarget() != null)
            gaeBolg.setTarget(this.getTarget());
        gaeBolg.shootAtPosition(pos.x(), pos.y(), pos.z(), 2, 0);
        this.level().addFreshEntity(gaeBolg);
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        this.gaeBolgThrowTick = 100;
        this.revealServant();
    }

    public void retrieveGaeBolg() {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(FateItems.GAEBOLG.get()));
        this.gaeBolgThrowTick = 0;
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector4f(0, 0, -1.4f, 1), new Vector4f(0, 0, -1.8f, 1));
    }
}
