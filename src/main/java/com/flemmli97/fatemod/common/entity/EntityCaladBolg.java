package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityCaladBolg extends EntityProjectile{

	public EntityCaladBolg(World worldIn)
    {
        super(worldIn);
    }

	public EntityCaladBolg(World world, EntityLivingBase shootingEntity)
    {
		super(world, shootingEntity);
	}
	
	public EntityCaladBolg(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
	
	@Override
	public int livingTickMax()
	{
		return 100;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if(result.entityHit != null)
		{
			result.entityHit.attackEntityFrom(CustomDamageSource.caladBolg(this, this.getShooter()), 25);
		}
		this.setDead();
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0.001F;
	}
}
