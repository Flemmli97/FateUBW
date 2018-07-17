package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.entity.EntityEnumaElish;
import com.flemmli97.fatemod.common.entity.ai.EntityAIGilgamesh;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGilgamesh extends EntityServant implements IRanged{

	public EntityAIGilgamesh attackRanged = new EntityAIGilgamesh(this,true, 1, 60, 30, 0, 0, 16);
	public EntityAIGilgamesh attackMelee = new EntityAIGilgamesh(this,false, 1, 15, 7, 15, 7, 1);

	public EntityGilgamesh(World world) {
		super(world, EnumServantType.ARCHER, "Enuma Elish", new Item[] {ModItems.enumaelish/*babylon=*/});
        this.tasks.addTask(1, attackRanged);
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
		if(this.getHeldItemMainhand()!=null && this.getHeldItemMainhand().getItem()==ModItems.enumaelish)
		{
			this.tasks.removeTask(attackRanged);
			this.tasks.addTask(1, attackMelee);
		}
	}

	public void attackWithNP()
	{
		EntityEnumaElish excalibur = new EntityEnumaElish(worldObj, this);
		this.worldObj.spawnEntityInWorld(excalibur);	
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.updateAI();
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
