package com.flemmli97.fatemod.common.gen;

import java.util.Random;

import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.init.ModBlocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator {
	
	private WorldGenerator gem_ore;
	private WorldGenerator servant_charm;

    public WorldGen() {
	    this.servant_charm = new WorldGenCharm();
	    this.gem_ore = new WorldGenMinable(ModBlocks.magicOre.getDefaultState(), 8);
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == 0) {
	    	this.runGenerator(gem_ore, world, random, chunkX, chunkZ, ConfigHandler.gemSpawnRate, 1, 40);
	    	this.runGenerator(servant_charm, world, random, chunkX, chunkZ, ConfigHandler.charmSpawnRate, 1, 30);
	    }		
	}
	
	private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
		if (minHeight < 0)
		{
			minHeight=0;
		}
		if(maxHeight > 256)
		{
			maxHeight=256;
		}
		if(minHeight > maxHeight)
		{
			minHeight=maxHeight;
		}
		for(int chance = 0; chance< chancesToSpawn; chance++)
		{
		    int heightDiff = maxHeight - minHeight + 1;
			int x = chunk_X * 16 + rand.nextInt(16);
	        int y = minHeight + rand.nextInt(heightDiff);
	        int z = chunk_Z * 16 + rand.nextInt(16);
			BlockPos pos = new BlockPos(x,y,z);
	        generator.generate(world, rand, pos);
	    }
	}
}
