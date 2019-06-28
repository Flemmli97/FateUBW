package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.EntityGordiusWheel;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIIskander;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityIskander extends EntityServant {

	public EntityAIIskander attackAI = new EntityAIIskander(this);
	
	private static final AnimatedAction npAttack = new AnimatedAction(20,0,"np");
	private static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, npAttack};

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
	public boolean canUse(AnimatedAction anim, AttackType type)
	{
		if(type==AttackType.NP)
			return anim.getID().equals("np");
		return anim.getID().equals("vanilla");
	}
	
	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
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
	
	public boolean attackWithNP() {
		
		if(this.isRiding() || this.world.isRemote)
			return false;
		EntityGordiusWheel wheel = new EntityGordiusWheel(this.world);
		wheel.setPosition(this.posX, this.posY, this.posZ);
		this.world.spawnEntity(wheel);
		this.startRiding(wheel);
		//spawn lightning
		this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX, this.posY, this.posZ, true));
		this.revealServant();
		return true;
	}
	
	@Override
	public boolean attacksFromMount()
	{
		return false;
	}
}
