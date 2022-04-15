package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.config.ServantProperties;
import io.github.flemmli97.fateubw.common.entity.IServantMinion;
import io.github.flemmli97.fateubw.common.entity.servant.ai.FollowMasterGoal;
import io.github.flemmli97.fateubw.common.entity.servant.ai.RetaliateGoal;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.utils.EnumServantType;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.ai.MoveControllerPlus;
import io.github.flemmli97.tenshilib.common.utils.NBTUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class BaseServant extends PathfinderMob implements IAnimated, OwnableEntity {

    //Mana
    private int servantMana, antiRegen, counter;
    private boolean died = false;
    protected int combatTick;
    protected boolean canUseNP, critHealth;
    protected boolean disableChunkload = true, chunkTracked;
    /*private int , attackTimer, , ;*/
    public boolean forcedNP;

    /**
     * 0 = normal, 1 = aggressive, 2 = defensive, 3 = follow, 4 = stay, 5 = guard an area
     */
    protected EnumServantUpdate commandBehaviour = EnumServantUpdate.NORMAL;

    private final EnumServantType servantType;
    //PlayerUUID
    private Player owner;

    private final TranslatableComponent hogou;

    protected static final EntityDataAccessor<Boolean> showServant = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> stationary = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Optional<UUID>> ownerUUID = SynchedEntityData.defineId(BaseServant.class, EntityDataSerializers.OPTIONAL_UUID);

    private final ServantProperties prop;

    private final Predicate<LivingEntity> targetPred = (target) -> {
        if (target instanceof BaseServant)
            return !Utils.inSameTeam(BaseServant.this, (BaseServant) target);
        if (target instanceof ServerPlayer)
            return target != BaseServant.this.getOwner() && !Utils.inSameTeam((ServerPlayer) target, BaseServant.this);
        return target instanceof Enemy;
    };

    public NearestAttackableTargetGoal<BaseServant> targetServant = new NearestAttackableTargetGoal<>(this, BaseServant.class, 10, true, true, this.targetPred);
    public NearestAttackableTargetGoal<Player> targetPlayer = new NearestAttackableTargetGoal<>(this, Player.class, 0, true, true, this.targetPred);
    public NearestAttackableTargetGoal<Mob> targetMob = new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, true, this.targetPred);

    public FollowMasterGoal<BaseServant> follow = new FollowMasterGoal<>(this, 16.0D, 9.0F, 3.0F, BaseServant::isStaying);
    public RetaliateGoal targetHurt = new RetaliateGoal(this);
    public MoveTowardsRestrictionGoal restrictArea = new MoveTowardsRestrictionGoal(this, 1.0D);
    public WaterAvoidingRandomStrollGoal wander = new WaterAvoidingRandomStrollGoal(this, 1.0D);

    public BaseServant(EntityType<? extends BaseServant> entityType, Level world, String hogou) {
        super(entityType, world);
        this.moveControl = new MoveControllerPlus(this);
        this.xpReward = 35;
        this.prop = Config.Common.attributes.getOrDefault(PlatformUtils.INSTANCE.entities().getIDFrom(entityType).toString(), ServantProperties.def);
        if (world != null && !world.isClientSide) {
            this.goals();
            this.updateAttributes();
        }
        this.servantType = ModEntities.get(PlatformUtils.INSTANCE.entities().getIDFrom(entityType));
        this.hogou = new TranslatableComponent(hogou);
    }

    protected void goals() {
        this.goalSelector.addGoal(1, this.follow);
        this.goalSelector.addGoal(2, this.restrictArea);
        this.goalSelector.addGoal(3, this.wander);
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new OpenDoorGoal(this, true));
        this.targetSelector.addGoal(0, this.targetHurt);
        this.targetSelector.addGoal(1, this.targetServant);
        this.targetSelector.addGoal(2, this.targetPlayer);
    }

    //=========Servant specific data
    public ServantProperties props() {
        return this.prop;
    }

    public Component nobelPhantasm() {
        return this.hogou;
    }

    public EnumServantType getServantType() {
        return this.servantType;
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

    public boolean attacksFromMount() {
        return true;
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
        return this.entityData.get(showServant);
    }

    public boolean isStaying() {
        return this.entityData.get(stationary);
    }

    public void setStaying(boolean stay) {
        this.entityData.set(stationary, stay);
    }

    public void revealServant() {
        this.entityData.set(showServant, true);
    }

    /**
     * Cooldown between each attack. (The time after an attack has fully finished)
     */
    public int attackCooldown(AnimatedAction anim) {
        return 0;
    }

    //=====Init

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(stationary, false);
        this.entityData.define(showServant, false);
        this.entityData.define(ownerUUID, Optional.empty());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData data, CompoundTag nbt) {
        super.finalizeSpawn(world, difficulty, reason, data, nbt);
        this.populateDefaultEquipmentSlots(difficulty);
        for (EquipmentSlot type : EquipmentSlot.values())
            this.setDropChance(type, 0);
        this.setLeftHanded(false);
        return data;
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createMonsterAttributes().add(ModAttributes.MAGIC_RESISTANCE.get()).add(ModAttributes.PROJECTILE_BLOCKCHANCE.get()).add(ModAttributes.PROJECTILE_RESISTANCE.get());
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.prop.health());
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.prop.strength());
        this.getAttribute(Attributes.ARMOR).setBaseValue(this.prop.armor());
        this.getAttribute(ModAttributes.MAGIC_RESISTANCE.get()).setBaseValue(this.prop.magicRes());
        this.getAttribute(ModAttributes.PROJECTILE_BLOCKCHANCE.get()).setBaseValue(this.prop.projectileBlockChance());
        this.getAttribute(ModAttributes.PROJECTILE_RESISTANCE.get()).setBaseValue(this.prop.projectileProt());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.prop.moveSpeed());//default 0.3
    }

    //=====Mana stuff

    public boolean useMana(float amount) {
        if (this.servantMana < amount) {
            return false;
        } else {
            this.servantMana -= amount;
            this.antiRegen = 30;
            return true;
        }
    }

    private void regenMana() {
        if (this.canUseNP && this.servantMana < 100 && --this.antiRegen <= 0 && ++this.counter >= 20) {
            this.servantMana += 1;
            this.counter = 0;
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
        if (this.hasOwner())
            this.owner = this.level.getPlayerByUUID(this.entityData.get(ownerUUID).get());
        return this.owner;
    }

    public boolean hasOwner() {
        return this.entityData.get(ownerUUID).isPresent();
    }

    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(ownerUUID).orElse(null);
    }

    public void setOwner(Player player) {
        if (player != null) {
            this.entityData.set(ownerUUID, Optional.of(player.getUUID()));
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.setServant(this));
        } else
            this.entityData.set(ownerUUID, Optional.empty());
        this.owner = player;
        this.disableChunkload = !this.hasOwner();
    }

    //=====NBT
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.hasOwner())
            tag.putUUID("Owner", this.entityData.get(ownerUUID).get());
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
            this.entityData.set(ownerUUID, Optional.of(tag.getUUID("Owner")));
        this.canUseNP = tag.getBoolean("CanUseNP");
        this.deathTime = tag.getInt("Death");
        this.died = tag.getBoolean("IsDead");
        this.updateAI(NBTUtils.get(EnumServantUpdate.class, tag, "Command", EnumServantUpdate.NORMAL));
        this.servantMana = tag.getInt("Mana");
        this.critHealth = tag.getBoolean("HealthMessage");
        this.entityData.set(showServant, tag.getBoolean("Revealed"));
        this.disableChunkload = tag.getBoolean("DisableChunkload");
    }

    //=====Entity AI updating

    /**
     * @param behaviour 0 = normal, 1 = aggressive, 2 = defensive, 3 = follow, 4 = stay, 5 = guard an area
     */
    public void updateAI(EnumServantUpdate behaviour) {
        this.commandBehaviour = behaviour;
        switch (behaviour) {
            case NORMAL -> {
                this.targetSelector.removeGoal(this.targetMob);
                this.targetSelector.addGoal(0, this.targetHurt);
                this.targetSelector.addGoal(1, this.targetServant);
            }
            case AGGRESSIVE -> {
                this.targetSelector.addGoal(0, this.targetHurt);
                this.targetSelector.addGoal(1, this.targetServant);
                this.targetSelector.addGoal(3, this.targetMob);
            }
            case DEFENSIVE -> {
                this.targetSelector.addGoal(0, this.targetHurt);
                this.targetSelector.removeGoal(this.targetServant);
                this.targetSelector.removeGoal(this.targetMob);
            }
            case FOLLOW -> {
                this.targetSelector.addGoal(0, this.targetHurt);
                this.targetSelector.addGoal(1, this.targetServant);
                this.goalSelector.addGoal(1, this.follow);
                this.goalSelector.addGoal(3, this.wander);
                this.setStaying(false);
                this.hasRestriction();
            }
            case STAY -> {
                this.targetSelector.removeGoal(this.targetHurt);
                this.targetSelector.removeGoal(this.targetServant);
                this.targetSelector.removeGoal(this.targetMob);
                this.goalSelector.removeGoal(this.follow);
                this.goalSelector.removeGoal(this.wander);
                this.setStaying(true);
                this.getNavigation().stop();
                this.setTarget(null);
                this.hasRestriction();
            }
            case GUARD -> {
                this.setStaying(false);
                this.goalSelector.removeGoal(this.follow);
                this.goalSelector.addGoal(3, this.wander);
                this.restrictTo(this.getOwner().blockPosition(), 8);
            }
        }
    }

    //=====Living update handling and stuff

    @Override
    public void tick() {
        super.tick();
        this.getAnimationHandler().tick();
        if (this.level instanceof ServerLevel serverLevel) {
            this.regenMana();
            this.combatTick = Math.max(0, --this.combatTick);
            if (!this.disableChunkload) {
                if (!this.chunkTracked) {
                    GrailWarHandler.get(serverLevel.getServer()).track(this);
                    this.chunkTracked = true;
                }
                ChunkPos pos = this.chunkPosition();
                ((ServerChunkCache) this.level.getChunkSource()).addRegionTicket(TicketType.UNKNOWN, pos, 1, pos);
            } else
                this.chunkTracked = false;
            if (this.getOwner() != null && !this.tracked.contains(this.getOwner()))
                this.updateDataManager((ServerPlayer) this.getOwner());
            if (this.getTarget() != null && this.getTarget().getVehicle() instanceof LivingEntity)
                this.setTarget((LivingEntity) this.getTarget().getVehicle());
        }
    }

    private final List<ServerPlayer> tracked = new ArrayList<>();

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        this.tracked.add(player);
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
        //set.clear();
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
		/*for(int i = 0; i < ((int)((7/(float)this.maxDeathTick())*this.deathTicks-1)); i++)
			Particles.spawnParticle(ModRender.particleFade, this.world, this.posX + (this.rand.nextDouble() - 0.5D) * (this.width+3),
    		this.posY + this.rand.nextDouble() * (this.height+1.5), 
    		this.posZ + (this.rand.nextDouble() - 0.5D) * (this.width+3), 
    		this.rand.nextGaussian() * 0.02D, 
    		this.rand.nextGaussian() * 0.02D,
    		this.rand.nextGaussian() * 0.02D);*/
        if (this.level instanceof ServerLevel serverLevel) {
            ++this.deathTime;
            if (this.deathTime == 1) {
                //if(this.getLastDamageSource()!=DamageSource.OUT_OF_WORLD)
                this.level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("chat.servant.death").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
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
            if (this.deathTime >= this.maxDeathTick() && (anim == null || anim.getTick() >= anim.getLength())) {
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

    public abstract boolean canUse(AnimatedAction anim, AttackType type);

    public AnimatedAction getRandomAttack(AttackType type) {
        List<AnimatedAction> matching = new ArrayList<>();
        for (AnimatedAction anim : this.getAnimationHandler().getAnimations()) {
            if (this.canUse(anim, type))
                matching.add(anim);
        }
        if (matching.isEmpty())
            return null;
        return matching.get(this.random.nextInt(matching.size()));
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
            if (!(damageSource.getEntity() instanceof BaseServant || damageSource.getEntity() instanceof IServantMinion))
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
        float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (entity instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entity).getMobType());
            f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            entity.setSecondsOnFire(i * 4);
        }
        f *= this.damageModifier(entity);
        boolean flag = entity.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            if (f1 > 0.0F && entity instanceof LivingEntity) {
                ((LivingEntity) entity).knockback(f1 * 0.5F, Mth.sin(this.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float) Math.PI / 180F)));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            /*if (entity instanceof PlayerEntity) {
                PlayerEntity playerentity = (PlayerEntity)entity;
                this.disablePlayerShield(playerentity, this.getHeldItemMainhand(), playerentity.isHandActive() ? playerentity.getActiveItemStack() : ItemStack.EMPTY);
            }*/

            this.doEnchantDamageEffects(this, entity);
            this.setLastHurtMob(entity);
        }

        return flag;
    }

    public float damageModifier(Entity target) {
        return 1;
    }

    public void onKillOrder(Player player, boolean success) {
        this.hurt(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
        player.sendMessage(new TranslatableComponent("chat.command.kill").withStyle(ChatFormatting.RED), Util.NIL_UUID);
    }

    public void onForfeit(Player player) {

    }

    public boolean projectileBlockChance(DamageSource damageSource, float damage) {
        return this.random.nextFloat() < (float) this.getAttributeValue(ModAttributes.PROJECTILE_BLOCKCHANCE.get());
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        super.knockback(strength * 0.75, xRatio, zRatio);
    }

    public enum AttackType {
        RANGED,
        MELEE,
        NP
    }
}
