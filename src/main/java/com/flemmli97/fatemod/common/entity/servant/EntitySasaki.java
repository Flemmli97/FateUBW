package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAISasaki;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySasaki extends EntityServant {

	EntityAISasaki attackAI = new EntityAISasaki(this);
	public EntitySasaki(World world) {
		super(world, EnumServantType.ASSASSIN, "Tsubame Gaeshi", new ItemStack[] {new ItemStack(ModItems.katana)});
        this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.katana));       
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		// TODO Auto-generated method stub
		return Pair.of(0, 0);
	}
	
	public boolean canAttack()
	{
		return State.isAttack(this.entityState()) && this.attackTimer()==this.attackTickerFromState(this.entityState()).getRight();
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
		if (!this.canUseNP && !this.dead && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
		}
    }
	
	public void attackWithNP(EntityLivingBase living)
	{
		
	}

}
