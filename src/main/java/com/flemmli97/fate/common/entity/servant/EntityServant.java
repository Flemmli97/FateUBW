package com.flemmli97.fate.common.entity.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.config.ServantProperties;
import com.flemmli97.fate.common.entity.IServantMinion;
import com.flemmli97.fate.common.grail.GrailWarHandler;
import com.flemmli97.fate.common.registry.FateAttributes;
import com.flemmli97.fate.common.utils.EnumServantType;
import com.flemmli97.fate.common.utils.Utils;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.google.common.collect.Lists;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.FollowBoatGoal;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.OpenDoorGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityMetadataPacket;
import net.minecraft.network.play.server.SEntityPropertiesPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.Ticket;
import net.minecraft.world.server.TicketType;
import org.apache.http.util.TextUtils;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class EntityServant extends CreatureEntity implements IAnimated {

	//Mana
	private int servantMana, antiRegen, counter;
	private boolean died = false;
	protected int combatTick;
	protected boolean canUseNP, critHealth;
	protected boolean disableChunkload = true;
	/*private int , attackTimer, , ;*/
	public boolean forcedNP;

	/**
	 * 0 = normal, 1 = aggressive, 2 = defensive, 3 = follow, 4 = stay, 5 = guard an area
	 */
	protected int commandBehaviour;

	private AnimatedAction currentAnim;

	private EnumServantType servantType;
	//PlayerUUID
	private PlayerEntity owner;

	private String hogou;
	private ItemStack[] drops;

	protected static final DataParameter<Boolean> showServant = EntityDataManager.createKey(EntityServant.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> stationary = EntityDataManager.createKey(EntityServant.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<String> ownerUUID = EntityDataManager.createKey(EntityServant.class, DataSerializers.STRING);

	private ServantProperties prop;

	public NearestAttackableTargetGoal<EntityServant> targetServant = new NearestAttackableTargetGoal<>(this, EntityServant.class, 10, true, true,
			(servant) -> servant != null && !Utils.inSameTeam(EntityServant.this, servant));
	public NearestAttackableTargetGoal<PlayerEntity> targetPlayer = new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, true,
			(player) -> player != null && player != EntityServant.this.getOwner() && !Utils.inSameTeam(player, EntityServant.this));
	public NearestAttackableTargetGoal<MobEntity> targetMob = new NearestAttackableTargetGoal<MobEntity>(this, MobEntity.class, 10, true, true,
			(living) -> living != null && !(living instanceof EntityServant) && IMob.VISIBLE_MOB_SELECTOR.apply(living));

	public EntityAIFollowMaster follow = new EntityAIFollowMaster(this, 15.0D, 16.0F, 3.0F);
	public EntityAIRetaliate targetHurt = new EntityAIRetaliate(this);
	public EntityAIMoveTowardsRestriction restrictArea = new EntityAIMoveTowardsRestriction(this, 1.0D);
	public RandomWalkingGoal wander = new RandomWalkingGoal(this, 1.0D);

	public EntityServant(EntityType<? extends EntityServant> entityType, World world, EnumServantType type, String hogou, ItemStack[] drops) {
		super(entityType, world);
		this.experienceValue = 35;
		this.goalSelector.addGoal(0, this.follow);
		this.goalSelector.addGoal(1, this.restrictArea);
		this.goalSelector.addGoal(2, this.wander);
		this.goalSelector.addGoal(3, new SwimGoal(this));
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
		this.targetSelector.addGoal(0, this.targetHurt);
		this.targetSelector.addGoal(1, this.targetServant);
		this.targetSelector.addGoal(2, this.targetPlayer);
		this.prop = Config.Common.attributes.getOrDefault(entityType.getRegistryName().toString(), ServantProperties.def);
		this.updateAttributes();
		this.servantType = type;
		this.drops = drops;
		this.hogou = hogou;
	}

	//=========Servant specifig data
	public ServantProperties props() {
		return this.prop;
	}

	public String nobelPhantasm() {
		return this.hogou;
	}

	public EnumServantType getServantType() {
		return this.servantType;
	}

	public ItemStack[] drops() {
		return this.drops;
	}

	@Override
	public ITextComponent getName() {
		if (this.world.isRemote)
			return new StringTextComponent("UNKNOWN");
		return super.getName();
	}

	public ITextComponent getRealName() {
		return super.getName();
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
		return this.dataManager.get(showServant);
	}

	public boolean isStaying() {
		return this.dataManager.get(stationary);
	}

	public void setStaying(boolean stay) {
		this.dataManager.set(stationary, stay);
	}

	public void revealServant() {
		this.dataManager.set(showServant, true);
	}

	/**
	 * Cooldown between each attack. (The time after an attack has fully finished)
	 */
	public abstract int attackCooldown(AnimatedAction anim);

	//=====Init

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(stationary, false);
		this.dataManager.register(showServant, false);
		this.dataManager.register(ownerUUID, "");
	}

	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData data, CompoundNBT p_213386_5_) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return data;
	}

	public static AttributeModifierMap.MutableAttribute createMobAttributes() {
		return CreatureEntity.createMobAttributes().add(FateAttributes.MAGIC_RESISTANCE).add(FateAttributes.PROJECTILE_BLOCKCHANCE).add(FateAttributes.PROJECTILE_RESISTANCE);
	}

	private void updateAttributes() {
		this.getAttribute(Attributes.GENERIC_MAX_HEALTH).setBaseValue(this.prop.health());
		this.setHealth(this.getMaxHealth());
		this.getAttribute(Attributes.GENERIC_ATTACK_DAMAGE).setBaseValue(this.prop.strength());
		this.getAttribute(Attributes.GENERIC_ARMOR).setBaseValue(this.prop.armor());
		this.getAttribute(FateAttributes.MAGIC_RESISTANCE).setBaseValue(this.prop.magicRes());
		this.getAttribute(FateAttributes.PROJECTILE_BLOCKCHANCE).setBaseValue(this.prop.projectileBlockChance());
		this.getAttribute(FateAttributes.PROJECTILE_RESISTANCE).setBaseValue(this.prop.projectileProt());
		this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(this.prop.moveSpeed());//default 0.3
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
	 *
	 * @return
	 */
	public PlayerEntity getOwner() {
		if (this.owner != null && this.owner.isAlive())
			return this.owner;
		if (this.hasOwner())
			this.owner = this.world.getPlayerByUuid(UUID.fromString(this.dataManager.get(ownerUUID)));
		return this.owner;
	}

	public boolean hasOwner() {
		return !this.dataManager.get(ownerUUID).isEmpty();
	}

	public UUID ownerUUID() {
		if (this.hasOwner())
			return UUID.fromString(this.dataManager.get(ownerUUID));
		return null;
	}

	public void setOwner(PlayerEntity player) {
		if (player != null) {
			this.dataManager.set(ownerUUID, player.getUniqueID().toString());
			Utils.capDo(player, (cap) -> cap.setServant(player, this));
		} else
			this.dataManager.set(ownerUUID, "");
		this.owner = player;
		this.disableChunkload = this.dataManager.get(ownerUUID).isEmpty();
	}

	//=====NBT
	@Override
	public void writeAdditional(CompoundNBT tag) {
		super.writeAdditional(tag);
		tag.putString("Owner", this.dataManager.get(ownerUUID));
		tag.putBoolean("CanUseNP", this.canUseNP);
		tag.putInt("Death", this.deathTime);
		tag.putBoolean("IsDead", this.died);
		tag.putInt("Command", this.commandBehaviour);
		tag.putInt("Mana", this.servantMana);
		tag.putBoolean("HealthMessage", this.critHealth);
		tag.putBoolean("Revealed", this.showServant());
		tag.putBoolean("DisableChunkload", this.disableChunkload);
	}

	@Override
	public void readAdditional(CompoundNBT tag) {
		super.readAdditional(tag);
		this.dataManager.set(ownerUUID, tag.getString("Owner"));
		this.canUseNP = tag.getBoolean("CanUseNP");
		this.deathTime = tag.getInt("Death");
		this.died = tag.getBoolean("IsDead");
		this.updateAI(tag.getInt("Command"));
		this.servantMana = tag.getInt("Mana");
		this.critHealth = tag.getBoolean("HealthMessage");
		this.dataManager.set(showServant, tag.getBoolean("Revealed"));
		this.disableChunkload = tag.getBoolean("DisableChunkload");
	}

	//=====Entity AI updating

	/**
	 * @param behaviour 0 = normal, 1 = aggressive, 2 = defensive, 3 = follow, 4 = stay, 5 = guard an area
	 */
	public void updateAI(int behaviour) {
		this.commandBehaviour = behaviour;
		if (this.commandBehaviour == 0) {
			this.targetSelector.removeGoal(this.targetMob);
			this.targetSelector.addGoal(0, this.targetHurt);
			this.targetSelector.addGoal(1, this.targetServant);
		} else if (this.commandBehaviour == 1) {
			this.targetSelector.addGoal(0, this.targetHurt);
			this.targetSelector.addGoal(1, this.targetServant);
			this.targetSelector.addGoal(3, this.targetMob);
		} else if (this.commandBehaviour == 2) {
			this.targetSelector.addGoal(0, this.targetHurt);
			this.targetSelector.removeGoal(this.targetServant);
			this.targetSelector.removeGoal(this.targetMob);
		} else if (this.commandBehaviour == 3) {
			this.targetSelector.addGoal(0, this.targetHurt);
			this.targetSelector.addGoal(1, this.targetServant);
			this.goalSelector.addGoal(0, this.follow);
			this.goalSelector.addGoal(2, this.wander);
			this.setStaying(false);
			this.detachHome();
		} else if (this.commandBehaviour == 4) {
			this.targetSelector.removeGoal(this.targetHurt);
			this.targetSelector.removeGoal(this.targetServant);
			this.targetSelector.removeGoal(this.targetMob);
			this.goalSelector.removeGoal(this.follow);
			this.goalSelector.removeGoal(this.wander);
			this.setStaying(true);
			this.getNavigator().clearPath();
			this.setAttackTarget(null);
			this.detachHome();
		} else if (this.commandBehaviour == 5) {
			this.setStaying(false);
			this.goalSelector.removeGoal(this.follow);
			this.goalSelector.addGoal(2, this.wander);
			this.setHomePosAndDistance(this.getOwner().getBlockPos(), 8);
		}
	}

	//=====Living update handling and stuff

	@Override
	public void tick() {
		super.tick();
		this.tickAnimation();
		if (!this.world.isRemote) {
			this.regenMana();
			this.combatTick = Math.max(0, --this.combatTick);
			if (!this.disableChunkload) {
				ChunkPos pos = new ChunkPos(this.chunkCoordX, this.chunkCoordZ);
				((ServerChunkProvider) this.world.getChunkProvider()).registerTicket(TicketType.UNKNOWN, pos, 1, pos);
			}
			if (this.getOwner() != null && !this.tracked.contains(this.getOwner()))
				this.updateDataManager((ServerPlayerEntity) this.getOwner());
			if (this.getAttackTarget() != null && this.getAttackTarget().getRidingEntity() instanceof LivingEntity)
				this.setAttackTarget((LivingEntity) this.getAttackTarget().getRidingEntity());
		}
	}

	private List<ServerPlayerEntity> tracked = Lists.newArrayList();

	@Override
	public void addTrackingPlayer(ServerPlayerEntity player) {
		this.tracked.add(player);
	}

	private void updateDataManager(ServerPlayerEntity player) {
		EntityDataManager entitydatamanager = this.getDataManager();
		if (entitydatamanager.isDirty()) {
			player.connection.sendPacket(new SEntityMetadataPacket(this.getEntityId(), entitydatamanager, false));
		}
		Set<ModifiableAttributeInstance> set = this.getAttributes().getTracked();
		if (!set.isEmpty()) {
			player.connection.sendPacket(new SEntityPropertiesPacket(this.getEntityId(), set));
		}
		//set.clear();
	}

	//=====Death Handling

	public int getDeathTick() {
		return this.deathTime;
	}

	/**
	 * Since onDeathUpdate doesnt kill it immediatly
	 */
	public boolean isDead() {
		return this.died;
	}

	@Override
	public boolean canDespawn(double d) {
		return false;
	}

	@Override
	protected void onDeathUpdate() {
		++this.deathTime;
		this.died = true;
		/*for(int i = 0; i < ((int)((7/(float)this.maxDeathTick())*this.deathTicks-1)); i++)
			Particles.spawnParticle(ModRender.particleFade, this.world, this.posX + (this.rand.nextDouble() - 0.5D) * (this.width+3),
    		this.posY + this.rand.nextDouble() * (this.height+1.5), 
    		this.posZ + (this.rand.nextDouble() - 0.5D) * (this.width+3), 
    		this.rand.nextGaussian() * 0.02D, 
    		this.rand.nextGaussian() * 0.02D,
    		this.rand.nextGaussian() * 0.02D);*/
		if (!this.world.isRemote) {
			if (this.deathTime == 1) {
				//if(this.getLastDamageSource()!=DamageSource.OUT_OF_WORLD)
				this.world.getServer().getPlayerList().broadcastChatMessage(new TranslationTextComponent("chat.servant.death").formatted(TextFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
				this.playSound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
				GrailWarHandler.get(this.world).removeServant(this);
				this.disableChunkload = true;
			}

			if (this.deathTime > 15 && this.deathTime % 5 == 0 && (this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
				int exp = this.experienceValue;
				int splitExp;
				while (exp > 0) {
					splitExp = ExperienceOrbEntity.getXPSplit(exp);
					exp -= splitExp;
					this.world.addEntity(new ExperienceOrbEntity(this.world, this.getX(), this.getY(), this.getZ(), splitExp));
				}
			}
			if (this.deathTime == this.maxDeathTick()) {
				this.remove();
			}
		}
	}

	public int maxDeathTick() {
		return 200;
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		for (int i = 0; i < this.drops.length; i++) {
			int chance = this.rand.nextInt(100) - lootingModifier * 20;
			if (chance < this.prop.dropChance()) {
				this.entityDropItem(this.drops[i], 1);
			}
		}
	}

	//=====Entity attack etc.

	@Override
	public AnimatedAction getAnimation() {
		return this.currentAnim;
	}

	@Override
	public void setAnimation(AnimatedAction anim) {
		this.currentAnim = anim == null ? null : anim.create();
		IAnimated.sentToClient(this);
	}

	public abstract boolean canUse(AnimatedAction anim, AttackType type);

	public AnimatedAction getRandomAttack(AttackType type) {
		AnimatedAction anim = this.getAnimations()[this.rand.nextInt(this.getAnimations().length)];
		if (this.canUse(anim, type))
			return anim;
		return this.getRandomAttack(type);
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if (!this.isInvulnerableTo(damageSrc)) {
			damageAmount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
			if (damageAmount <= 0) return;
			if (damageSrc.isProjectile())
				damageAmount = Utils.projectileReduce(this, damageAmount);
			damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
			if (damageSrc.isMagicDamage())
				damageAmount = Utils.getDamageAfterMagicAbsorb(this, damageAmount);
			damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);
			float f = damageAmount;
			damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0F);
			this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
			damageAmount = net.minecraftforge.common.ForgeHooks.onLivingDamage(this, damageSrc, damageAmount);

			if (damageAmount != 0.0F) {
				float f1 = this.getHealth();
				this.getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
				this.setHealth(f1 - damageAmount);
				this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
				this.combatTick = 300;
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (damageSource == DamageSource.OUT_OF_WORLD) {
			return this.preAttackEntityFrom(damageSource, damage);
		} else {
			if (!(damageSource.getTrueSource() instanceof EntityServant || damageSource.getTrueSource() instanceof IServantMinion))
				damage *= 0.5;

			if (damageSource.isProjectile() && !damageSource.isUnblockable() && this.projectileBlockChance(damageSource, damage)) {
				this.world.playSound(null, this.getBlockPos(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 1, 1);
				if (damageSource.getImmediateSource() != null)
					damageSource.getImmediateSource().remove();
				return false;
			}
			return this.preAttackEntityFrom(damageSource, Math.min(50, damage));
		}
	}

	protected boolean preAttackEntityFrom(DamageSource damageSource, float par2) {
		return super.attackEntityFrom(damageSource, par2);
	}

	public void onKillOrder(PlayerEntity player, boolean success) {
		this.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
		player.sendMessage(new TranslationTextComponent("chat.command.kill").formatted(TextFormatting.RED), Util.NIL_UUID);
	}

	public void onForfeit(PlayerEntity player) {

	}

	public boolean projectileBlockChance(DamageSource damageSource, float damage) {
		return this.rand.nextFloat() < (float) this.getAttributeValue(FateAttributes.PROJECTILE_BLOCKCHANCE);
	}

	@Override
	public void takeKnockback(float strength, double xRatio, double zRatio) {
		super.takeKnockback(strength * 0.75F, xRatio, zRatio);
	}

	public enum AttackType {
		RANGED,
		MELEE,
		NP;
	}
}
