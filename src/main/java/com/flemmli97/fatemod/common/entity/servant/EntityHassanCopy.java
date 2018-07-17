package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIAnimatedAttack;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIFollowMaster;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityHassanCopy extends EntityServant{

	private EntityHassan original;
	private int livingTick;
	public EntityAIFollowMaster followOriginal = new EntityAIFollowMaster(this, 1.0D, 8.0F, 4.0F) {
		@Override
		public boolean shouldExecute()
	    {
	    	EntityLivingBase owner = EntityHassanCopy.this.original;
	        if (owner == null)
	        {
	            return false;
	        }
	        else if (this.servant.getDistanceSq(owner) < (double)(this.minDist * this.minDist))
	        {
	            return false;
	        }
	        else if(this.servant.getAttackTarget() != null)
	        {
	    		return false;
	        }
	        else
	        {
	        	this.setFollowTarget(owner);
	            return true;
	        }
		}};
	public EntityAIAnimatedAttack attackAI = new EntityAIAnimatedAttack(this,false, 1, 1);
		
	public EntityHassanCopy(World world) {
		this(world, null);
	}
	public EntityHassanCopy(World world, EntityHassan original) {
		super(world, EnumServantType.ASSASSIN, "", new ItemStack[] {});
		this.original=original;
	    this.tasks.addTask(1, this.followOriginal);
	    this.updateAttributes();
	    this.tasks.addTask(1, attackAI);
		this.targetTasks.removeTask(targetServant);
	    this.targetTasks.removeTask(targetPlayer);
	    this.targetTasks.removeTask(targetHurt);
	}
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.livingTick++;
		if(this.livingTick>3600)
			this.setDead();
	}
	@Override
	protected void updateAITasks() {}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.dagger));       
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		// TODO Auto-generated method stub
		return Pair.of(0, 0);
	}
	
	@Override
	public boolean writeToNBTOptional(NBTTagCompound compound)
    {
		return false;
    }
	
	public NBTTagCompound entityNBT(NBTTagCompound compound)
	{
		//compound.setString("id", this.getEntityString());
		this.writeToNBT(compound);
		return compound;
	}
	
	private void updateAttributes()
	{
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.setHealth(this.getMaxHealth());
	}
	
	@Override
	protected void onDeathUpdate() {
		if(this.original!=null)
			this.original.removeCopy(this);
		super.onDeathUpdate();
	}
}
