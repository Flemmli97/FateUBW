package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.ai.EntityAIMedea;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMedea extends EntityServant implements IRanged{

	EntityAIMedea attackAI = new EntityAIMedea(this);
	public EntityMedea(World world) {
		super(world, EnumServantType.CASTER, "Rule Breaker", new Item[] {ModItems.staff});
		this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.staff));       
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
			this.dropItem(ModItems.ruleBreaker, 1);
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
	
	public void attackWithNP()
	{

	}

	@Override
	public void attackWithRangedAttack(EntityLivingBase target) {
		//EntityMagicBeam beam = new EntityMagicBeam(this.worldObj, this, target);
		//beam.setProjectileAreaPosition(3);
	}

}
