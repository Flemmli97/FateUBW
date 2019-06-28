package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityGaeBolg extends EntityProjectile{
		
	public EntityGaeBolg(World worldIn)
    {
        super(worldIn);
    }

	public EntityGaeBolg(World world, EntityLivingBase shootingEntity)
    {
		super(world, shootingEntity);
	}
	
	public EntityGaeBolg(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
	  
	@Override
	public int livingTickMax()
	{
		return 100;
	}
	
	/*public float radius()
	{
		return 5;
	}*/

	@Override
	protected void onImpact(RayTraceResult raytraceResultIn) {
		if(raytraceResultIn.entityHit != null && raytraceResultIn.entityHit instanceof EntityLivingBase && raytraceResultIn.entityHit != this.getShooter())
		{
			EntityLivingBase hittedEntity = (EntityLivingBase) raytraceResultIn.entityHit;
			hittedEntity.attackEntityFrom(CustomDamageSource.gaeBolg(this, this.getShooter()), ConfigHandler.gaeBolgDmg);
			if(!(hittedEntity instanceof EntityPlayer) || ((EntityPlayer) hittedEntity).capabilities.isCreativeMode)
			{
				for(PotionEffect effect : ConfigHandler.gaeBolgEffect.potions())
					hittedEntity.addPotionEffect(effect);
			}
			this.setDead();
		}
		else if(raytraceResultIn.typeOfHit==Type.BLOCK)
		{
			this.setDead();
		}
	}
	
	@Override
    public void setDead()
    {
		if(!this.world.isRemote)
		{
			if(this.getShooter() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) this.getShooter();
				if(!player.capabilities.isCreativeMode)
				{
					EntityItem gaeBolg = new EntityItem(world, this.getShooter().posX, this.getShooter().posY, this.getShooter().posZ, new ItemStack(ModItems.gaebolg));
					gaeBolg.setPickupDelay(0);
					player.world.spawnEntity(gaeBolg);	
					player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.8f, 1);
				}
			}
			else if(this.getShooter() instanceof EntityCuchulainn)
			{
				((EntityCuchulainn)this.getShooter()).retrieveGaeBolg();
			}
		}
        super.setDead();
    }

	@Override
	protected float getGravityVelocity() {
		return 0.01F;
	}
}
