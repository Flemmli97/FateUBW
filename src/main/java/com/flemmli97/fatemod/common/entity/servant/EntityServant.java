package com.flemmli97.fatemod.common.entity.servant;

import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIFollowMaster;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIRetaliate;
import com.flemmli97.fatemod.common.handler.GrailWarPlayerTracker;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.init.ServantAttributes;
import com.flemmli97.fatemod.common.utils.ServantProperties;
import com.flemmli97.fatemod.common.utils.ServantUtils;
import com.google.common.base.Predicate;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class EntityServant extends EntityCreature{

	//Mana
	private int servantMana, antiRegen, counter;
	protected int deathTicks, combatTick;
	protected boolean canUseNP, critHealth;
	/*private int , attackTimer, , ;*/
	public boolean forcedNP;
	
	/**0 = normal, 1 = aggressive, 2 = defensive, 3 = follow, 4 = stay, 5 = guard an area*/
	protected int commandBehaviour;
			
	private int attackTimer;
	private EnumServantType servantType = EnumServantType.NOTASSIGNED;
	//PlayerUUID
	private EntityPlayer owner;
	
	private String hogou="";
	private ItemStack[] drops;
	
	protected static final DataParameter<Boolean> showServant = EntityDataManager.<Boolean>createKey(EntityServant.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Integer> entityState = EntityDataManager.<Integer>createKey(EntityServant.class, DataSerializers.VARINT);
	protected static final DataParameter<String> ownerUUID = EntityDataManager.<String>createKey(EntityServant.class, DataSerializers.STRING);

	public static final IAttribute MAGIC_RESISTANCE = (new RangedAttribute((IAttribute)null, "generic.magicResistance", 0.0D, 0.0D, 1.0D)).setDescription("Magic Resistance");
	public static final IAttribute PROJECTILE_RESISTANCE = (new RangedAttribute((IAttribute)null, "generic.projectileResistance", 0.0D, 0.0D, 30.0D)).setDescription("Projectile Resistance");
	
	private ServantProperties prop;
	//Chunk load ticket
	private Ticket ticket;
	public EntityAINearestAttackableTarget<EntityServant> targetServant = new EntityAINearestAttackableTarget<EntityServant>(this, EntityServant.class, 10, true, true, new Predicate<EntityServant>()    {
        public boolean apply(@Nullable EntityServant living)
        {
        		boolean flag = true;
        		EntityPlayer targetOwner = living.getOwner();
        		if(EntityServant.this.getOwner()!=null && targetOwner!=null)
        		{
        			IPlayer capSync = EntityServant.this.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
        			flag = !capSync.isPlayerTruce(targetOwner);
        		}
            return living != null && flag;
        }});
	public EntityAINearestAttackableTarget<EntityPlayer> targetPlayer = new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 0, true, true, new Predicate<EntityPlayer>()    {
        public boolean apply(@Nullable EntityPlayer living)
        {
        	boolean flag = true;
    		if(EntityServant.this.getOwner()!=null)
    		{
    			IPlayer capSync = EntityServant.this.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
    			flag = !capSync.isPlayerTruce(living);
    		}
            return living != null && living != EntityServant.this.getOwner() && flag;
        }});
	public EntityAINearestAttackableTarget<EntityLiving> targetMob = new EntityAINearestAttackableTarget<EntityLiving>(this, EntityLiving.class, 10, true, true, new Predicate<EntityLiving>()    {
        public boolean apply(@Nullable EntityLiving living)
        {
            return living != null&&!(living instanceof EntityServant) && IMob.VISIBLE_MOB_SELECTOR.apply(living);
        }});
	public EntityAIFollowMaster follow = new EntityAIFollowMaster(this, 1.0D, 15.0F, 4.0F);
	public EntityAIRetaliate targetHurt = new EntityAIRetaliate(this);
	public EntityAIMoveTowardsRestriction restrictArea = new EntityAIMoveTowardsRestriction(this, 1.0D);
	public EntityAIWander wander = new EntityAIWander(this, 1.0D);
	private EntityServant(World world) {
		super(world);
		this.experienceValue = 50;
	    this.tasks.addTask(0, this.follow);
        this.tasks.addTask(1, this.restrictArea);
	    this.tasks.addTask(2, this.wander);
	    this.tasks.addTask(3, new EntityAISwimming(this));
	    this.tasks.addTask(4, new EntityAILookIdle(this));
	    this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	    this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
	    this.targetTasks.addTask(0, this.targetServant);
	    this.targetTasks.addTask(1, this.targetPlayer);
	    this.targetTasks.addTask(2, this.targetHurt);
		this.prop = ServantAttributes.attributes.get(this.getClass());
		if (this.prop == null) {
            throw new NullPointerException("Properties of " + this.getClass() + " is null. This is not allowed");
        }
		this.updateAttributes();
		this.ticket = ForgeChunkManager.requestTicket(Fate.instance, world, Type.ENTITY);
		this.ticket.bindEntity(this);
		this.ticket.setChunkListDepth(1);
	}
	
	public EntityServant(World world, EnumServantType type, String hogou, ItemStack[] drops)
	{
		this(world);
		this.drops=drops;
		this.hogou=hogou;
	}
	
	//=========Servant specifig data

	public ServantProperties props()
	{
		return this.prop;
	}
	
	public String nobelPhantasm()
	{
		return this.hogou;
	}

	public EnumServantType getServantType() {
		return this.servantType;
	}
	
	public ItemStack[] drops()
	{
		return this.drops;
	}
	
	//=====Client-Server sync
	
	public boolean showServant()
	{
		return this.dataManager.get(showServant);
	}

	@Override
	public void handleStatusUpdate(byte b)
    {
        if (b == 4)
        {
           this.attackTimer = this.attackTickerFromState(this.entityState()).getLeft();
        }
        else
        {
            super.handleStatusUpdate(b);
        }
    }
	
	public int attackTimer()
	{
		return this.attackTimer;
	}
	
	public State entityState()
	{
		return State.values()[this.dataManager.get(entityState)];
	}
	
	public void setState(State state)
	{
		this.dataManager.set(entityState, state.ordinal());
		if(!this.world.isRemote)
           this.attackTimer = this.attackTickerFromState(this.entityState()).getLeft();
		this.world.setEntityState(this, (byte) 4);
	}
	
	public void revealServant()
	{
		this.dataManager.set(showServant, true);
	}
	
	/**
	 * For attacks
	 * @param state IDDLE and STAY should be ignored
	 * @return First number is animation duration, second is when to actually do damage. 
	 * Example: an armswing should do damage when the sword "hits", not when its swung at beginning
	 */
	public abstract Pair<Integer, Integer> attackTickerFromState(State state);
	
	//=====Init
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(entityState, 0);
        this.dataManager.register(showServant, false);
        this.dataManager.register(ownerUUID, "");
    }
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return livingdata;
	}
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1);
        this.getAttributeMap().registerAttribute(EntityServant.MAGIC_RESISTANCE);
        this.getAttributeMap().registerAttribute(EntityServant.PROJECTILE_RESISTANCE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);//default 0.3
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }
	
	private void updateAttributes()
	{
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.prop.health());
		this.setHealth(this.getMaxHealth());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.prop.strength());
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(this.prop.armor());
        this.getEntityAttribute(EntityServant.MAGIC_RESISTANCE).setBaseValue(this.prop.magicRes());;
        this.getEntityAttribute(EntityServant.PROJECTILE_RESISTANCE).setBaseValue(this.prop.projectileProt());;
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.prop.moveSpeed());//default 0.3
	}
	
	//=====Mana stuff
	
	public boolean useMana(float amount) {
		if(servantMana < amount) {
			return false;
		}		
		else
		{
			servantMana -= amount;
			antiRegen = 30;
			return true;
		}
	}
	
	private void regenMana()
    {
		if(canUseNP)
		{
			if(antiRegen == 0)
			{
				if(servantMana < 100)
				{
					++counter;
					if(counter == 20)
					{
						this.servantMana += 1;
						counter = 0;
					}
				}
			}
			else
			{
				antiRegen--;
			}
		}
    }
	
	public int getMana()
	{
		return this.servantMana;
	}
	
	public boolean canUseNP()
	{
		return this.canUseNP;
	}
	
	//=====Player-Owner handling
	
    public EntityPlayer getOwner()
    {
    	if(this.owner!=null)
    		return this.owner;
        try
        {
            UUID var1 = UUID.fromString(this.dataManager.get(ownerUUID));
            return var1 == null ? null : this.world.getPlayerEntityByUUID(var1);
        }
        catch (IllegalArgumentException var2)
        {
            return null;
        }
    }
    
    public boolean hasOwner()
    {
    	return !this.dataManager.get(ownerUUID).isEmpty();
    }
    
    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
    	if(key==ownerUUID)
    	{
    		if(this.getOwner()!=null)
    		{
    			IPlayer capSync = this.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
    			capSync.setServant(this.getOwner(), this);
    		}
    	}
    }
    
    public void setOwner(EntityPlayer player)
    {
		if(player!=null)
			this.dataManager.set(ownerUUID, player.getUniqueID().toString());
		else
			this.dataManager.set(ownerUUID, "");
		this.owner=player;
    }
    
    //=====NBT
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setString("Owner", this.dataManager.get(ownerUUID));
		tag.setBoolean("CanUseNP", this.canUseNP);
		tag.setInteger("Death", this.deathTicks);
		tag.setInteger("Command", this.commandBehaviour);
		tag.setInteger("Mana", this.servantMana);
		tag.setBoolean("HealthMessage", this.critHealth);
		tag.setBoolean("Revealed", this.showServant());
		if(this.ticket!=null)
		{
			this.ticket.getModData().setIntArray("Chunk", 
					new int[] { MathHelper.floor(this.posX) >> 4, MathHelper.floor(this.posZ) >> 4});
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.dataManager.set(ownerUUID, tag.getString("Owner"));
		this.canUseNP = tag.getBoolean("CanUseNP");
		this.deathTicks = tag.getInteger("Death");
		this.updateAI(tag.getInteger("Command"));
		this.servantMana = tag.getInteger("Mana");
		this.critHealth = tag.getBoolean("HealthMessage");
		this.dataManager.set(showServant, tag.getBoolean("Revealed"));
	}
	
	//=====Entity AI updating
	
	/** 
	 * @param bevahiour 0 = normal, 1 = aggressive, 2 = defensive, 3 = follow, 4 = stay, 5 = guard an area
	 */
	public void updateAI(int behaviour) {
		this.commandBehaviour=behaviour;
		if(commandBehaviour == 0)
		{
			this.targetTasks.removeTask(targetMob);
			this.targetTasks.addTask(2, targetHurt);
			this.targetTasks.addTask(0, targetServant);
		}
		else if(commandBehaviour == 1)
		{
			this.targetTasks.addTask(2, targetHurt);
			this.targetTasks.addTask(0, targetServant);
			this.targetTasks.addTask(3, targetMob);
		}
		else if(commandBehaviour == 2)
		{
			this.targetTasks.addTask(2, targetHurt);
			this.targetTasks.removeTask(targetServant);
			this.targetTasks.removeTask(targetMob);
		}
		else if(commandBehaviour == 3)
		{
			this.targetTasks.addTask(2, targetHurt);
			this.targetTasks.addTask(0, targetServant);
			this.tasks.addTask(0, follow);
			this.tasks.addTask(2, this.wander);
			this.setState(State.IDDLE);
			this.detachHome();
		}
		else if(commandBehaviour == 4)
		{
			this.targetTasks.removeTask(targetHurt);
			this.targetTasks.removeTask(targetServant);
			this.targetTasks.removeTask(targetMob);
			this.tasks.removeTask(follow);
			this.tasks.removeTask(this.wander);
			this.setState(State.STAY);
			this.getNavigator().clearPath();
			this.setAttackTarget(null);
			this.detachHome();
		}
		else if(commandBehaviour == 5)
		{
			this.setState(State.IDDLE);
			this.tasks.removeTask(follow);
			this.tasks.addTask(2, this.wander);
			this.setHomePosAndDistance(this.getOwner().getPosition(), 8);
		}
	}
	
	//=====Living update handling and stuff
	

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();		
		this.attackTimer=Math.max(0, --this.attackTimer);
		if(!this.world.isRemote)
		{
			this.regenMana();
			this.combatTick=Math.max(0, --this.combatTick);
			//Decide it on server, wether attack has finished or not
			if(!this.world.isRemote && this.attackTimer==0 && State.isAttack(this.entityState()))
				this.setState(State.IDDLE);
			if(this.ticksExisted%10==0)
				System.out.println("chunk " + this.getPosition());
			if(this.ticket!=null && this.shouldLoadChunk())
				ForgeChunkManager.forceChunk(this.ticket, new ChunkPos(this.getPosition()));
		}
	}
	
	public boolean shouldLoadChunk()
	{
		return this.deathTicks==0;//this.isEntityAlive();
	}
	
	//=====Death Handling
	
	public int getDeathTick()
	{
		return this.deathTicks;
	}
	
	@Override
    protected boolean canDespawn()
    {
        return false;
    }
	
	@Override
	protected void onDeathUpdate() {
		++this.deathTicks;
		if(!this.world.isRemote)
		{
			if(deathTicks == 1)
			{	
				this.dead=true;
				MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
				minecraftserver.getPlayerList().sendMessage(new TextComponentString(TextFormatting.RED + "A servant has been killed"));			
				this.playSound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
				if(this.getOwner() != null)
				{
					IPlayer servantprop = this.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
					servantprop.setServant(this.getOwner(), null);
					GrailWarPlayerTracker track = GrailWarPlayerTracker.get(this.world);
					track.removePlayer(this.getOwner());
				}
				if(this.ticket!=null)
					ForgeChunkManager.unforceChunk(this.ticket, new ChunkPos(this.getPosition()));
			}
			int exp;
	        int splitExp;
	        if (this.deathTicks > 15 && this.deathTicks % 5 == 0 && (this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))
	        {
                exp = this.experienceValue;
                while (exp > 0)
                {
                    splitExp = EntityXPOrb.getXPSplit(exp);
                    exp -= splitExp;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, splitExp));
                }
	        }
			if (this.deathTicks == 200)
	        {
	            this.setDead();
	        }
		}
	}
	
	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		for(int i = 0; i < this.drops.length; i++)
		{
			int chance = rand.nextInt(100)-lootingModifier*20;
			if(chance < this.prop.dropChance())
			{
				this.entityDropItem(drops[i], 1);
			}
		}
	}
	
	//=====Entity attack etc.
	
	public boolean canAttack()
	{
		return State.isAttack(this.entityState()) && this.attackTimer==this.attackTickerFromState(this.entityState()).getRight();
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc))
        {
            damageAmount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
            if (damageAmount <= 0) return;
            damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
            damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);
            float f = damageAmount;
            damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
            damageAmount = net.minecraftforge.common.ForgeHooks.onLivingDamage(this, damageSrc, damageAmount);

            if (damageAmount != 0.0F)
            {
                float f1 = this.getHealth();
                this.getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
                this.setHealth(f1 - damageAmount); // Forge: moved to fix MC-121048
                this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
                this.combatTick=300;
            }
        }
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage)
    {
		if(damageSource == DamageSource.OUT_OF_WORLD)
		{
			return this.preAttackEntityFrom(damageSource, damage);
		}
		else
		{
			if(!(damageSource.getTrueSource() instanceof EntityServant))
				damage*=0.5;
			if(damageSource.isMagicDamage())
				damage = ServantUtils.getDamageAfterMagicAbsorb(this, damage);
			if(damageSource.isProjectile())
			{
				damage = ServantUtils.projectileReduce(this, damage);
				if(damage<=0)
					return false;
			}
			return this.preAttackEntityFrom(damageSource, (float) Math.min(50, damage));
		}
    }
	
	private boolean preAttackEntityFrom(DamageSource damageSource, float par2)
	{
		if (this.isEntityInvulnerable(damageSource))
        {
            return false;
        }
        else if (super.attackEntityFrom(damageSource, par2))
        {
            Entity var3 = damageSource.getTrueSource();

            if (!this.isRidingOrBeingRiddenBy(var3))
            { 
                return true;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
	}

	@Override
	public boolean attackEntityAsMob(Entity entity)
    {
        float damage = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int knockbackDealt = 0;
        damage*=this.damageModifier();
        damage += this.rand.nextGaussian();
        if(this.rand.nextFloat()<0.05)
        		damage*=1.5;
		if (entity instanceof EntityLivingBase)
        {
			damage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entity).getCreatureAttribute());
			knockbackDealt += EnchantmentHelper.getKnockbackModifier(this);
        }		 
        boolean var4 = entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
        if (var4)
        {
            if (knockbackDealt > 0 && entity instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entity).knockBack(this, (float)knockbackDealt * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int fire = EnchantmentHelper.getFireAspectModifier(this);

            if (fire > 0)
            {
                entity.setFire(fire * 4);
            }

            if (entity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : null;

                if (itemstack != null && itemstack1 != null && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD)
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entity);
        }
        return var4;
    }
	
	/**
	 * Used to modify attack damage from the entity
	 * @return Multiply amount
	 */
	public float damageModifier()
	{
		return 1;
	}
	
	@Override
	public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
		strenght *= 0.75F;
		super.knockBack(entityIn, strenght, xRatio, zRatio);
	}
	
	public static enum State
	{
		IDDLE,
		STAY,
		ATTACK1,
		ATTACK2,
		ATTACK3,
		NP;
		
		public static State randomAttackState(Random rand)
		{
			switch(rand.nextInt(3))
			{
				case 0: return ATTACK1;
				case 1: return ATTACK2;
				case 2: return ATTACK3;
			}
			return ATTACK1;
		}
		
		public static boolean isAttack(State state)
		{
			return state!=IDDLE && state!=STAY;
		}
	}
	public static enum EnumServantType
	{
		SABER,
		ARCHER,
		LANCER,
		RIDER,
		BERSERKER,
		CASTER,
		ASSASSIN,
		NOTASSIGNED;
	}
}
