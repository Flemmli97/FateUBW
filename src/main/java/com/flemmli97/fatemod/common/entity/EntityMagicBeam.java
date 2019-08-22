package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityBeam;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityMagicBeam extends EntityBeam{

	EntityLivingBase target;
	private int strengthMod;
	public EntityMagicBeam(World worldIn) {
		super(worldIn);
    }

	public EntityMagicBeam(World world, EntityLivingBase shootingEntity)
    {
		super(world, shootingEntity);
	}  
	
	public EntityMagicBeam(World world, EntityLivingBase shootingEntity, EntityLivingBase target, int strength)
    {
		this(world, shootingEntity);
		this.target=target;
		this.strengthMod=strength;
	}
	
	@Override
	public void onUpdate() {
		/*EntityLivingBase thrower = getThrower();
		if(this.livingTick<=10)
		{
			livingTick++;
		}
		if(livingTick == 10)
		{
			if(!worldObj.isRemote)
			{
				if(thrower instanceof EntityPlayer)
				{
					this.entityRayTrace(64);
				}
				else if(target!=null)
				{
					this.setHeadingToPosition(target.posX, target.posY+target.height/2, target.posZ, 0.5F , 1);
				}
			}
		}
		else if(livingTick>10)
		{
			if(!worldObj.isRemote) 
			{
				if(thrower == null || thrower.isDead) 
				{
					setDead();
					return;
				}
			}
			super.onUpdate();
		}*/
		super.onUpdate();
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		result.entityHit.attackEntityFrom(CustomDamageSource.magicBeam(this, this.getShooter()), ConfigHandler.magicBeam*(1+0.25f*this.strengthMod));
	}
}
