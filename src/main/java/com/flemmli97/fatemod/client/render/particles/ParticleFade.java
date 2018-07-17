package com.flemmli97.fatemod.client.render.particles;

import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.world.World;

public class ParticleFade extends ParticleFlame{

	public ParticleFade(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
	}
}
