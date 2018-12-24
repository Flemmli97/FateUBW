package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.entity.EntityEnumaElish;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIGilgamesh;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGilgamesh extends EntityServant implements IRanged{

	public EntityAIGilgamesh attackRanged = new EntityAIGilgamesh(this,true, 1, 16);
	public EntityAIGilgamesh attackMelee = new EntityAIGilgamesh(this,false, 1, 1);

	private boolean rangedAttack;

	public EntityGilgamesh(World world) {
		super(world, EnumServantType.ARCHER, "Enuma Elish", new ItemStack[] {new ItemStack(ModItems.enumaelish)/*babylon=*/});
        this.tasks.addTask(1, attackRanged);
        this.revealServant();
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		if(this.rangedAttack)
			return Pair.of(20, 10);
		return Pair.of(20, 20);
	}
	
	@Override
	public int attackCooldown()
	{
		return this.rangedAttack?40:10;
	}

	@Override
	public void updateAI(int behaviour) {
		super.updateAI(behaviour);
		if(commandBehaviour == 3)
		{
			this.updateAI();
		}
		else if(commandBehaviour == 4)
		{
			this.tasks.removeTask(attackRanged);
			this.tasks.removeTask(attackMelee);
		}
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		super.damageEntity(damageSrc, damageAmount);
		if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP = true;
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.enumaelish));
			this.updateAI();
		}
    }
	
	private void updateAI() {
		if(this.getHeldItemMainhand().getItem()==ModItems.enumaelish)
		{
			this.tasks.removeTask(attackRanged);
			this.tasks.addTask(1, attackMelee);
			this.rangedAttack=false;
		}
		else
		{
			this.tasks.removeTask(attackMelee);
			this.tasks.addTask(1, attackRanged);
			this.rangedAttack=true;
		}
	}

	public void attackWithNP()
	{
		EntityEnumaElish excalibur = new EntityEnumaElish(world, this);
		this.world.spawnEntity(excalibur);	
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.updateAI();
	}

	@Override
	public void attackWithRangedAttack(EntityLivingBase target) {
		int weaponAmount = this.getRNG().nextInt(15)+4;
		for(int x = 0; x < weaponAmount; x++)
		{
			EntityBabylonWeapon weapon = new EntityBabylonWeapon(this.world, this, target);
			if(!world.isRemote)
			{
				weapon.setEntityProperties();
			}
		}	
	}
	
	@Override
	public boolean isDoingRangedAttack() {
		return this.rangedAttack;
	}
}
