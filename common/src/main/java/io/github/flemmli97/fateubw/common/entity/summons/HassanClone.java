package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.fateubw.common.entity.servant.EntityHassan;
import io.github.flemmli97.fateubw.common.entity.utils.TargetableOpponent;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
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
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HassanClone extends PathfinderMob implements AnimatedEntity, OwnableEntity, AOEAttackEntity, TargetableOpponent {

    public static final ResourceLocation BACKSTAB_MODIFIER = Fate.modRes("hassan_backstab");

    public static final AnimationDefinitionContainer ANIMS = EntityHassan.BUILDER.build();

//    public static final List<WeightedEntry.Wrapper<GoalAttackAction<HassanClone>>> ATTACKS = List.of(
//            WeightedEntry.wrap(new GoalAttackAction<HassanClone>(EntityHassan.DAGGER_1)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
//                    .chain(GoalAttackAction.<HassanClone>chainBuilder(EntityHassan.DAGGER_2, 2, 0.2f, 1)
//                            .or(EntityHassan.DAGGER_3, 2, 0.2f, 1)
//                            .or(EntityHassan.DAGGER_4, 2, 0.16f, 1)
//                            .withChance(0.6f))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<HassanClone>(EntityHassan.DAGGER_1)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
//                    .chain(GoalAttackAction.<HassanClone>chainBuilder(EntityHassan.DAGGER_2, 2, 0.2f, 1)
//                            .or(EntityHassan.DAGGER_3, 2, 0.2f, 1)
//                            .or(EntityHassan.DAGGER_4, 2, 0.16f, 1)
//                            .withChance(0.6f))
//                    .prepare(() -> new WrappedRunner<>(new MoveBehindAttackRunner<>(1))), 11),
//            WeightedEntry.wrap(new GoalAttackAction<HassanClone>(EntityHassan.DAGGER_3)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
//                    .chain(GoalAttackAction.<HassanClone>chainBuilder(EntityHassan.DAGGER_1, 2, 0.2f, 1)
//                            .chain(EntityHassan.DAGGER_2, 2, 0.2f)
//                            .or(EntityHassan.DAGGER_1, 2, 0.2f, 1)
//                            .chain(EntityHassan.DAGGER_4, 2, 0.16f)
//                            .withChance(0.6f))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.2))), 11),
//            WeightedEntry.wrap(new GoalAttackAction<HassanClone>(EntityHassan.TOP_STAB)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1.2))), 8),
//            WeightedEntry.wrap(new GoalAttackAction<HassanClone>(EntityHassan.TOP_STAB)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
//                    .prepare(() -> new WrappedRunner<>(new MoveBehindAttackRunner<>(1.2))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<HassanClone>(EntityHassan.THROW)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
//                    .withCondition(((goal, target, previous) -> goal.distanceToTargetSq > 25 || goal.attacker.getRandom().nextFloat() < 0.5))
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.1))), 13)
//    );
//    public static final List<WeightedEntry.Wrapper<IdleAction<HassanClone>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 6),
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
//    );

    private UUID ownerUUID;
    private EntityHassan owner;

    public final Predicate<LivingEntity> targetPred = Utils.servantTargetPredicate(this);

    private final AnimationHandler<HassanClone> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (anim != null && this.getTarget() != null)
            this.targetPosition = this.getTarget().position();
        if (anim == null || !anim.is(EntityHassan.SUMMON)) {
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

    protected Vec3 targetPosition;

    private ItemStack mainHandCache = ItemStack.EMPTY;
    private ItemStack offHandCache = ItemStack.EMPTY;

    public HassanClone(EntityType<? extends HassanClone> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.goals();
            this.updateAttributes();
        }
    }

    public HassanClone(Level level, EntityHassan entityHassan) {
        this(FateEntities.HASSAN_COPY.get(), level);
        this.setOriginal(entityHassan);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.ASSASSIN_DAGGER.get()));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData data) {
        super.finalizeSpawn(world, difficulty, reason, data);
        this.populateDefaultEquipmentSlots(this.getRandom(), difficulty);
        for (EquipmentSlot type : EquipmentSlot.values())
            this.setDropChance(type, 0);
        if (reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.MOB_SUMMONED) {
            this.getAnimationHandler().setAnimation(EntityHassan.SUMMON);
        }
        return data;
    }

    public void setOriginal(EntityHassan entityHassan) {
        this.ownerUUID = entityHassan.getUUID();
        this.owner = entityHassan;
    }

    protected void goals() {
//        this.goalSelector.addGoal(0, this.attack);
//        this.goalSelector.addGoal(1, new FollowMasterGoal<>(this, 16.0D, 9.0F, 3.0F));
//        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
//        this.goalSelector.addGoal(4, new FloatGoal(this));
//        this.goalSelector.addGoal(7, new OpenDoorGoal(this, true));
//        this.targetSelector.addGoal(0, new TargetOwnerEnemyGoal<>(this));
    }

    private void updateAttributes() {
        AttributeHolderProperties props = DatapackHandler.SERVANT_PROPS.getGeneric(this.getType());
        props.attributes().forEach((att, val) -> {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                inst.setBaseValue(val);
                if (att == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth());
            }
        });
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public EntityHassan getOwner() {
        if ((this.owner == null || !this.owner.isAlive()) && this.getOwnerUUID() != null)
            this.owner = EntityUtils.findFromUUID(EntityHassan.class, this.level(), this.getOwnerUUID());
        return this.owner;
    }

    @Override
    public AnimationHandler<HassanClone> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            if (this.tickCount > 200 && this.isAlive() && (this.getOwner() == null || !this.getOwner().isAlive())) {
                this.hurt(this.damageSources().genericKill(), Integer.MAX_VALUE);
                return;
            }
            if (this.getTarget() == null) {
                if (this.getFirstPassenger() instanceof Mob mob) {
                    if (mob.getTarget() != this.getTarget())
                        this.setTarget(mob.getTarget());
                }
            }
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
        }
        super.tick();
        this.getAnimationHandler().tick();
    }

    public void handleAttack(AnimationState anim) {
        if (anim.is(EntityHassan.THROW)) {
            if (anim.isAt("attack")) {
                this.throwItem(true);
            } else if (anim.isAt(0.84)) {
                this.throwItem(false);
            }
        } else {
            if (anim.is(EntityHassan.SUMMON))
                return;
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.lookAt(this.getTarget(), 60, 90);
            }
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.targetPosition = null;
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
        boolean behind = EntityHassan.behind(this, entity);
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
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(damageSource, damage);
        } else {
            if (damageSource.getEntity() == null || !damageSource.getEntity().getType().is(FateTags.EntityTypes.STRONG_MOB))
                damage *= 0.75;
            if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR) && this.projectileBlockChance(damageSource, damage)) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 1, 1);
                if (damageSource.getDirectEntity() != null)
                    damageSource.getDirectEntity().remove(RemovalReason.KILLED);
                return false;
            }
            return super.hurt(damageSource, Math.min(50, damage));
        }
    }

    public boolean projectileBlockChance(DamageSource damageSource, float damage) {
        return this.random.nextFloat() < (float) this.getAttributeValue(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUUID != null)
            tag.putUUID("Owner", this.ownerUUID);
        tag.put("MainHandCache", this.mainHandCache.save(this.registryAccess(), new CompoundTag()));
        tag.put("OffHandCache", this.offHandCache.save(this.registryAccess(), new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner"))
            this.ownerUUID = tag.getUUID("Owner");
        this.mainHandCache = ItemStack.parseOptional(this.registryAccess(), tag.getCompound("MainHandCache"));
        this.offHandCache = ItemStack.parseOptional(this.registryAccess(), tag.getCompound("OffHandCache"));
    }

    @Override
    protected void tickDeath() {
        if (this.level().isClientSide) {
            for (int i = 0; i < ((int) ((9 / (float) this.maxDeathTick()) * this.deathTime - 1)); i++) {
                this.level().addParticle(new ColoredParticleData(FateParticles.LIGHT.get(), 76 / 255f, 128 / 255f, 207 / 255f, 0.3f, 0.15f), this.getX(this.random.nextDouble() * 3 - 1.5),
                        this.getY(this.random.nextDouble() * 3 - 1.5),
                        this.getZ(this.random.nextDouble() * 3 - 1.5),
                        this.random.nextGaussian() * 0.02D,
                        this.random.nextGaussian() * 0.02D,
                        this.random.nextGaussian() * 0.02D);
            }
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            ++this.deathTime;
            if (this.deathTime == 1) {
                serverLevel.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.servant.death").withStyle(ChatFormatting.RED), true);
                this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 1.0F);
            }
            if (this.deathTime == this.maxDeathTick()) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

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
        this.playSound(SoundEvents.FISHING_BOBBER_THROW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
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

    public void mobAttack(AnimationState anim, LivingEntity target, Consumer<LivingEntity> cons) {
        OrientedBoundingBox obb = this.calculateAttackAABB(anim, this.targetPosition != null || target == null ? this.targetPosition : target.position(), 0.2);
        this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox())).forEach(cons);
        if (!this.level().isClientSide)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
    }

    @Override
    public OrientedBoundingBox prepareAttackBox(String anim, Entity target, double grow, boolean debug) {
        OrientedBoundingBox obb = this.calculateAttackAABB(this.getAnimationHandler().createDefaulted(anim),
                target != null ? target.position() : null, grow);
        if (debug)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTEMPT, this);
        return obb;
    }

    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, @Nullable Vec3 target, double grow) {
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
        if (anim.is(EntityHassan.DAGGER_1)) {
            width += 0.5;
            length += 0.4;
        }
        if (anim.is(EntityHassan.DAGGER_2, EntityHassan.DAGGER_3)) {
            width += 0.7;
            length += 0.4;
        }
        if (anim.is(EntityHassan.DAGGER_4)) {
            width += 0.3;
            length += 0.8;
        }
        if (anim.is(EntityHassan.TOP_STAB)) {
            width += 0.2;
            length += 0.8;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public Predicate<LivingEntity> validTargetPredicate() {
        return this.targetPred;
    }
}