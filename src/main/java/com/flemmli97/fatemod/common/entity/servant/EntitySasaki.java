package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.ai.EntityAISasaki;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySasaki extends EntityServant {

	EntityAISasaki attackAI = new EntityAISasaki(this);
	public EntitySasaki(World world) {
		super(world, EnumServantType.ASSASSIN, "Tsubame Gaeshi", 65, new Item[] {ModItems.katana});
        this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.katana));       
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(350.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12.0D);
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
			this.dropItem(ModItems.katana, 1);
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (this.getHealth()-damage < 0.5 * this.getMaxHealth())
		{
			canUseNP = true;
		}
		return super.attackEntityFrom(damageSource, damage);
	}
	
	public void attackWithNP(EntityLivingBase living)
	{
		
	}

}
