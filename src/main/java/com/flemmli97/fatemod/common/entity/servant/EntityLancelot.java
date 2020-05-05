package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAILancelot;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.item.ItemUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class EntityLancelot extends EntityServant {

	public EntityAILancelot attackAI = new EntityAILancelot(this);

	public EntityLancelot(World world) {
		super(world, EnumServantType.BERSERKER, "Knight of Owner", new ItemStack[] {new ItemStack(ModItems.arondight)});
        this.tasks.addTask(1, this.attackAI);
	}

	@Override
	public boolean canUse(AnimatedAction anim, AttackType type)
	{
		return true;
	}
	
	@Override
	public AnimatedAction[] getAnimations() {
		return AnimatedAction.vanillaAttackOnly;
	}
	
	@Override
	public void updateAI(int behaviour) {
		super.updateAI(behaviour);
		if(this.commandBehaviour == 3)
		{
			this.tasks.addTask(1, this.attackAI);
		}
		else if(this.commandBehaviour == 4)
		{
			this.tasks.removeTask(this.attackAI);
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
		
		if(damageSource == DamageSource.OUT_OF_WORLD)
		{
			return this.preAttackEntityFrom(damageSource, damage);
		}
		else
		{
			if(!(damageSource.getTrueSource() instanceof EntityServant))
				damage*=0.5;
			
			if(damageSource.isProjectile() && !damageSource.isUnblockable() && this.projectileBlockChance(damageSource, damage))
			{
				if(this.getRNG().nextFloat()<ConfigHandler.lancelotReflectChance&&damageSource.getImmediateSource() instanceof IProjectile && !this.world.isRemote)
				{
					this.reflectProjectile(damageSource.getImmediateSource());
					this.world.playSound(null, this.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.NEUTRAL, 1, 1);
				}
				else
					this.world.playSound(null, this.getPosition(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 1, 1);
				damageSource.getImmediateSource().setDead();
				return true;
			}
			return this.preAttackEntityFrom(damageSource, (float) Math.min(50, damage));
		}
	}
	
	public boolean canPickWeapon()
	{
		return this.getHeldItemMainhand().getItem()!=ModItems.arondight;
	}
	
	private void reflectProjectile(Entity oldProjectile)
	{
		if(!(oldProjectile instanceof IProjectile))
			return;
		NBTTagCompound old = new NBTTagCompound();
		oldProjectile.writeToNBTAtomically(old);
		old.removeTag("UUIDMost");
		old.removeTag("UUIDLeast");
		//Vanilla-fix incompability
		old.removeTag("VFAABB");
		if(old.hasKey("Shooter", Constants.NBT.TAG_STRING))
			old.setString("Shooter", this.cachedUniqueIdString);
		Entity e = EntityList.createEntityFromNBT(old, this.world);
		IProjectile proj = (IProjectile)e;
		if(proj!=null)
		{
			if(this.getAttackTarget()!=null)
			{
				EntityLivingBase target = this.getAttackTarget();
				Vec3d dir = new Vec3d (target.posX-e.posX, (target.posY+target.getEyeHeight()) - e.posY, target.posZ-e.posZ);
				proj.shoot(dir.x, dir.y, dir.z, 1, 1);
			}
			else
				proj.shoot(-oldProjectile.motionX, -oldProjectile.motionY, -oldProjectile.motionZ, 1, 1);
			this.world.spawnEntity(e);
		}
	}

	@Override
	public void onLivingUpdate() {
		for (int x = 0; x < 2; x++)
			{this.world.spawnParticle(
				EnumParticleTypes.SMOKE_LARGE,
        		this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width,
        		this.posY + this.rand.nextDouble() * (double)this.height, 
        		this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width,
					this.rand.nextGaussian() * 0.02D,
					this.rand.nextGaussian() * 0.02D,
					this.rand.nextGaussian() * 0.02D, 1);
			}	
		if (!this.world.isRemote && this.canPickWeapon() && !this.isDead() && this.world.getGameRules().getBoolean("mobGriefing"))
        {
            for (EntityItem entityitem : this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D)))
            {
                if (!entityitem.isDead && !entityitem.cannotPickup() && this.checkItemToWield(entityitem.getItem()) && ItemUtil.isItemBetter(this.getHeldItemMainhand(), entityitem.getItem()))
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
