package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityBeam;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEnumaElish extends EntityBeam{

	public static final float radius = 1.2f;
	public static final float range = 48;
	
    public EntityEnumaElish(World worldIn)
    {
        super(worldIn);
    }

    public EntityEnumaElish(World world, EntityLivingBase shooter)
    {
        super(world, shooter);
    }
    
    @Override
    public float radius()
    {
    	return radius;
    }
    
    @Override
    public float getRange()
    {
    	return range;
    }
    
    @Override
    public boolean piercing()
	{
		return true;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		result.entityHit.attackEntityFrom(CustomDamageSource.excalibur(this, this.getShooter()), 13.0F);
	}
}
