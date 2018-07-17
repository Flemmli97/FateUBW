package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGaeBolg extends EntitySpecialProjectile{
	
	public EntityGaeBolg(World worldIn)
    {
        super(worldIn);
		this.livingTickMax = 100;
    }

	public EntityGaeBolg(World world, EntityLivingBase shootingEntity)
    {
		super(world, shootingEntity, false, false);
	}
	
	  public EntityGaeBolg(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

	@Override
	protected void onImpact(RayTraceResult raytraceResultIn) {
		if(!world.isRemote)
			if(raytraceResultIn.entityHit != null && raytraceResultIn.entityHit instanceof EntityLivingBase && raytraceResultIn.entityHit != this.getThrower())
			{
				EntityLivingBase hittedEntity = (EntityLivingBase) raytraceResultIn.entityHit;
				if(hittedEntity instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) hittedEntity;
					if(!player.capabilities.isCreativeMode)
					{
						player.attackEntityFrom(CustomDamageSource.gaeBolg(this, this.getThrower()), 10);
						player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:wither"), 200, 2));
						player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 100, 7));
						player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:jump_boost"), 100, 128));
						this.setDead();					
					}
				}
				else
				{
					hittedEntity.attackEntityFrom(CustomDamageSource.gaeBolg(this, this.getThrower()), 10);
					hittedEntity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:wither"), 200, 2));
					hittedEntity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 100, 7));
					hittedEntity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:jump_boost"), 100, 128));
					this.setDead();
				}
			}
			else
			{
				this.setDead();
			}
			if(getThrower() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) this.getThrower();
				if(!player.capabilities.isCreativeMode)
				{
					EntityItem gaeBolg = new EntityItem(world, getThrower().posX, getThrower().posY, getThrower().posZ, new ItemStack(ModItems.gaebolg));
					gaeBolg.setPickupDelay(0);
					player.world.spawnEntity(gaeBolg);	
					player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.8f, 1);
				}
			}
	}

	@Override
	protected float getGravityVelocity() {
		return 0.01F;
	}
	
	
}
