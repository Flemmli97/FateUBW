package com.flemmli97.fatemod.common.entity.servant;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIHassan;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.init.ModItems;
import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityHassan extends EntityServant {

	public EntityAIHassan attackAI = new EntityAIHassan(this);
	private List<EntityHassanCopy> copies = new ArrayList<EntityHassanCopy>();

	public EntityAINearestAttackableTarget<EntityServant> targetHassan = new EntityAINearestAttackableTarget<EntityServant>(this, EntityServant.class, 10, true, true, new Predicate<EntityServant>()    {
        public boolean apply(@Nullable EntityServant living)
        {
        		boolean flag = true;
        		EntityPlayer targetOwner = living.getOwner();

        		if(EntityHassan.this.getOwner()!=null && targetOwner!=null)
        		{
        			IPlayer capSync = EntityHassan.this.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
        			flag = !capSync.isPlayerTruce(targetOwner);
        		}
            return living != null && flag && !EntityHassan.this.copies.contains(living);
        }});
	
	public EntityHassan(World world) {
		super(world, EnumServantType.ASSASSIN, "Delusional Illusion", new ItemStack[] {new ItemStack(ModItems.dagger)});
        this.tasks.addTask(1, attackAI);
	    //this.targetTasks.removeTask(targetServant);
	    this.targetTasks.addTask(0, targetHassan);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.dagger));       
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		// TODO Auto-generated method stub
		return Pair.of(0, 0);
	}
	
	public void removeCopy(EntityHassanCopy copy)
	{
		this.copies.remove(copy);
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
		for(EntityHassanCopy hassan : this.copies)
		{
			if(hassan!=null)
				hassan.setAttackTarget(entitylivingbaseIn);
		}
		super.setAttackTarget(entitylivingbaseIn);
	}

	@Override
	public void updateAI(int behaviour) {
		this.commandBehaviour=behaviour;
		if(commandBehaviour == 0)
		{
			this.targetTasks.removeTask(targetMob);
			this.targetTasks.addTask(2, targetHurt);
			this.targetTasks.addTask(0, targetHassan);
		}
		else if(commandBehaviour == 1)
		{
			this.targetTasks.addTask(2, targetHurt);
			this.targetTasks.addTask(0, targetHassan);
			this.targetTasks.addTask(3, targetMob);
		}
		else if(commandBehaviour == 2)
		{
			this.targetTasks.addTask(2, targetHurt);
			this.targetTasks.removeTask(targetHassan);
			this.targetTasks.removeTask(targetMob);
		}
		else if(commandBehaviour == 3)
		{
			this.targetTasks.addTask(2, targetHurt);
			this.targetTasks.addTask(0, targetHassan);
			this.tasks.addTask(0, follow);
			this.tasks.addTask(1, attackAI);
			this.stay = false;
			world.setEntityState(this, (byte)7);
			this.detachHome();
		}
		else if(commandBehaviour == 4)
		{
			this.targetTasks.removeTask(targetHurt);
			this.targetTasks.removeTask(targetHassan);
			this.targetTasks.removeTask(targetMob);
			this.tasks.removeTask(attackAI);
			this.tasks.removeTask(follow);
			this.stay = true;
			world.setEntityState(this, (byte)6);
			this.getNavigator().clearPath();
			this.setAttackTarget(null);
			this.detachHome();
		}
		else if(commandBehaviour == 5)
		{
			this.stay = false;
			this.tasks.removeTask(follow);
			world.setEntityState(this, (byte)7);
			this.setHomePosAndDistance(this.getOwner().getPosition(), 8);
		}
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		super.damageEntity(damageSrc, damageAmount);
		if (!this.dead && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
		}
    }

	public void attackWithNP()
	{
		if(this.copies.isEmpty())
		{
			for(int i = 0; i < 5; i++)
			{
				EntityHassanCopy hassan = new EntityHassanCopy(world, this);
				hassan.setLocationAndAngles(this.posX, this.posY,this.posZ, MathHelper.wrapDegrees(this.world.rand.nextFloat() * 360.0F), 0.0F);
				hassan.setOwner(this.getOwner());
				hassan.setAttackTarget(this.getAttackTarget());
				hassan.onInitialSpawn(this.world.getDifficultyForLocation(this.getPosition()), null);
				this.world.spawnEntity(hassan); 
				this.copies.add(hassan);
			}
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:invisibility"),3600,1,true,false));
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"),100,2,true,false));
			if(this.getAttackTarget() instanceof EntityLiving)
				((EntityLiving) this.getAttackTarget()).setAttackTarget(null);
		}
	}

	@Override
	protected void onDeathUpdate() {
		for(EntityHassanCopy hassan : this.copies)
		{
			if(hassan!=null)
			hassan.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
		}
		super.onDeathUpdate();
	}
	
	/*@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		NBTTagList copies = new NBTTagList();
		for(EntityHassanCopy hassan : this.copies)
		{
			if(hassan!=null)
				copies.appendTag(hassan.entityNBT(new NBTTagCompound()));
		}
		tag.setTag("Copies", copies);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		//this.copies.clear();
		//NBTTagList copies = tag.getTagList("Copies", Constants.NBT.TAG_COMPOUND);
		//for(int i = 0; i < copies.tagCount(); i++)
		{
			/*try
	        {
				System.out.println(copies.getCompoundTagAt(i));
	            Entity e =  /*SpawnEntityCustomList*///EntityList.createEntityFromNBT(copies.getCompoundTagAt(i), this.worldObj);
	            /*System.out.println(e);
	            if(e !=null && e instanceof EntityHassanCopy)
	            {
	            	EntityHassanCopy copy = (EntityHassanCopy) e;
	            	copy.readFromNBT(copies.getCompoundTagAt(i));
	            	this.copies.add(copy);
	            	copy.setOriginal(this);
	            	if(!this.worldObj.isRemote)
	            		this.worldObj.spawnEntityInWorld(copy);
	            }
	        }
	        catch (RuntimeException e)
	        {
	        	Fate.logger.error("Error reading Hassan copy from nbt");
	            e.printStackTrace();
	        }*/
	            /*EntityHassanCopy copy = new EntityHassanCopy(this.worldObj, this);
	            copy.readFromNBT(copies.getCompoundTagAt(i));
	            copy.forceSpawn=true;
	            this.copies.add(copy);
	            if(!this.worldObj.isRemote)
	            {
	            	System.out.println(this.worldObj.spawnEntityInWorld(copy));
	            	System.out.println(this.copies);
	            	System.out.println(copy);
	            }*/
	        //Entity e = AnvilChunkLoader.readWorldEntity(copies.getCompoundTagAt(i), this.worldObj, true);
	        /*if(e instanceof EntityHassanCopy)
	        {
	        	((EntityHassanCopy)e).setOriginal(this);
	        	this.copies.add((EntityHassanCopy) e);
	        }
		}
	}*/
}
