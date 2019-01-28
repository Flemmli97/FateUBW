package com.flemmli97.fatemod.client.render.particles;

import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.particles.IParticleCreator;
import com.flemmli97.tenshilib.client.particles.ParticleSimpleTexture;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleFade extends ParticleSimpleTexture{

	private static final ResourceLocation tex1 = new ResourceLocation(LibReference.MODID,"textures/particles/fade_1.png");
	private static final ResourceLocation tex2 = new ResourceLocation(LibReference.MODID,"textures/particles/fade_2.png");
	private static final ResourceLocation tex3 = new ResourceLocation(LibReference.MODID,"textures/particles/fade_3.png");

	private int texID;
	
	public ParticleFade(World world, double xCoord, double yCoord, double zCoord, double xSpeed,
			double ySpeed, double zSpeed) {
		super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
		this.particleScale*=0.5;
		this.setRBGColorF(68/255f, 121/255f, 206/255f);
		this.texID=world.rand.nextInt(3);
	}

	@Override
	public ResourceLocation texture() {
		switch(this.texID)
		{
			case 0: return tex1;
			case 1: return tex2;
			case 2: return tex3;
		}
		return tex1;
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
