package com.flemmli97.fatemod.common.entity.servant;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.common.entity.ai.EntityAIFollowMaster;
import com.flemmli97.fatemod.common.entity.ai.EntityAIRetaliate;
import com.flemmli97.fatemod.common.handler.GrailWarPlayerTracker;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.network.CustomDataPacket;
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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * 	Base servant class
 */
public class EntityServant extends EntityCreature{
	
	private int servantMana, antiRegen, attackTimer, counter, hoguManaUse;
		
	public boolean canUseNP, critHealth, nearbyEnemy, stay, forcedNP;
	public int specialAnimation, attackTimerValue, specialAnimationValue, deathTicks;
	
	/** 0 = normal, 1 = aggressive, 2 = defensive, 3 = follow, 4 = stay, 5 = guard an area */
	public int commandBehaviour;
			
	/* variables here are/should be set at init */
	private EnumServantType servantType = EnumServantType.NOTASSIGNED;
	private String owner, hogu;
	private Item[] drops;
	//===========//
	protected static final DataParameter<ItemStack> itemStackMain = EntityDataManager.<ItemStack>createKey(EntityServant.class, CustomDataPacket.ITEM_STACK);
    protected static final DataParameter<ItemStack> itemStackOff = EntityDataManager.<ItemStack>createKey(EntityServant.class, CustomDataPacket.ITEM_STACK);
    
	public EntityAINearestAttackableTarget<EntityServant> targetServant = new EntityAINearestAttackableTarget<EntityServant>(this, EntityServant.class, 10, true, true, new Predicate<EntityServant>()    {
        public boolean apply(@Nullable EntityServant living)
        {
        		boolean flag = true;
        		if(EntityServant.this.getOwner()!=null)
        		{
        			IPlayer capSync = EntityServant.this.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
        			flag = !capSync.getServantTruce().contains(living);
        		}
            return living != null && flag;
        }});
	public EntityAINearestAttackableTarget<EntityPlayer> targetPlayer = new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 10, true, true, new Predicate<EntityPlayer>()    {
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
            return living != null && IMob.VISIBLE_MOB_SELECTOR.apply(living);
        }});
	
	public EntityAIFollowMaster follow = new EntityAIFollowMaster(this, 1.0D, 20.0F, 4.0F);
	public EntityAIRetaliate targetHurt = new EntityAIRetaliate(this);
	public EntityAIMoveTowardsRestriction restrictArea = new EntityAIMoveTowardsRestriction(this, 1.0D);
	public static final IAttribute MAGIC_RESISTANCE = (new RangedAttribute((IAttribute)null, "generic.magicResistance", 0.0D, 0.0D, 1.0D)).setDescription("Magic Resistance");
	public static final IAttribute PROJECTILE_RESISTANCE = (new RangedAttribute((IAttribute)null, "generic.projectileResistance", 0.0D, 0.0D, 30.0D)).setDescription("Projectile Resistance");
	 
	private EntityServant(World world) {
		super(world);
        this.experienceValue = 50;
	    this.tasks.addTask(0, follow);
        this.tasks.addTask(1, restrictArea);
	    this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
	    this.tasks.addTask(3, new EntityAISwimming(this));
	    this.tasks.addTask(4, new EntityAILookIdle(this));
	    this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	    this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
	    this.targetTasks.addTask(0, targetHurt);
	    this.targetTasks.addTask(1, targetServant);
	    this.targetTasks.addTask(2, targetPlayer);
	}
	
	public EntityServant(World world, EnumServantType servantType, String hogu, int hoguMana, Item[] drops)
	{
		this(world);
		this.setServantAttributes(servantType);
		this.hogu = hogu;
		this.hoguManaUse = hoguMana;
		this.drops = drops;
	}

	//=====Entity initialization
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(itemStackMain, null);
        this.dataManager.register(itemStackOff, null);
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
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getAttributeMap().registerAttribute(EntityServant.MAGIC_RESISTANCE);
        this.getAttributeMap().registerAttribute(EntityServant.PROJECTILE_RESISTANCE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }
	
	/** Attributes like magic resistance, projectile resistance etc are defined here
	 * Saber:
	 * Lancer:
	 * Archer:
	 * Berserker:
	 * Rider:
	 * Caster:
	 * Assassin:
	 * */
	private void setServantAttributes(EnumServantType type)
	{
		if(type == EnumServantType.SABER)
		{
			this.getEntityAttribute(MAGIC_RESISTANCE).setBaseValue(0.8D);
			this.getEntityAttribute(PROJECTILE_RESISTANCE).setBaseValue(17.0D);
		}
		else if(type == EnumServantType.ARCHER)
		{
			
		}
		else if(type == EnumServantType.LANCER)
		{
			
		}
		else if(type == EnumServantType.BERSERKER)
		{
			
		}
		else if(type == EnumServantType.CASTER)
		{
			
		}
		else if(type == EnumServantType.ASSASSIN)
		{
			
		}
		else if(type == EnumServantType.RIDER)
		{
			
		}
	}
	
	//=====Client-Server sync

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack stack) {
		if(slot==EntityEquipmentSlot.MAINHAND)
			dataManager.set(itemStackMain, stack);
		else if(slot==EntityEquipmentSlot.OFFHAND)
			dataManager.set(itemStackOff, stack);
		super.setItemStackToSlot(slot, stack);
	}

	public ItemStack stackFromHand(EnumHand hand)
	{
		return hand==EnumHand.MAIN_HAND ? dataManager.get(itemStackMain) : dataManager.get(itemStackOff);
	}

	@Override
	public void handleStatusUpdate(byte b)
    {
        if (b == 4)
        {
           attackTimer = attackTimerValue;
        }
        else if(b==5)
        {
        		specialAnimation = specialAnimationValue;
        }
        else if(b==6)
        {
        		stay=true;
        }
        else if(b==7)
        {
        		stay=false;
        }
        else
        {
            super.handleStatusUpdate(b);
        }
    }
	
	//=====Mana handling
	
	public int getSpecialMana()
	{
		return this.hoguManaUse;
	}
	
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
    					if(counter == 7)
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
	
	//=====Entity AI updating
	
	@Override
	protected void updateAITasks() {
		if(commandBehaviour == 0)
		{
			this.targetTasks.removeTask(targetMob);
			this.targetTasks.addTask(0, targetHurt);
			this.targetTasks.addTask(1, targetServant);
		}
		else if(commandBehaviour == 1)
		{
			this.targetTasks.addTask(0, targetHurt);
			this.targetTasks.addTask(1, targetServant);
			this.targetTasks.addTask(3, targetMob);
		}
		else if(commandBehaviour == 2)
		{
			this.targetTasks.addTask(0, targetHurt);
			this.targetTasks.removeTask(targetServant);
			this.targetTasks.removeTask(targetMob);
		}
		else if(commandBehaviour == 3)
		{
			this.targetTasks.addTask(0, targetHurt);
			this.targetTasks.addTask(1, targetServant);
			this.tasks.addTask(0, follow);
			this.stay = false;
			worldObj.setEntityState(this, (byte)7);
			this.detachHome();
		}
		else if(commandBehaviour == 4)
		{
			this.targetTasks.removeTask(targetHurt);
			this.targetTasks.removeTask(targetServant);
			this.targetTasks.removeTask(targetMob);
			this.tasks.removeTask(follow);
			this.stay = true;
			worldObj.setEntityState(this, (byte)6);
			this.getNavigator().clearPathEntity();
			this.setAttackTarget(null);
			this.detachHome();
		}
		else if(commandBehaviour == 5)
		{
			this.stay = false;
			this.tasks.removeTask(follow);
			worldObj.setEntityState(this, (byte)7);
			this.setHomePosAndDistance(this.getOwner().getPosition(), 8);
		}
		super.updateAITasks();
	}

	//=====Player-Owner handling
	
    public EntityPlayer getOwner()
    {
	    	if(owner != null)
	    	{
	        try
	        {
	            UUID var1 = UUID.fromString(owner);
	            return var1 == null ? null : this.worldObj.getPlayerEntityByUUID(var1);
	        }
	        catch (IllegalArgumentException var2)
	        {
	            return null;
	        }
	    	}
		return null;
    }
    
    public void setOwner(EntityPlayer player)
    {
    		if(player!=null)
    			this.owner = player.getUniqueID().toString(); 		
    }

    //=====NBT
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		
		super.writeEntityToNBT(tag);
		if(owner != null)
		{
			tag.setString("Owner", owner);
		}
		tag.setBoolean("CanUseNP", canUseNP);
		tag.setInteger("Death", deathTicks);
		tag.setInteger("Command", commandBehaviour);
		tag.setInteger("Mana", servantMana);
		tag.setBoolean("HealthMessage", critHealth);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		
		super.readEntityFromNBT(tag);
		if(tag.getString("Owner") != null)
		{
			owner = tag.getString("Owner");
		}
		canUseNP = tag.getBoolean("CanUseNP");
		deathTicks = tag.getInteger("Death");
		commandBehaviour = tag.getInteger("Command");
		servantMana = tag.getInteger("Mana");
		critHealth = tag.getBoolean("HealthMessage");
	}
	
	//=====Helper methods

	
	public boolean testNearbyEnemy()
	{
		List<?> var1 = this.worldObj.getEntitiesWithinAABB(EntityServant.class, this.getEntityBoundingBox().expand((double)32, 3.0D, (double)32));
		List<?> var2 = this.worldObj.getEntitiesWithinAABB(EntityServant.class, this.getEntityBoundingBox().expand((double)15, 3.0D, (double)15));
		
		if (var1.size() > 1)
		{
			if(var2.size() < 2)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		
	}

	/**max 20: 80% reduction*/
	public float getDamageAfterMagicAbsorb(float damage)
    {
		float magicProt = MathHelper.floor_double(this.getEntityAttribute(MAGIC_RESISTANCE).getAttributeValue());
        float f = MathHelper.clamp_float(magicProt , 0.0F, 20.0F);
        return damage * (1.0F - f / 25.0F);
    }
	
	//=====Living update handling and stuff
	

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();		
		if(!this.worldObj.isRemote)
		{
			regenMana();
		}
		if(testNearbyEnemy())
			nearbyEnemy = true;		
		else	
			nearbyEnemy = false;		
		if(attackTimer > 0)
		{
			--attackTimer;
		}
		if(specialAnimation > 0)
		{
			--specialAnimation;
		}
	}
	
	//public 
	
	public int getDeathTick()
	{
		return this.deathTicks;
	}
	
	@Override
	protected void onDeathUpdate() {
		++this.deathTicks;
		if(!this.worldObj.isRemote)
		{
			if(deathTicks == 1)
			{	
				this.dead=true;
				MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
				minecraftserver.getPlayerList().sendChatMsg(new TextComponentString(TextFormatting.RED + "A servant has been killed"));			
				this.playSound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
				if(getOwner() != null)
				{
					IPlayer servantprop = this.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
					servantprop.setServant(null);
					GrailWarPlayerTracker track = GrailWarPlayerTracker.get(this.worldObj);
	    				track.removePlayer(this.getOwner());
				}
			}
			int exp;
	        int splitExp;
	        if (this.deathTicks > 15 && this.deathTicks % 5 == 0 && (this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot"))
	        {
                exp = this.experienceValue;
                while (exp > 0)
                {
                    splitExp = EntityXPOrb.getXPSplit(exp);
                    exp -= splitExp;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, splitExp));
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
			if(chance < 10)
			{
				this.dropItem(drops[i], 1);
			}
		}
	}
	
	//=====Entity attack etc.

	public int getAttackTime()
	{
		return this.attackTimer;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage)
    {
		if(damageSource == DamageSource.outOfWorld)
		{
			return this.preAttackEntityFrom(damageSource, damage);
		}
		else
		{
			if(!(damageSource.getEntity() instanceof EntityServant))
				damage*=0.5;
			if(damageSource.isMagicDamage())
				damage = this.getDamageAfterMagicAbsorb(damage);
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
            Entity var3 = damageSource.getEntity();

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
        damage += this.rand.nextGaussian();
        if(this.rand.nextFloat()<0.05)
        		damage*=1.5;
		if (entity instanceof EntityLivingBase)
        {
			damage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entity).getCreatureAttribute());
			knockbackDealt += EnchantmentHelper.getKnockbackModifier(this);
        }
		 
        this.attackTimer=attackTimerValue;
        this.worldObj.setEntityState(this, (byte)4);
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
                        this.worldObj.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entity);
        }
        return var4;
    }
	
	//=====Getter for various things
	
	public String getSpecialName()
	{
		return this.hogu;
	}

	public EnumServantType getServantType() {
		return servantType;
	}
	
	//=====Everything else to add
	
}
