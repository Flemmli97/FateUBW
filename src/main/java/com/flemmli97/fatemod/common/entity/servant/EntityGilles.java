package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityLesserMonster;
import com.flemmli97.fatemod.common.entity.EntityMonster;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIGilles;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityGilles extends EntityServant implements IRanged{

	EntityAIGilles attackAI = new EntityAIGilles(this);

	public EntityGilles(World world) {
		super(world, EnumServantType.CASTER, "Prelati's Spellbook", new ItemStack[] {new ItemStack(ModItems.grimoire)});
		this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.grimoire));       
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		return Pair.of(80, 30);
	}
	
	@Override
	public int attackCooldown()
	{
		return 20;
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
		if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
		}
    }

	public void attackWithNP() {
		if(!this.world.isRemote)
		{
			EntityMonster minion = new EntityMonster(this.world, this);
			this.world.spawnEntity(minion);
			minion.setAttackTarget(this.getAttackTarget());
		}
	}

	@Override
	public void attackWithRangedAttack(EntityLivingBase target) {
		if(!this.world.isRemote)
		{
			//TODO: config
			if(this.world.getEntitiesWithinAABB(EntityLesserMonster.class, this.getEntityBoundingBox().grow(16)).size()<6)
			{
				EntityLesserMonster minion = new EntityLesserMonster(this.world, this);
				this.world.spawnEntity(minion);
				minion.setAttackTarget(this.getAttackTarget());
				this.revealServant();
			}
		}
	}

	@Override
	public boolean isDoingRangedAttack() {
		return true;
	}
}
