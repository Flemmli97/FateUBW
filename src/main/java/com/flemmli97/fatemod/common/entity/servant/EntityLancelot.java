package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAILancelot;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.item.EntityItem;
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
	
	public EntityLancelot(World world) {
		super(world, EnumServantType.BERSERKER, "Knight of Owner", new ItemStack[] {new ItemStack(ModItems.arondight)});
        this.tasks.addTask(1, attackAI);
	}

	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		return Pair.of(20, 20);
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
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.arondight)); 
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
		return this.getHeldItemMainhand().getItem()!=ModItems.arondight;
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
		if (!this.world.isRemote && this.canPickWeapon() && !this.isDead() && this.world.getGameRules().getBoolean("mobGriefing"))
        {
            for (EntityItem entityitem : this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D)))
            {
                if (!entityitem.isDead && !entityitem.cannotPickup() && checkItemToWield(entityitem.getItem()))
                {
                    this.updateEquipmentIfNeeded(entityitem);
                    this.revealServant();
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
}
