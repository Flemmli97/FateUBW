package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.entity.ai.EntityAIGilgamesh;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGilgamesh extends EntityServant implements IRanged{

	public EntityAIGilgamesh attackRanged = new EntityAIGilgamesh(this,true, 1, 60, 30, 0, 0, 16);
	public EntityAIGilgamesh attackMelee = new EntityAIGilgamesh(this,false, 1, 15, 7, 15, 7, 1);

	public EntityGilgamesh(World world) {
		super(world, EnumServantType.ARCHER, "Enuma Elish", 100, new Item[] {ModItems.enumaelish});
        this.tasks.addTask(1, attackRanged);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(11.0D);
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		int chance = rand.nextInt(2);
		if(chance == 0)
		{
			this.dropItem(ModItems.enumaelish, 1);
		//gateofbabylon
		}
		super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		float healthPoint;
		float maxHealth;
		healthPoint = this.getHealth();
		maxHealth = this.getMaxHealth();
		if (healthPoint < 0.5 * maxHealth)
		{
			this.canUseNP = true;
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.enumaelish));
			this.updateAI();
		}
		return super.attackEntityFrom(damageSource, damage);	
	}
	
	private void updateAI() {
		this.tasks.removeTask(attackRanged);
		this.tasks.addTask(1, attackMelee);
	}

	public void attackWithNP()
	{
		//enuma elish
	}

	@Override
	public void attackWithRangedAttack(EntityLivingBase target) {
		int weaponAmount = this.getRNG().nextInt(10)+1;
		for(int x = 0; x < weaponAmount; x++)
		{
			EntityBabylonWeapon weapon = new EntityBabylonWeapon(this.worldObj, this, target);
			if(!worldObj.isRemote)
			{
				weapon.setEntityProperties();
			}
		}	
	}
}
