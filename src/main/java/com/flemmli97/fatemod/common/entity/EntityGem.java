package com.flemmli97.fatemod.common.entity;

import com.flemmli97.tenshilib.common.entity.EntityProjectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGem extends EntityProjectile {


	public EntityGem(World world) {
		super(world);
	}
	
    public EntityGem(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }

    public EntityGem(World world, double d1, double d2, double d3)
    {
        super(world, d1, d2, d3);
    }

	@Override
	protected void onImpact(RayTraceResult result) {	
    	this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2.0F, false);
        this.setDead();
	}
	
}
