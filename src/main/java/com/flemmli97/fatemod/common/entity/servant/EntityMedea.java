package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIMedea;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMedea extends EntityServant implements IRanged{

	EntityAIMedea attackAI = new EntityAIMedea(this);
	
	private boolean rangedAttack;

	public EntityMedea(World world) {
		super(world, EnumServantType.CASTER, "Rule Breaker", new ItemStack[] {new ItemStack(ModItems.staff)});
		this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.staff));       
	}

	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		// TODO Auto-generated method stub
		return Pair.of(0, 0);
	}
	
	@Override
	public void updateAI(int behaviour) {
		super.updateAI(behaviour);
		if(commandBehaviour == 3)
		{
			this.tasks.addTask(1, attackAI);
		}
		else if(commandBehaviour == 4)
		{
			this.tasks.removeTask(attackAI);
		}
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
	
	public void attackWithNP()
	{

	}

	@Override
	public void attackWithRangedAttack(EntityLivingBase target) {
		//EntityMagicBeam beam = new EntityMagicBeam(this.worldObj, this, target);
		//beam.setProjectileAreaPosition(3);
		this.revealServant();
	}

	@Override
	public boolean isDoingRangedAttack() {
		return this.rangedAttack;
	}
}
