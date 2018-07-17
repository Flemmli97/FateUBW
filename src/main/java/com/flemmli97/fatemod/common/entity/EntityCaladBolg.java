package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.CustomDamageSource;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityCaladBolg extends EntitySpecialProjectile{

	public EntityCaladBolg(World worldIn)
    {
        super(worldIn);
		this.livingTickMax = 100;
    }

	public EntityCaladBolg(World world, EntityLivingBase shootingEntity)
    {
		super(world, shootingEntity, false, false);
		this.livingTickMax = 100;
	}
	
	public EntityCaladBolg(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
		this.livingTickMax = 100;
    }

	@Override
	protected void onImpact(RayTraceResult result) {
		if(!world.isRemote)
		{
			if(result.entityHit != null && result.entityHit instanceof EntityLivingBase && result.entityHit != this.getThrower())
			{
				EntityLivingBase hittedEntity = (EntityLivingBase) result.entityHit;
				hittedEntity.attackEntityFrom(CustomDamageSource.caladBolg(this, this.getThrower()), 25);
				this.setDead();
			}
			else if(result.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				this.setDead();
			}
		}
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0.001F;
	}
}
