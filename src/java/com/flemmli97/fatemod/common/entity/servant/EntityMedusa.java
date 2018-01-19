package com.flemmli97.fatemod.common.entity.servant;

import java.util.List;

import com.flemmli97.fatemod.common.entity.EntityPegasus;
import com.flemmli97.fatemod.common.entity.ai.EntityAIMedusa;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMedusa extends EntityServant {

	EntityAIMedusa attackAI = new EntityAIMedusa(this, false, 1, 15, 5, 15, 5, 1);
	public EntityMedusa(World world) {
		super(world, EnumServantType.RIDER, "Bellerophon", 80, new Item[] {ModItems.medusaDagger});
		this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.medusaDagger));       
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
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
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		int chance = rand.nextInt(100)-lootingModifier*20;
		if(chance < 10)
		{
			this.dropItem(ModItems.medusaDagger, 1);
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (this.getHealth()-damage < 0.5 * this.getMaxHealth())
		{
			canUseNP = true;
		}
		if(this.getRidingEntity()!=null && this.getRidingEntity() instanceof EntityLiving)
		{
			EntityLiving mount = (EntityLiving) this.getRidingEntity();
			return mount.attackEntityFrom(damageSource, damage);
		}
		return super.attackEntityFrom(damageSource, damage);
	}
	
	public void attackWithNP()
	{
		if(!this.worldObj.isRemote)
		{
			EntityPegasus peg = new EntityPegasus(this.worldObj);
			for(int x=0;x < 5;x++)
			{
				EntityLightningBolt bolt = new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ, true);
				this.worldObj.spawnEntityInWorld(bolt);
			}
			List<Entity> list =this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand((double)15, 3.0D, (double)15));
			for(int x = 0; x< list.size();x++)
			{
				if(list.get(x) instanceof EntityLivingBase)
				{
					EntityLivingBase ent = (EntityLivingBase) list.get(x);
					Vec3d posVec = ent.getPositionVector();
					Vec3d distVec;
					float degree;
	                ent.knockBack(this, 1.0F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				}
			}
			this.worldObj.spawnEntityInWorld(peg);
		}
	}

}
