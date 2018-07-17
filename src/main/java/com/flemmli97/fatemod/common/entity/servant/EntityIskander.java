package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIIskander;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityIskander extends EntityServant {

	public EntityAIIskander attackAI = new EntityAIIskander(this);
	
	public EntityIskander(World world) {
		super(world, EnumServantType.RIDER, "Gordius Wheel", new ItemStack[] {new ItemStack(ModItems.kupriots)});
		this.tasks.addTask(1, attackAI);
		this.canUseNP=true;
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.kupriots));       
	}

	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		// TODO Auto-generated method stub
		return Pair.of(0, 0);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage)
    {
		if(damageSource == DamageSource.OUT_OF_WORLD)
		{
			return super.attackEntityFrom(damageSource, damage);
		}
		else if(this.isRiding())
		{
			this.getRidingEntity().attackEntityFrom(damageSource, damage);
		}
		return super.attackEntityFrom(damageSource, damage);
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
	
	public boolean attackWithNP() {
		if(this.isRiding())
			return false;
		return true;
	}
}
