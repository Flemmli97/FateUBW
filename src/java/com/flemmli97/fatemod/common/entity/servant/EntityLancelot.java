package com.flemmli97.fatemod.common.entity.servant;

import java.util.List;

import com.flemmli97.fatemod.common.entity.ai.EntityAILancelot;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityLancelot extends EntityServant {

	public EntityAILancelot attackAI = new EntityAILancelot(this);
	private boolean pickUpWeapon;
	
	public EntityLancelot(World world) {
		super(world, EnumServantType.BERSERKER, "", 0, new Item[] {ModItems.arondight});
        this.tasks.addTask(1, attackAI);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(800.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0D);
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		int chance = rand.nextInt(2);
		if(chance == 0)
		{
			this.dropItem(ModItems.arondight, 1);
		}
		super.dropLoot(wasRecentlyHit, lootingModifier, source);
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
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		super.attackEntityFrom(damageSource, damage);
		float healthPoint;
		float maxHealth;
		healthPoint = this.getHealth();
		maxHealth = this.getMaxHealth();
		if (healthPoint < 0.5 * maxHealth)
		{
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(com.flemmli97.fatemod.common.init.ModItems.arondight));       
		}
		if(damageSource == DamageSource.outOfWorld)
		{
			return super.attackEntityFrom(damageSource, damage);
		}
		else
		{
			return super.attackEntityFrom(damageSource, (float) Math.min(50, damage));
		}
	}
	
	public boolean canPickWeapon()
	{
		return this.pickUpWeapon;
	}

	@Override
	public void onLivingUpdate() {
		double motionX = rand.nextGaussian() * 0.02D;
	    double motionY = rand.nextGaussian() * 0.02D;
	    double motionZ = rand.nextGaussian() * 0.02D;
		for (int x = 0; x < 10; x++)
			{this.worldObj.spawnParticle(
				EnumParticleTypes.SMOKE_LARGE,
        		this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width,
        		this.posY + this.rand.nextDouble() * (double)this.height, 
        		this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 
        		motionX, 
        		motionY,
        		motionZ, 1);
			}	
		float healthPoint;
		float maxHealth;
		healthPoint = this.getHealth();
		maxHealth = this.getMaxHealth();
		if (healthPoint > 0.5 * maxHealth)
		{
			this.pickUpWeapon = true;
		}
		else
		{
			this.pickUpWeapon = false;
		}
		if (!this.worldObj.isRemote && this.canPickWeapon() && !this.dead && this.worldObj.getGameRules().getBoolean("mobGriefing"))
        {
            for (EntityItem entityitem : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D)))
            {
                if (!entityitem.isDead && entityitem.getEntityItem() != null && !entityitem.cannotPickup() && checkItemToWield(entityitem.getEntityItem()))
                {
                    this.updateEquipmentIfNeeded(entityitem);
                }
            }
        }

		super.onLivingUpdate();
	}
	
	public boolean checkItemToWield(ItemStack stack)
	{
		Item item = stack.getItem();
		if(item instanceof ItemSword || item instanceof ItemTool)
		{
			return true;
		}
		return false;
	}
	
	public Entity testForRiding()
	{
		List<Entity> list = this.worldObj.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox().expand((double)16, 3.0D, (double)32));
		
		if (!list.isEmpty())
		{
			for(int x=0;x<list.size();x++)
			{
				Entity entity = list.get(x);
				if(!this.isRiding())
				{
					if(entity instanceof EntityHorse||entity.canRiderInteract())
					{
						return entity;
					}
				}
			}
		}
		return null;
	}
	
}
