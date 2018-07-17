package com.flemmli97.fatemod.common.entity.servant;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAILancelot;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityLancelot extends EntityServant {

	public EntityAILancelot attackAI = new EntityAILancelot(this);
	private boolean pickUpWeapon;
	
	public EntityLancelot(World world) {
		super(world, EnumServantType.BERSERKER, "Knight of Owner", new ItemStack[] {new ItemStack(ModItems.arondight)});
        this.tasks.addTask(1, attackAI);
	}

	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		// TODO Auto-generated method stub
		return Pair.of(0, 0);
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
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.arondight)); 
			this.pickUpWeapon = false;
		}
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if(this.isRiding())
			return this.getRidingEntity().attackEntityFrom(damageSource, damage);
		else
			return super.attackEntityFrom(damageSource, damage);
	}
	
	public boolean canPickWeapon()
	{
		return this.pickUpWeapon;
	}

	@Override
	public void onLivingUpdate() {
		for (int x = 0; x < 2; x++)
			{this.world.spawnParticle(
				EnumParticleTypes.SMOKE_LARGE,
        		this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width,
        		this.posY + this.rand.nextDouble() * (double)this.height, 
        		this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 
        		rand.nextGaussian() * 0.02D, 
        		rand.nextGaussian() * 0.02D,
        		rand.nextGaussian() * 0.02D, 1);
			}	
		if (!this.world.isRemote && this.canPickWeapon() && !this.dead && this.world.getGameRules().getBoolean("mobGriefing"))
        {
            for (EntityItem entityitem : this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D)))
            {
                if (!entityitem.isDead && entityitem.getItem() != null && !entityitem.cannotPickup() && checkItemToWield(entityitem.getItem()))
                {
                    this.updateEquipmentIfNeeded(entityitem);
                }
            }
        }
		this.testForRiding();
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
	
	public void testForRiding()
	{
		List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, this.getEntityBoundingBox().expand((double)8, 3.0D, (double)8));
		
		if (!list.isEmpty())
		{
			for(int x=0;x<list.size();x++)
			{
				EntityLiving entity = list.get(x);
				if(!this.isRiding())
				{
					if(entity != this && !entity.isBeingRidden() && entity instanceof EntityAnimal || entity instanceof EntityMob)
					{
							if(entity instanceof EntityHorse)
								((EntityHorse)entity).setHorseTamed(true);
							double old = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
							entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(old*3);
							entity.setHealth(entity.getMaxHealth());
							entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"), 1000, 1));
							this.startRiding(entity);
					}
				}
			}
		}
	}
	
}
