package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.MoveBehindBehaviour;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.fateubw.common.entity.servant.Hassan;
import io.github.flemmli97.fateubw.common.entity.utils.ServantModelLike;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailHolder;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailHolderProvider;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedMobDataHandler;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.InteractWithDoor;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class HassanClone extends SummonedEntity implements ServantModelLike, SyncedMobDataHandler, EntityWeaponTrailHolderProvider {

    public static final ResourceLocation BACKSTAB_MODIFIER = Fate.modRes("hassan_backstab");

    public static final AnimationDefinitionContainer ANIMS = Hassan.BUILDER.build();

    private final AnimationHandler<HassanClone> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (anim != null)
            this.setupAttack(anim);
        if (anim == null || !anim.is(Hassan.SUMMON)) {
            if (!this.offHandCache.isEmpty()) {
                this.setItemInHand(InteractionHand.OFF_HAND, this.offHandCache);
                this.offHandCache = ItemStack.EMPTY;
            }
            if (!this.mainHandCache.isEmpty()) {
                this.setItemInHand(InteractionHand.MAIN_HAND, this.mainHandCache);
                this.mainHandCache = ItemStack.EMPTY;
            }
        }
        return false;
    });

    private final SyncedDataContainer<HassanClone> syncedDataContainer = SyncedDataContainer.builder(this)
            .define(BaseServant.TARGET_POSITION, TenshilibSyncableEntityDatas.TARGET_POS.get(), null).build();

    private ItemStack mainHandCache = ItemStack.EMPTY;
    private ItemStack offHandCache = ItemStack.EMPTY;

    private final EntityWeaponTrailHolder<HassanClone> trailHolder = new EntityWeaponTrailHolder<>(this);

    public HassanClone(EntityType<? extends HassanClone> type, Level level) {
        super(type, level);
    }

    @Override
    public SyncedDataContainer<?> getDataContainer() {
        return this.syncedDataContainer;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.ASSASSIN_DAGGER.get()));
    }

    @Override
    public ItemStack getRenderHandStack(InteractionHand hand) {
        return switch (hand) {
            case MAIN_HAND -> {
                ItemStack stack = this.getMainHandItem();
                if (stack.is(FateItems.ASSASSIN_DAGGER.get())) {
                    yield stack;
                }
                yield ServantModelLike.getStack(FateItems.ASSASSIN_DAGGER.get());
            }
            case OFF_HAND -> this.getOffhandItem();
        };
    }

    @Override
    public BrainActivityGroup<? extends SummonedEntity> getCoreTasks() {
        return super.getCoreTasks().behaviours(new InteractWithDoor<>(),
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 100))
                        .whenStopping(m -> BrainUtils.clearMemory(m, MemoryModuleType.LOOK_TARGET)));
    }

    @Override
    public ExtendedBehaviour<? extends HassanClone> getCombatAI() {
        return AttackBehaviourBuilder.<HassanClone>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<HassanClone>builder(Hassan.DAGGER_1)
                        .start(Hassan.DAGGER_3, 2, 0.24f, 1)
                        .start(Hassan.DAGGER_4, 2, 0.24f, 1)
                        .start(Hassan.TOP_STAB, 2, 0.24f, 1).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(BehaviourUtils.of(AnimationPlayHolder.<HassanClone>builder(Hassan.DAGGER_1)
                        .start(Hassan.DAGGER_3, 2, 0.24f, 1)
                        .start(Hassan.DAGGER_4, 2, 0.24f, 1).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new MoveBehindBehaviour<>())
                .end(7)
                .start(BehaviourUtils.of(AnimationPlayHolder.<HassanClone>builder(Hassan.DAGGER_3)
                        .start(Hassan.DAGGER_1, 2, 0.24f, 1)
                        .chain(Hassan.DAGGER_2, 2, 0.24f)
                        .start(Hassan.DAGGER_1, 2, 0.24f, 1)
                        .chain(Hassan.DAGGER_4, 2, 0.24f).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(8)
                .start(Hassan.TOP_STAB).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new SetWalkTargetToAttackTarget<HassanClone>().speedMod((e, t) -> 1.2f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(9)
                .start(BehaviourUtils.of(AnimationPlayHolder.<HassanClone>builder(Hassan.DAGGER_4)
                        .start(Hassan.TOP_STAB, 2, 0.24f, 1).build())).play(BehaviourUtils.cooldownedPlay(true, 15, 26))
                .prepare(new MoveBehindBehaviour<HassanClone>().speedMod(1.2f)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(11)
                .start(Hassan.THROW).play(BehaviourUtils.cooldownedPlay(false, 15, 26))
                .prepare(new SetWalkTargetWithinDist<HassanClone>()
                        .min(7).max(14).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(9)
                .start(Hassan.THROW).play(BehaviourUtils.cooldownedPlay(false, 15, 26))
                .condition(entity -> {
                    if (BehaviourUtils.ifFurtherThan(8).test(entity))
                        return true;
                    LivingEntity target = BrainUtils.getTargetOfEntity(entity);
                    return target != null && target.getY() - entity.getY() > 4;
                })
                .prepare(new SetWalkTargetWithinDist<HassanClone>()
                        .min(7).max(14).speedMod(1.2f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(11)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends HassanClone> getCooldownAI() {
        return SelectableBehaviourBuilder.<HassanClone>builder()
                .add(6, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(3, new SetWalkTargetAwayFromTarget<HassanClone>()
                        .radius(7), BehaviourUtils.moveTo()).build();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.tickCount > 200 && this.isAlive() && (this.getOwner() == null || !this.getOwner().isAlive())) {
                this.hurt(this.damageSources().genericKill(), Integer.MAX_VALUE);
            }
        } else {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null) {
                if (anim.isAt(EntityWeaponTrailProvider.TRAIL_START)) {
                    this.level().addParticle(new TrailParticleData(FateParticles.TRAIL.get(),
                                    TrailInfo.builder(EntityWeaponTrailProvider.EntityTrailData.create(this, anim.getID(), false))
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
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("MainHandCache", this.mainHandCache.save(this.registryAccess(), new CompoundTag()));
        tag.put("OffHandCache", this.offHandCache.save(this.registryAccess(), new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.mainHandCache = ItemStack.parseOptional(this.registryAccess(), tag.getCompound("MainHandCache"));
        this.offHandCache = ItemStack.parseOptional(this.registryAccess(), tag.getCompound("OffHandCache"));
    }

    public TargetPosition getTargetPosition() {
        return this.getDataContainer().get(BaseServant.TARGET_POSITION);
    }

    public void setTargetPosition(LivingEntity target, boolean ranged) {
        this.setTargetPosition(target == null ? null : ranged ? TargetPosition.reducedRangeOf(target) : TargetPosition.fullRangeOf(target));
    }

    public void setTargetPosition(TargetPosition position) {
        this.getDataContainer().set(BaseServant.TARGET_POSITION, position);
    }

    @Nullable
    public Vec3 tryGetTargetPosition(LivingEntity target) {
        if (this.getTargetPosition() != null)
            return this.getTargetPosition()
                    .asVec(this.position());
        return target != null ? target.position() : null;
    }

    public void setupAttack(AnimationDefinition anim) {
        BrainUtils.clearMemory(this, MemoryModuleType.LOOK_TARGET);
        if (this.getTarget() != null) {
            this.setTargetPosition(this.getTarget(), false);
            this.lookAt(this.getTarget(), 60, 30);
        }
        this.getNavigation().stop();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(Hassan.THROW)) {
            if (anim.isAt("attack")) {
                this.throwItem(true);
            } else if (anim.isAt(0.84)) {
                this.throwItem(false);
            }
        } else {
            if (anim.is(Hassan.SUMMON))
                return;
            if (anim.isAt("step")) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(0.35);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("attack")) {
                this.playSound(FateSounds.SWOOSH_2.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.1f);
            }
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, this::runHurtTarget, 0);
    }

    private boolean runHurtTarget(Entity entity) {
        if (entity instanceof Mob) {
            LivingEntity target = ((Mob) entity).getTarget();
            if (target == this.getOwner())
                ((Mob) entity).setTarget(this);
        }
        boolean behind = Hassan.behind(this, entity);
        if (behind) {
            this.getAttribute(Attributes.ATTACK_DAMAGE)
                    .addTransientModifier(new AttributeModifier(HassanClone.BACKSTAB_MODIFIER, 0.5,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        boolean hurt = super.doHurtTarget(entity);
        if (behind) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(HassanClone.BACKSTAB_MODIFIER);
            if (hurt) {
                this.level().playSound(null, this, SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 0.7f, 0.9f);
                if (this.level() instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 15; i++)
                        serverLevel.sendParticles(DustParticleOptions.REDSTONE, entity.getRandomX(1.4), entity.getRandomY(), entity.getRandomZ(1.4), 0, 0, 0, 0, 0);
                }
            }
        }
        return hurt;
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, double grow) {
        Vec3 target = this.tryGetTargetPosition(this.getTarget());
        float yRot = this.getYHeadRot();
        float xRot = this.getXRot();
        if (this.getControllingPassenger() instanceof Player player) {
            yRot = player.getYHeadRot();
            xRot = player.getXRot();
        } else if (target != null) {
            Vec3 dir = target.subtract(this.position()).normalize();
            float[] yXRot = MathsHelper.YXRotFrom(dir);
            yRot = yXRot[0];
            xRot = -yXRot[1];
        }
        double off = this.getBbHeight() * 0.5;
        return new OrientedBoundingBox(this.attackBB(anim)
                .inflate(grow, 0, grow)
                .move(0, -off, grow), yRot, Mth.clamp(xRot, -15, 15), this.position().add(0, off, 0));
    }

    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(Hassan.DAGGER_1)) {
            width += 0.5;
            length += 0.4;
        }
        if (anim.is(Hassan.DAGGER_2, Hassan.DAGGER_3)) {
            width += 0.7;
            length += 0.4;
        }
        if (anim.is(Hassan.DAGGER_4)) {
            width += 0.3;
            length += 0.8;
        }
        if (anim.is(Hassan.TOP_STAB)) {
            width += 0.2;
            length += 0.8;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public AnimationHandler<HassanClone> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(damageSource, damage);
        } else {
            if (damageSource.getEntity() == null || !damageSource.getEntity().getType().is(FateTags.EntityTypes.STRONG_MOB))
                damage *= 0.75;
            if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR) && this.projectileBlockChance()) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 1, 1);
                if (damageSource.getDirectEntity() != null)
                    damageSource.getDirectEntity().remove(RemovalReason.KILLED);
                return false;
            }
            return super.hurt(damageSource, Math.min(50, damage));
        }
    }

    public boolean projectileBlockChance() {
        return this.random.nextFloat() < (float) this.getAttributeValue(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData data) {
        super.finalizeSpawn(world, difficulty, reason, data);
        this.populateDefaultEquipmentSlots(this.getRandom(), difficulty);
        for (EquipmentSlot type : EquipmentSlot.values())
            this.setDropChance(type, 0);
        if (reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.MOB_SUMMONED) {
            this.getAnimationHandler().setAnimation(Hassan.SUMMON);
        }
        return data;
    }

    @Override
    protected void tickDeath() {
        if (this.level().isClientSide) {
            for (int i = 0; i < ((int) ((9 / (float) this.maxDeathTick()) * this.deathTime - 1)); i++) {
                AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                        .addData(new ColorData(76 / 255f, 128 / 255f, 207 / 255f, 0.6f))
                        .addData(new ScaleData(0.15f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D))
                        .addData(new ParticleMetaData(20, false, 0))
                        .add(this.level(), this.getX(this.random.nextDouble() * 3 - 1.5),
                                this.getY(this.random.nextDouble() * 3 - 1.5),
                                this.getZ(this.random.nextDouble() * 3 - 1.5));
            }
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            ++this.deathTime;
            if (this.deathTime == 1) {
                serverLevel.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.servant.death").withStyle(ChatFormatting.RED), true);
                this.playSound(FateSounds.SERVANT_DEATH.get(), 1.0F, 1.0F);
            }
            if (this.deathTime == this.maxDeathTick()) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    @Override
    public int maxDeathTick() {
        return 200;
    }

    public void throwItem(boolean main) {
        ThrownItemEntity item = new ThrownItemEntity(this.level(), this);
        item.setWeapon(this.getWeaponToThrowAndReplace(main));
        if (this.getTarget() != null) {
            item.shootAtEntity(this.getTarget(), 1.2f, 7 - this.level().getDifficulty().getId() * 2);
        } else {
            item.shootFromRotation(this, this.getXRot() + 5, this.getYRot(), 0.0F, 1.2f, 1.0F);
        }
        this.playSound(FateSounds.DAGGER_THROW.get(), 1.0F, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F);
        this.level().addFreshEntity(item);
    }

    private ItemStack getWeaponToThrowAndReplace(boolean main) {
        ItemStack weapon;
        if (!main) {
            if (!this.getOffhandItem().isEmpty()) {
                this.offHandCache = this.getOffhandItem();
                this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                weapon = this.offHandCache;
            } else
                weapon = this.getMainHandItem().isEmpty() ? this.mainHandCache.copy() : this.getMainHandItem();
        } else {
            this.mainHandCache = this.getMainHandItem();
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            weapon = this.mainHandCache;
        }
        return weapon.isEmpty() ? new ItemStack(FateItems.ASSASSIN_DAGGER.get()) : weapon.copy();
    }

    @Override
    public EntityWeaponTrailHolder<?> getTrailHolder() {
        return this.trailHolder;
    }

    @Override
    public WeaponTrail weaponTrailEdge(boolean left) {
        return new WeaponTrail(new Vector3f(0, 0, -0.2f), new Vector3f(0, 0, -0.6f));
    }
}