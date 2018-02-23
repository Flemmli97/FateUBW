package com.flemmli97.fatemod.common.gen;

import java.util.Random;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.init.ModBlocks;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator {
	
	private WorldGenerator gem_ore;
	private WorldGenerator servant_charm;
    int config_gem = ConfigHandler.gemSpawnRate;
    int config_charm = ConfigHandler.charmSpawnRate;
    
    public WorldGen() {
	    this.servant_charm = new WorldGenCharm();
	    this.gem_ore = new WorldGenMinable(ModBlocks.magicOre.getDefaultState(), 8);
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == 0) {
	    	this.runGenerator(gem_ore, world, random, chunkX, chunkZ, config_gem, 1, 40);
	    	this.runGenerator(servant_charm, world, random, chunkX, chunkZ, config_charm, 1, 30);
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
	
	//uses .nbt file
	public void generateTemplate(World world, BlockPos pos)
	{
		WorldServer worldServer = (WorldServer)world;
		MinecraftServer minecraftServer = world.getMinecraftServer();
		TemplateManager templateManager = worldServer.getStructureTemplateManager();
		Template template = templateManager.get(minecraftServer, new ResourceLocation(Fate.MODID, ":structures/grail"));
		
		if(template != null)
		{
		    template.addBlocksToWorld(world, pos, new PlacementSettings());
		}
	}

}
