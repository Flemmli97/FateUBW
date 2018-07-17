package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.EntityArcherArrow;
import com.flemmli97.fatemod.common.entity.EntityCaladBolg;
import com.flemmli97.fatemod.common.entity.ai.EntityAIEmiya;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityEmiya extends EntityServant implements IRanged{
	
	EntityAIEmiya attackMelee = new EntityAIEmiya(this, false, 1, 10, 7, 0, 0, 1);
	EntityAIEmiya attackRanged = new EntityAIEmiya(this, true, 1, 30, 25, 40, 10, 16);

	public EntityEmiya(World world) {
		super(world, EnumServantType.ARCHER, "Calad Bolg II", new Item[] {ModItems.archbow, ModItems.bakuya, ModItems.kanshou});
		this.tasks.addTask(1, attackMelee);
	}	
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.kanshou));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.bakuya));
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource par1damageSource, float par2) {
		float healthPoint;
		float maxHealth;
		healthPoint = this.getHealth();
		maxHealth = this.getMaxHealth();
		if (healthPoint < 0.5 * maxHealth)
		{
			this.canUseNP=true;
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.archbow));
			this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, null);
			this.setAttackAI();
		}
        return super.attackEntityFrom(par1damageSource, par2);
	}
	
	private void setAttackAI() {
		this.tasks.removeTask(attackMelee);
		this.tasks.removeTask(attackRanged);
        ItemStack itemstack = this.getHeldItemMainhand();

		if(itemstack!=null && itemstack.getItem() == ModItems.archbow)
		{
			this.tasks.addTask(1, attackRanged);
		}
		else
		{
			this.tasks.addTask(1, attackMelee);
		}
	}

	@Override
	public void attackWithRangedAttack(EntityLivingBase target) {
		EntityArcherArrow arrow = new EntityArcherArrow(this.worldObj, this);
		if(!worldObj.isRemote)
		{
			double d0 = target.posX - this.posX;
			double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - arrow.posY;
			double d2 = target.posZ - this.posZ;
			double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
			arrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 0);	       
			arrow.setDamage(arrow.getDamage()+5.0);	        
			arrow.setKnockbackStrength(1);	       
			this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));	        
			this.worldObj.spawnEntityInWorld(arrow);
		}
    }

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.setAttackAI();
	}

	public void attackWithNP() {
		EntityLivingBase target = this.getAttackTarget();
		if(target != null)
		{
			EntityCaladBolg bolg = new EntityCaladBolg(this.worldObj, this);
			bolg.setHeadingToPosition(target.posX, target.posY+target.getEyeHeight(), target.posZ, 2F, 0);
			this.worldObj.spawnEntityInWorld(bolg);
		}
	}
}
