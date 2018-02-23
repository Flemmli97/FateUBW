package com.flemmli97.fatemod.common.entity.servant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.common.entity.ai.EntityAIFollowMaster;
import com.flemmli97.fatemod.common.entity.ai.EntityAIHassan;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.init.ModItems;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityHassan extends EntityServant {

	private boolean isOriginal=true;
	public EntityAIHassan attackAI = new EntityAIHassan(this);
	private List<EntityHassan> copies = new ArrayList<EntityHassan>();
	private EntityHassan original;
	public EntityHassan.EntityAIHassanTarget<EntityServant> hassanTarget = new EntityHassan.EntityAIHassanTarget<EntityServant>(this, EntityServant.class, true, true);
	public EntityAIFollowMaster followOriginal = new EntityAIFollowMaster(this, 1.0D, 20.0F, 4.0F, this.original);

	public EntityHassan(World world) {
		super(world, EnumServantType.ASSASSIN, "Delusional Illusion", 1, new Item[] {ModItems.dagger});
        this.tasks.addTask(1, attackAI);
	    this.targetTasks.removeTask(targetServant);
	    this.targetTasks.addTask(0, hassanTarget);
	    this.tasks.addTask(1, this.followOriginal);
	}
	
	public EntityHassan(World world, boolean isOriginal)
	{
		this(world);
		this.isOriginal = isOriginal;
        this.updateAttributes();
	}
	
	private void updateAttributes()
	{
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.setHealth(this.getMaxHealth());
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.dagger));       
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);        
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.44D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.5D);

	}
	
	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
		if(this.isOriginal)
			for(EntityHassan hassan : this.copies)
			{
				if(hassan!=null)
					hassan.setAttackTarget(entitylivingbaseIn);
			}
		super.setAttackTarget(entitylivingbaseIn);
	}

	@Override
	protected void updateAITasks() {
		if(commandBehaviour == 3)
		{
			this.tasks.addTask(1, attackAI);
		}
		else if(commandBehaviour == 4)
		{
			this.tasks.removeTask(attackAI);
		}
		if(!this.isOriginal)
		{
			this.targetTasks.removeTask(targetServant);
		    this.targetTasks.removeTask(targetPlayer);
		    this.targetTasks.removeTask(targetHurt);
		}
		super.updateAITasks();
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (!this.dead && this.getHealth()-(float) Math.min(50, damage) < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
		}
		return super.attackEntityFrom(damageSource, damage);
	}

	public void attackWithNP()
	{
		if(this.isOriginal)
		{
			if(this.copies.size()<1)
				for(int i = 0; i < 5; i++)
				{
					EntityHassan hassan = new EntityHassan(worldObj, false);
					hassan.setLocationAndAngles(this.posX, this.posY,this.posZ, MathHelper.wrapDegrees(this.worldObj.rand.nextFloat() * 360.0F), 0.0F);
					hassan.setOwner(this.getOwner());
					hassan.setAttackTarget(this.getAttackTarget());
					hassan.original=this;
					this.worldObj.spawnEntityInWorld(hassan); 
					this.copies.add(hassan);
				}
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:invisibility"),3600,1,true,false));
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"),100,2,true,false));
			if(this.getAITarget() instanceof EntityLiving)
				((EntityLiving) this.getAITarget()).setAttackTarget(null);
		}
	}

	@Override
	protected void onDeathUpdate() {
		if(this.isOriginal)
			for(EntityHassan hassan : this.copies)
			{
				if(hassan!=null)
				hassan.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
			}
		super.onDeathUpdate();
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		if(this.isOriginal)
			super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setBoolean("original", isOriginal);
		if(this.isOriginal)
		{
			NBTTagList copies = new NBTTagList();
			for(EntityHassan hassan : this.copies)
			{
				copies.appendTag(hassan.saveCopies());
			}
			tag.setTag("Copies", copies);
		}
		else
			tag.setTag("Original", this.original.saveCopies());
	}
	
	private NBTTagCompound saveCopies()
	{
		NBTTagCompound compound = new NBTTagCompound();
		super.writeEntityToNBT(compound);
		return compound;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.isOriginal=tag.getBoolean("original");
		if(this.isOriginal)
		{
			this.copies.clear();
			NBTTagList copies = tag.getTagList("Copies", 10);
			for(int i = 0; i < copies.tagCount(); i++)
			{
				this.copies.add((EntityHassan) EntityList.createEntityFromNBT(copies.getCompoundTagAt(i), this.worldObj));
			}
		}
		else
			this.original = (EntityHassan) EntityList.createEntityFromNBT(tag.getCompoundTag("Original"), this.worldObj);
	}
	
	public static class EntityAIHassanTarget<T extends EntityLivingBase> extends EntityAITarget
	{
		protected final Class<T> targetClass;
	    private final int targetChance;
	    /** Instance of EntityAINearestAttackableTargetSorter. */
	    protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	    protected final Predicate <? super T > targetEntitySelector;
	    protected T targetEntity;

	    public EntityAIHassanTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight)
	    {
	        this(creature, classTarget, checkSight, false);
	    }

	    public EntityAIHassanTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby)
	    {
	        this(creature, classTarget, 10, checkSight, onlyNearby, (Predicate <? super T >)null);
	    }

	    public EntityAIHassanTarget(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate <? super T > targetSelector)
	    {
	        super(creature, checkSight, onlyNearby);
	        this.targetClass = classTarget;
	        this.targetChance = chance;
	        this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(creature);
	        this.setMutexBits(1);
	        this.targetEntitySelector = new Predicate<T>()
	        {
	            public boolean apply(@Nullable T p_apply_1_)
	            {
	                return p_apply_1_ == null ? false : (targetSelector != null && !targetSelector.apply(p_apply_1_) ? false : (!EntitySelectors.NOT_SPECTATING.apply(p_apply_1_) ? false : EntityAIHassanTarget.this.isSuitableTarget(p_apply_1_, false)));
	            }
	        };
	    }

	    /**
	     * Returns whether the EntityAIBase should begin execution.
	     */
	    @SuppressWarnings("unchecked")
		public boolean shouldExecute()
	    {
	        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
	        {
	            return false;
	        }
	        else if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class)
	        {
	            List<T> list = this.taskOwner.worldObj.<T>getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

	            if (list.isEmpty())
	            {
	                return false;
	            }
	            else
	            {
	                Collections.sort(list, this.theNearestAttackableTargetSorter);
	                this.targetEntity = list.get(0);
	                return true;
	            }
	        }
	        else
	        {
	            this.targetEntity = (T) this.taskOwner.worldObj.getNearestAttackablePlayer(this.taskOwner.posX, this.taskOwner.posY + (double)this.taskOwner.getEyeHeight(), this.taskOwner.posZ, this.getTargetDistance(), this.getTargetDistance(),null, 
	            				(Predicate<EntityPlayer>) this.targetEntitySelector);
	            return this.targetEntity != null;
	        }
	    }

	    protected AxisAlignedBB getTargetableArea(double targetDistance)
	    {
	        return this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
	    }

	    /* new Predicate<EntityServant>()    {
        public boolean apply(@Nullable EntityServant living)
        {
        		boolean flag = true;
        		if(EntityHassan.this.getOwner()!=null)
        		{
        			IPlayer capSync = EntityHassan.this.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
        			flag = !capSync.getServantTruce().contains(living);
        		}
            return living != null && flag && 
            		EntityHassan.this.isOriginal ?!EntityHassan.this.copies.contains(living):!EntityHassan.this.original.copies.contains(living);
        }}*/
	    /**
	     * Execute a one shot task or start executing a continuous task
	     */
	    public void startExecuting()
	    {
	        this.taskOwner.setAttackTarget(this.targetEntity);
	        super.startExecuting();
	    }

	    public static class Sorter implements Comparator<Entity>
	        {
	            private final Entity theEntity;

	            public Sorter(Entity theEntityIn)
	            {
	                this.theEntity = theEntityIn;
	            }

	            public int compare(Entity p_compare_1_, Entity p_compare_2_)
	            {
	                double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
	                double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
	                return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
	            }
	        }
	}
}
