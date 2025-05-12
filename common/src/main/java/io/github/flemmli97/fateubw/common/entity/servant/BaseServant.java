package io.github.flemmli97.fateubw.common.entity.servant;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.StandingVehicle;
import io.github.flemmli97.fateubw.common.entity.TargetableOpponent;
import io.github.flemmli97.fateubw.common.entity.ai.FollowMasterGoal;
import io.github.flemmli97.fateubw.common.entity.ai.HurtByTargetPredicateGoal;
import io.github.flemmli97.fateubw.common.entity.ai.StandStillGoal;
import io.github.flemmli97.fateubw.common.entity.ai.TargetNoneGoal;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityTrailHolder;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityTrailHolderProvider;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AoeAttackEntity;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.ai.MoveControllerPlus;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BaseServant extends PathfinderMob implements IAnimated, OwnableEntity, AoeAttackEntity, TargetableOpponent, EntityTrailHolderProvider {

    public static final TicketType<ChunkPos> TRACKINGTICKET = TicketType.create("servant", Comparator.comparingLong(ChunkPos::toLong), 5);

    protected static final EntityDataAccessor<Boolean> SHOW_SERVANT = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> STATIONARY = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.BYTE);

    protected static final Vector4f SUMMON_COLOR = new Vector4f(60 / 255f, 118 / 255f, 199 / 255f, 0.8f);
    //Mana
    private int servantMana = 100, manaRegenCounter;
    private boolean died = false;
    protected int combatTick;
    protected boolean canUseNP, critHealth;
    protected boolean disableChunkload = true, chunkTracked;
    public boolean forcedNP;

    protected EnumServantUpdate commandBehaviour = EnumServantUpdate.NORMAL;

    //PlayerUUID
    private Player owner;

    private final TranslatableComponent hogou;

    private final ServantProperties prop;

    public final Predicate<LivingEntity> targetPred = Utils.servantTargetPredicate(this);

    public final Predicate<LivingEntity> retaliatePred = (target) -> {
        if (target == this)
            return false;
        if (target instanceof BaseServant)
            return !Utils.inSameTeam(BaseServant.this, (BaseServant) target);
        if (target instanceof ServerPlayer)
            return target != BaseServant.this.getOwner() && !Utils.inSameTeam((ServerPlayer) target, BaseServant.this);
        return true;
    };

    public NearestAttackableTargetGoal<BaseServant> targetServant = new NearestAttackableTargetGoal<>(this, BaseServant.class, 10, true, true, this.targetPred);
    public NearestAttackableTargetGoal<Player> targetPlayer = new NearestAttackableTargetGoal<>(this, Player.class, 20, true, true, this.targetPred);
    public NearestAttackableTargetGoal<Mob> targetMob = new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, true, this.targetPred);

    public FollowMasterGoal<BaseServant> follow = new FollowMasterGoal<>(this, 16.0D, 9.0F, 3.0F, BaseServant::isStaying);
    public HurtByTargetPredicateGoal targetHurt = new HurtByTargetPredicateGoal(this, this.retaliatePred);
    public MoveTowardsRestrictionGoal restrictArea = new MoveTowardsRestrictionGoal(this, 1.0D);
    public WaterAvoidingRandomStrollGoal wander = new WaterAvoidingRandomStrollGoal(this, 1.0D);
    protected Vec3 targetPosition;

    private int moveTick;

    public static final int MOVE_TICK_MAX = 3;

    private final List<ServerPlayer> tracked = new ArrayList<>();
    private boolean addToOwner;
    private boolean initAnim;

    private final EntityTrailHolder<BaseServant> trailHolder = new EntityTrailHolder<>(this);

    public BaseServant(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new MoveControllerPlus(this);
        this.xpReward = 35;
        ResourceLocation id = Registry.ENTITY_TYPE.getKey(this.getType());
        this.prop = DatapackHandler.getServantProp(id);
        if (!level.isClientSide) {
            this.goals();
            this.updateAttributes();
        }
        this.hogou = new TranslatableComponent(id + ".hogou");
    }

    protected void goals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new StandStillGoal(this));
        this.goalSelector.addGoal(2, this.follow);
        this.goalSelector.addGoal(3, this.restrictArea);
        this.goalSelector.addGoal(4, this.wander);
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new OpenDoorGoal(this, true));
        this.targetSelector.addGoal(0, new TargetNoneGoal(this));
        this.targetSelector.addGoal(1, this.targetHurt);
        this.targetSelector.addGoal(2, this.targetServant);
        this.targetSelector.addGoal(3, this.targetPlayer);
    }

    //=========Servant specific data
    public ServantProperties props() {
        return this.prop;
    }

    public Component nobelPhantasm() {
        return this.hogou;
    }

    @Override
    public Component getTypeName() {
        if (this.level.isClientSide && !this.showServant())
            return new TextComponent("UNKNOWN");
        return super.getTypeName();
    }

    public Component getRealName() {
        return super.getTypeName();
    }

    /**
     * 5 max atm
     */
    public String[] specialCommands() {
        return null;
    }

    public void doSpecialCommand(String s) {
    }

    //=====Client-Server sync

    public boolean showServant() {
        return this.entityData.get(SHOW_SERVANT);
    }

    public boolean isStaying() {
        return this.entityData.get(STATIONARY);
    }

    public void setStaying(boolean stay) {
        this.entityData.set(STATIONARY, stay);
    }

    public void revealServant() {
        this.entityData.set(SHOW_SERVANT, true);
    }

    //=====Init

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATIONARY, false);
        this.entityData.define(SHOW_SERVANT, false);
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(MOVE_FLAGS, (byte) 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData data, CompoundTag nbt) {
        super.finalizeSpawn(world, difficulty, reason, data, nbt);
        this.populateDefaultEquipmentSlots(difficulty);
        for (EquipmentSlot type : EquipmentSlot.values())
            this.setDropChance(type, 0);
        this.setLeftHanded(false);
        if (this.getSummonAnimation() != null) {
            if (reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.MOB_SUMMONED) {
                this.getAnimationHandler().setAnimation(this.getSummonAnimation());
            }
        }
        return data;
    }

    protected AnimatedAction getSummonAnimation() {
        return null;
    }

    public float getSummonProgress(float partialTicks) {
        AnimatedAction summon = this.getSummonAnimation();
        if (summon != null && this.getAnimationHandler().isCurrent(summon)) {
            return this.getAnimationHandler().getAnimation().progress(partialTicks);
        }
        return -1;
    }

    public Vector4f summonColor() {
        return SUMMON_COLOR;
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(ModAttributes.MAGIC_ATTACK.get()).add(ModAttributes.MAGIC_RESISTANCE.get())
                .add(ModAttributes.PROJECTILE_BLOCK_CHANCE.get()).add(ModAttributes.PROJECTILE_RESISTANCE.get());
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.prop.health());
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.prop.strength());
        this.getAttribute(Attributes.ARMOR).setBaseValue(this.prop.armor());
        this.getAttribute(ModAttributes.MAGIC_ATTACK.get()).setBaseValue(this.prop.magic());
        this.getAttribute(ModAttributes.MAGIC_RESISTANCE.get()).setBaseValue(this.prop.magicRes());
        this.getAttribute(ModAttributes.PROJECTILE_BLOCK_CHANCE.get()).setBaseValue(this.prop.projectileBlockChance());
        this.getAttribute(ModAttributes.PROJECTILE_RESISTANCE.get()).setBaseValue(this.prop.projectileProt());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.prop.moveSpeed());
    }

    //=====Mana stuff

    public boolean useMana(float amount) {
        if (this.servantMana < amount) {
            return false;
        } else {
            this.servantMana -= amount;
            this.manaRegenCounter = 40;
            return true;
        }
    }

    protected void regenMana() {
        if (this.canUseNP && this.servantMana < 100 && --this.manaRegenCounter <= 0) {
            this.servantMana += 1;
            this.manaRegenCounter = 10;
        }
    }

    public int getMana() {
        return this.servantMana;
    }

    public boolean canUseNP() {
        return this.canUseNP;
    }

    //=====Player-Owner handling

    /**
     * Can return null despite having an owner if player is offline
     */
    @Override
    public Player getOwner() {
        if (this.owner != null && this.owner.isAlive())
            return this.owner;
        if (this.hasOwner()) {
            if (this.getServer() != null)
                this.setOwner(this.getServer().getPlayerList().getPlayer(this.entityData.get(OWNER_UUID).get()));
            else
                this.setOwner(this.level.getPlayerByUUID(this.entityData.get(OWNER_UUID).get()));
        }
        return this.owner;
    }

    public boolean hasOwner() {
        return this.entityData.get(OWNER_UUID).isPresent();
    }

    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    public void setOwner(Player player) {
        if (player != null) {
            this.entityData.set(OWNER_UUID, Optional.of(player.getUUID()));
        } else
            this.entityData.set(OWNER_UUID, Optional.empty());
        this.owner = player;
        this.disableChunkload = !this.hasOwner();
    }

    //=====NBT
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.hasOwner())
            tag.putUUID("Owner", this.entityData.get(OWNER_UUID).get());
        tag.putBoolean("CanUseNP", this.canUseNP);
        tag.putInt("Death", this.deathTime);
        tag.putBoolean("IsDead", this.died);
        tag.putString("Command", this.commandBehaviour.toString());
        tag.putInt("Mana", this.servantMana);
        tag.putBoolean("HealthMessage", this.critHealth);
        tag.putBoolean("Revealed", this.showServant());
        tag.putBoolean("DisableChunkload", this.disableChunkload);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Owner"))
            this.entityData.set(OWNER_UUID, Optional.of(tag.getUUID("Owner")));
        this.canUseNP = tag.getBoolean("CanUseNP");
        this.deathTime = tag.getInt("Death");
        this.died = tag.getBoolean("IsDead");
        try {
            this.updateAI(EnumServantUpdate.valueOf(tag.getString("Command")));
        } catch (IllegalArgumentException ignored) {
        }
        this.servantMana = tag.getInt("Mana");
        this.critHealth = tag.getBoolean("HealthMessage");
        this.entityData.set(SHOW_SERVANT, tag.getBoolean("Revealed"));
        this.disableChunkload = tag.getBoolean("DisableChunkload");
    }

    //=====Entity AI updating

    public abstract Goal getAttackAI();

    public void updateAI(EnumServantUpdate behaviour) {
        this.commandBehaviour = behaviour;
        this.goalSelector.addGoal(0, this.getAttackAI());
        switch (behaviour) {
            case NORMAL -> {
                this.targetSelector.removeGoal(this.targetMob);
                this.targetSelector.addGoal(1, this.targetHurt);
                this.targetSelector.addGoal(2, this.targetServant);
                this.targetSelector.addGoal(3, this.targetPlayer);
            }
            case AGGRESSIVE -> {
                this.targetSelector.addGoal(1, this.targetHurt);
                this.targetSelector.addGoal(2, this.targetServant);
                this.targetSelector.addGoal(3, this.targetPlayer);
                this.targetSelector.addGoal(4, this.targetMob);
            }
            case DEFENSIVE -> {
                this.targetSelector.addGoal(1, this.targetHurt);
                this.targetSelector.removeGoal(this.targetServant);
                this.targetSelector.removeGoal(this.targetPlayer);
                this.targetSelector.removeGoal(this.targetMob);
            }
            case FOLLOW -> {
                this.goalSelector.addGoal(2, this.follow);
                this.setStaying(false);
                this.clearRestriction();
            }
            case STAY -> {
                this.goalSelector.removeGoal(this.getAttackAI());
                this.setStaying(true);
                this.getNavigation().stop();
                this.setTarget(null);
            }
            case GUARD -> {
                this.setStaying(false);
                this.goalSelector.removeGoal(this.follow);
                this.restrictTo(this.getOwner().blockPosition(), 8);
            }
        }
    }

    //=====Living update handling and stuff

    @Override
    public void tick() {
        if (!this.initAnim) {
            this.getAnimationHandler().withChangeListener(anim -> {
                if (anim != null)
                    this.setupAttack(anim);
                return false;
            });
            this.initAnim = true;
        }
        super.tick();
        this.getAnimationHandler().tick();
        this.getTrailHolder().tick();
        if (this.getSummonAnimation() != null && this.getAnimationHandler().isCurrent(this.getSummonAnimation())) {
            this.setDeltaMovement(Vec3.ZERO);
            this.getNavigation().stop();
        }
        if (this.level instanceof ServerLevel serverLevel) {
            this.regenMana();
            this.combatTick = Math.max(0, --this.combatTick);
            if (!this.disableChunkload) {
                if (!this.chunkTracked) {
                    GrailWarHandler.get(serverLevel.getServer()).track(this);
                    this.chunkTracked = true;
                }
                ChunkPos pos = this.chunkPosition();
                ((ServerChunkCache) this.level.getChunkSource()).addRegionTicket(TRACKINGTICKET, pos, 2, pos);
            } else
                this.chunkTracked = false;

            this.getAnimationHandler().runIfNotNull(this::handleAttack);
            if (this.getOwner() instanceof ServerPlayer serverPlayer) {
                if (!this.tracked.contains(serverPlayer)) {
                    if (!this.addToOwner) {
                        this.addEntityOwner(serverPlayer);
                        this.addToOwner = true;
                    }
                    this.updateDataManager(serverPlayer);
                }
            }
            if (this.getTarget() != null && this.getTarget().getVehicle() instanceof LivingEntity)
                this.setTarget((LivingEntity) this.getTarget().getVehicle());
        }
        if (this.getMoveFlag() != MoveType.NONE) {
            this.moveTick = Math.min(MOVE_TICK_MAX, ++this.moveTick);
        } else {
            this.moveTick = Math.max(0, --this.moveTick);
        }
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        if (!this.canBeControlledByRider() && this.isMoving() && this.isAlive()) {
            double d0 = this.getMoveControl().getSpeedModifier();
            MoveType move;
            if (d0 > 1) {
                move = MoveType.RUN;
            } else if (d0 <= 0.8) {
                move = MoveType.SNEAK;
            } else {
                move = MoveType.WALK;
            }
            if (this.isImmobile())
                move = MoveType.NONE;
            this.setMovingFlag(move);
        } else {
            this.setMovingFlag(MoveType.NONE);
            this.setShiftKeyDown(false);
            this.setSprinting(false);
        }
    }

    protected boolean isMoving() {
        return this.getDeltaMovement().x != 0 || this.getDeltaMovement().z != 0;
    }

    public float interpolatedMoveTick(float partialTicks) {
        return Mth.clamp((this.moveTick + (this.getMoveFlag() != MoveType.NONE ? partialTicks : -partialTicks)) / (float) MOVE_TICK_MAX, 0, 1);
    }

    public void setMovingFlag(MoveType type) {
        this.entityData.set(MOVE_FLAGS, (byte) type.ordinal());
    }

    public MoveType getMoveFlag() {
        return MoveType.values()[this.entityData.get(MOVE_FLAGS)];
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || (this.getSummonAnimation() != null && this.getAnimationHandler().isCurrent(this.getSummonAnimation()));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (this.getSummonAnimation() != null && this.getAnimationHandler().isCurrent(this.getSummonAnimation()))
            return true;
        return super.isInvulnerableTo(source);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        this.tracked.add(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        this.tracked.remove(player);
        this.addToOwner = false;
    }

    @Override
    public double getMyRidingOffset() {
        return StandingVehicle.shouldSit(this) ? -0.35 : 0;
    }

    private void addEntityOwner(ServerPlayer serverPlayer) {
        serverPlayer.connection.send(this.getAddEntityPacket());
        serverPlayer.connection.send(new ClientboundSetEntityDataPacket(this.getId(), this.entityData, true));
        ArrayList<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayList();
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = this.getItemBySlot(equipmentSlot);
            if (itemStack.isEmpty()) continue;
            list.add(Pair.of(equipmentSlot, itemStack.copy()));
        }
        if (!list.isEmpty()) {
            serverPlayer.connection.send(new ClientboundSetEquipmentPacket(this.getId(), list));
        }
    }

    private void updateDataManager(ServerPlayer player) {
        SynchedEntityData entitydatamanager = this.getEntityData();
        if (entitydatamanager.isDirty()) {
            player.connection.send(new ClientboundSetEntityDataPacket(this.getId(), entitydatamanager, false));
        }
        Set<AttributeInstance> set = this.getAttributes().getDirtyAttributes();
        if (!set.isEmpty()) {
            player.connection.send(new ClientboundUpdateAttributesPacket(this.getId(), set));
        }
    }

    //=====Death Handling

    public int getDeathTick() {
        return this.deathTime;
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Override
    public boolean isAlive() {
        return !this.died && super.isAlive();
    }

    @Override
    protected void tickDeath() {
        this.died = true;
        if (this.level.isClientSide) {
            for (int i = 0; i < ((int) ((9 / (float) this.maxDeathTick()) * this.deathTime - 1)); i++) {
                this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 76 / 255f, 128 / 255f, 207 / 255f, 0.3f, 0.15f), this.getX(this.random.nextDouble() * 3 - 1.5),
                        this.getY(this.random.nextDouble() * 3 - 1.5),
                        this.getZ(this.random.nextDouble() * 3 - 1.5),
                        this.random.nextGaussian() * 0.02D,
                        this.random.nextGaussian() * 0.02D,
                        this.random.nextGaussian() * 0.02D);
            }
        }
        ++this.deathTime;
        if (this.level instanceof ServerLevel serverLevel) {
            if (this.deathTime == 1) {
                //if(this.getLastDamageSource()!=DamageSource.OUT_OF_WORLD)
                this.level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.servant.death").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 1.0F);
                GrailWarHandler.get(serverLevel.getServer()).removeServant(this);
                this.disableChunkload = true;
                this.getAnimationHandler().setAnimation(this.deathAnim());
            }

            if (this.deathTime > 15 && this.deathTime % 5 == 0 && (this.lastHurtByPlayerTime > 0 || this.isAlwaysExperienceDropper()) && this.shouldDropExperience() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                int exp = this.xpReward;
                int splitExp;
                while (exp > 0) {
                    splitExp = ExperienceOrb.getExperienceValue(exp);
                    exp -= splitExp;
                    this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY(), this.getZ(), splitExp));
                }
            }
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
            if (this.deathTime >= this.maxDeathTick() && (anim == null || anim.done(0))) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    public AnimatedAction deathAnim() {
        return null;
    }

    public boolean transparentOnDeath() {
        return true;
    }

    public int maxDeathTick() {
        return 200;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.level instanceof ServerLevel serverLevel)
            GrailWarHandler.get(serverLevel.getServer()).untrack(this);
    }

    //=====Entity attack etc.

    public void setupAttack(AnimatedAction anim) {
        if (this.getTarget() != null) {
            this.targetPosition = this.getTarget().position();
        }
    }

    public void handleAttack(AnimatedAction anim) {
        if (anim.is(this.getSummonAnimation()))
            return;
        this.getNavigation().stop();
        if (this.getTarget() != null) {
            this.lookAtNow(this.getTarget(), 60, 90);
        }
        if (anim.isAt("attack")) {
            this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
            this.targetPosition = null;
        }
    }

    public void lookAtNow(Entity entity, float maxYRotIncrease, float maxXRotIncrease) {
        super.lookAt(entity, maxYRotIncrease, maxXRotIncrease);
        this.yHeadRot = this.getYRot();
        this.yBodyRot = this.yHeadRot;
    }

    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        OrientedBoundingBox obb = this.calculateAttackAABB(anim, this.targetPosition != null || target == null ? this.targetPosition : target.position(), 0.2);
        this.level.getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox())).forEach(e -> {
            if (e.getLastHurtByMob() == this)
                e.invulnerableTime = 0;
            cons.accept(e);
        });
        if (!this.level.isClientSide)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
    }

    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        float yRot = this.getYRot();
        float xRot = this.getXRot();
        Vec3 dir;
        if (target != null && !this.canBeControlledByRider()) {
            dir = target.subtract(this.position()).normalize();
            float[] xYRot = MathsHelper.XYRotFrom(dir);
            yRot = xYRot[0];
            xRot = xYRot[1];
        } else if (this.getControllingPassenger() instanceof Player player) {
            yRot = player.getYRot();
            xRot = player.getXRot();
        }
        double off = this.getBbHeight() * 0.5;
        return new OrientedBoundingBox(this.attackBB(anim)
                .inflate(grow, 0, grow)
                .move(0, -off, grow), yRot, -Mth.clamp(xRot, -15, 15), this.position().add(0, off, 0));
    }

    @Override
    public OrientedBoundingBox prepareAttackBox(AnimatedAction anim, LivingEntity target, double grow, boolean debug) {
        OrientedBoundingBox obb = this.calculateAttackAABB(anim, target.position(), grow);
        if (debug)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTEMPT, this);
        return obb;
    }

    public AABB attackBB(AnimatedAction anim) {
        double range = 1;
        return new AABB(-range * 0.5, -0.02, 0, range * 0.5, this.getBbHeight() + 0.02, range);
    }

    @Override
    public Predicate<LivingEntity> validTargetPredicate() {
        return this.targetPred;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        if (!this.isInvulnerableTo(damageSrc)) {
            damageAmount = Platform.INSTANCE.onLivingHurt(this, damageSrc, damageAmount);
            if (damageAmount <= 0) return;
            if (damageSrc.isProjectile())
                damageAmount = Utils.projectileReduce(this, damageAmount);
            damageAmount = this.getDamageAfterArmorAbsorb(damageSrc, damageAmount);
            if (damageSrc.isMagic())
                damageAmount = Utils.getDamageAfterMagicAbsorb(this, damageAmount);
            damageAmount = this.getDamageAfterMagicAbsorb(damageSrc, damageAmount);
            float f = damageAmount;
            damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
            damageAmount = Platform.INSTANCE.onLivingDamage(this, damageSrc, damageAmount);

            if (damageAmount != 0.0F) {
                float f1 = this.getHealth();
                this.getCombatTracker().recordDamage(damageSrc, f1, damageAmount);
                this.setHealth(f1 - damageAmount);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
                this.combatTick = 300;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return this.preAttackEntityFrom(damageSource, damage);
        } else {
            if (damageSource.getEntity() == null || !damageSource.getEntity().getType().is(FateTags.STRONG_MOB))
                damage *= 0.5;

            if (damageSource.isProjectile() && !damageSource.isBypassArmor() && this.projectileBlockChance(damageSource, damage)) {
                this.level.playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 1, 1);
                if (damageSource.getDirectEntity() != null)
                    damageSource.getDirectEntity().remove(RemovalReason.KILLED);
                return false;
            }
            return this.preAttackEntityFrom(damageSource, Math.min(50, damage));
        }
    }

    protected boolean preAttackEntityFrom(DamageSource damageSource, float par2) {
        return super.hurt(damageSource, par2);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return this.mobHurtTarget(entity);
    }

    protected boolean mobHurtTarget(Entity target) {
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float knockback = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity living) {
            damage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), living.getMobType());
            knockback += EnchantmentHelper.getKnockbackBonus(this);
        }
        int fireAspect = EnchantmentHelper.getFireAspect(this);
        if (fireAspect > 0) {
            target.setSecondsOnFire(fireAspect * 4);
        }
        damage *= this.damageModifier(target);
        boolean bl = target.hurt(this.damageSourceAttack(target), damage);
        if (bl) {
            if (knockback > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity) target).knockback(knockback * 0.5F, Mth.sin(this.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(this.getYRot() * Mth.DEG_TO_RAD));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }
            if (target instanceof Player player) {
                this.tryDisableShield(player, this.getMainHandItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
            }
            this.doEnchantDamageEffects(this, target);
            this.setLastHurtMob(target);
        }
        return bl;
    }

    public float damageModifier(Entity target) {
        return 1;
    }

    protected DamageSource damageSourceAttack(Entity target) {
        return DamageSource.mobAttack(this);
    }

    protected void tryDisableShield(Player player, ItemStack stack, ItemStack playerUseItem) {
        if (!stack.isEmpty() && !playerUseItem.isEmpty() && stack.getItem() instanceof AxeItem && playerUseItem.is(Items.SHIELD)) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.level.broadcastEntityEvent(player, (byte) 30);
            }
        }
    }

    public void onKillOrder(Player player, boolean success) {
        this.hurt(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
        player.sendMessage(new TranslatableComponent("fateubw.chat.command.kill").withStyle(ChatFormatting.RED), Util.NIL_UUID);
    }

    public void onForfeit(Player player) {

    }

    public boolean projectileBlockChance(DamageSource damageSource, float damage) {
        return this.random.nextFloat() < (float) this.getAttributeValue(ModAttributes.PROJECTILE_BLOCK_CHANCE.get());
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        super.knockback(strength * 0.75, xRatio, zRatio);
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return SpawnEgg.fromType(this.getType()).map(ItemStack::new).orElse(null);
    }

    public boolean flipAnimation() {
        return false;
    }

    @Override
    public EntityTrailHolder<BaseServant> getTrailHolder() {
        return this.trailHolder;
    }

    public enum MoveType {
        NONE,
        WALK,
        RUN,
        SNEAK
    }
}
