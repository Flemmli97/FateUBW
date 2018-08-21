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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityHassan extends EntityServant {

	public EntityAIHassan attackAI = new EntityAIHassan(this);
	private List<EntityHassanCopy> copies = new ArrayList<EntityHassanCopy>();
	
	public EntityHassan(World world) {
		super(world, EnumServantType.ASSASSIN, "Delusional Illusion", new ItemStack[] {new ItemStack(ModItems.dagger)});
        this.tasks.addTask(1, attackAI);
	    this.targetTasks.removeTask(targetServant);
	    this.targetServant = new EntityAINearestAttackableTarget<EntityServant>(this, EntityServant.class, 10, true, true, new Predicate<EntityServant>()    {
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
	    this.targetTasks.addTask(0, targetServant);
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
	protected void updateAITasks() {
		if(commandBehaviour == 3)
		{
			this.tasks.addTask(1, attackAI);
		}
		else if(commandBehaviour == 4)
		{
			this.tasks.removeTask(attackAI);
		}
		super.updateAITasks();
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
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		NBTTagList copies = new NBTTagList();
		for(EntityHassanCopy hassan : this.copies)
		{
			if(hassan!=null)
				copies.appendTag(new NBTTagString(hassan.getCachedUniqueIdString()));
		}
		tag.setTag("Copies", copies);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		//this.copies.clear();
		//NBTTagList copies = tag.getTagList("Copies", Constants.NBT.TAG_COMPOUND);
	}
}
