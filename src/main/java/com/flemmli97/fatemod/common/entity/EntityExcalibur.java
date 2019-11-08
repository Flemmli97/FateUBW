package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityBeam;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityExcalibur extends EntityBeam{
	    
	public static final float radius = 2f;
	public static final float range = 16;
	
    public EntityExcalibur(World worldIn)
    {
        super(worldIn);
    }

    public EntityExcalibur(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
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
		result.entityHit.attackEntityFrom(CustomDamageSource.excalibur(this, this.getShooter()), ConfigHandler.excaliburDamage);
	}
}
