package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.ai.EntityAIGilles;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityGilles extends EntityServant {

	EntityAIGilles attackAI = new EntityAIGilles(this);
	
	public EntityGilles(World world) {
		super(world, EnumServantType.CASTER, "", new Item[] {ModItems.grimoire});
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.grimoire));       
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

	public void attackWithNP() {
		// summon tentacle thingy?
		
	}
}
