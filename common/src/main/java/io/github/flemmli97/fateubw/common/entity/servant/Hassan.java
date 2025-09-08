package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.MoveBehindBehaviour;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.fateubw.common.entity.summons.HassanClone;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Hassan extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DAGGER_1 = BUILDER.add("dagger_1", AnimationsBuilder.definition(0.6)
            .marker("attack", 0.48).marker("step", 0.4)
            .marker(EntityTrailProvider.TRAIL_START, 0.32)
            .marker(EntityTrailProvider.TRAIL_END, 0.48));
    public static final String DAGGER_2 = BUILDER.add("dagger_2", AnimationsBuilder.definition(0.56)
            .marker("attack", 0.44).marker("step", 0.4)
            .marker(EntityTrailProvider.TRAIL_START, 0.32)
            .marker(EntityTrailProvider.TRAIL_END, 0.44));
    public static final String DAGGER_3 = BUILDER.add("dagger_3", AnimationsBuilder.definition(0.6)
            .marker("attack", 0.48).marker("step", 0.44)
            .marker(EntityTrailProvider.TRAIL_START, 0.32)
            .marker(EntityTrailProvider.TRAIL_END, 0.48));
    public static final String DAGGER_4 = BUILDER.add("dagger_4", AnimationsBuilder.definition(0.52)
            .marker("attack", 0.4).marker("step", 0.32));
    public static final String TOP_STAB = BUILDER.add("top_stab", AnimationsBuilder.definition(0.6).marker("attack", 0.48));
    public static final String THROW = BUILDER.add("dagger_throw", AnimationsBuilder.definition(1.32)
            .marker("attack_1", 0.36).marker("attack_2", 0.88));
    public static final String DUPE = BUILDER.add("dupe", AnimationsBuilder.definition(1.56).marker("attack", 0.84));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final byte SMOKE = 64;

    private final AnimationHandler<Hassan> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final Vector4f summonColor = new Vector4f(28 / 255f, 29 / 255f, 31 / 255f, 0.8f);

    private final Set<UUID> copies = new HashSet<>();
    private boolean behind;

    public Hassan(EntityType<? extends Hassan> entityType, Level level) {
        super(entityType, level);
    }

    public static boolean behind(Entity source, Entity target) {
        Vec3 vec3 = target.getViewVector(1.0f);
        Vec3 vec31 = source.position().vectorTo(target.position()).normalize();
        vec31 = new Vec3(vec31.x, 0.0, vec31.z);
        return vec31.dot(vec3) > 0.0;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.ASSASSIN_DAGGER.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.ASSASSIN_DAGGER.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<BaseServant>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(DAGGER_1)
                        .start(DAGGER_3, 2, 0.24f, 1)
                        .start(DAGGER_4, 2, 0.24f, 1)
                        .start(TOP_STAB, 2, 0.24f, 1).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(11)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(DAGGER_1)
                        .start(DAGGER_3, 2, 0.24f, 1)
                        .start(DAGGER_4, 2, 0.24f, 1).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new MoveBehindBehaviour<>())
                .end(9)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(DAGGER_3)
                        .start(DAGGER_1, 2, 0.24f, 1)
                        .chain(DAGGER_2, 2, 0.24f)
                        .start(DAGGER_1, 2, 0.24f, 1)
                        .chain(DAGGER_4, 2, 0.24f).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(11)
                .start(TOP_STAB).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new SetWalkTargetToAttackTarget<BaseServant>().speedMod((e, t) -> 1.2f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BehaviourUtils.of(AnimationPlayHolder.<BaseServant>builder(DAGGER_4)
                        .start(TOP_STAB, 2, 0.24f, 1).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new MoveBehindBehaviour<BaseServant>().speedMod(1.2f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(10)
                .start(THROW).play(BehaviourUtils.cooldownedPlay(false, 15, 26))
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(7).max(14).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(9)
                .start(THROW).play(BehaviourUtils.cooldownedPlay(false, 15, 26))
                .condition(entity -> {
                    if (BehaviourUtils.ifFurtherThan(8).test(entity))
                        return true;
                    LivingEntity target = BrainUtils.getTargetOfEntity(entity);
                    return target != null && target.getY() - entity.getY() > 4;
                })
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(7).max(14).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(11)
                .start(DUPE).play(BehaviourUtils.cooldownedPlay(false, 30, 50))
                .condition(BaseServant::canUseNP)
                .prepare(new SetWalkTargetWithinDist<BaseServant>()
                        .min(4).max(11).speedMod(1.3f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(60)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(6, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(3, new SetWalkTargetAwayFromTarget<BaseServant>()
                        .radius(7), BehaviourUtils.moveTo()).build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityTrailProvider.TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityTrailProvider.EntityTrailData.create(this, anim.getID(), false))
                                            .setColor(68 / 255f, 68 / 255f, 68 / 255f, 0.6f)
                                            .setColor2(68 / 255f, 68 / 255f, 68 / 255f, 0.2f)
                                            .setType(TrailInfo.Visual.TEXTURE, 0)
                                            .build()),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                }
            }
        }
    }

    @Override
    public boolean canUseNP() {
        return super.canUseNP() && this.gatherCopies().isEmpty();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == SMOKE) {
            for (int i = 0; i < 32; i++) {
                this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        this.getX((this.getRandom().nextDouble() * 2 - 1) * 1.2), this.getY(this.getRandom().nextDouble() * 1.2), this.getZ((this.getRandom().nextDouble() * 2 - 1) * 1.2),
                        this.getRandom().nextGaussian() * 0.02, this.getRandom().nextGaussian() * 0.02, this.getRandom().nextGaussian() * 0.02);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ListTag copies = new ListTag();
        this.copies.forEach(hassan -> copies.add(NbtUtils.createUUID(hassan)));
        tag.put("Copies", copies);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        tag.getList("Copies", Tag.TAG_INT_ARRAY).forEach(nbt -> this.copies.add(NbtUtils.loadUUID(nbt)));
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(DUPE)) {
            if (anim.isAt(0.72)) {
                this.level().broadcastEntityEvent(this, SMOKE);
            }
            if (anim.isAt("attack")) {
                this.summonClones();
            }
        } else if (anim.is(THROW)) {
            if (anim.isAt("attack_1")) {
                this.throwItem(true);
            } else if (anim.isAt("attack_2")) {
                this.throwItem(false);
            }
        } else {
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.25);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double height = this.getBbHeight();
        double width = this.getBbWidth();
        double length = 1 * this.getScale();
        if (anim.is(DAGGER_1)) {
            width += 0.6 * this.getScale();
            length += 0.4 * this.getScale();
        }
        if (anim.is(DAGGER_2)) {
            width += 0.8 * this.getScale();
            length += 0.4 * this.getScale();
        }
        if (anim.is(DAGGER_3)) {
            width += 0.5 * this.getScale();
            length += 0.7 * this.getScale();
        }
        if (anim.is(DAGGER_4)) {
            width += 0.3 * this.getScale();
            length += 0.8 * this.getScale();
        }
        if (anim.is(TOP_STAB)) {
            width += 0.2 * this.getScale();
            length += 0.8 * this.getScale();
        }
        return new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.03, length);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.behind = behind(this, entity);
        boolean hurt = super.doHurtTarget(entity);
        if (this.behind && hurt) {
            this.level().playSound(null, this, SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 0.7f, 0.9f);
            if (this.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 15; i++)
                    serverLevel.sendParticles(DustParticleOptions.REDSTONE, entity.getRandomX(1.4), entity.getRandomY(), entity.getRandomZ(1.4), 0, 0, 0, 0, 0);
            }
        }
        this.behind = false;
        return hurt;
    }

    @Override
    public float damageModifier(Entity target) {
        return this.behind ? 1.5f : super.damageModifier(target);
    }

    @Override
    public AnimationHandler<Hassan> getAnimationHandler() {
        return this.animationHandler;
    }

    public boolean addCopy(HassanClone copy) {
        if (this.copies.size() < this.props().getConfig(ServantExtraData.HASSAN_COPIES)) {
            this.copies.add(copy.getUUID());
            return true;
        }
        return false;
    }

    public List<HassanClone> gatherCopies() {
        ArrayList<HassanClone> list = new ArrayList<>();
        for (HassanClone e : this.level().getEntitiesOfClass(HassanClone.class, this.getBoundingBox().inflate(32))) {
            if (this.copies.contains(e.getUUID())) {
                e.setOriginal(this);
                list.add(e);
            }
        }
        return list;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.dead && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void summonClones() {
        if (!this.level().isClientSide && this.gatherCopies().isEmpty()) {
            if (!this.attemptUseNobelPhantasm())
                return;
            this.copies.clear();
            for (int i = 0; i < this.props().getConfig(ServantExtraData.HASSAN_COPIES); i++) {
                HassanClone hassan = new HassanClone(this.level(), this);
                hassan.moveTo(this.getX(), this.getY(), this.getZ(), Mth.wrapDegrees(this.level().random.nextFloat() * 360.0F), 0.0F);
                hassan.finalizeSpawn((ServerLevelAccessor) this.level(), this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
                this.level().addFreshEntity(hassan);
                this.addCopy(hassan);
            }
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300, 1, true, false));
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2, true, false));
            for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(32))) {
                if (mob.getTarget() == this)
                    mob.setTarget(null);
            }
            this.revealServant();
        }
    }

    public void throwItem(boolean main) {
        ThrownItemEntity item = new ThrownItemEntity(this.level(), this);
        item.setWeapon(this.getWeaponToThrowAndReplace(main));
        if (this.getTarget() != null) {
            item.shootAtEntity(this.getTarget(), 1.2f, 7 - this.level().getDifficulty().getId() * 2);
        } else {
            item.shootFromRotation(this, this.getXRot() + 5, this.getYRot(), 0.0F, 1.2f, 1.0F);
        }
        this.playSound(SoundEvents.FISHING_BOBBER_THROW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(item);
    }

    private ItemStack getWeaponToThrowAndReplace(boolean main) {
        ItemStack weapon;
        if (!main) {
            weapon = this.getOffhandItem().isEmpty() ? this.getOffhandItem() : this.getMainHandItem();
        } else {
            weapon = this.getMainHandItem();
        }
        return weapon.isEmpty() ? new ItemStack(FateItems.ASSASSIN_DAGGER.get()) : weapon.copy();
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
        return new WeaponTrail(new Vector4f(0, 0, -0.2f, 1), new Vector4f(0, 0, -0.6f, 1));
    }
}