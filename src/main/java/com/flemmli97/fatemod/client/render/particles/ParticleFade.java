package com.flemmli97.fatemod.client.render.particles;

import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.particles.IParticleCreator;
import com.flemmli97.tenshilib.client.particles.ParticleSimpleTexture;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleFade extends ParticleSimpleTexture{

	private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID,"textures/particles/fade.png");
	
	public ParticleFade(World world, double xCoord, double yCoord, double zCoord, double xSpeed,
			double ySpeed, double zSpeed) {
		super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
		this.particleScale*=0.5;
	}

	@Override
	public ResourceLocation texture() {
		return tex;
	}
	
	public static class Factory implements IParticleCreator
	{
		@Override
		public Particle createParticle(World world, double xCoord, double yCoord, double zCoord,
				double xSpeed, double ySpeed, double zSpeed, Object... modifier) {
			return new ParticleFade(world, xCoord, yCoord, zCoord, zSpeed, zSpeed, zSpeed);
		}
	}
}
